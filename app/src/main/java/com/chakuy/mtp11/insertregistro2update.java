package com.chakuy.mtp11;

import android.os.Bundle;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Arrays;
import java.util.List;


public class insertregistro2update extends AppCompatActivity {

    Button btnalarma;
    EditText ubicacion;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertregistro2update);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        this.setTitle("Registro Ubicacion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ubicacion = findViewById(R.id.ubicacion);
        btnalarma = findViewById(R.id.btnalarma);

        btnalarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ubicacion.getText().toString();
                //sendWhatsAppMessages(Arrays.asList("+59177087685","+59163962491"), message); // Handle button click if needed
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);
        // Create a new instance of OnMapReadyCallback and implement the onMapReady() method
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                // Call a method to update the map with the location
                updateMapLocation();
            }
        });
    }

    private void updateMapLocation() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            // Get the last known location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Save the location in the lastKnownLocation variable
                                lastKnownLocation = location;

                                // Update the EditText with the location coordinates
                                ubicacion.setText(location.getLatitude() + ", " + location.getLongitude());

                                // Update the map with a marker at the location
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                            }
                        }
                    });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Primero, asegurarse de tener permisos de ubicación
        if (ActivityCompat.checkSelfPermission(insertregistro2update.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(insertregistro2update.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(insertregistro2update.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            // Si ya tiene los permisos, obtener la ubicación
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Guardar la ubicación en la variable lastKnownLocation
                                lastKnownLocation = location;
                                ubicacion.setText(location.getLatitude() + " ," + location.getLongitude());
                            }
                        }
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}


