package com.sqh.buddiesgo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuddiesActivity extends AppCompatActivity {
    private static final String TAG = "BuddiesActivity";
    private DatabaseReference mDatabase;
    private ListView mBuddyView;
    String[] item1 = {"buddy1", "buddy2"};
    String[] item2 = {"distance1", "distance2"};
    private String interest;
    private double latitude;
    private double longitude;
    private String memail;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddies);

        // To get the information of the user
        TextView showLocationView = (TextView) findViewById(R.id.showLocation);
        Bundle b = getIntent().getExtras();
        latitude = b.getDouble("latitude");
        longitude = b.getDouble("longitude");
        memail = b.getString("email");
        username = b.getString("username");
        showLocationView.setText(Double.toString(latitude) + ", " + Double.toString(longitude));

        // Display title of interest
        changeInterestName();

        // Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // add user information to firebase
        writeNewUser(username,memail,latitude,longitude);

        //Initialize Views
        mBuddyView = (ListView) findViewById(R.id.budList);
        mBuddyView.setAdapter(new mAdapter(item1, item2));

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
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User buddy = dataSnapshot.child(interest).getValue(User.class);
                    Log.d("lon",Double.toString(longitude));
                    Log.d("user",buddy.getUsername());
                    Log.d("distance",Double.toString(buddy.distance(longitude,latitude)));
                }

                // [START_EXCLUDE]
                // Update buddies View
                //mBuddyView.setAdapter();

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

    private void writeNewUser( String name, String email, Double lati, Double longi) {
        User user = new User(name, email, lati, longi);
        String key = mDatabase.child(interest).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + interest + "/" + key, user);
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
        String[] Buddy, Distance;
        mAdapter() {
            Buddy = null;
            Distance = null;
        }

        public mAdapter(String[] item1, String[] item2) {
            Buddy = item1;
            Distance = item2;
        }

        @Override
        public int getCount() {
            return Buddy.length;
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
            buddy.setText(Buddy[position]);
            distance.setText(Distance[position]);
            return (row);
        }
    }
}
