package com.example.geofencingtutorial;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.geofencingtutorial.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    //GEOFENCE HARDCODE MIRAR FORMA DE OBTENER RADIO E ID
    private float GEOFENCE_RADIUS = 100;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private static final String TAG = "MapsActivity";
    private SendToDatabase sendToDatabase;
    private LatLng initialPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
        sendToDatabase = new SendToDatabase(this);


        //establecemos un false en la BD al iniciar la APP. Lo comentamos para probar cosas
        //sendToDatabase.addItemDBWithoutNotification();

        //añadimos el listener para el GPSCheck
        onCheckGPS();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Conseguimos la ubicación del usuario y pedimos permisos
        enableUserLocation();

        //Cogemos el bundle
        Bundle bundle = getIntent().getExtras();
        Log.d(TAG, "onCreate: " + bundle.getDouble("latitude") + " " + bundle.getDouble("longitude"));
        initialPosition = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 16));

        //establecemos las geofences pulsando en el mapa
        mMap.setOnMapLongClickListener(this);

        //establecemos la geofence con el botón
        /*findViewById(R.id.SetGeofenceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                @SuppressLint("MissingPermission")
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng latlng = new LatLng(location.getLatitude(), location.getLatitude());
                tryAddingGeofence(latlng);
            }
        });*/
    }

    private void onCheckGPS(){
        registerReceiver(new GPSCheck(new GPSCheck.LocationCallBack() {
            @Override
            public void turnedOn() {
                Log.e("GpsReceiver","is turned on");
            }
            @Override
            public void turnedOff() {
                Log.e("GpsReceiver","is turned off");
            }
        }), new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }
    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //preguntamos por el permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Necesitamos mostrar un dialogo al usuario los permisos que necesitamos
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //tenemos los permisos
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                //No tenemos los permisos
            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //tenemos los permisos
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Toast.makeText(geofenceHelper, "You can add geofences", Toast.LENGTH_SHORT).show();
            } else {
                //No tenemos los permisos
                Toast.makeText(geofenceHelper, "Background location is necessary for geofences",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Añadimos el circulo pulsando en el mapa mirar de coger una latitud y longitud del usuario
    @Override
    public void onMapLongClick(@NonNull LatLng latlng) {
        if(Build.VERSION.SDK_INT >= 29){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){
                tryAddingGeofence(latlng);
            }else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        }else{
            tryAddingGeofence(latlng);
        }
    }

    private void tryAddingGeofence(LatLng latlng){
        mMap.clear();
        addMarker(latlng);
        addCircle(latlng, GEOFENCE_RADIUS);
        addGeofence(latlng, GEOFENCE_RADIUS);
    }

    private void addGeofence(LatLng latLng, float radius) {

        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }
    private void addMarker(LatLng latlng){
        MarkerOptions markerOptions = new MarkerOptions().position(latlng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latlng, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latlng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }
}