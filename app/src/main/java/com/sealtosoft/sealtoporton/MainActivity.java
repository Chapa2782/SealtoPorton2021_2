package com.sealtosoft.sealtoporton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnPropietario,btnInvitado;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPropietario = findViewById(R.id.btnPropietario);
        btnInvitado = findViewById(R.id.btnInvitado);
        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String modo = sharedPreferences.getString("Modo","NULL");
        Log.d("Mensaje",modo);

        if(modo.equals("Propietario")){
            Log.d("Mensaje","Ingreso en modo propietario");
            String dispositivo = sharedPreferences.getString("Dispositivo","");
            Log.d("Mensaje",dispositivo);
            if(!dispositivo.isEmpty()){
                startActivity(new Intent(MainActivity.this,ComandoPropietario.class));
            }
        }
        if(modo.equals("Invitado")){
            Log.d("Mensaje","Ingreso en modo invitado");
            String dispositivo = sharedPreferences.getString("Dispositivo","");
            String permiso = sharedPreferences.getString("Permiso","");
            Log.d("Mensaje",dispositivo);
            Log.d("Mensaje",permiso);
            if(!dispositivo.isEmpty()){
                if(!permiso.isEmpty()){
                    startActivity(new Intent(MainActivity.this,ComandoInvitado.class));
                }
            }
        }

        btnPropietario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PropietarioLogin.class));
            }
        });

        btnInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,InvitadoLogin.class));
            }
        });
    }
}