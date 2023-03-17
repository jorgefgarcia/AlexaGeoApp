package com.example.geofencingtutorial.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_mostrar_ubicacion, container, false);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        direcciones = new ArrayList<>();
        mostrarDireccion = view.findViewById(R.id.direccion);

        //Cogemos la información del FragmentManager
        getParentFragmentManager().setFragmentResultListener("bundle", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Double latitude = bundle.getDouble("latitude");
                Double longitude = bundle.getDouble("longitude");
                getCompleteAddress(longitude, latitude, mostrarDireccion);
            }
        });
        return view;
    }

    private void getCompleteAddress(Double longitude, Double latitude, TextView textView) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    direcciones = geocoder.getFromLocation(latitude, longitude, 1);
                    textView.setText(direcciones.get(0).getAddressLine(0));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();
    }
}
