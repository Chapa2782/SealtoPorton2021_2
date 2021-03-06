package com.sealtosoft.sealtoporton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComandoPropietario extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Dispositivo;

    DatabaseReference ref,refEstado;
    FirebaseDatabase database;
    Button btnComando, btnCambiarModo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comando_propietario);


        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        //Log.d("Mensaje",sharedPreferences.getString("Dispositivo","NULL"));


        Dispositivo = sharedPreferences.getString("Dispositivo","NULL");

        Dispositivo = "Dispositivos/" + Dispositivo;

        database = FirebaseDatabase.getInstance();
        ref = database.getReference(Dispositivo + "/Comando");
        refEstado = database.getReference(Dispositivo + "/Estado");

        btnComando = findViewById(R.id.btnComando);
        btnCambiarModo = findViewById(R.id.btnCambiarModo);

        btnCambiarModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCambiarModo.getText().toString() == getString(R.string.CambiarAbierto)){
                    refEstado.setValue(2);
                }
                if(btnCambiarModo.getText().toString() == getString(R.string.CambiarCerrado)){
                    refEstado.setValue(0);
                }

            }
        });


        btnComando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnComando.getText().toString() == getString(R.string.ABRIR)){
                    ref.setValue(1);

                }
                if(btnComando.getText().toString() == getString(R.string.CERRAR)){
                    ref.setValue(2);

                }
            }
        });

        refEstado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {

                    Integer estado = Integer.valueOf(snapshot.getValue().toString());
                    if(estado == 0){
                        btnCambiarModo.setText(R.string.CambiarAbierto);
                        btnComando.setText(R.string.ABRIR);
                        //btnComando.setBackgroundColor(R.drawable.comando_abrir);
                        btnComando.setBackgroundResource(R.drawable.comando_abrir);
                        btnComando.setTextColor(getColor(R.color.verde));
                    }
                    if(estado == 2){
                        btnCambiarModo.setText(R.string.CambiarCerrado);
                        btnComando.setText(R.string.CERRAR);
                        //btnComando.setBackgroundColor(R.drawable.comando_cerrar);
                        btnComando.setBackgroundResource(R.drawable.comando_cerrar);
                        btnComando.setTextColor(getColor(R.color.rojo));
                    }
                    if(estado == 1){
                        btnCambiarModo.setText(R.string.CambiarAbierto);
                        btnComando.setText(R.string.ABRIENDO);
                        //btnComando.setBackgroundColor(R.drawable.comando_abrir);
                        btnComando.setBackgroundResource(R.drawable.comando_abrir);
                        btnComando.setTextColor(getColor(android.R.color.holo_blue_dark));
                    }
                    if(estado == 3){
                        btnCambiarModo.setText(R.string.CambiarCerrado);
                        btnComando.setText(R.string.CERRANDO);
                        //btnComando.setBackgroundColor(R.drawable.comando_cerrar);
                        btnComando.setBackgroundResource(R.drawable.comando_cerrar);
                        btnComando.setTextColor(getColor(android.R.color.holo_blue_dark));
                    }
                }catch (Exception e){
                    Log.d("Mensaje",e.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.agregarPermiso:
                startActivity(new Intent(ComandoPropietario.this,agregarPermiso.class));
                break;
            case R.id.gestionPermiso:
                startActivity(new Intent(ComandoPropietario.this,gestionPermisos.class));
                break;
            case R.id.cerrarSession:
                sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("Dispositivo","");
                editor.putString("Permiso","");
                editor.putString("Modo","");
                editor.commit();
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
}