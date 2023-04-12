package com.chakuy.mtp11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import java.util.HashMap;
import java.util.Map;

public class InsertarRegistro extends AppCompatActivity {

    Button btn_registrar;
    EditText nombre,apellido,tipo,detalle,pedido,cantidad,color,precio,ubicacion,fecha;

    private FirebaseFirestore mfirestore;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_registro);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        this.setTitle("Crear Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mfirestore = FirebaseFirestore.getInstance();
        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        tipo = findViewById(R.id.tipo);
        detalle = findViewById(R.id.detalle);
        pedido = findViewById(R.id.pedido);
        cantidad = findViewById(R.id.cantidad);
        color = findViewById(R.id.color);
        precio = findViewById(R.id.precio);
        ubicacion = findViewById(R.id.ubicacion);
        fecha = findViewById(R.id.fecha);
        btn_registrar = findViewById(R.id.btn_registrar);


        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nombre.getText().toString().trim();
                String lastname = apellido.getText().toString().trim();
                String type = tipo.getText().toString().trim();
                String detail = detalle.getText().toString().trim();
                String order = pedido.getText().toString().trim();
                String quantity = cantidad.getText().toString().trim();
                String tecolor = color.getText().toString().trim();
                String teprecio = precio.getText().toString().trim();
                String ubication = ubicacion.getText().toString().trim();
                String tefecha = fecha.getText().toString().trim();

// Primero, asegurarse de tener permisos de ubicación
                if (ActivityCompat.checkSelfPermission(InsertarRegistro.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(InsertarRegistro.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InsertarRegistro.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
                    return;
                }

// Obtener la última ubicación conocida del usuario
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    // Guardar la ubicación en la variable lastKnownLocation
                                    lastKnownLocation = location;
                                    // Luego, llamar al método Postmtp para almacenar el registro en Firestore
                                    Postmtp(name,lastname,type,detail,order,quantity,tecolor,teprecio,lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude(),tefecha);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Si falla obtener la ubicación, llamar al método Postmtp sin la ubicación
                                Postmtp(name,lastname,type,detail,order,quantity,tecolor,teprecio,"",tefecha);
                            }
                        });




                if (name.isEmpty() && lastname.isEmpty() && type.isEmpty() && detail.isEmpty() && order.isEmpty() && 
                        quantity.isEmpty() && tecolor.isEmpty() && teprecio.isEmpty() && ubication.isEmpty() &&tefecha.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                }else {
                    Postmtp (name,lastname,type,detail,order,quantity,tecolor,teprecio,ubication,tefecha);
                }

                }


            });
        }

    private void Postmtp(String name, String lastname, String type, String detail, String order, String quantity, String tecolor, String teprecio, String ubication,String tefecha) {

        Map<String, Object> map = new HashMap<>();
        map.put ("nombre",name);
        map.put ("apellido",lastname);
        map.put ("tipo",type);
        map.put ("detalle",detail);
        map.put ("pedido",order);
        map.put ("cantidad",quantity);
        map.put ("color",tecolor);
        map.put ("precio",teprecio);
        map.put ("ubicacion",ubication);
        map.put ("fecha",tefecha);


        mfirestore.collection("mtpdb").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Registro Existoso", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Ingresar", Toast.LENGTH_SHORT).show();
            }
        });

    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}