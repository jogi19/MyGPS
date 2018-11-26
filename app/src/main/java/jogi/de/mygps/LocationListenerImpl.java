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

        home_location = new Location("homelocatiom");
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
        tvLatitude.setText("Latitude: " + lat);
        Double lon = location.getLongitude();
        tvLongitude.setText("Longitude: "+ lon);
        Float bearing_to_home = location.bearingTo(home_location);
        if (bearing_to_home <=0)
        {
            bearing_to_home = 360+bearing_to_home;
        }
        tvAltitude.setText("Altitude: "+ location.getAltitude()+ " m");
        tvSpeed.setText("Speed: " + location.getSpeed() + " m/s");
        tvBearing.setText("Bearing: "+ location.getBearing()+ "'");
        tvBearingTo.setText("BtH: "+bearing_to_home+ "'");
        tvDistanceTo.setText("DtH: "+ location.distanceTo(home_location)+ " m");
        Float distance_to_home = location.distanceTo(home_location);
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
        textview.setText("Deviation \n"+deviation);
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
