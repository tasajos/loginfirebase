package com.chakuy.mtp11;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class insertregistro2update extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE = 200;

    private EditText nombre, apellido, detalle, pedido, cantidad, color, precio, ubicacion, fecha;
    private Button btnalarma;

    private ImageButton btn_registrar;
    private Spinner tipoSpinner;

    private FirebaseFirestore mfirestore;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertregistro2update);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mfirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Registro Ubicacion");

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        tipoSpinner = findViewById(R.id.tipo);
        detalle = findViewById(R.id.detalle);
        pedido = findViewById(R.id.pedido);
        cantidad = findViewById(R.id.cantidad);
        color = findViewById(R.id.color);
        precio = findViewById(R.id.precio);
        ubicacion = findViewById(R.id.ubicacion);
        fecha = findViewById(R.id.fecha);
        btn_registrar = findViewById(R.id.btn_registrar);
        btnalarma = findViewById(R.id.btnalarma);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_producto, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoSpinner.setAdapter(adapter);

        btnalarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ubicacion.getText().toString();
                // Envía mensajes de WhatsApp si es necesario
                //sendWhatsAppMessages(Arrays.asList("+59177087685", "+59163962491"), message);
            }
        });

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nombre.getText().toString().trim();
                String lastName = apellido.getText().toString().trim();
                String type = tipoSpinner.getSelectedItem().toString().trim();
                String detail = detalle.getText().toString().trim();
                String order = pedido.getText().toString().trim();
                String quantity = cantidad.getText().toString().trim();
                String colorValue = color.getText().toString().trim();
                String price = precio.getText().toString().trim();
                String locationValue = ubicacion.getText().toString().trim();
                String date = fecha.getText().toString().trim();

                if (name.isEmpty() || lastName.isEmpty() || type.isEmpty() || detail.isEmpty() ||
                        order.isEmpty() || quantity.isEmpty() || colorValue.isEmpty() || price.isEmpty() ||
                        locationValue.isEmpty() || date.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingresar todos los datos", Toast.LENGTH_SHORT).show();
                } else {
                    saveRecord(name, lastName, type, detail, order, quantity, colorValue, price, locationValue, date);
                }
            }
        });

        // Obtener fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDate = sdf.format(new Date());

        // Establecer fecha actual en EditText y desactivar edición
        fecha.setText(currentDate);
        fecha.setEnabled(false);

        ubicacion.setEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        updateMapLocation();
    }

    private void updateMapLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                lastKnownLocation = location;
                                ubicacion.setText(location.getLatitude() + ", " + location.getLongitude());
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
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

    private void saveRecord(String name, String lastName, String type, String detail, String order, String quantity, String colorValue, String price, String locationValue, String date) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", name);
        map.put("apellido", lastName);
        map.put("tipo", type);
        map.put("detalle", detail);
        map.put("pedido", order);
        map.put("cantidad", quantity);
        map.put("color", colorValue);
        map.put("precio", price);
        map.put("ubicacion", locationValue);
        map.put("fecha", date);

        mfirestore.collection("mtpdb").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Registro Existoso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al Ingresar", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}