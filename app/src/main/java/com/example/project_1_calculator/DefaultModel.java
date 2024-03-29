package com.example.project_1_calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.BigDecimal.*;

public class DefaultModel extends AbstractModel {
    private final String defaultOutputText = "0";
    private final int maxLength = 9;
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
        String oldOutputText = this.outputText;

        if (oldOutputText == null) {
             oldOutputText = BigDecimal.ZERO.toString();
        }
        this.outputText = outText;

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText, outText);
    }
    public void setLeftDec(BigDecimal newLeft) {
        String newText = newLeft.toString();
        this.leftDec = newLeft;

        setLeftString(new StringBuilder(newText));
        setOutputText(newText);
    }
    public void setRightDec(BigDecimal newRight) {
        String newText = newRight.toString();
        this.rightDec = newRight;

        setRightString(new StringBuilder(newText));
        setOutputText(newText);
    }
    public void setResult(BigDecimal result) {
        String newText = result.toString();

        this.result = result;
        setOutputText(newText);
    }
    public void setLeftString(StringBuilder leftString) {
        this.leftString = leftString;
    }
    public void setRightString(StringBuilder rightString) {
        this.rightString = rightString;
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
        EnumCalcState currState = getState();

        switch (tag_arr[0].toLowerCase()) {
            case "number":
                if (currState != EnumCalcState.ERROR) {
                    HandleNumClick(tag_arr[1], getState());
                }
                break;
            case "operation":
                HandleOpClick(tag_arr[1], currState);
                break;
            default:
                Log.e("An Error has occurred", "SetText(): " + tag);
                setState(EnumCalcState.ERROR);
                setOutputText("ERROR");
                break;
        }
    }
    // Method called if user clicks a number
    public void HandleNumClick(String num, EnumCalcState currState) {
        StringBuilder currOutString = new StringBuilder(getOutputText());
        StringBuilder newOutString = new StringBuilder();

        switch (currState) {
            case CLEAR:
                setState(EnumCalcState.LEFT);
            case LEFT:
                int L_len = String.join("", getLeftDec().abs().toString().split("\\.")).length();

                if (currOutString.toString().equals("0")) {
                    newOutString.append(num);
                } else if (L_len < getMaxLength()) {
                    newOutString.append(currOutString).append(num);
                }
                setLeftDec(new BigDecimal(newOutString.toString()));
                break;
            case RIGHT:
                StringBuilder r = getRightString();
                int R_len = String.join("", getRightDec().abs().toString().split("\\.")).length();

                if (r.toString().equals("0")) {
                    setRightDec(new BigDecimal(num));
                } else if (R_len < getMaxLength()) {
                    setRightDec(new BigDecimal(r.append(num).toString()));
                }
                setOutputText(newOutString.append(currOutString).append(num).toString());
                break;
            default:
                Log.e("An Error has occurred", "HandleNumClick(): " + num);
                setState(EnumCalcState.ERROR);
                setOutputText("ERROR");
                break;
        }
    }
    // Method called if user selects a non-number
    public void HandleOpClick(String tag_op, EnumCalcState currState) {
        StringBuilder currOutString = new StringBuilder(getOutputText());
        StringBuilder newOutString = new StringBuilder();
        EnumOperation currOp = getOperation();

        if (currState != EnumCalcState.ERROR) {
            switch (tag_op.toLowerCase()) {
                case "percent":
                    if (currState == EnumCalcState.LEFT) {
                        setLeftDec(CalculateResult(getLeftDec(), EnumOperation.DIVIDE, new BigDecimal(100)));
                        setState(EnumCalcState.RIGHT);
                    } else if (currState == EnumCalcState.RIGHT) {
                        setState(EnumCalcState.RESULT);
                        setRightDec(CalculateResult(getLeftDec(), EnumOperation.DIVIDE, new BigDecimal(100)));
                        setResult(CalculateResult(getLeftDec(), currOp, getRightDec()));
                    }
                    break;
                case "equal":
                    setState(EnumCalcState.RESULT);
                    setResult(CalculateResult(getLeftDec(), getOperation(), getRightDec()));
                    break;
                case "decimal":
                    if (currState == EnumCalcState.LEFT) {
                        // Counts the total amount of numbers in the output
                        int L_len = String.join("", getLeftDec().toString().split("\\.")).length();
                        boolean containsDecimal = getLeftString().indexOf(".") != -1;
                        if (!containsDecimal && (L_len < getMaxLength()+1)) {
                            newOutString.append(currOutString).append(".");
                            setLeftDec(new BigDecimal(newOutString.toString()));
                        }
                    } else if (currState == EnumCalcState.RIGHT) {
                        int R_len = String.join("", getRightDec().toString().split("\\.")).length();
                        boolean containsDecimal = getRightString().indexOf(".") != -1;
                        if (!containsDecimal && (R_len < getMaxLength() + 1)) {
                            newOutString.append(currOutString).append(".");
                            setOutputText(newOutString.toString());
                        }
                    }
                    break;
                case "negative":
                    if ((currState == EnumCalcState.CLEAR) || (currState == EnumCalcState.LEFT)) {
                        setLeftDec(getLeftDec().negate());
                    } else if (currState == EnumCalcState.RIGHT) {
                        setRightDec(getRightDec().negate());
                    }
                    break;
                case "sqrt":
                    Log.i("Root", "sqrt");
                    try {
                        if (currState == EnumCalcState.LEFT) {
                            setLeftDec(SquareRoot(getLeftDec()));
                        } else if (currState == EnumCalcState.RIGHT) {
                            StringBuilder r = new StringBuilder();
                            setRightDec(SquareRoot(getRightDec()));
                            if (currOp == EnumOperation.ADD) {
                                setOutputText(r.append(getLeftDec()).append(" + ").append(getRightString()).toString());
                            } else if (currOp == EnumOperation.SUBTRACT) {
                                setOutputText(r.append(getLeftDec()).append(" - ").append(getRightString()).toString());

                            } else if (currOp == EnumOperation.MULTIPLY) {
                                setOutputText(r.append(getLeftDec()).append(" ÷ ").append(getRightString()).toString());
                            } else if (currOp == EnumOperation.DIVIDE) {
                                setOutputText(r.append(getLeftDec()).append(" × ").append(getRightString()).toString());
                            }
                        }
                    } catch (ArithmeticException e) {
                        Log.e("An Error has occurred", "HandleOpClick(): " + tag_op);
                        setState(EnumCalcState.ERROR);
                        setOutputText("ERROR");
                    }
                    break;
                case "divide":
                    setOperation(EnumOperation.DIVIDE);
                    AddOperationToOutput(EnumOperation.DIVIDE, currState, " ÷ ", currOutString);
                    break;
                case "multiply":
                    setOperation(EnumOperation.MULTIPLY);
                    AddOperationToOutput(EnumOperation.MULTIPLY, currState, " × ", currOutString);
                    break;
                case "subtract":
                    setOperation(EnumOperation.SUBTRACT);
                    AddOperationToOutput(EnumOperation.SUBTRACT, currState, " - ", currOutString);
                    break;
                case "addition":
                    setOperation(EnumOperation.ADD);
                    AddOperationToOutput(EnumOperation.ADD, currState, " + ", currOutString);
                    break;
                default:
                    Log.e("An Error has occurred", "HandleOpClick(): " + tag_op);
                    setState(EnumCalcState.ERROR);
                    setOutputText("ERROR");
                    break;
            }
            if (tag_op.equals("clear")) {
                ClearClicked();
            }
        }
    }
    private void AddOperationToOutput(EnumOperation op, EnumCalcState currState, String symbol, StringBuilder currOut) {
        EnumOperation currOp = getOperation();

        switch (currState) {
            case LEFT:
                setState(EnumCalcState.RIGHT);
                setOutputText(currOut.toString() + symbol);
                break;
            case RIGHT:
                BigDecimal newLeft = CalculateResult(getLeftDec(), currOp, getRightDec());
                setLeftDec(newLeft);
                break;
            default:
                break;
        }
    }
    private BigDecimal CalculateResult(BigDecimal leftNum, EnumOperation op, BigDecimal rightNum) {
        BigDecimal res = BigDecimal.ZERO;

        switch (op) {
            case ADD:
                res = leftNum.add(rightNum);
                break;
            case SUBTRACT:
                res = leftNum.subtract(rightNum);
                break;
            case MULTIPLY:
                res = leftNum.multiply(rightNum);
                break;
            case DIVIDE:
                res = leftNum.divide(rightNum, getMaxLength(), BigDecimal.ROUND_HALF_UP);
                break;
            default:
                Log.e("Calculation Error", "Error: " + op);
                break;
        }

        return res;
    }
    private BigDecimal SquareRoot(BigDecimal number) {
        Log.i("SqRt function", number.toString());
        // BigDecimal.sqrt doesn't work with API 22.
        // Documentation refers to API version 33
        // Used ChatGPT code to fill the gap in knowledge
        BigDecimal two = BigDecimal.valueOf(2);
        BigDecimal res = number.divide(two);

        while (true) {
            BigDecimal one = number.divide(res, 100, BigDecimal.ROUND_HALF_UP);
            one = one.add(res);
            one = one.divide(two, 100, BigDecimal.ROUND_HALF_UP);
            if (res.equals(one)) {
                break;
            }
            res = one;
        }

        return res.stripTrailingZeros();
    }
    private void ClearClicked() {
        StringBuilder zero = new StringBuilder("0");
        setOutputText(zero.toString());
        setState(EnumCalcState.CLEAR);
        setLeftDec(BigDecimal.ZERO);
        setRightDec(BigDecimal.ZERO);
        setResult(BigDecimal.ZERO);
        setLeftString(zero);
        setRightString(zero);
    }
}