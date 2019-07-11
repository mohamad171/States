package ir.moderndata.states.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import ir.moderndata.states.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MassActivity extends AppCompatActivity {

    Spinner AID58;
    LinearLayout AID60_layout;
    LinearLayout AID59_layout;
    Button submit_mass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass);
        AID58 = findViewById(R.id.AID58);
        AID59_layout = findViewById(R.id.AID59_layout);
        AID60_layout = findViewById(R.id.AID60_layout);
        submit_mass = findViewById(R.id.submit_mass);

        submit_mass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AID58.getSelectedItemPosition() == 3){
                    Intent intent = new Intent(MassActivity.this,RegenerationActivity.class);
                    startActivity(intent);
                }

            }
        });

        AID58.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 3){
                    AID60_layout.setVisibility(View.VISIBLE);
                    AID59_layout.setVisibility(View.GONE);
                }else{
                    AID59_layout.setVisibility(View.VISIBLE);
                    AID60_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private String getIdFromString(String value){
        return value.split("-")[0];
    }
}
