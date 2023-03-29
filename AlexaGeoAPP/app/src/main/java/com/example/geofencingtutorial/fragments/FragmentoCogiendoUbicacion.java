package com.example.geofencingtutorial.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.geofencingtutorial.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

public class FragmentoCogiendoUbicacion extends Fragment implements LocationListener{

    private static final String TAG = "FragmentoCogiendoUbicac";
    private ProgressBar progressBar;
    private ImageView tickImage;
    private TextView ubicacionText;
    private LocationManager lm;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragmento_cogiendo_ubicacion, container, false);
        Button next = view.findViewById(R.id.buttonNext);
        progressBar = view.findViewById(R.id.progressBar);
        tickImage = view.findViewById(R.id.imageViewTick);
        ubicacionText = view.findViewById(R.id.ubicacion);

        //Desactivamos el botón, conseguimos la posición del usuario y activamos el botón
        localizacionActual();
        waitProgress(next,progressBar, tickImage, ubicacionText);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentoCogiendoUbicacion_to_fragmentoMostrarUbicacion);
            }
        });
        return view;
    }

    private void localizacionActual(){
        bundle = new Bundle();
        lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lm.removeUpdates(this);
        //Añadimos los datos al bundle
        bundle.putDouble("latitude", location.getLatitude());
        bundle.putDouble("longitude", location.getLongitude());
        Log.d(TAG, "getCurrentLocation: " + location.getLatitude() + " " + location.getLongitude());
        //Añadimos la información en el fragmentManager
        getParentFragmentManager().setFragmentResult("bundle",bundle);
    }


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
}
