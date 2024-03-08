package com.example.project_1_calculator;

import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import java.beans.PropertyChangeEvent;

import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.project_1_calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AbstractView {
    private ActivityMainBinding binding;
    private DefaultController controller;
    private final int NORTH = R.id.guidelineTop;
    private final int SOUTH = R.id.guidelineBottom;
    private final int WEST = R.id.guidelineLeft;
    private final int EAST = R.id.guidelineRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initializeLayout();

        controller = new DefaultController();
        controller.addView(this);

        DefaultModel model = new DefaultModel();
        controller.addModel(model);
    }

    private void initializeLayout() {
        ConstraintLayout layout = binding.layout;
        ClickHandler click = new ClickHandler();
        int hr = getResources().getInteger(R.integer.chain_horizontal),
                vr = getResources().getInteger(R.integer.chain_vertical);
        int btn_txt_sz = getResources().getInteger(R.integer.button_text_size),
                op_txt_sz = getResources().getInteger(R.integer.output_text_size);

        /* **************************************************************** */
        /*                      CREATE OUTPUT TEXTVIEW                      */
        /* **************************************************************** */
        TextView output = new TextView(this);
        output.setId(View.generateViewId());
        output.setTag(getResources().getString(R.string.output_tag));
        output.setText(getResources().getString(R.string.output_txt));
        output.setTextSize(op_txt_sz);
        layout.addView(output);
        int outputId = output.getId();

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
        String[] btnTxtArr = getResources().getStringArray(R.array.button_text);
        String[] btnTagArr = getResources().getStringArray(R.array.button_tags);
        int btn_counter = 0;
        int[][] horizontal = new int[vr][hr];
        int[][] vertical   = new int[hr][vr];

        // Creates 2-D button array
        for (int v = 0; v < vr; v++) {
            for (int h = 0; h < hr; h++) {
                int id = View.generateViewId();
                Button btn = new Button(this);

                btn.setId(id);
                btn.setTag(btnTagArr[btn_counter]);
                btn.setText(btnTxtArr[btn_counter]);
                btn.setTextSize(btn_txt_sz);
                layout.addView(btn);
                horizontal[v][h] = id;
                vertical[h][v]   = id;

                LayoutParams btn_params = btn.getLayoutParams();
                btn_params.width  = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                btn_params.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                btn.setLayoutParams(btn_params);
                btn.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

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
        String propertyName = event.getPropertyName();

        if (propertyName.equals(DefaultController.OUTPUT_PROPERTY) ) {
            TextView output = binding.layout.findViewWithTag("output");
            output.setText(event.getNewValue().toString());
        }
    }

    class ClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String btn_tag = view.getTag().toString();
            Toast toast = Toast.makeText(binding.getRoot().getContext(), btn_tag, Toast.LENGTH_SHORT);
            toast.show();

            controller.changeOutputText(btn_tag);
        }
    }
}