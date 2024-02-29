package com.example.project_1_calculator;

import android.content.ContextWrapper;
import android.util.Log;
import android.content.Context;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class DefaultModel extends AbstractModel {
    private final String defaultOutputText = BigDecimal.ZERO.toString();
    private int hr, vr, btn_txt_sz, op_txt_sz, length;
    private String[] btnTxtArr;
    private String[] btnTagArr;
    private String outputTag;
    private String outputText;
    private BigDecimal right = BigDecimal.ZERO;
    private BigDecimal left = BigDecimal.ZERO;
    private StringBuilder New_Output;
    private static CalculatorState state;
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
        length = context.getResources().getInteger(R.integer.max_characters);
        state = CalculatorState.CLEAR;
        operation = Operation.CLEAR;
    }

    // USE STRING BUILDER FOR CREATING NEW OUTPUT TEXT
    // Setter Methods
    private void setOutputText(String newOutputText) {
        String oldOutputText = this.outputText;
        if (outputText == null) {
            this.outputText = newOutputText;
        } else {
            this.outputText += newOutputText;
        }
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
    private void setRight(BigDecimal right) {
        this.right = right;
    }
    private void setLeft(BigDecimal left) {
        this.left = left;
    }

    // Getter methods
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
    public static CalculatorState getState() {
        return state;
    }
    public Operation getOperation() {
        return operation;
    }
    public BigDecimal getRight(BigDecimal right) {
        return this.right = right;
    }
    public BigDecimal getLeft(BigDecimal left) {
        return this.left = left;
    }
    public static void handleClick(String tag) {
        String[] tagArr = tag.split("_");
        switch(getState()) {
            case CLEAR:
            case CLEAR:
            case CLEAR:
            case CLEAR:
            case CLEAR:
        }
    }
}
