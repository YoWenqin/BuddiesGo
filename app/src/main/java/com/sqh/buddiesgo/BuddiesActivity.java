package com.sqh.buddiesgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BuddiesActivity extends AppCompatActivity {
    Button btnShowLocation;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddies);

        btnShowLocation = (Button) findViewById(R.id.GetLoc);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(BuddiesActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){
                    double lati = gps.getLatitude();
                    double longi = gps.getLongitude();
                    Log.d("lat",Double.toString(lati));
                    Log.d("longi",Double.toString(longi));

                // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                }else{gps.showSettingsAlert();}

            }
        });

    }
}
