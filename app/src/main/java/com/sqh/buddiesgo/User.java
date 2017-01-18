package com.sqh.buddiesgo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by demouser on 1/18/17.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public Double lati;
    public Double longi;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,Double lati, Double longi) {
        this.username = username;
        this.email = email;
        this.lati = lati;
        this.longi = longi;
    }

}
