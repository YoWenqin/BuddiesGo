package com.sqh.buddiesgo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class BuddiesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "BuddiesActivity";
    private DatabaseReference mDatabase;
    private ListView mBuddyView;

    public String[] buddy = {"hello"};
    public String distance="1";

    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddies);

        // This is here to test that the GPS is working
        TextView showLocationView = (TextView) findViewById(R.id.showLocation);
        Bundle b = getIntent().getExtras();
        double latitude = b.getDouble("latitude");
        double longitude = b.getDouble("longitude");
        showLocationView.setText(Double.toString(latitude)+", "+Double.toString(longitude));

        // Display title of interest
        changeInterestName();

        // Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference();


        //Initialize Views
        mBuddyView = (ListView) findViewById(R.id.budList);

        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns ={ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {android.R.id.text1}; //The TextView in simple_list_item_1

        //Create an empty adapter we will use to display the loaded data.
        //We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, fromColumns,toViews,0);
        mBuddyView.setAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener buddyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User buddy = dataSnapshot.getValue(User.class);
                // [START_EXCLUDE]
                // Update buddies View
                mBuddyView.setAdapter(mAdapter);

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

    private void writeNewUser(String userId, String name, String email, Double lati, Double longi) {
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

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                buddy, distance, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }
}
