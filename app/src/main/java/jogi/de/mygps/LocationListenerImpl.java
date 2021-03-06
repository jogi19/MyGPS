package jogi.de.mygps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LocationListenerImpl implements LocationListener {

    MainActivity ma;
    private LocationManager locationManager;
    private TextView textview;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvAltitude;
    private TextView tvSpeed;
    private TextView tvBearing;
    private TextView tvBearingTo;
    private TextView tvDistanceTo;
    private Location home_location;
    private EditText et_longitude;
    private EditText et_latitude;
    private Long last_update;
    private Float last_distance_to_home;

    LocationListenerImpl(MainActivity ma){
        this.ma = ma;
        locationManager = (LocationManager) ma.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }
        catch(SecurityException se){
            se.printStackTrace();
        }

        ma.setContentView(R.layout.activity_main);
        textview = (TextView) ma.findViewById(R.id.tv_listener_events);
        tvLatitude = (TextView) ma.findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) ma.findViewById(R.id.tvLongitude);
        tvAltitude = (TextView) ma.findViewById(R.id.tvAltitude);
        tvSpeed = (TextView) ma.findViewById(R.id.tvSpeed);
        tvBearing = (TextView) ma.findViewById(R.id.tvBearing);
        tvBearingTo = (TextView) ma.findViewById(R.id.tvBearingTo);
        tvDistanceTo = (TextView) ma.findViewById(R.id.tvDistaceTo);

        et_longitude = (EditText) ma.findViewById(R.id.et_longitute);
        et_latitude = (EditText) ma.findViewById(R.id.et_latitude);
        String s_long = et_longitude.getText().toString();
        String s_lat = et_latitude.getText().toString();

        home_location = new Location("homelocation");
        home_location.setLongitude(Double.parseDouble(s_long));
        home_location.setLatitude(Double.parseDouble(s_lat));

        last_update = 1L;
        last_distance_to_home = 1.0f;

    }
    @Override
    public void onLocationChanged(Location location) {
        if(last_update == 1L){
            last_update = location.getTime();
            last_distance_to_home = location.distanceTo(home_location);
        }
        String s_long = et_longitude.getText().toString();
        String s_lat = et_latitude.getText().toString();

        home_location.setLongitude(Double.parseDouble(s_long));
        home_location.setLatitude(Double.parseDouble(s_lat));

        Double lat = location.getLatitude();
        String latText = String.format("Latitude: %7.4f",lat);
        tvLatitude.setText(latText);
        Double lon = location.getLongitude();
        String longText = String.format("Longitude:  %7.4f",lon);
        tvLongitude.setText(longText);
        Float bearing_to_home = location.bearingTo(home_location);
        if (bearing_to_home <=0)
        {
            bearing_to_home = 360+bearing_to_home;
        }
        Double alt = location.getAltitude();
        String altText = String.format("Altitude: %5.2f",alt);
        tvAltitude.setText(altText+ " m");

        String s_speed = String.format("Speed: %5.2f Km/h",3.6*location.getSpeed());
        tvSpeed.setText(s_speed);

        tvBearing.setText("Bearing: "+ location.getBearing()+ "'");
        String bthome_text = String.format("BtH: %3.1f '",bearing_to_home);
        tvBearingTo.setText(bthome_text);
        Float distance_to_home = location.distanceTo(home_location);
        if(distance_to_home>2000) {
            Float dis = distance_to_home/1000;
            String s = String.format("DtH: %8.3f  km",dis);
            tvDistanceTo.setText(s);
        }
        else {
            tvDistanceTo.setText("DtH: " + distance_to_home + " m");
        }
        Date locationDate = new Date(location.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy\nhh:mm:ss");
        String sDate = sdf.format(locationDate);
        //textview.setText("Last Update\n"+sDate);
        Float speed_to_home = (last_distance_to_home-location.distanceTo(home_location))/(location.getTime()-last_update);
        Float time_to_home = location.distanceTo(home_location) / speed_to_home*1000;
        int seconds = (int) (time_to_home / 1000) % 60 ;
        int minutes = (int) ((time_to_home / (1000*60)) % 60);
        int hours   = (int) ((time_to_home / (1000*60*60)) % 24);
        //textview.setText("Speed To Home\n"+speed_to_home*1000 + " m/s\nTime: "+hours+":"+minutes+":"+seconds);
        Float deviation = bearing_to_home - location.getBearing();
        if (deviation>=180.0)
        {
            deviation = deviation -360;
        }
        else if (deviation<=-180)
        {
            deviation = deviation +360;
        }
        if(deviation >-5 && deviation <5){
            textview.setText("Geradeaus ! \n"+deviation);
        }
        else if(deviation >=5 && deviation <45){
            textview.setText("Halbrechts \n"+deviation);
        }
        else if(deviation >=45 && deviation <135){
            textview.setText("Rechts\n"+deviation);
        }
        else if(deviation >=135 && deviation <=180){
            textview.setText("falsche Richtung ! \n"+deviation);
        }
        else if(deviation <=-135 && deviation >=-180){
            textview.setText("falsche Richtung ! \n"+deviation);
        }
        else if(deviation <=-45 && deviation >-135){
            textview.setText("Links\n"+deviation);
        }
        else if(deviation <=-5 && deviation >-45){
            textview.setText("Halblinks\n"+deviation);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        textview.setText("\"+provider+ "+status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        textview.setText("\nonProviderEnabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        textview.setText("\nonProviderDisabled: " + provider);
    }
}
