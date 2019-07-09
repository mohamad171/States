package ir.moderndata.states;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    Button get_projects;
    LinearLayout projects_table;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_projects = findViewById(R.id.get_projects);
        projects_table = findViewById(R.id.projects_table);
        prefs = getSharedPreferences("States",MODE_PRIVATE);


        get_projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProjects();
            }
        });


    }
    private void getProjects(){
        projects_table.removeAllViews();
        ServerInterface serverInterface = new ServerInterface(MainActivity.this);
        serverInterface.Get("getproject/"+prefs.getString("TeamId","0"), new ServerInterface.responseListeneer() {
            @Override
            public void OnResponse(String content) {
                try{
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("status").equals("faild")){
                        Toast.makeText(MainActivity.this, "تیم مورد نظر یافت نشد", Toast.LENGTH_SHORT).show();

                    }else if(obj.getString("status").equals("empty")){
                        Toast.makeText(MainActivity.this, "پروژه ای برای این تیم تعریف نشده است", Toast.LENGTH_SHORT).show();

                    }else{
                        final JSONObject c = new JSONObject(obj.getString("data"));


                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.team_table_layout,projects_table,false);
                        final TextView project_id = view.findViewById(R.id.project_id);
                        TextView project_name = view.findViewById(R.id.project_name);
                        TextView project_team = view.findViewById(R.id.project_team);
                        TextView project_latUpdate = view.findViewById(R.id.project_latUpdate);
                        Button target_location = view.findViewById(R.id.target_location);
                        target_location.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    Intent intent = new Intent(MainActivity.this,StartActivity.class);
                                    intent.putExtra("TeamId",prefs.getString("TeamId","0"));
                                    intent.putExtra("ProjectId",project_id.getText().toString());
                                    intent.putExtra("ProjectLocation",c.getString("TargetLocation"));
                                    startActivity(intent);
                                }catch (JSONException jsx){}

                            }
                        });
                        project_id.setText(c.getString("id"));
                        project_name.setText(c.getString("ProjectName"));
                        project_team.setText(c.getString("TeamName"));
                        project_latUpdate.setText(c.getString("LastUpdate"));
                        projects_table.addView(view);
                    }



                }catch (JSONException jsx){
                    Toast.makeText(MainActivity.this, jsx.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
