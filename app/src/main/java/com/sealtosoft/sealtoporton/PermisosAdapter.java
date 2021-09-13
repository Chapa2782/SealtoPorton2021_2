package com.sealtosoft.sealtoporton;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    DatabaseReference ref,ref2;
    SharedPreferences sharedPreferences;


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
        sharedPreferences = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        final String DIS = sharedPreferences.getString("Dispositivo","NULL");

        TextView id = convertView.findViewById(R.id.nombrePermiso);
        final Switch estado = convertView.findViewById(R.id.swEstado);
        final Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        final Permisos permisos = getItem(position);
        id.setText(permisos.getID());
        estado.setChecked(permisos.getHabilitado());
        ID = id.getText().toString();
        estado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Cambia el estado del permiso
                ref = database.getReference("Permisos/" + permisos.getID() + "/habilitado");
                ref.setValue(isChecked);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado.setOnCheckedChangeListener(null);
                ref = database.getReference("Permisos/" + permisos.getID());
                ref.removeValue();
                String path = "Dispositivos/" + DIS + "/Permisos/" + permisos.getID();

                ref2 = database.getReference(path);
                ref2.removeValue();
                Permisos.postition = position;



            }
        });
        return convertView;
    }

}
