package ir.moderndata.states;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("States",MODE_PRIVATE);


        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (sharedPreferences.getBoolean("isLogin",false)){
                    getMemberTeam(sharedPreferences.getString("token","0"));

                }else{
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        }.start();
    }

    private void getMemberTeam(String token){
        ServerInterface serverInterface = new ServerInterface(SplashActivity.this);
        HashMap<String,String> params = new HashMap<>();
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization","Token "+token);
        serverInterface.Post("getMemberTeam", params, headers, new ServerInterface.responseListeneer() {
            @Override
            public void OnResponse(String content) {
                try{
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("status").equals("ok")){
                        JSONObject o = new JSONObject(obj.getString("data"));
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("TeamId",o.getString("TeamId"));
                        editor.putString("TeamName",o.getString("TeamName"));
                        editor.apply();
                        Intent intent = new Intent(SplashActivity.this,GetStartActivity.class);
                        startActivity(intent);
                    }
                }catch (JSONException jsx){}

            }
        }, new ServerInterface.ErrorListeneer() {
            @Override
            public void OnError(VolleyError error) {

            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
