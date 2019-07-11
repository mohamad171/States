package ir.moderndata.states.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import ir.moderndata.states.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CurrentRolesActivity extends AppCompatActivity {

    RadioGroup current_roll_chg;
    Button submit_current_roll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_roles);
        current_roll_chg = findViewById(R.id.current_roll_chg);
        submit_current_roll = findViewById(R.id.submit_current_roll);

        submit_current_roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_roll_chg.getCheckedRadioButtonId() == R.id.measure){
                    Intent intent = new Intent(CurrentRolesActivity.this,SampleActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
