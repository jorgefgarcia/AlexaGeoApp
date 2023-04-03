package com.example.geofencingtutorial.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.geofencingtutorial.MainActivity;
import com.example.geofencingtutorial.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import static com.example.geofencingtutorial.constantes.Constantes.*;

public class FragmentoCogiendoUbicacion extends Fragment /*implements LocationListener*/{

    private static final String TAG = "FragmentoCogiendoUbicac";
    private ProgressBar progressBar;
    private ImageView tickImage;
    private TextView ubicacionText;
    private LocationManager lm;
    private Bundle bundle;
    private FusedLocationProviderClient mFusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // Inicializamos lo necesario
        View view =  inflater.inflate(R.layout.fragmento_cogiendo_ubicacion, container, false);
        Button next = view.findViewById(R.id.buttonNext);
        progressBar = view.findViewById(R.id.progressBar);
        tickImage = view.findViewById(R.id.imageViewTick);
        ubicacionText = view.findViewById(R.id.ubicacion);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        bundle = new Bundle();

        //Desactivamos el botón, conseguimos la posición del usuario y activamos el botón
        searchLastLocation();
        waitProgress(next,progressBar, tickImage, ubicacionText);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentoCogiendoUbicacion_to_fragmentoMostrarUbicacion);
            }
        });
        return view;
    }

    //Método de juguete para simular que se tarda en coger ubicación
    private void waitProgress(Button button, ProgressBar progressBar, ImageView imageView, TextView textView){
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText("Ubicación encontrada");
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        }, 5000);
    }

    @SuppressLint("MissingPermission")
    private void searchLastLocation() {
        // Miramos si se dieron permisos
        if (checkPermissions()) {

            // Miramos si la localizacion está activada
            if (isLocationEnabled()) {
                //Enviar petición para comprobar su localización
                requestNewLocationData();
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            //Si los permisos no fueron dados pedimos permisos
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Inicializamos las peticiones para averiguar la nueva localización
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // Establecemos la petición en el fusedlocation
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            sendBundle(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };

    // Miramos los permisos
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.getMyContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Pedimos los permisos
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // Métood para mirar si la localización está activada
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) MainActivity.getMyContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // Si está bien
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                searchLastLocation();
            }
        }
    }

    //Método para enviar los datos al siguiente fragmento
    private void sendBundle(Double latitude, Double longitude){
        //Añadimos los datos al bundle y lo enviamos
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        Log.d(TAG, "onComplete: "+ latitude + " " + longitude);
        //Añadimos la información en el fragmentManager
        getParentFragmentManager().setFragmentResult("bundle",bundle);
    }
}
