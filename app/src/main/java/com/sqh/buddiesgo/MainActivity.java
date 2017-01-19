package com.sqh.buddiesgo;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    // GPS
    GPSTracker gps;

    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String mUsername;
    private String mPhotoUrl;
    private String mEmail;
    public static final String ANONYMOUS = "anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mEmail = mFirebaseUser.getEmail();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        // Creates event listeners for home page buttons
        final Button foodButton = (Button) findViewById(R.id.foodButton);
        foodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gps = new GPSTracker(MainActivity.this);
                newAct("Food", getLocation(gps));
                //startActivity(new Intent(MainActivity.this, BuddiesActivity.class));
            }
        });

        final Button hikingButton = (Button) findViewById(R.id.hikingButton);
        hikingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gps = new GPSTracker(MainActivity.this);
                newAct("Hiking", getLocation(gps));

            }
        });

        final Button movieButton = (Button) findViewById(R.id.movieButton);
        movieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gps = new GPSTracker(MainActivity.this);
                newAct("Movie", getLocation(gps));
            }
        });

    }

    public double[] getLocation(GPSTracker gps) {
        double[] location = new double[2];
        if (gps.canGetLocation()){
            double latitude = gps.getLatitude();
            location[0] = latitude;
            double longitude = gps.getLongitude();
            location[1] = longitude;
        } else {
            gps.showSettingsAlert();
        }
        return location;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates different intents based on interest selection
     * @param interest user's interest
     * @param location gps location in latitude and longitude
     */
    private void newAct(String interest, double[] location){
        Intent intent = new Intent(MainActivity.this, BuddiesActivity.class);
        Bundle b = new Bundle();
        b.putString("username",mUsername);
        b.putString("email",mEmail);
        b.putString("interest", interest);
        b.putDouble("latitude", location[0]);
        b.putDouble("longitude", location[1]);
        intent.putExtras(b);
        startActivity(intent);
        finish();

    }
}