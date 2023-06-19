package com.example.geofencingtutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class GPSCheck extends BroadcastReceiver {

    private final LocationCallBack locationCallBack;
    public interface LocationCallBack {
        void turnedOn();
        void turnedOff();
    }

    public GPSCheck(LocationCallBack iLocationCallBack){
        this.locationCallBack = iLocationCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        LocationManager locationManager = (LocationManager) context
                                            .getSystemService(context.LOCATION_SERVICE);
        SendToDatabase sendToDatabase = new SendToDatabase(context);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            sendToDatabase.addItemDBWithoutNotification();
        }
    }
}
