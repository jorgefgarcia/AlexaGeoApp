package com.example.geofencingtutorial.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.geofencingtutorial.R;

public class FragmentoMostrarUbicacion extends Fragment {
    private static final String TAG = "FragmentoMostrarUbicaci";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragmento_mostrar_ubicacion, container, false);

        //Cogemos la información del FragmentManager
        getParentFragmentManager().setFragmentResultListener("bundle", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Double latitude = bundle.getDouble("latitude");
                Double longitude = bundle.getDouble("longitude");
                Log.d(TAG, "onFragmentResult: " + latitude + " "+ longitude);
            }
        });
        return view;
    }
}
