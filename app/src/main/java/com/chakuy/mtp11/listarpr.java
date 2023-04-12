package com.chakuy.mtp11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.chakuy.mtp11.adapter.mtpadapter;
import com.chakuy.mtp11.models.mtpcontroller;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class listarpr extends AppCompatActivity {

    RecyclerView mRecycler;
    mtpadapter mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listarpr);
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.recyclerViewMTP);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        Query query = mFirestore.collection("mtpdb");

        FirestoreRecyclerOptions<mtpcontroller> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<mtpcontroller>().setQuery(query,mtpcontroller.class).build();


        mAdapter = new mtpadapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);



        this.setTitle("Listado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<MTPData> dataList = new ArrayList<>();

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                MTPData data = document.toObject(MTPData.class);
                dataList.add(data);
            }

            // aquí es donde puedes escribir los datos en Excel

            // crear un nuevo archivo de Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Datos MTP");

            // agregar los datos a las celdas
            int rowNum = 0;
            for (MTPData data : dataList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(data.getNombre());
                row.createCell(1).setCellValue(data.getApellido());
                row.createCell(2).setCellValue(data.getDetalle());
                row.createCell(3).setCellValue(data.getPedido());
                row.createCell(4).setCellValue(data.getCantidad());
                row.createCell(5).setCellValue(data.getColor());
                row.createCell(6).setCellValue(data.getPrecio());
                row.createCell(7).setCellValue(data.getTipo());
                row.createCell(8).setCellValue(data.getUbicacion());
                row.createCell(9).setCellValue(data.getFecha());




            }

            // guardar el archivo
            String filePath = getExternalFilesDir(null).getPath().toString() + "/datos_mtp.xlsx";


        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}

class MTPData {
    private String nombre;
    private String apellido;
    private String tipo;
    private String detalle;
    private String pedido;
    private String cantidad;
    private String color;
    private String precio;
    private String ubicacion;
    private String fecha;




    public MTPData() {} // constructor vacío requerido por Firestore

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setapellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setdetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getPedido() {
        return pedido;
    }

    public void setpedido(String pedido) {
        this.pedido = pedido;
    }


    public String getCantidad() {
        return cantidad;
    }

    public void setcantidad(String cantidad) {
        this.cantidad = cantidad;
    }


    public String getColor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }



    public String getPrecio() {
        return precio;
    }

    public void setprecio(String precio) {
        this.precio = precio;
    }




    public String getUbicacion() {
        return ubicacion;
    }

    public void setubicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }


    public String getFecha() {
        return fecha;
    }

    public void setfecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void settipo(String tipo) {
        this.tipo = tipo;
    }

}