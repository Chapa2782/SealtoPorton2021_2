package com.sealtosoft.sealtoporton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PropietarioLogin extends AppCompatActivity {
    Button btnIngresar;
    EditText editID,editPass;
    DatabaseReference ref;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String cadena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propietario_login);

        btnIngresar = findViewById(R.id.btnIngresarPropietario);
        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        editID = findViewById(R.id.editID);
        editPass = findViewById(R.id.editPass);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Dispositivos");

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadena = editID.getText().toString().toUpperCase();
                cadena = cadena.replace(" ","");
                ref = database.getReference("Dispositivos/" + cadena);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.getValue() == null){
                            Log.d("Mensaje","Valor nulo");
                            editID.setError("El dispositvo no existe");
                            return;
                        }
                        Dispositivos dispositivos = snapshot.getValue(Dispositivos.class);

                        int clave = Integer.valueOf(editPass.getText().toString());
                        if(clave != dispositivos.Clave){
                            editPass.setError("Clave incorrecta");
                            return;
                        }else{
                            editor = sharedPreferences.edit();
                            editor.putString("Dispositivo",cadena);
                            editor.putString("Modo","Propietario");
                            editor.commit();
                            startActivity(new Intent(PropietarioLogin.this,ComandoPropietario.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

            }
        });
    }
}