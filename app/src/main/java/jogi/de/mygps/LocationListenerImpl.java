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
    private TextView tv_message;
    private TextView tvBearing;
    private TextView tvBearingTo;
    private TextView tvDistanceTo;
    private Location destination;
    private EditText et_longitude;
    private EditText et_latitude;
    private Long last_update;
    private Button b_previous;
    private Button b_next;
    private int mission_counter = 0;
    private String[] mission_message;
    private Location[] mission_location;
    private Location actual_location;


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
        
        tv_message = (TextView) ma.findViewById(R.id.tv_listener_events);
        tvBearing = (TextView) ma.findViewById(R.id.tvBearing);
        tvBearingTo = (TextView) ma.findViewById(R.id.tvBearingTo);
        tvDistanceTo = (TextView) ma.findViewById(R.id.tvDistaceTo);

        et_longitude = (EditText) ma.findViewById(R.id.et_longitute);
        et_latitude = (EditText) ma.findViewById(R.id.et_latitude);
        String s_long = et_longitude.getText().toString();
        String s_lat = et_latitude.getText().toString();

        destination = new Location("location");
        destination.setLongitude(Double.parseDouble(s_long));
        destination.setLatitude(Double.parseDouble(s_lat));

        last_update = 1L;

        mission_message = new String[3];
        mission_location = new Location[3];
        mission_message[0] = "1. Macht ein Bild bei dem Ihr alle Spring";
        mission_location[0] = new Location("location1");
        mission_location[0].setLongitude(Double.parseDouble( "8.485164"));
        mission_location[0].setLatitude(Double.parseDouble("50.581443"));


        mission_message[1] = "2. Ein Bild, bei dem alle auf einem Bein stehen";
        mission_location[1] = new Location("location2");
        mission_location[1].setLongitude(Double.parseDouble("8.584234"));
        mission_location[1].setLatitude(Double.parseDouble("50.62078"));

        mission_message[2] = "3. Ein Photo, bei dem Ihr alle die Zunge rausstreckt";
        mission_location[2] = new Location("location2");
        mission_location[2].setLongitude(Double.parseDouble("8.609065"));
        mission_location[2].setLatitude(Double.parseDouble("50.700378"));

    }

    private void initGame()
    {


    }
    @Override
    public void onLocationChanged(Location location) {
        actual_location = location;
        if(last_update == 1L){
            last_update = location.getTime();
        }
        String s_long = et_longitude.getText().toString();
        String s_lat = et_latitude.getText().toString();

        destination.setLongitude(Double.parseDouble(s_long));
        destination.setLatitude(Double.parseDouble(s_lat));

        //TODO in methode auslagern !
        Float bearing_to_destination = actual_location.bearingTo(destination);
        if (bearing_to_destination <=0)
        {
            bearing_to_destination = 360+bearing_to_destination;
        }
        tvBearing.setText("Aktuelle Richtung: "+ actual_location.getBearing()+ "'");
        tvBearingTo.setText("Ihr muesst: "+bearing_to_destination+ "'");
        
        Float f_distance_to_dest = actual_location.distanceTo(destination);
        String s_distance_to_dest = String.format("%.00f",f_distance_to_dest);
        tvDistanceTo.setText("Entfernung: "+ s_distance_to_dest+ " m");
        
        Float deviation = bearing_to_destination - actual_location.getBearing();
        if (deviation>=180.0)
        {
            deviation = deviation -360;
        }
        else if (deviation<=-180)
        {
            deviation = deviation +360;
        }
        tv_message.setText("Deviation \n"+deviation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        tv_message.setText("\"+provider+ "+status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        tv_message.setText("\nonProviderEnabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        tv_message.setText("\nonProviderDisabled: " + provider);
    }

    public void onClick(android.view.View v){
        int used_mission_counter = mission_counter; // just to have a valid value
        switch (v.getId()){
            case R.id.b_previous:
                // go one element back
                if(mission_counter < 0)
                    mission_counter = (mission_message.length-1);
                if(mission_counter >= mission_message.length)
                    mission_counter = (mission_message.length-1);
                tv_message.setText(mission_message[mission_counter]);
                et_longitude.setText(Double.toString(mission_location[mission_counter].getLongitude()));
                et_latitude.setText(Double.toString(mission_location[mission_counter].getLatitude()));
                used_mission_counter = mission_counter;
                mission_counter = mission_counter-1;
                break;
            case R.id.b_next:
                if(mission_counter >=(mission_message.length)||mission_counter <0)
                    mission_counter = 0;

                tv_message.setText(mission_message[mission_counter]);
                et_longitude.setText(Double.toString(mission_location[mission_counter].getLongitude()));
                et_latitude.setText(Double.toString(mission_location[mission_counter].getLatitude()));
                used_mission_counter = mission_counter;
                mission_counter = mission_counter+1;
                break;
        }
        Float bearing_to_destination  = actual_location.bearingTo(mission_location[used_mission_counter]);
        Float f_distance_to_dest = actual_location.distanceTo(mission_location[used_mission_counter]);

        //TODO in methode auslagern !
        if (bearing_to_destination <=0)
        {
            bearing_to_destination = 360+bearing_to_destination;
        }
        tvBearing.setText("Aktuelle Richtung: "+ actual_location.getBearing()+ "'");
        tvBearingTo.setText("Ihr muesst: "+bearing_to_destination+ "'");

        String s_distance_to_dest = String.format("%.00f",f_distance_to_dest);
        tvDistanceTo.setText("Entfernung: "+ s_distance_to_dest+ " m");
    }
}
