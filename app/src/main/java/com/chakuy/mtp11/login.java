package com.chakuy.mtp11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    Button btn_login;
    EditText contrasena,correo_mail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        contrasena = findViewById(R.id.contrasena);
        correo_mail = findViewById(R.id.correo_mail);
        btn_login = findViewById(R.id.btn_login);

        TextView txtversion = findViewById(R.id.txtversion);
        txtversion.setText("Version: " + BuildConfig.VERSION_NAME);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailuser = correo_mail.getText().toString().trim();
                String passuser = contrasena.getText().toString().trim();


                if (emailuser.isEmpty() && passuser.isEmpty()){
                    Toast.makeText(login.this, "Ingresar datos", Toast.LENGTH_SHORT).show();
                }



            else{
                    loginUser(emailuser,passuser);
                }
        };

    });
}

    private void loginUser(String emailuser, String passuser) {

        mAuth.signInWithEmailAndPassword(emailuser, passuser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()){
                finish();
                startActivity(new Intent(login.this,minicio2.class));
                Toast.makeText(login.this, "Bienvenido", Toast.LENGTH_SHORT).show();

            }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(login.this, "Error al Iniciar Sesion", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(login.this,minicio2.class));
            finish();
        }
    }



}