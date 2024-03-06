package com.example.project_1_calculator;

import android.content.Context;
import android.util.Log;

import java.math.BigDecimal;

public class DefaultModel extends AbstractModel {
    private int hr;
    private int vr;
    private int btn_txt_sz;
    private int op_txt_sz;
    private int maxLength;
    private String[] btnTxtArr;
    private String[] btnTagArr;
    private String outputTag;
    private final String defaultOutputText = "0";
    private String outputText;
    private BigDecimal leftDec = BigDecimal.ZERO;
    private BigDecimal rightDec = BigDecimal.ZERO;
    private BigDecimal result = BigDecimal.ZERO;
    private EnumCalcState state;
    private EnumOperation operation;

    // Constructor
    public void model(Context context) {
        setOutputText(defaultOutputText);
        setOutputTag(context.getResources().getString(R.string.output_tag));
        setBtnTagArr(context.getResources().getStringArray(R.array.button_tags));
        setBtnTxtArr(context.getResources().getStringArray(R.array.button_text));
        hr = context.getResources().getInteger(R.integer.chain_horizontal);
        vr = context.getResources().getInteger(R.integer.chain_vertical);
        btn_txt_sz = context.getResources().getInteger(R.integer.button_text_size);
        op_txt_sz = context.getResources().getInteger(R.integer.output_text_size);
        maxLength = context.getResources().getInteger(R.integer.max_characters);
        state = EnumCalcState.CLEAR;
        operation = EnumOperation.NONE;
    }

    // Setter Methods
    public void setDefaultOutputText(String defText) {
        firePropertyChange(DefaultController.OUTPUT_PROPERTY, defText, defText);
    }
    public void setOutputText(String result) {
        String oldOutputText = getOutputText();

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText, result);
    }
    public void setOutputTag(String id) {
        this.outputTag = outputTag + id;
    }
    private void setBtnTagArr(String[] sTag) {
        this.btnTagArr = sTag;
    }
    private void setBtnTxtArr(String[] sTxt) {
        this.btnTxtArr = sTxt;
    }
    public void setLeftDec(BigDecimal newLeft) {
        String oldOutputText = leftDec.toString();

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText, newLeft.toString());
    }
    public void setRightDec(BigDecimal newRight) {
        String oldOutputText = rightDec.toString();

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText, newRight.toString());
    }
    public void setState(EnumCalcState state) {
        this.state = state;
    }
    public void setOperation(EnumOperation operation) {
        this.operation = operation;
    }

    // Getter methods
    // Initialize Layout getter methods
    public String getOutputText() {
        return outputText;
    }
    public String getOutputTag() {
        return outputTag;
    }
    public String getBtnTagArr(int index) {
        return btnTagArr[index];
    }
    public String getBtnTxtArr(int index) {
        return btnTxtArr[index];
    }
    public int getHorizontal() {
        return hr;
    }
    public int getVertical() {
        return vr;
    }
    public int getBtnTxtSize() {
        return btn_txt_sz;
    }
    public int getOutputTxtSize() {
        return op_txt_sz;
    }
    public int getMaxLength() {
        return maxLength;
    }

    // Calculator methods
    public EnumCalcState getState() {
        return state;
    }
    public EnumOperation getOperation() {
        return operation;
    }
    public BigDecimal getLeftDec() {
        return leftDec;
    }
    public BigDecimal getRightDec() {
        return rightDec;
    }
    public StringBuilder getLeftString() {
        return new StringBuilder(getLeftDec().toString());
    }
    public StringBuilder getRightString() {
        return new StringBuilder(getRightDec().toString());
    }

    // Click handler methods
    public void setText(String tag) {
        String[] tag_arr = tag.split("_");
        switch (tag_arr[0].toLowerCase()) {
            case "operation":
                Log.i("Operation", tag_arr[1]);
                handleOpClick(tag_arr[1]);
                break;
            case "number":
                Log.i("Number", tag_arr[1]);
                // (number > 0) ? "positive" : (number < 0) ? "negative" : "zero";
                if (getState() == EnumCalcState.CLEAR) {
                    setState(EnumCalcState.LEFT);
                }
                handleNumClick(tag_arr[1], getState());
                break;
            default:
                Log.e("setText", "No tag found");
                break;
        }
    }
    public void handleOpClick(String tag_op) {
        /*
        switch (getState()) {
            case CLEAR:
                setState(EnumCalcState.LEFT);
                break;
            case LEFT:
                if (operation == EnumOperation.SQRT) {
                    setState(EnumCalcState.RESULT);
                } else {
                    setRight(BigDecimal.ZERO);
                    setState(EnumCalcState.RIGHT);
                }
            case ERROR:
                Log.e("Error", "An error occurred");
                break;
            default:
                Log.e("handleOpClick", "getState() returned invalid results");
                break;
        }
        */

        switch (tag_op.toLowerCase()) {
            case "clear":
                if (getState() != EnumCalcState.CLEAR) {
                    setState(EnumCalcState.LEFT);
                    setLeftDec(BigDecimal.ZERO);
                    setRightDec(BigDecimal.ZERO);
                    setOutputText(defaultOutputText);
                }
                break;
            case "percent":
                setState(EnumCalcState.RIGHT);
                break;
            case "equals":
                setState(EnumCalcState.RESULT);
                calculteResult();
                break;
            case "decimal":
                StringBuilder leftString = getLeftString();
                boolean containsDecimal = leftString.indexOf(".") != -1;
                if (!containsDecimal && leftString.length() < getMaxLength()) {
                    leftString.append(".");
                }
                break;
            case "negative":
                if (getState() == EnumCalcState.LEFT) {
                    setLeftDec(getLeftDec().negate());
                } else if (getState() == EnumCalcState.RIGHT) {
                    setRightDec(getRightDec().negate());
                }
                break;
            case "sqrt":
            case "divide":
            case "multiply":
            case "subtract":
            case "addition":

                break;
            default:
                break;
        }
    }
    public void handleNumClick(String num, EnumCalcState state) {
        Log.i("num click", num);
        switch (state) {
            case LEFT:
                StringBuilder oldLeftString = getLeftString();
                if (oldLeftString.length() < getMaxLength()) {
                    oldLeftString.append(num);
                    setLeftDec(new BigDecimal(getLeftString().toString()));
                }
                Log.d("Left", oldLeftString.toString());
                break;
            case RIGHT:
                StringBuilder oldRightString = getRightString();
                if (oldRightString.length() < getMaxLength()) {
                    oldRightString.append(num);
                    setRightDec(new BigDecimal(getRightString().toString()));
                }
                Log.d("Right",oldRightString.toString());
                break;
            default:
                Log.d("Num Click",getOperation().toString());
                break;
        }
    }
    private void calculteResult() {}
}