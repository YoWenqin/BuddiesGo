package com.sqh.buddiesgo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuddiesActivity extends AppCompatActivity {
    private static final String TAG = "BuddiesActivity";
    private DatabaseReference mDatabase;

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> distances = new ArrayList<String>();
    private String interest;
    private double lati;
    private double longi;
    private String memail;
    private String username;
    private ListView mBuddyView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddies);

        // Event listener for back button, go back to home page
        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(BuddiesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // To get the information of the user
        //TextView showLocationView = (TextView) findViewById(R.id.showLocation);
        Bundle b = getIntent().getExtras();
        lati = b.getDouble("latitude");
        longi = b.getDouble("longitude");
        memail = b.getString("email");
        username = b.getString("username");


        user = new User(username,memail,lati,longi);
        showLocationView.setText(Double.toString(lati) + ", " + Double.toString(longi));
        //showLocationView.setText(Double.toString(latitude) + ", " + Double.toString(longitude));

        // Display title of interest
        changeInterestName();

        // Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // add user information to firebase
        //writeNewUser(username,memail,lati,longitude);

        //Initialize Views
        mBuddyView = (ListView) findViewById(R.id.budList);
        mBuddyView.setAdapter(new mAdapter(names, distances));

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener buddyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object and use the values to update the UI
                names = new ArrayList<String>();
                distances = new ArrayList<String>();

                DataSnapshot children = dataSnapshot.child(interest);
                if(children.hasChild(username)){
                    mDatabase.child(interest).child(username).setValue(user);
                }else{
                    writeNewUser(user);
                }
                for (DataSnapshot postSnapshot: children.getChildren()) {
                    User buddy = postSnapshot.getValue(User.class);
                    if (buddy.distance(lati,longi)<3 && !buddy.getEmail().equals(memail)){
                        names.add(buddy.getUsername());
                        double distance = buddy.distance(lati,longi);
                        DecimalFormat df = new DecimalFormat("#.####");
                        distances.add(df.format(distance));
                    }
                }

                // [START_EXCLUDE]
                // Update buddies View

                mBuddyView.setAdapter(new mAdapter(names, distances));

                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(BuddiesActivity.this, "Failed to load list.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mDatabase.addValueEventListener(buddyListener);
        // [END post_value_event_listener]

    }

    private void writeNewUser( User user) {
        //String key = mDatabase.child(interest).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + interest + "/" + user.getUsername(), user);
        mDatabase.updateChildren(childUpdates);
    }

    /**
     * Change title of page according to interest
     */
    private void changeInterestName() {
        TextView displaysInterestView = (TextView) findViewById(R.id.displayInterest);

        Bundle b = getIntent().getExtras();
        if (b != null) {
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

    class mAdapter extends BaseAdapter {

        ArrayList<String> Buddy, Distance;
        mAdapter() {
            Buddy = null;
            Distance = null;
        }

        public mAdapter(ArrayList<String> item1, ArrayList<String> item2) {
            Buddy = item1;
            Distance = item2;
        }

        @Override
        public int getCount() {
            return Buddy.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.row, parent, false);
            TextView buddy, distance;
            buddy = (TextView) row.findViewById(R.id.buddy);
            distance = (TextView) row.findViewById(R.id.distance);
            buddy.setText(Buddy.get(position));
            distance.setText(Distance.get(position));
            return (row);
        }
    }

}
