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
    public String getEmail(){
        return email;
    }
    public String getUsername(){
        return username;
    }
    public double distance(Double lat2, Double lon2){
        Double dlon = lon2 - longi;
        Double dlat = lat2 - lati;
        Double a = Math.pow((Math.sin(dlat/2)),2) + Math.cos(lati) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
        Double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) );
        Double d = 6371 * c;
        return d;
    }

}
