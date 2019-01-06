package jogi.de.mygps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.util.Log;


public class LocationListenerImpl implements LocationListener, View.OnClickListener {

    MainActivity ma;
    private LocationManager locationManager;
    private TextView tv_message;
    private TextView tvBearing;
    private TextView tvBearingTo;
    private TextView tvDistanceTo;
    private TextView tvMission;
    private Location destination;
    private EditText et_longitude;
    private EditText et_latitude;
    private Long last_update;
    private Button b_previous;
    private Button b_next;
    private int mission_counter = 0;
    private String[] mission_message;
    private Location[] mission_location;
    private String[] photographer;
    private Location actual_location;
    private Float f_distance_to_dest = Float.parseFloat("1000.0");
    private int used_mission_counter = 0;
    private Location lastLocation;
    private float track_count = 0;

    private final float mission_range = 25;

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
        tvMission = (TextView) ma.findViewById(R.id.tvMission);
        et_longitude = (EditText) ma.findViewById(R.id.et_longitute);
        et_latitude = (EditText) ma.findViewById(R.id.et_latitude);
        String s_long = et_longitude.getText().toString();
        String s_lat = et_latitude.getText().toString();

        //set here already a valid_location to avoid null pointer exceptions
        actual_location = new Location("location_home");
        actual_location.setLongitude(Double.parseDouble(s_long));
        actual_location.setLatitude(Double.parseDouble(s_lat));

        destination = new Location("location");
        destination.setLongitude(Double.parseDouble(s_long));
        destination.setLatitude(Double.parseDouble(s_lat));

        last_update = 1L;

