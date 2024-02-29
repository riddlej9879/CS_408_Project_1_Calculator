package com.example.project_1_calculator;

import android.util.Log;
import android.content.Context;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DefaultModel extends AbstractModel {
    public static final String TAG = "DefaultModel";
    private final StringBuilder defaultOutputText = new StringBuilder("0");
    private int hr;
    private int vr;
    private int btn_txt_sz;
    private int op_txt_sz;
    private int maxLength;
    private String[] btnTxtArr;
    private String[] btnTagArr;
    private String outputTag;
    private StringBuilder outputText = new StringBuilder("0");
    private BigDecimal left = BigDecimal.ZERO;
    private BigDecimal right = BigDecimal.ZERO;
    private CalculatorState state;
    private Operation operation;

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
        state = CalculatorState.CLEAR;
        operation = Operation.CLEAR;
    }

    // USE STRING BUILDER FOR CREATING NEW OUTPUT TEXT
    // Setter Methods
    public void setOutputText(StringBuilder newOutputText) {
        Log.i(TAG, "setOutputText");
        StringBuilder oldOutputText = outputText;

        firePropertyChange(DefaultController.OUTPUT_PROPERTY, oldOutputText.toString(), newOutputText.toString());
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
    public void setRight(BigDecimal right) {
        this.right = right;
    }
    public void setLeft(BigDecimal left) {
        this.left = left;
    }
    public void setState(CalculatorState state) {
        this.state = state;
    }
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    // Getter methods
    public StringBuilder getOutputText() {
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
    public CalculatorState getState() {
        return state;
    }
    public int getMaxLength() {
        return maxLength;
    }
    public Operation getOperation() {
        return operation;
    }
    public BigDecimal getRight() {
        return right;
    }
    public BigDecimal getLeft() {
        return left;
    }
}
