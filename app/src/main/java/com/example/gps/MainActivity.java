package com.example.gps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
//import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Context context;
    //Button btnGetLoc;
    Location l;
    Location mel = new Location ("");
    Location syd = new Location ("");
    Location can = new Location ("");
    Location hob = new Location ("");
    Location sal = new Location ("");
    Location mec = new Location ("");
    int[] cityCounter = {1};
    ConstraintLayout constraintLayout;
    City melbourne;
    City sydney;
    City canberra;
    City hobart;
    City sale;
    //City mecca;
    Compass compass;
    City[] cityList;

    // Debug data
    private TextView azimuthDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        compass = new Compass(this);

        // Melbourne
        mel.setLatitude(-37.8136); mel.setLongitude(144.9631);
        melbourne = new City(mel, "Melbourne", this, cityCounter, constraintLayout, context);
        //Sydney
        syd.setLatitude(-33.8688); syd.setLongitude(151.2093);
        sydney = new City(syd, "Sydney", this, cityCounter, constraintLayout, context);
        //Canberra
        can.setLatitude(-35.2809); can.setLongitude(149.1300);
        canberra = new City(can, "Canberra", this, cityCounter, constraintLayout, context);
        //Hobart
        hob.setLatitude(-42.8821); hob.setLongitude(147.3272);
        hobart = new City(hob, "Hobart", this, cityCounter, constraintLayout, context);
        //Sale
        sal.setLatitude(-38.1026); sal.setLongitude(147.0730);
        sale = new City(sal, "Sale", this, cityCounter, constraintLayout, context);
        //Mecca
        //mec.setLatitude(21.3891); mec.setLongitude(39.8579);
        //mecca = new City(mec, "Mecca", this, cityCounter, constraintLayout, context);

        // Debug setup
        azimuthDebug = findViewById(R.id.textView);
        azimuthDebug.setText("");

        // Initiate repeating functions
        handler.postDelayed(r, delay);
    }

    // Repetition Handler
    final int fps = 60;
    final int delay = 1000 / fps;
    final Handler handler = new Handler();
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            // Repeating functions
            updateLocation();

            // Update Cities
            melbourne.update(l, compass.mAzimuth);
            sydney.update(l, compass.mAzimuth);
            canberra.update(l, compass.mAzimuth);
            hobart.update(l, compass.mAzimuth);
            sale.update(l, compass.mAzimuth);
            //mecca.update(l, compass.mAzimuth);

            // Debug data
            azimuthDebug.setText("Azimuth: " + Float.toString(compass.mAzimuth));

            // Repost
            handler.postDelayed(this, delay);
        }
    };

    public void updateLocation(){
        GPSTracker g = new GPSTracker(getApplicationContext());
        l = g.getLocation();
        //l.setLongitude(144.9631); l.setLatitude(-37.8136);
        //if (l != null){
        //}
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
