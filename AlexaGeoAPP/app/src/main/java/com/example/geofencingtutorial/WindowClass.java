package com.example.geofencingtutorial;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

public class WindowClass extends Worker {

    public WindowClass(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        Document document = new Document();

        if(isStopped()){
            return Result.failure();
        }else{
            try{
                databaseAccess.addUser(document, true);
            }catch (Exception e){
                Log.i("dyanamoDBapp", "error add user", e);
            }
            return Result.success();
        }
    }
}
