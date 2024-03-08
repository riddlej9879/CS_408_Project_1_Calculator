package com.example.project_1_calculator;

import android.util.Log;

import java.math.BigDecimal;

public class DefaultModel extends AbstractModel {
    private final String LogTag = "DEFAULT MODEL";
    private final String defaultOutputText = "0";
    private final int maxLength = 10;
    private String outputText;
    private BigDecimal leftDec, rightDec, result;

    private StringBuilder leftString, rightString;
    private EnumCalcState state;
    private EnumOperation operation;

    // Constructor
    public DefaultModel() {
        setOutputText(defaultOutputText);
        leftDec = BigDecimal.ZERO;
        rightDec = BigDecimal.ZERO;
        leftString = new StringBuilder(leftDec.toString());
        rightString = new StringBuilder(rightDec.toString());
        result = BigDecimal.ZERO;
        state = EnumCalcState.CLEAR;
        operation = EnumOperation.NONE;
    }

    // Setter Methods
    public void setOutputText(String outText) {
        Log.d(LogTag, "setOutputText");
        String oldOutputText = this.outputText;
        Log.d(LogTag, "Old: " + oldOutputText + " New: " + outText);
        if (oldOutputText == null) {
            Log.d("SetOut called", "Text was null");
             oldOutputText = BigDecimal.ZERO.toString();
        }
        this.outputText = outText;

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText, outText);
    }
    public void setLeftDec(BigDecimal newLeft) {
        Log.d(LogTag, "setLeftDec: " + newLeft.toString());
        String newText = newLeft.toString();
        this.leftDec = newLeft;

        setOutputText(newText);
    }
    public void setRightDec(BigDecimal newRight) {
        Log.d(LogTag, "setRightDec: " + newRight.toString());
        String newText = newRight.toString();
        this.rightDec = newRight;

        setOutputText(newText);
    }
    public void setResult(BigDecimal result) {
        Log.d(LogTag, "setResult: " + result.toString());
        String newText = result.toString();

        this.result = result;
        setOutputText(newText);
    }
    public void setLeftString(StringBuilder leftString) {
        this.leftString = leftString;
        setOutputText(leftString.toString());
    }
    public void setRightString(StringBuilder rightString) {
        this.rightString = rightString;
    }
    public void setState(EnumCalcState state) {
        Log.d(LogTag, "setState");
        this.state = state;
    }
    public void setOperation(EnumOperation operation) {
        Log.d(LogTag, "setOperation");
        this.operation = operation;
    }

    // Getter methods
    // Initialize Layout getter methods
    public String getOutputText() {
        return outputText;
    }
    public int getMaxLength() {
        return maxLength;
    }

    // Calculator methods
    public EnumCalcState getState() {
        Log.d(LogTag, "getState");
        return state;
    }
    public EnumOperation getOperation() {
        Log.d(LogTag, "getOperation");
        return operation;
    }
    public BigDecimal getLeftDec() {
        Log.d(LogTag, "getLeftDec");
        return leftDec;
    }
    public BigDecimal getRightDec() {
        Log.d(LogTag, "getRightDec");
        return rightDec;
    }
    public StringBuilder getLeftString() {
        Log.d(LogTag, "getLeftString");
        return new StringBuilder(getLeftDec().toString());
    }
    public StringBuilder getRightString() {
        Log.d(LogTag, "getRightString");
        return new StringBuilder(getRightDec().toString());
    }

    // Click handler methods
    public void setText(String tag) {
        Log.d(LogTag, "setText");
        String[] tag_arr = tag.split("_");
        EnumCalcState currState = getState();

        switch (tag_arr[0].toLowerCase()) {
            case "number":
                // (number > 0) ? "positive" : (number < 0) ? "negative" : "zero";
                if (currState == EnumCalcState.CLEAR) {
                    setState(EnumCalcState.LEFT);
                } else if (currState == EnumCalcState.OPERAND_SELECTED) {
                    setState(EnumCalcState.RIGHT);
                }
                handleNumClick(tag_arr[1], getState());
                break;
            case "operation":
                handleOpClick(tag_arr[1]);
                break;
            default:
                Log.e("setText", "No tag found");
                break;
        }
    }
    // Method called if user clicks a number
    public void handleNumClick(String num, EnumCalcState state) {
        StringBuilder currOutString = new StringBuilder(getOutputText());
        StringBuilder newOutString = new StringBuilder();
        EnumOperation currOp = getOperation();

        switch (state) {
            case LEFT:
                if (currOutString.toString().equals("0")) {
                    newOutString.append(num);
                } else if (currOutString.length() < getMaxLength()) {
                    newOutString.append(currOutString).append(num);
                }
                setLeftString(newOutString);
                break;
            case RIGHT:
                Log.d(LogTag, "If state == operation_selected output may not be 0");
                if (currOutString.toString().equals("0")) {
                    newOutString.append(num);
                } else if (currOutString.length() < getMaxLength()) {
                    newOutString.append(currOutString).append(num);
                }
                setRightString(newOutString);
                break;
            default:
                Log.e("handleNumClick", "Handle Number Click default case.");
                break;
        }
    }
    // Method called if user selects a non-number
    public void handleOpClick(String tag_op) {
        Log.d(LogTag, "handleOpClick");
        StringBuilder currOutString = new StringBuilder(getOutputText());
        StringBuilder newOutString = new StringBuilder();
        EnumCalcState currState = getState();
        EnumOperation currOperation = getOperation();

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
                ClearClicked();
                break;
            case "percent":
                setState(EnumCalcState.RIGHT);
                break;
            case "equals":
                setState(EnumCalcState.RESULT);
                calculateResult();
                break;
            case "decimal":
                Log.d(LogTag, "Decimal");
                if (currState == EnumCalcState.LEFT) {
                    boolean containsDecimal = currOutString.indexOf(".") != -1;
                    if (!containsDecimal && (currOutString.length() < getMaxLength())) {
                        newOutString.append(currOutString).append(".");
                        setOutputText(newOutString.toString());
                    }
                }
                break;
            case "negative":
                if (state == EnumCalcState.LEFT) {
                    setLeftDec(getLeftDec().negate());
                } else if (state == EnumCalcState.RIGHT) {
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
    private void calculateResult() {
        Log.d(LogTag, "calculateResult");
    }

    private void ClearClicked() {
        if (getState() != EnumCalcState.CLEAR) {
            StringBuilder zero = new StringBuilder("0");
            setOutputText(defaultOutputText);
            setState(EnumCalcState.LEFT);
            setLeftDec(BigDecimal.ZERO);
            setRightDec(BigDecimal.ZERO);
            setResult(BigDecimal.ZERO);
            setLeftString(zero);
            setRightString(zero);
        }
    }
}