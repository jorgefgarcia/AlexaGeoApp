package com.example.geofencingtutorial;

import android.content.Context;
import android.util.Log;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.example.geofencingtutorial.constantes.Constantes.*;

public class DatabaseAccess{

    private Context context;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;

    private static volatile DatabaseAccess instance;


    private DatabaseAccess(Context context){
        this.context=context;
        credentialsProvider = new CognitoCachingCredentialsProvider(context, COGNITO_IDENTITY_POOL_ID, COGNITO_IDENTITY_POOL_REGION);
        dbClient= new AmazonDynamoDBClient(credentialsProvider);
        dbClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
    }

    //accedemos a una única instancia siempre
    public static synchronized DatabaseAccess getInstance(Context context){
        if(instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public boolean addUser(Document userContact, boolean estado){
        boolean check = false;
        int  customer_id= 100;

        //UTILIZAMOS LA FECHA PARA ALMACENARLA EN DYNAMOBD
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateString = dateFormat.format(new Date());
        Long currentDate = Long.parseLong(currentDateString);

        userContact.put("id",customer_id);
        userContact.put("date", currentDate);
        userContact.put("inRange", estado);

        try{
            dbTable.putItem(userContact);
            check = true;
        }catch(Exception e){
            Log.d("Database", e.getMessage());
        }

        return check;

    }

}