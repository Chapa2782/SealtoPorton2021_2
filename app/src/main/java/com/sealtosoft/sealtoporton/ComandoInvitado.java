package com.sealtosoft.sealtoporton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class ComandoInvitado extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Dispositivo,permiso;
    TextView texto;

    DatabaseReference ref,refEstado,refPermiso;
    FirebaseDatabase database;
    Button btnComando, btnCambiarModo;
    Boolean estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comando_invitado);
        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        texto = findViewById(R.id.texto);

        permiso = sharedPreferences.getString("Permiso","");

        Dispositivo = sharedPreferences.getString("Dispositivo","NULL");
        Log.d("Mensaje",Dispositivo);
        Dispositivo = "Dispositivos/" + Dispositivo;

        database = FirebaseDatabase.getInstance();
        ref = database.getReference(Dispositivo + "/Comando");
        refEstado = database.getReference(Dispositivo + "/Estado");
        refPermiso = database.getReference("Permisos/" + permiso + "/habilitado");

        btnComando = findViewById(R.id.btnComando);
        btnCambiarModo = findViewById(R.id.btnCambiarModo);
        refPermiso.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                estado = Boolean.valueOf(snapshot.getValue().toString());
                if(estado){
                    texto.setText(R.string.PermisoHabilitado);
                }else{
                    texto.setText(R.string.PermisoDeshabilitado);
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        btnCambiarModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!estado){
                    Toast.makeText(ComandoInvitado.this,getString(R.string.PermisoDeshabilitado),Toast.LENGTH_LONG).show();
                    return;
                }
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
                if(!estado){
                    Toast.makeText(ComandoInvitado.this,getString(R.string.PermisoDeshabilitado),Toast.LENGTH_LONG).show();
                    return;
                }
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
        editor = sharedPreferences.edit();
        editor.putString("Modo","");
        editor.putString("Dispositivo","");
        editor.putString("Permiso","");
        editor.commit();
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_invitado,menu);
        return true;
    }
}