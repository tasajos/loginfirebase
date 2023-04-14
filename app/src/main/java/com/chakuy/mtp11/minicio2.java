package com.chakuy.mtp11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.net.Uri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import androidx.annotation.NonNull;

public class minicio2 extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minicio2);






        LinearLayout linearLayoutAgregar = findViewById(R.id.linearLayoutAgregar);
        LinearLayout listado = findViewById(R.id.listado);
        LinearLayout descargar = findViewById(R.id.descargar);
        LinearLayout cerrarsesion = findViewById(R.id.cerrarsesion);
        linearLayoutAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(minicio2.this, InsertarRegistro.class);
                startActivity(intent);
            }
        });

        listado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(minicio2.this, listarpr.class);
                startActivity(intent);
            }
        });

        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(minicio2.this, login.class));
            }
        });

        descargar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {




                List<MTPData> dataList = new ArrayList<>();
                FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
                Query query = mFirestore.collection("mtpdb");
                query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        MTPData data = document.toObject(MTPData.class);
                        dataList.add(data);
                    }

                    // crear un nuevo archivo de Excel
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("Datos MTP");

                    // agregar los datos a las celdas
                    int rowNum = 0;
                    for (MTPData data : dataList) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(data.getNombre());
                        row.createCell(1).setCellValue(data.getApellido());
                        row.createCell(2).setCellValue(data.getFecha());
                        row.createCell(3).setCellValue(data.getTipo());
                        row.createCell(4).setCellValue(data.getDetalle());
                        row.createCell(5).setCellValue(data.getPedido());
                        row.createCell(6).setCellValue(data.getCantidad());
                        row.createCell(7).setCellValue(data.getPrecio());
                        row.createCell(8).setCellValue(data.getUbicacion());
                        row.createCell(9).setCellValue(data.getColor());
                    }

                    // guardar el archivo
                    String filePath = getExternalFilesDir(null).getPath().toString() + "/datos_mtp.xlsx";
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        workbook.write(outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri fileUri = FileProvider.getUriForFile(minicio2.this, BuildConfig.APPLICATION_ID + ".provider", new File(filePath));
                    grantUriPermission(getPackageName(), fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // abrir el archivo Excel en una aplicación y compartirlo
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("application/vnd.ms-excel");
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(minicio2.this, BuildConfig.APPLICATION_ID + ".provider", new File(filePath)));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    startActivity(Intent.createChooser(intent, "Compartir archivo"));

                });
            }
        });

        Map<String, Object> data = new HashMap<>();
        data.put("nombre", "Juan");
        data.put("apellido", "Pérez");
        data.put("fecha", "2022-01-01");
        data.put("tipo", "Producto 1");
        data.put("detalle", "Descripción del producto 1");
        data.put("pedido", "Pedido 123");
        data.put("cantidad", 10);
        data.put("precio", 100.0);
        data.put("ubicacion", "Ciudad");
        data.put("color", "Rojo");


        FirebaseFunctions functions = FirebaseFunctions.getInstance();

        functions.getHttpsCallable("myFunction")
                .call(data)
                .addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                    @Override
                    public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                        if (task.isSuccessful()) {
                            HttpsCallableResult result = task.getResult();
                            // Procesar el resultado
                        } else {
                            Exception e = task.getException();
                            // Manejar la excepción
                        }
                    }
                });





    }
}