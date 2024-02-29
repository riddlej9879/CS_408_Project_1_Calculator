package com.example.project_1_calculator;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.math.BigInteger;

import android.view.ViewGroup.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.project_1_calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AbstractView {
    private static final String TAG_MAIN_ACTIVITY = "MainActivity";
    private ActivityMainBinding binding;
    private DefaultController controller;
    private final int NORTH = R.id.guidelineTop;
    private final int SOUTH = R.id.guidelineBottom;
    private final int WEST = R.id.guidelineLeft;
    private final int EAST = R.id.guidelineRight;
    public CalculatorState state;
    public DefaultModel model;

    // SQLite section
    // Saving the application state and data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG_MAIN_ACTIVITY, "MainActivity");

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        controller = new DefaultController();
        model = new DefaultModel();

        controller.addView(this);
        controller.addModel(model);

        // context: this Passes resource access to model class
        model.model(this);

        ConstraintLayout layout = binding.layout;

        initializeLayout(model, layout);
    }

    private void initializeLayout(DefaultModel model, ConstraintLayout layout) {
        String TAG_INIT = "Init Layout";
        ClickHandler click = new ClickHandler();
        state = CalculatorState.CLEAR;

        /* **************************************************************** */
        /*                      CREATE OUTPUT TEXTVIEW                      */
        /* **************************************************************** */
        TextView output = new TextView(this);
        output.setId(View.generateViewId());
        int outputId = output.getId();
        model.setOutputTag(String.valueOf(outputId));
        output.setTag(model.getOutputTag());
        output.setText(model.getOutputText());
        output.setTextSize(model.getOutputTxtSize());
        layout.addView(output);

        LayoutParams output_params = output.getLayoutParams();
        output_params.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
        output_params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        output.setGravity(Gravity.BOTTOM | Gravity.END);
        output.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        set.connect(outputId, ConstraintSet.TOP, NORTH, ConstraintSet.BOTTOM, 0);
        set.connect(outputId, ConstraintSet.LEFT, WEST, ConstraintSet.RIGHT, 0);
        set.connect(outputId, ConstraintSet.RIGHT, EAST, ConstraintSet.LEFT, 0);

        /* **************************************************************** */
        /*                       CREATE BUTTON LAYOUT                       */
        /* **************************************************************** */
        int hr = model.getHorizontal(), vr = model.getVertical();
        int btn_txt_sz = model.getBtnTxtSize();
        int btn_counter = 0;
        int[][] horizontal = new int[vr][hr];
        int[][] vertical   = new int[hr][vr];

        // Creates 2-D button array
        for (int v = 0; v < vr; v++) {
            for (int h = 0; h < hr; h++) {
                int id = View.generateViewId();
                Button btn = new Button(this);

                btn.setId(id);
                btn.setTag(model.getBtnTagArr(btn_counter));
                btn.setText(model.getBtnTxtArr(btn_counter));
                btn.setTextSize(btn_txt_sz);
                layout.addView(btn);
                horizontal[v][h] = id;
                vertical[h][v]   = id;

                LayoutParams btn_params = btn.getLayoutParams();
                btn_params.width  = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                btn_params.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                btn.setLayoutParams(btn_params);
                btn.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL );

                btn.setOnClickListener(click);
                btn_counter++;
            }
        }

        //  Layout buttons horizontally and vertically
        for (int[] btn_id : horizontal) {
            set.createHorizontalChain(
                    WEST,
                    ConstraintSet.RIGHT,
                    EAST,
                    ConstraintSet.LEFT,
                    btn_id,
                    null,
                    ConstraintSet.CHAIN_SPREAD
            );
        }
        for (int[] btn_id : vertical) {
            set.createVerticalChain(
                    outputId,
                    ConstraintSet.BOTTOM,
                    SOUTH,
                    ConstraintSet.TOP,
                    btn_id,
                    null,
                    ConstraintSet.CHAIN_SPREAD
            );
        }
        set.applyTo(layout);
    }

    @Override
    public void modelPropertyChange(final PropertyChangeEvent event) {
        String propName = event.getPropertyName();
        String propValue = event.getNewValue().toString();
    }

    class ClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String btnTag = ((Button) view).getTag().toString();
            CalculatorState clickState = model.getState();

            handleClick(btnTag, clickState);
        }
    }
    public void handleClick(String tag, CalculatorState clickState) {
        String[] tagArr = tag.split("_");
        switch(tagArr[0]) {
            case "Number":
                switch(clickState) {
                    case CLEAR:
                        digitClicked(tagArr[1], state);
                        break;
                    default:
                        break;
                }
                break;
            case "Operation":
                switch(clickState) {
                    case OPERAND:
                        Log.i("operand", "word");
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }
    }
    public void digitClicked(String digit, CalculatorState state) {
        StringBuilder Old_Output = new StringBuilder();
        StringBuilder New_Output = new StringBuilder();
        BigDecimal Old_Number = new BigDecimal(Integer.parseInt(model.getOutputText().toString()));
        BigDecimal New_Number = new BigDecimal(BigInteger.ZERO);
        int number = Integer.parseInt(digit);

        if (Old_Output.length() < model.getMaxLength()) {
            if ((Old_Number.compareTo(BigDecimal.ZERO) == 0) && (number > 0)) {
                New_Output.append(number);
                New_Number = new BigDecimal(String.valueOf(New_Output));
            } else if (Old_Number.compareTo(BigDecimal.ZERO) != 0) {
                New_Output = new StringBuilder(Old_Output + digit);
                New_Number = new BigDecimal(String.valueOf(New_Output));
            }
            if (New_Output.length() <= model.getMaxLength()) {
                model.setOutputText(New_Output);
                model.setLeft(New_Number);
            }
        }
    }
}

// import java.math.BigDecimal;
// use BigDecimal for floating point numbers
// To add BigDecimal sum = LeftNumber.add(RightNumber)
// Create new BigDecimal values using strings