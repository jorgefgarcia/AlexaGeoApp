package com.example.geofencingtutorial;


import android.content.Context;
import android.content.Intent;
import com.google.android.gms.location.Geofence;
import static com.example.geofencingtutorial.constantes.Constantes.*;


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
                notificationHelper.sendHighPriorityNotification(TITLE_TRANSITION_ENTER, BODY_TRANSITION_ENTER);
                serviceIntent.putExtra("estado",true);
                context.startService(serviceIntent);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notificationHelper.sendHighPriorityNotification(TITLE_TRANSITION_EXIT, BODY_TRANSITION_EXIT);
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
