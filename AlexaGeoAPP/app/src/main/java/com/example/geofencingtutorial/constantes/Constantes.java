package com.example.geofencingtutorial.constantes;

import com.amazonaws.regions.Regions;

public final class Constantes {

    private Constantes(){}

    //Características GEOFENCE
    public static final String GEOFENCE_ID = "SOME_GEOFENCE_ID";
    public static final float GEOFENCE_RADIUS = 100;
    //Permisos localización aplicación
    public static final int  PERMISSION_ID = 44;
    public static final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    public static final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    //Servicios Amazon
    public static final String COGNITO_IDENTITY_POOL_ID = "us-east-1:3bf65834-1787-42af-a75d-7269e77ea4ca";
    public static final Regions COGNITO_IDENTITY_POOL_REGION =  Regions.US_EAST_1;
    public static final String DYNAMODB_TABLE = "tablaTutorial";
}
