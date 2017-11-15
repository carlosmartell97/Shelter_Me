package com.example.shelter_me.shelter_me;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Albergue extends AppCompatActivity {
    View mainView;
    final DatabaseReference alberguesRef = MainActivity.database.getReference("albergues");
    TextView albergueNombre, albergueDireccion, albergueHorarios, albergueCapacidad;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albergue);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.albergueNombre = findViewById(R.id.albergueNombre);
        this.albergueDireccion = findViewById(R.id.albergueDireccion);
        this.albergueHorarios = findViewById(R.id.albergueHorarios);
        this.albergueCapacidad = findViewById(R.id.albergueCapacidad);

        this.mainView = findViewById(android.R.id.content);

    }

    public void albergue1(View view){
        Log.d("->","acopio1()");
        updateInfo("Nuestra Casa");
    }

    public void albergue2(View view){
        Log.d("->","acopio2()");
        updateInfo("Albergue Santa Anita");
    }

    public void updateInfo(final String acopioName){
        alberguesRef.orderByChild("nombre").equalTo(acopioName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            albergueNombre.setText(child.child("nombre").getValue().toString());
                            albergueDireccion.setText(child.child("direccion").getValue().toString());
                            albergueHorarios.setText(child.child("horarios").getValue().toString());
                            albergueCapacidad.setText(child.child("capacidad").getValue().toString());
                        }
                        Log.d("->","value:"+snapshot.toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
    }

}
