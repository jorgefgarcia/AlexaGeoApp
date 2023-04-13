package com.example.geofencingtutorial;


import android.content.Context;
import android.content.Intent;
import com.google.android.gms.location.Geofence;


public class SendToDatabase{

    private Context context;
    private Intent serviceIntent;
    public SendToDatabase(Context context){
        this.context = context;
        this.serviceIntent = new Intent(context,MiIntentService.class);
    }


    public void addItemDB(int transitionTypes) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        switch (transitionTypes) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notificationHelper.sendHighPriorityNotification("Bienvenido!", "Perímetro de seguridad establecido en rojo. Peticiones activadas.", MapsActivity.class);
                serviceIntent.putExtra("estado",true);
                context.startService(serviceIntent);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                //notificationHelper.sendHighPriorityNotification("Dentro de la geofence", "", MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notificationHelper.sendHighPriorityNotification("Saliendo del perímetro de seguridad", "Peticiones desactivadas.", MapsActivity.class);
                serviceIntent.putExtra("estado",false);
                context.startService(serviceIntent);
                break;
        }
    }

    public void addItemDBWithoutNotification(){
        serviceIntent.putExtra("estado",false);
        context.startService(serviceIntent);
    }

}
