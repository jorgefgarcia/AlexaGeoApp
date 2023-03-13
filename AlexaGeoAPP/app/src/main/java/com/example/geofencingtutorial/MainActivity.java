package com.example.geofencingtutorial;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.geofencingtutorial.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private float radius;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mainClassIntent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        EditText radiusNumber = (EditText)findViewById(R.id.NumberRadius);
        Button radiusButton = (Button)findViewById(R.id.RadiusButton);
        radiusButton.setEnabled(false);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        findViewById(R.id.GeofenceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //añadimos los valores al bundle
                if(getRadius() == 0){
                    Toast.makeText(MainActivity.this, "Establish a value for radius", Toast.LENGTH_SHORT).show();
                }else{
                    bundle.putDouble("latitude",location.getLatitude());
                    bundle.putDouble("longitude", location.getLongitude());
                    bundle.putFloat("radius", getRadius());
                    //vinculamos el bundle con el intent
                    mainClassIntent.putExtras(bundle);

                    startActivity(mainClassIntent);
                }


            }
        });

        radiusNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                //Desactivamos el botón para que no entre nulo o 0 en el radio
                if(charSequence.toString().equals("") || Float.parseFloat(charSequence.toString()) == 0.0){
                    radiusButton.setEnabled(false);
                }else{
                    radiusButton.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Desactivamos el botón por si cambia el valor una vez escrito
                if(editable.toString().equals("") || Float.parseFloat(editable.toString()) < 100){
                    radiusButton.setEnabled(false);
                }
            }
        });

       radiusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRadius(Float.parseFloat(radiusNumber.getText().toString()));
                Toast.makeText(MainActivity.this, "Radius added to geofence", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


}