package com.example.geofencingtutorial;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

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
        if(intent.getBooleanExtra("estado",true)){
            try{
                estado = true;
                databaseAccess.addUser(document, estado);
            }catch (Exception e){
                Log.i("dyanamoDBapp", "error add user", e);
            }
        }else {
            try{
                estado = false;
                databaseAccess.addUser(document, estado);
            }catch (Exception e){
                Log.i("dyanamoDBapp", "error add user", e);
            }
        }
    }
}