        //initWTZ();
        //initTrain();
        initDrive();
        //initSpiel();
  }

    private void initTrain()
    {
        mission_message = new String[5];
        mission_location = new Location[5];
        mission_message[0] = "1. Arbeit";
        mission_location[0] = new Location("location1");
        mission_location[0].setLongitude(Double.parseDouble("8.505890"));
        mission_location[0].setLatitude(Double.parseDouble("50.568087"));


        mission_message[1] = "2. Bahnhof WTZ";
        mission_location[1] = new Location("location2");
        mission_location[1].setLongitude(Double.parseDouble("8.503924"));
        mission_location[1].setLatitude(Double.parseDouble("50.565803"));

        mission_message[2] = "3. Bahnhof Haiger";
        mission_location[2] = new Location("location3");
        mission_location[2].setLongitude(Double.parseDouble(" 8.221374"));
        mission_location[2].setLatitude(Double.parseDouble("50.741495"));

        mission_message[3] = "4. Parkplatz";
        mission_location[3] = new Location("location4");
        mission_location[3].setLongitude(Double.parseDouble("8.220256"));
        mission_location[3].setLatitude(Double.parseDouble("50.740873"));

        mission_message[4] = "5. Zuhause";
        mission_location[4] = new Location("location5");
        mission_location[4].setLongitude(Double.parseDouble("8.187390"));
        mission_location[4].setLatitude(Double.parseDouble("50.735454"));
    }

    private void initDrive()
    {
        mission_message = new String[5];
        mission_location = new Location[5];
        photographer    = new String[5];

        mission_message[0] = "1. Abfahrt Zuhause";
        mission_location[0] = new Location("location1");
        mission_location[0].setLongitude(Double.parseDouble( "8.187384"));
        mission_location[0].setLatitude(Double.parseDouble("50.735446"));
        photographer[0] = "Nathanael";

        mission_message[1] = "Hier Rechts ab zur Autobahn A45";
        mission_location[1] = new Location("location2");
        mission_location[1].setLongitude(Double.parseDouble("8.482959"));
        mission_location[1].setLatitude(Double.parseDouble("50.589889"));
        photographer[1] = "Mark";

        mission_message[2] = "Die Abfahrt von Ehringhausen";
        mission_location[2] = new Location("location2");
        mission_location[2].setLongitude(Double.parseDouble(" 8.385991"));
        mission_location[2].setLatitude(Double.parseDouble("50.61868"));
        photographer[2] = "Vincent";

        mission_message[3] = "Wir sind beim Kreisverkehr";
        mission_location[3] = new Location("location2");
        mission_location[3].setLongitude(Double.parseDouble("8.203260"));
        mission_location[3].setLatitude(Double.parseDouble("50.746983"));
        photographer[3] = "Maximilian";


        mission_message[4] = "Und jetzt geht es rechts nach Steinbach";
        mission_location[4] = new Location("location2");
        mission_location[4].setLongitude(Double.parseDouble("8.190974"));
        mission_location[4].setLatitude(Double.parseDouble("50.763999"));
        photographer[4] = "Tom";

    }

    public void initSpiel(){
        mission_message = new String[14];
        mission_location = new Location[14];
        photographer    = new String[14];

        mission_message[0] = "1. Macht ein Bild bei dem Ihr alle Springt"; //TODO
        mission_location[0] = new Location("location1");
        mission_location[0].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[0].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[0] = "Nathanael";

        mission_message[1] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[1] = new Location("location1");
        mission_location[1].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[1].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[1] = "Mark";

        mission_message[2] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[2] = new Location("location1");
        mission_location[2].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[2].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[2] = "Vincent";

        mission_message[3] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[3] = new Location("location1");
        mission_location[3].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[3].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[3] = "Maximilian";

        mission_message[4] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[4] = new Location("location1");
        mission_location[4].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[4].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[4] = "Tom";

        mission_message[5] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[5] = new Location("location1");
        mission_location[5].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[5].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[5] = "Felix";


        mission_message[6] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[6] = new Location("location1");
        mission_location[6].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[6].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[6] = "Nelson";

        mission_message[7] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[7] = new Location("location1");
        mission_location[7].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[7].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[7] = "Carla";

        mission_message[8] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[8] = new Location("location1");
        mission_location[8].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[8].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[8] = "Emmy";

        mission_message[9] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[9] = new Location("location1");
        mission_location[9].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[9].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[9] = "Ben";

        mission_message[10] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[10] = new Location("location1");
        mission_location[10].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[10].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[10] = "Matteo";

        mission_message[11] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[11] = new Location("location1");
        mission_location[11].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[11].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[11] = "Talha";

        mission_message[12] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[12] = new Location("location1");
        mission_location[12].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[12].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[12] = "Leo";

        mission_message[13] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[13] = new Location("location1");
        mission_location[13].setLongitude(Double.parseDouble( "8.485164")); //TODO
        mission_location[13].setLatitude(Double.parseDouble("50.581443")); //TODO
        photographer[13] = "Fynn";


    }

    public void initWTZ(){
        mission_message = new String[3];
        mission_location = new Location[3];
        mission_message[0] = "1. Macht ein Bild bei dem Ihr alle Springt";
        mission_location[0] = new Location("location1");
        mission_location[0].setLongitude(Double.parseDouble( "8.485164"));
        mission_location[0].setLatitude(Double.parseDouble("50.581443"));


        mission_message[1] = "2. Ein Bild, bei dem alle auf einem Bein stehen";
        mission_location[1] = new Location("location2");
        mission_location[1].setLongitude(Double.parseDouble("8.584234"));
        mission_location[1].setLatitude(Double.parseDouble("50.62078"));

        mission_message[2] = "3. Ein Photo, bei dem Ihr alle die Zunge rausstreckt";
        mission_location[2] = new Location("location2");
        mission_location[2].setLongitude(Double.parseDouble("8.505890"));
        mission_location[2].setLatitude(Double.parseDouble("50.568087"));
    }
    @Override
    public void onLocationChanged(Location location) {
        actual_location = location;
        if(last_update == 1L){
            last_update = location.getTime();
        }
        String s_long = et_longitude.getText().toString();
        String s_lat = et_latitude.getText().toString();

        if(lastLocation == null)
        {
            lastLocation = location;
        }

        float distance_to_last_location = actual_location.distanceTo(lastLocation);
        //TODO check if this works as pespected
        if(distance_to_last_location>10) {
            track_count = track_count + distance_to_last_location;
            lastLocation = actual_location;
        }
        destination.setLongitude(Double.parseDouble(s_long));
        destination.setLatitude(Double.parseDouble(s_lat));

        //TODO in methode auslagern !
        Float bearing_to_destination = actual_location.bearingTo(destination);
        if (bearing_to_destination <=0)
        {
            bearing_to_destination = 360+bearing_to_destination;
        }
        String s_bearing_to_destination = String.format("%.00f",bearing_to_destination);
        tvBearing.setText("Ihr geht: "+ actual_location.getBearing()+ " Grad");
        tvBearingTo.setText(s_bearing_to_destination+ " Grad");
        
        f_distance_to_dest = actual_location.distanceTo(destination);
        String s_distance_to_dest = String.format("%.00f",f_distance_to_dest);
        tvDistanceTo.setText(s_distance_to_dest+ " Meter");
        
        Float deviation = bearing_to_destination - actual_location.getBearing();
        if (deviation>=180.0)
        {
            deviation = deviation -360;
        }
        else if (deviation<=-180)
        {
            deviation = deviation +360;
        }
        Log.i("deviation","deviation"+deviation);
        if(f_distance_to_dest<mission_range) {
            tv_message.setText(mission_message[used_mission_counter]);
            tvMission.setText("Fotograph: "+ photographer[used_mission_counter]);
        }
        else{
            if((deviation>-10)&&(deviation<10)) {
                tv_message.setText("Geradeaus ! \n"+deviation);
            }
            else if((deviation>-90)&&(deviation<-10)) {
                tv_message.setText("<-- nach Links! \n"+deviation);
            }
            else if((deviation<90)&&(deviation>10)) {
                tv_message.setText("nach  Rechts! --> \n"+deviation);
            }
            else if((deviation>90)||(deviation<=-90)) {
                tv_message.setText("falsche Richtung ! \n"+deviation);
            }
            else{
                tv_message.setText("Deviation \n"+deviation);
            }
            String s_track_count = String.format("%.00f",this.track_count);
            tvMission.setText("Strecke " + s_track_count+ " Meter");
        }
        //String s_track_count = String.format("%.00f",this.track_count);
        //tvMission.setText("Strecke " + s_track_count+ " Meter");
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

        used_mission_counter = mission_counter; // just to have a valid value
        Log.i("f_distance_to_dest","f_distance_to_dest "+Float.toString(f_distance_to_dest));
        switch (v.getId()){
            case R.id.b_previous:
                mission_counter = mission_counter-1;
                // go one element back
                if(mission_counter < 0)
                    mission_counter = (mission_message.length-1);
                if(mission_counter >= mission_message.length)
                    mission_counter = (mission_message.length-1);
                used_mission_counter = mission_counter;
                f_distance_to_dest = actual_location.distanceTo(mission_location[mission_counter]);
                if(f_distance_to_dest<mission_range) {
                    tv_message.setText(mission_message[mission_counter]);
                }
                et_longitude.setText(Double.toString(mission_location[mission_counter].getLongitude()));
                et_latitude.setText(Double.toString(mission_location[mission_counter].getLatitude()));
                //mission_counter = mission_counter-1;
                break;
            case R.id.b_next:
                mission_counter = mission_counter+1;
                if(mission_counter >=(mission_message.length)||mission_counter <0)
                    mission_counter = 0;
                f_distance_to_dest = actual_location.distanceTo(mission_location[mission_counter]);
                used_mission_counter = mission_counter;
                if(f_distance_to_dest<mission_range) {
                    tv_message.setText(mission_message[mission_counter]);
                }
                 et_longitude.setText(Double.toString(mission_location[mission_counter].getLongitude()));
                et_latitude.setText(Double.toString(mission_location[mission_counter].getLatitude()));
                //mission_counter = mission_counter+1;
                break;
        }
        Float bearing_to_destination  = actual_location.bearingTo(mission_location[used_mission_counter]);
        //TODO in methode auslagern !
        if (bearing_to_destination <=0)
        {
            bearing_to_destination = 360+bearing_to_destination;
        }
        tvBearing.setText("Ihr geht: "+ actual_location.getBearing()+ " Grad");
        tvBearingTo.setText(bearing_to_destination+ " Grad");

        String s_distance_to_dest = String.format("%.00f",f_distance_to_dest);
        tvDistanceTo.setText(s_distance_to_dest+ " Meter");
        tvMission.setText("Aufgabe "+(used_mission_counter+1));
    }
}
