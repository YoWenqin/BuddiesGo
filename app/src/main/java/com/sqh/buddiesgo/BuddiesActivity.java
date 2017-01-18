package com.sqh.buddiesgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class BuddiesActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddies);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // This is here to test that the GPS is working
        TextView showLocationView = (TextView) findViewById(R.id.showLocation);
        Bundle b = getIntent().getExtras();
        double latitude = b.getDouble("latitude");
        double longitude = b.getDouble("longitude");
        showLocationView.setText(Double.toString(latitude)+", "+Double.toString(longitude));

        // Display title of interest
        changeInterestName();
    }

    private void writeNewUser(String userId, String name, String email,Double lati,Double longi) {
        User user = new User(name, email,lati,longi);
        mDatabase.child("users").child(userId).setValue(user);
    }

    /**
     * Change title of page according to interest
     */
    private void changeInterestName(){
        TextView displaysInterestView = (TextView) findViewById(R.id.displayInterest);

        Bundle b = getIntent().getExtras();
        String interest = "";
        if(b != null) {
            interest = b.getString("interest");
            switch (interest) {
                case "Food":
                    displaysInterestView.setText(getResources().getString(R.string.food));
                    break;
                case "Movie":
                    displaysInterestView.setText(getResources().getString(R.string.movie));
                    break;
                case "Hiking":
                    displaysInterestView.setText(getResources().getString(R.string.hiking));
                    break;
            }

        }

    }
}
