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

public class Acopio extends AppCompatActivity {
    View mainView;
    EditText usernameTextBox, passwordTextBox;
    final DatabaseReference acopiosRef = MainActivity.database.getReference("centros_acopio");
    TextView acopioNombre, acopioDireccion, acopioHorarios, nivelAbastecimiento;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acopio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.acopioNombre = findViewById(R.id.acopio_nombre);
        this.acopioDireccion = findViewById(R.id.acopioDireccion);
        this.acopioHorarios = findViewById(R.id.acopioHorarios);
        this.nivelAbastecimiento = findViewById(R.id.acopioAbastecimiento);

        this.mainView = findViewById(android.R.id.content);

    }

    public void acopio1(View view){
        Log.d("->","acopio1()");
        updateInfo("Comida Para Todos");
    }

    public void acopio2(View view){
        Log.d("->","acopio2()");
        updateInfo("Juntos Podemos");
    }

    public void updateInfo(final String acopioName){
        acopiosRef.orderByChild("nombre").equalTo(acopioName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            acopioNombre.setText(child.child("nombre").getValue().toString());
                            acopioDireccion.setText(child.child("direccion").getValue().toString());
                            acopioHorarios.setText(child.child("horarios").getValue().toString());
                            nivelAbastecimiento.setText(child.child("nivel_abastecimiento").getValue().toString());
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
