package ir.moderndata.states;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GroupActivity extends AppCompatActivity {

    ImageView selfie_img;
    TextView member1_edt;
    TextView member2_edt;
    TextView member3_edt;
    Button submit_group;
    String pictureImagePath;
    File file;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        selfie_img = findViewById(R.id.selfie_img);
        member1_edt = findViewById(R.id.member1_edt);
        member2_edt = findViewById(R.id.member2_edt);
        member3_edt = findViewById(R.id.member3_edt);
        submit_group = findViewById(R.id.submit_group);
        prefs = getSharedPreferences("Hamgan",MODE_PRIVATE);
        selfie_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCamera();
            }
        });
        submit_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGroup();
            }
        });
    }

    private void setGroup(){
        ServerInterface serverInterface = new ServerInterface(GroupActivity.this);
        HashMap<String,String> params = new HashMap<>();
        params.put("user1",member1_edt.getText().toString());
        params.put("user2",member2_edt.getText().toString());
        params.put("user3",member3_edt.getText().toString());
        params.put("teamImage",convert( imageView2Bitmap(selfie_img) ));
        params.put("teamid",prefs.getString("TeamId","0"));

        serverInterface.Post("setTeam", params, new ServerInterface.responseListeneer() {
            @Override
            public void OnResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("status").equals("ok")){
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean("isGroup",true);
                        edit.apply();
                        Toast.makeText(GroupActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException ex) {
                }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            try{
                file = new  File(pictureImagePath);
                if(file.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    selfie_img.setImageBitmap(myBitmap);

                }

            }catch (NullPointerException nux){
                Toast.makeText(this, nux.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
    private Bitmap imageView2Bitmap(ImageView view){
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        return bitmap;
    }
    private void OpenCamera(){
        if(ActivityCompat.checkSelfPermission(GroupActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(GroupActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(GroupActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GroupActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},2000);
        }else{
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
            File file = new File(pictureImagePath);
            Uri outputFileUri = Uri.fromFile(file);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            startActivityForResult(cameraIntent,1000);
        }
    }
}
