package com.chakuy.mtp11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
import java.util.List;

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

                    // abrir el archivo Excel en una aplicación y compartirlo
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("application/vnd.ms-excel");
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(minicio2.this, BuildConfig.APPLICATION_ID + ".provider", new File(filePath)));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Compartir archivo"));

                });
            }
        });
    }
}