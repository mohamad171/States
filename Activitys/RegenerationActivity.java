package ir.moderndata.states.Activitys;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import ir.moderndata.states.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegenerationActivity extends AppCompatActivity {


    Button regen_add_row;
    Button regen_remove_row;
    LinearLayout main_layout;
    int Current_row = 1;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regeneration);
        regen_add_row = findViewById(R.id.regen_add_row);
        regen_remove_row = findViewById(R.id.regen_remove_row);
        main_layout = findViewById(R.id.main_layout);
        scrollView = findViewById(R.id.scrollView_reg);

        regen_remove_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Current_row != 1){

                    main_layout.removeViewAt(Current_row-1);
                    Current_row--;
                }

            }
        });
        regen_add_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(RegenerationActivity.this).inflate(R.layout.regeneration_table_row,main_layout,false);
                TextView reg_row_counter = view.findViewById(R.id.reg_row_counter);
                reg_row_counter.setText(String.valueOf(Current_row));
                main_layout.addView(view);
                Current_row++;
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
