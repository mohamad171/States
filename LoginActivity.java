package ir.moderndata.states;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button login_btn;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        sharedPreferences = getSharedPreferences("States",MODE_PRIVATE);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0 ){
                    Login();
                }else{
                    Toast.makeText(LoginActivity.this, "پر کردن تمامی فیلد ها اجباری است", Toast.LENGTH_LONG).show();
                }



            }
        });

    }
    private void Login(){
        ServerInterface serverInterface = new ServerInterface(LoginActivity.this);
        HashMap<String,String> params = new HashMap<>();
        params.put("username",username.getText().toString());
        params.put("password",password.getText().toString());

        serverInterface.Post("signin", params, new ServerInterface.responseListeneer() {
            @Override
            public void OnResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    String token = obj.getString("token");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin", true);
                    editor.putString("token", token);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, GetStartActivity.class);
                    startActivity(intent);

                } catch (JSONException ex) {
                    Toast.makeText(LoginActivity.this, "نام کاربری یا رمز عبور شما صحیح نمیباشد", Toast.LENGTH_SHORT).show();

                }
            }
        }, new ServerInterface.ErrorListeneer() {
            @Override
            public void OnError(VolleyError error) {
                if(error.networkResponse.statusCode == 400){
                    Toast.makeText(LoginActivity.this, "نام کاربری یا رمز عبور صحیح نمیباشد.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
