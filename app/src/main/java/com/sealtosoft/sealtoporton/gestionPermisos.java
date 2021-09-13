package com.sealtosoft.sealtoporton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class gestionPermisos extends AppCompatActivity {
    DatabaseReference  refPermisos,ref;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;
    String perString;

    ListView listado;
    PermisosAdapter adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_permisos);

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        perString = sharedPreferences.getString("Dispositivo","");

        adaptador = new PermisosAdapter(getBaseContext(),new ArrayList<>());
        listado = findViewById(R.id.listadoPermisos);
        listado.setAdapter(adaptador);


        database = FirebaseDatabase.getInstance();

        refPermisos = database.getReference("Permisos/");
        refPermisos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                try{
                    PermisosClass permisos = dataSnapshot.getValue(PermisosClass.class);
                    //se comprueba que el permiso sea del usuario
                    String path = "Dispositivos/" + perString + "/Permisos/" + permisos.ID;
                    ref = database.getReference(path);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            if(snapshot.getValue() == null){
                                return;
                            }else{
                                try {
                                    adaptador.add(new Permisos(permisos.ID,permisos.habilitado));
                                }catch (Exception e){
                                    Log.d("Mensaje",e.toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });


                }catch (Exception e){
                    Log.d("Mensaje", e.toString());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                try {
                    adaptador.remove(adaptador.getItem(Permisos.postition));
                    adaptador.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("Mensaje",e.toString());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}