package jogi.de.mygps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;



public class LocationListenerImpl implements LocationListener, View.OnClickListener {

    MainActivity ma;
    private LocationManager locationManager;
    private TextView textview;
    private TextView tvBearing;
    private TextView tvBearingTo;
    private TextView tvDistanceTo;
    private Location destination;
    private EditText et_longitude;
    private EditText et_latitude;
    private Long last_update;
    private Float last_distance_to_home;
    private Button b_previous;
    private Button b_next;
    private int mission_counter = 0;
    private String[] mission;

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
        b_previous = (Button) ma.findViewById(R.id.b_previous);
        b_next = (Button) ma.findViewById(R.id.b_next);
        b_previous.setOnClickListener(this);
        b_next.setOnClickListener(this);
        
        textview = (TextView) ma.findViewById(R.id.tv_listener_events);
        tvBearing = (TextView) ma.findViewById(R.id.tvBearing);
        tvBearingTo = (TextView) ma.findViewById(R.id.tvBearingTo);
        tvDistanceTo = (TextView) ma.findViewById(R.id.tvDistaceTo);

        et_longitude = (EditText) ma.findViewById(R.id.et_longitute);
        et_latitude = (EditText) ma.findViewById(R.id.et_latitude);
        String s_long = et_longitude.getText().toString();
        String s_lat = et_latitude.getText().toString();

        destination = new Location("homelocatiom");
        destination.setLongitude(Double.parseDouble(s_long));
        destination.setLatitude(Double.parseDouble(s_lat));

        last_update = 1L;
        last_distance_to_home = 1.0f;
        mission = new String[3];
        mission[0] = "1. Macht ein Bild bei dem Ihr alle Spring";
        mission[1] = "2. Ein Bild, bei dem alle auf einem Bein stehen";
        mission[2] = "3. Ein Photo, bei dem Ihr alle die Zunge rausstreckt";

    }
    @Override
    public void onLocationChanged(Location location) {
        if(last_update == 1L){
            last_update = location.getTime();
            last_distance_to_home = location.distanceTo(destination);
        }
        String s_long = et_longitude.getText().toString();
        String s_lat = et_latitude.getText().toString();

        destination.setLongitude(Double.parseDouble(s_long));
        destination.setLatitude(Double.parseDouble(s_lat));

        Double lat = location.getLatitude();
        Double lon = location.getLongitude();
        Float bearing_to_destination = location.bearingTo(destination);
        if (bearing_to_destination <=0)
        {
            bearing_to_destination = 360+bearing_to_destination;
        }
        tvBearing.setText("Aktuelle Richtung: "+ location.getBearing()+ "'");
        tvBearingTo.setText("Ihr muesst: "+bearing_to_destination+ "'");
        tvDistanceTo.setText("Entfernung: "+ location.distanceTo(destination)+ " m");
        Float distance_to_home = location.distanceTo(destination);
        Date locationDate = new Date(location.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy\nhh:mm:ss");
        String sDate = sdf.format(locationDate);
        //textview.setText("Last Update\n"+sDate);
        Float speed_to_home = (last_distance_to_home-location.distanceTo(destination))/(location.getTime()-last_update);
        Float time_to_home = location.distanceTo(destination) / speed_to_home*1000;
        int seconds = (int) (time_to_home / 1000) % 60 ;
        int minutes = (int) ((time_to_home / (1000*60)) % 60);
        int hours   = (int) ((time_to_home / (1000*60*60)) % 24);
        //textview.setText("Speed To Home\n"+speed_to_home*1000 + " m/s\nTime: "+hours+":"+minutes+":"+seconds);
        Float deviation = bearing_to_destination - location.getBearing();
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

    public void onClick(android.view.View v){
        switch (v.getId()){
            case R.id.b_previous:
                // go on element back
                if(mission_counter < 0)
                    mission_counter = (mission.length-1);
                textview.setText(mission[mission_counter]);
                mission_counter = mission_counter-1;
                break;
            case R.id.b_next:
                if(mission_counter >=(mission.length)||mission_counter <0)
                    mission_counter = 0;

                textview.setText(mission[mission_counter]);
                mission_counter = mission_counter+1;

                break;
        }
    }
}
