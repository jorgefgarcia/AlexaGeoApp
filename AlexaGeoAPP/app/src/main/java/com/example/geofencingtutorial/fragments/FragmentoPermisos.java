package com.example.geofencingtutorial.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private boolean permisosLocalizacion;
    private boolean permisosBackground;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_permisos, container, false);
        geofenceHelper = new GeofenceHelper(getContext());

        //Pedimos permisos al usuario
        checkForPermissions();

        Button next = view.findViewById(R.id.button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Establecemos la vista en función de los permisos.
                if(permisosLocalizacion && permisosBackground){
                    Navigation.findNavController(view).navigate(R.id.action_fragmentoPrimario_to_fragmentoCogiendoUbicacion);
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_fragmentoPermisos_to_fragmentoNoHayPermisos);
                }
            }
        });
        return view.getRootView();
    }

    private void checkForPermissions(){
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            permisosLocalizacion = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            //Si el dispositivo tiene una versión igual o posterior a las API 29 tiene que pedir permisos de Background.
                            permisosBackground = true;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                permisosBackground = result.getOrDefault(
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION, false);
                            }
                        }
                );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            });
        }else{
            locationPermissionRequest.launch(new String [] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        }
    }
}
