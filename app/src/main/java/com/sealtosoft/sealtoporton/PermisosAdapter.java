package com.sealtosoft.sealtoporton;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PermisosAdapter extends ArrayAdapter<Permisos> {
    FirebaseDatabase database;
    DatabaseReference ref;

    String ID;
    public PermisosAdapter(Context context, List<Permisos> objects){
        super(context,0,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.adaptador_lista,
                    parent,
                    false);
        }
        database = FirebaseDatabase.getInstance();

        // Referencias UI.
        TextView id = convertView.findViewById(R.id.nombrePermiso);
        final Switch estado = convertView.findViewById(R.id.swEstado);

        // Lead actual.
        final Permisos permisos = getItem(position);

        // Setup.
        id.setText(permisos.ID);
        estado.setChecked(permisos.habilitado);
        ID = id.getText().toString();
        estado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: Hacer que cambie el estado en la base de datos
                ref = database.getReference("Permisos/" + ID + "/habilitado");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("Mensaje/swChanged",snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
            }
        });
        return convertView;
    }
}
