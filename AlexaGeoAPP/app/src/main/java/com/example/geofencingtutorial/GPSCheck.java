package com.example.geofencingtutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.google.android.gms.location.Geofence;

public class GPSCheck extends BroadcastReceiver {
    //Listener para detectar cuando el usuario desactiva el GPS.
    public interface LocationCallBack {
        void turnedOn();
        void turnedOff();
    }

    private final LocationCallBack locationCallBack;

    public GPSCheck(LocationCallBack iLocationCallBack){
        this.locationCallBack = iLocationCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        SendToDatabase sendToDatabase = new SendToDatabase(context);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            sendToDatabase.addItemDBWithoutNotification();
        }
    }
}
