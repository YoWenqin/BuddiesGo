package com.sqh.buddiesgo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by demouser on 1/18/17.
 */

@IgnoreExtraProperties
public class User {

    private String username;
    private String email;
    private Double lati;
    private Double longi;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, Double lati, Double longi) {
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
    public double getLati (){return lati;}
    public double getLongi (){return longi;}
    public double distance(Double lat2, Double lon2){
        double earthRadius = 6371; //meters
        double dLat = Math.toRadians(lat2-lati);
        double dLng = Math.toRadians(lon2-longi);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lati)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }


}
