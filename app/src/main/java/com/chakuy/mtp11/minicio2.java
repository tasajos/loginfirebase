package com.chakuy.mtp11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class minicio2 extends AppCompatActivity {

    private static final String TAG = "minicio2";

    private TextView txtItem, txtItem2a;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mtpdbRef = db.collection("mtpdb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minicio2);

        LinearLayout linearLayoutAgregar = findViewById(R.id.linearLayoutAgregar);
        LinearLayout listado = findViewById(R.id.listado);
        LinearLayout descargar = findViewById(R.id.descargar);
        LinearLayout cerrarsesion = findViewById(R.id.cerrarsesion);
        LinearLayout mapa = findViewById(R.id.mapa);
        txtItem = findViewById(R.id.txtitem);
        txtItem2a = findViewById(R.id.txtitem2a);

        linearLayoutAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(minicio2.this, insertregistro2update.class);
            startActivity(intent);
        });

        listado.setOnClickListener(v -> {
            Intent intent = new Intent(minicio2.this, listarpr.class);
            startActivity(intent);
        });

        cerrarsesion.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finish();
            startActivity(new Intent(minicio2.this, login.class));
        });

        mapa.setOnClickListener(view -> {
            startActivity(new Intent(minicio2.this, ubicacionmacti.class));
        });

        descargar.setOnClickListener(view -> {
            List<MTPData> dataList = new ArrayList<>();
            Query query = mtpdbRef;
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    MTPData data = document.toObject(MTPData.class);
                    dataList.add(data);
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Datos MTP");

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

                String filePath = getExternalFilesDir(null).getPath().toString() + "/datos_mtp.xlsx";
                try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                    workbook.write(outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri fileUri = FileProvider.getUriForFile(minicio2.this, BuildConfig.APPLICATION_ID + ".provider", new File(filePath));
                grantUriPermission(getPackageName(), fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/vnd.ms-excel");
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(minicio2.this, BuildConfig.APPLICATION_ID + ".provider", new File(filePath)));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                startActivity(Intent.createChooser(intent, "Compartir archivo"));

                updateTotalItemCountByDate();
            });
        });

        updateTotalItemCount();
        updateTotalItemCountByDate();
    }

    private void updateTotalItemCount() {
        mtpdbRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = task.getResult().size();
                txtItem.setText(String.valueOf(count));
                Log.d(TAG, "Cantidad de registros: " + count);
            } else {
                Log.d(TAG, "Error obteniendo documentos: ", task.getException());
            }
        });
    }

    private void updateTotalItemCountByDate() {
        String currentDate = getCurrentDateWithoutTime();
        Query queryByDate = mtpdbRef.whereGreaterThanOrEqualTo("fecha", currentDate).whereLessThan("fecha", getNextDate(currentDate));
        queryByDate.get().addOnSuccessListener(queryDocumentSnapshots -> {
            int count = queryDocumentSnapshots.size();
            txtItem2a.setText(String.valueOf(count));
            Log.d(TAG, "Cantidad de registros por fecha actual: " + count);
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Error obteniendo documentos por fecha actual: " + e.getMessage());
        });
    }

    private String getCurrentDateWithoutTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getNextDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date currentDate = sdf.parse(date);
            long nextDateMillis = currentDate.getTime() + 24 * 60 * 60 * 1000; // Add 24 hours in milliseconds
            Date nextDate = new Date(nextDateMillis);
            return sdf.format(nextDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}