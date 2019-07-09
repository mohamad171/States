package ir.moderndata.states;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    Button end_route;
    Button dont_access;
    GPSTracker gps;
    Location location;
    Timer timer;
    TextView location_latlng;

    LinearLayout img_layout;
    LinearLayout spinner_layout;
    Button submit_dont_access_btn;
    String pictureImagePath;
    File file;
    ImageView selfie_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        end_route = findViewById(R.id.end_route);
        dont_access = findViewById(R.id.dont_access);
        location_latlng = findViewById(R.id.location_latlng);
        img_layout = findViewById(R.id.img_layout);
        spinner_layout = findViewById(R.id.spinner_layout);
        submit_dont_access_btn = findViewById(R.id.submit_dont_access_btn);
        selfie_img = findViewById(R.id.selfie_img);
        selfie_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCamera();
            }
        });

        location_latlng.setText("مختصات مقصد: "+getIntent().getStringExtra("ProjectLocation"));
        dont_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_layout.setVisibility(View.VISIBLE);
                img_layout.setVisibility(View.VISIBLE);
            }
        });
        submit_dont_access_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,CurrentRolesActivity.class);
                startActivity(intent);

            }
        });
        end_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
//                List<Locations> locations = SQLite.select().from(Locations.class).queryList();
//                int counter = locations.size();
//                for(int i = 0;i<locations.size();i++){
//                    setLocations(locations.get(i).Id,locations.get(i).LatLng,String.valueOf(locations.get(i).ProjectId),String.valueOf(locations.get(i).TeamId));
//                    counter--;
//                }





            }
        });



        final Handler handler = new Handler();
        timer = new Timer();
        final TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            location = gps.getLocation();
                            Locations loc = new Locations();
                            loc.LatLng = location.getLatitude()+","+location.getLongitude();
                            loc.ProjectId = Integer.parseInt(getIntent().getStringExtra("ProjectId"));
                            loc.TeamId = Integer.parseInt(getIntent().getStringExtra("TeamId"));
                            loc.save();

                        } catch (Exception e) {
                            Toast.makeText(StartActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        gps = new GPSTracker(StartActivity.this, new GPSTracker.ProviderEnabled() {
            @Override
            public void Enabled() {
                timer.schedule(doAsynchronousTask, 0, 60000);
            }
        }, new GPSTracker.ProviderDisabled() {
            @Override
            public void Disabled() {
                timer.cancel();
                gps.showSettingsAlert();
            }
        });


        timer.schedule(doAsynchronousTask, 0, 60000);
        end_route = findViewById(R.id.end_route);
    }
    private void setLocations(final int recId, String location, String projectId, String teamId){
        timer.cancel();

        ServerInterface serverInterface = new ServerInterface(StartActivity.this);
        HashMap<String,String> params = new HashMap<>();
        params.put("location",location);
        serverInterface.Post("setLocations/" + teamId + "/" + projectId, params, new ServerInterface.responseListeneer() {
            @Override
            public void OnResponse(String content) {
                try{
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("status").equals("ok")){
                        SQLite.select().from(Locations.class).where(Locations_Table.Id.eq(recId)).querySingle().delete();
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
    private void OpenCamera(){
        if(ActivityCompat.checkSelfPermission(StartActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(StartActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(StartActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(StartActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},2000);
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
