package com.example.mtp11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class MainActivity extends AppCompatActivity {

    Button btn_add;
    Button btn_listar,btn_cerrar,btn_descargar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        mAuth = FirebaseAuth.getInstance();
        btn_listar = findViewById(R.id.btn_listar);
        btn_cerrar = findViewById(R.id.btn_cerrar);
        btn_descargar = findViewById(R.id.btn_descargar);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InsertarRegistro.class));
            }
        });

        btn_listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,listarpr.class));
            }
        });

        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this,login.class));
            }
        });

        btn_descargar.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(filePath)));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Compartir archivo"));

                });
            }
        });
    }
}
