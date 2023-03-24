package com.example.geofencingtutorial.fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import com.example.geofencingtutorial.MapsActivity;
import com.example.geofencingtutorial.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentoMostrarUbicacion extends Fragment {
    private static final String TAG = "FragmentoMostrarUbicaci";
    private Geocoder geocoder;
    private List<Address> direcciones;
    private TextView mostrarDireccion;
    private Button yesButton, noButton;
    private Intent intentMaps;

    private Double latitude, longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_mostrar_ubicacion, container, false);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        direcciones = new ArrayList<>();
        intentMaps = new Intent(getActivity(), MapsActivity.class);
        mostrarDireccion = view.findViewById(R.id.direccion);
        yesButton = view.findViewById(R.id.buttonYes);
        noButton = view.findViewById(R.id.buttonNo);

        //Cogemos la información del FragmentManager
        getParentFragmentManager().setFragmentResultListener("bundle", this, new FragmentResultListener() {

            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                //Double latitude = bundle.getDouble("latitude");
                //Double longitude = bundle.getDouble("longitude");
                setLatitude(bundle.getDouble("latitude"));
                setLongitude(bundle.getDouble("longitude"));
                getCompleteAddress(getLongitude(), getLatitude(), mostrarDireccion);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentoMostrarUbicacion_to_fragmentoPrimario);
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundlemaps = new Bundle();
                bundlemaps.putDouble("latitude",getLatitude());
                bundlemaps.putDouble("longitude", getLongitude());
                intentMaps.putExtras(bundlemaps);
                startActivity(intentMaps);
            }
        });
        return view.getRootView();
    }

    private void getCompleteAddress(Double longitude, Double latitude, TextView textView) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    direcciones = geocoder.getFromLocation(latitude, longitude, 1);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(direcciones.get(0).getAddressLine(0));
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();
        thread.interrupt();
    }
}
