package com.sqh.buddiesgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BuddiesActivity extends AppCompatActivity {
    Button btnShowLocation;
    GPSTracker gps;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddies);

        btnShowLocation = (Button) findViewById(R.id.GetLoc);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        changeButtonName();

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(BuddiesActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()){
                    double lati = gps.getLatitude();
                    double longi = gps.getLongitude();
                    Log.d("lat",Double.toString(lati));
                    Log.d("longi",Double.toString(longi));


                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                } else {
                    gps.showSettingsAlert();
                }

            }
        });

    }
    private void writeNewUser(String userId, String name, String email,Double lati,Double longi) {
        User user = new User(name, email,lati,longi);

        mDatabase.child("users").child(userId).setValue(user);
    }
    private void changeButtonName(){
        Button getLoc = (Button)findViewById(R.id.GetLoc);

        Bundle b = getIntent().getExtras();
        String interest = ""; // or other values
        if(b != null) {
            interest = b.getString("interest");
            switch (interest) {
                case "food":
                    getLoc.setText(getResources().getString(R.string.food));
                    break;
                case "movie":
                    getLoc.setText(getResources().getString(R.string.movie));
                    break;
                case "hiking":
                    getLoc.setText(getResources().getString(R.string.hiking));
                    break;
            }

        }

    }
}
