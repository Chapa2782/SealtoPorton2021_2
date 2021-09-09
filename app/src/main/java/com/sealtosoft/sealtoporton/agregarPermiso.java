package com.sealtosoft.sealtoporton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class agregarPermiso extends AppCompatActivity {
    EditText editAlias,editPass;
    Switch habilitar;
    Button btnCrear;

    FirebaseDatabase database;
    DatabaseReference ref,refDispo;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String Dispo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_permiso);

        editAlias = findViewById(R.id.editAlias);
        editPass = findViewById(R.id.editPass);

        habilitar = findViewById(R.id.permisoSwith);

        btnCrear = findViewById(R.id.btnCrearPermiso);

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        Dispo = sharedPreferences.getString("Dispositivo","NULL");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Permisos");

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPass.getText().length() < 4){
                    editPass.setError("debe tener almenos 4 caracteres");
                    return;
                }
                if(editAlias.getText().length() < 4){
                    editPass.setError("debe tener almenos 4 caracteres");
                    return;
                }
                String cadena = editAlias.getText().toString().toLowerCase();
                cadena = cadena.replace(" ","");

                ref = database.getReference("Permisos/" + cadena);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() == null){
                            Log.d("Mensaje","Es nulo");
                            Permisos permisos = new Permisos();
                            permisos.Clave = Integer.valueOf(editPass.getText().toString());
                            permisos.ID = editAlias.getText().toString().replace(" ","").toLowerCase();
                            permisos.Dispositivo = Dispo;
                            permisos.habilitado = habilitar.isChecked();
                            ref.setValue(permisos);
                            String cadena = "Dispositivos/" + Dispo + "/Permisos/" + editAlias.getText().toString().replace(" ","").toLowerCase();

                            refDispo = database.getReference(cadena);
                            refDispo.setValue(editAlias.getText().toString().replace(" ","").toLowerCase());
                            finish();
                            return;
                        }else{
                            editAlias.setError("Ya existe un permiso con este nombre");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



    }
}