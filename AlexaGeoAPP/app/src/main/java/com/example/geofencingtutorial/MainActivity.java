package com.example.geofencingtutorial;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private float radius;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextInputLayout radiusNumberLayout = (TextInputLayout) findViewById(R.id.text_input_layout_radius);
        TextInputEditText radiusNumber = (TextInputEditText) findViewById(R.id.editTextRadius);
        Intent mainClassIntent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();

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
                if(getRadius() == 0 || getRadius() < 100){
                    Toast.makeText(MainActivity.this, "Establish a valid value for radius", Toast.LENGTH_SHORT).show();
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
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                //Desactivamos el botón por si cambia el valor una vez escrito
                if(editable.toString().equals("") || Float.parseFloat(editable.toString()) < 100){
                    setRadius(0);
                    radiusNumberLayout.setError("At least 100m");
                    requestFocus(radiusNumber);
                }else{
                    radiusNumberLayout.setErrorEnabled(false);
                    setRadius(Float.parseFloat(radiusNumber.getText().toString()));
                }
            }
        });
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


}