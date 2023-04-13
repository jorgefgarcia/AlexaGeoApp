package com.example.geofencingtutorial.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.geofencingtutorial.GeofenceHelper;
import com.example.geofencingtutorial.R;
import static com.example.geofencingtutorial.constantes.Constantes.*;

public class FragmentoPermisos extends Fragment {

    private GeofenceHelper geofenceHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_permisos, container, false);
        geofenceHelper = new GeofenceHelper(getContext());

        //Conseguimos la ubicación del usuario y pedimos permisos
        enableUserLocation();

        Button next = view.findViewById(R.id.button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentoPrimario_to_fragmentoCogiendoUbicacion);
            }
        });
        return view.getRootView();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //mMap.setMyLocationEnabled(true);
        } else {
            //preguntamos por el permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Necesitamos mostrar un dialogo al usuario los permisos que necesitamos
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //tenemos los permisos
                //mMap.setMyLocationEnabled(true);
            } else {
                //No tenemos los permisos
            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //tenemos los permisos
                Toast.makeText(geofenceHelper, "You can add geofences", Toast.LENGTH_SHORT).show();
            } else {
                //No tenemos los permisos
                Toast.makeText(geofenceHelper, "Background location is necessary for geofences",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
