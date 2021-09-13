package com.sealtosoft.sealtoporton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InvitadoLogin extends AppCompatActivity {
    EditText editAlias,editPass;
    Button btnIngresar;

    FirebaseDatabase database;
    DatabaseReference ref;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitado_login);

        editAlias = findViewById(R.id.editAlias);
        editPass = findViewById(R.id.editPass);
        btnIngresar = findViewById(R.id.btnIngresar);

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        database = FirebaseDatabase.getInstance();


        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Mensaje","Se esta presionando el boton");
                ref = database.getReference("Permisos/" + editAlias.getText().toString().replace(" ","").toLowerCase());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() == null) {
                                editAlias.setError("El permiso no existe");
                                return;
                            }
                            PermisosClass permisos = snapshot.getValue(PermisosClass.class);

                            int clave = Integer.valueOf(editPass.getText().toString());
                            if (clave != permisos.Clave) {
                                editPass.setError("Clave incorrecta");
                                return;
                            } else {
                                editor = sharedPreferences.edit();
                                editor.putString("Dispositivo", permisos.Dispositivo);
                                editor.putString("Modo","Invitado");
                                editor.putString("Permiso", editAlias.getText().toString().replace(" ", "").toLowerCase());
                                editor.commit();
                                startActivity(new Intent(InvitadoLogin.this, ComandoInvitado.class));
                            }
                        }catch (Exception e){
                            Log.d("Mensaje",e.toString());
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