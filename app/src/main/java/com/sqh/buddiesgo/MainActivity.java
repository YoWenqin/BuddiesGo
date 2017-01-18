package com.sqh.buddiesgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates event listeners for home page buttons
        final Button foodButton = (Button) findViewById(R.id.foodButton);
        foodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Perform action on click
                // Call food buddy page
                startActivity(new Intent(MainActivity.this, BuddiesActivity.class));
            }
        });

        final Button hikingButton = (Button) findViewById(R.id.hikingButton);
        hikingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Call hiking buddy page
            }
        });

        final Button movieButton = (Button) findViewById(R.id.movieButton);
        movieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Call movie buddy page
            }
        });
    }
}
