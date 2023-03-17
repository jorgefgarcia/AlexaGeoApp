package com.example.geofencingtutorial.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

public class FragmentoCogiendoUbicacion extends Fragment {

    private static final String TAG = "FragmentoCogiendoUbicac";
    private ProgressBar progressBar;
    private ImageView tickImage;
    private TextView ubicacionText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragmento_cogiendo_ubicacion, container, false);
        Button next = view.findViewById(R.id.buttonNext);
        progressBar = view.findViewById(R.id.progressBar);
        tickImage = view.findViewById(R.id.imageViewTick);
        ubicacionText = view.findViewById(R.id.ubicacion);

        //Desactivamos el botón, conseguimos la posición del usuario y activamos el botón
        getCurrentLocation();
        waitProgress(next,progressBar, tickImage, ubicacionText);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentoCogiendoUbicacion_to_fragmentoMostrarUbicacion);
            }
        });
        return view;
    }

    private void getCurrentLocation(){
        Bundle bundle = new Bundle();

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Conseguimos posición actual
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Añadimos los datos al bundle
        bundle.putDouble("latitude", location.getLatitude());
        bundle.putDouble("longitude", location.getLongitude());
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
