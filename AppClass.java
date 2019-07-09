package ir.moderndata.states;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AppClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iransans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


    }
}
