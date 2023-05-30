package com.example.geofencingtutorial;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

import java.util.concurrent.TimeUnit;

public class MiIntentService extends IntentService {
    private static final String TAG = "MiIntentService";
    private boolean estado = false;
    public MiIntentService() {
        super("MiIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getBaseContext());
        Document document = new Document();
        WorkManager workManager = WorkManager.getInstance();
        PeriodicWorkRequest uploadDatabasePetition = new PeriodicWorkRequest.Builder(WindowClass.class, 15, TimeUnit.MINUTES).build();

        if(intent.getBooleanExtra("estado",true)){
            //Establecemos el trabajador en la cola
            workManager.enqueue(uploadDatabasePetition);

        }else {
            //Cancelamos los trabajadores de la cola
            workManager.cancelAllWork();
            try{
                estado = false;
                databaseAccess.addUser(document, estado);
            }catch (Exception e){
                Log.i("dyanamoDBapp", "error add user", e);
            }
        }
    }
}
