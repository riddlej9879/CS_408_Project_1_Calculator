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
    private BigDecimal left = BigDecimal.ZERO;
    private BigDecimal right = BigDecimal.ZERO;
    private BigDecimal result = BigDecimal.ZERO;
    private EnumCalcState state;
    private EnumOperation operation;
    private boolean leftNeg, rightNeg;

    // Constructor
    public void model(Context context) {
        setDefaultOutputText(defaultOutputText);
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
        leftNeg = false;
        rightNeg = false;
    }

    // Setter Methods
    public void setDefaultOutputText(String defText) {
        firePropertyChange(DefaultController.OUTPUT_PROPERTY, defText, defText);
    }
    public void setOutputText(BigDecimal result) {
        String oldOutputText = getOutputText();
        String newOutputText = result.toString();

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText, newOutputText);
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
    public void setLeft(BigDecimal left) {
        String oldOutputText = left.toString();
        String newOutputText = left.toString();
        this.left = left;

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText, newOutputText);
    }
    public void setRight(BigDecimal right) {
        String oldOutputText = right.toString();
        String newOutputText = right.toString();
        this.right = right;

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText, newOutputText);
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
    public BigDecimal getLeft() {
        return left;
    }
    public BigDecimal getRight() {
        return right;
    }
    public StringBuilder getLeftString() {
        return new StringBuilder(getLeft().toString());
    }

    // Click handler methods
    public void setText(String tag){
        String[] tag_arr = tag.split("_");
        switch (tag_arr[0].toLowerCase()){
            case "operation":
                handleOpClick(tag_arr);
                break;
            case "number":
                handleNumClick(tag_arr);
                break;
            default:
                break;
        }

    }
    public void handleOpClick(String[] tag_arr){
        switch (getState()) {
            case CLEAR:
                setState(EnumCalcState.LEFT);
                break;
            case OPERAND:
                if (operation == EnumOperation.SQRT) {
                    setState(EnumCalcState.RESULT);
                } else {
                    setRight(BigDecimal.ZERO);
                    setState(EnumCalcState.RIGHT);
                }
            default:
                Log.e("handleOpClick", "getState() returned invalid results");
                break;
        }

        switch (tag_arr[1].toLowerCase()) {
            case "clear":
                setState(EnumCalcState.LEFT);
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
                if (!containsDecimal && leftString.length() < maxLength) {
                    leftString.append(".");
                }
                break;
            case "negative":
                applyNegative();
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
    public void handleNumClick(String[] tag_arr){}
    private void calculteResult() {}
    private void applyNegative() {
        //String result = (number % 2 == 0) ? "even" : "odd";
        if (getState() == EnumCalcState.LEFT) {
            leftNeg = !leftNeg;
        } else if (getState() == EnumCalcState.RIGHT) {
            rightNeg = !rightNeg;
        }

    }
}