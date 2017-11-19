package com.example.shelter_me.shelter_me;

import android.graphics.Color;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AlbergueAdmin extends AppCompatActivity {
    View mainView;
    final DatabaseReference alberguesRef = MainActivity.database.getReference("albergues");
    EditText albergueNombre, albergueDireccion, albergueHorarios;
    TextView albergueCapacidad, codigoRes;
    String place,cupo,ocup,albergueKey;
    TableLayout table;
    TableRow header;
    TextView test,test2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albergue_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.albergueNombre = findViewById(R.id.albergueNombre);
        this.albergueDireccion = findViewById(R.id.albergueDireccion);
        this.albergueHorarios = findViewById(R.id.albergueHorarios);
        this.albergueCapacidad = findViewById(R.id.albergueCapacidad);
        this.codigoRes = findViewById(R.id.codigoRes);

        this.table = findViewById(R.id.reservas);
        setTable();
        this.mainView = findViewById(android.R.id.content);

        Bundle bundle = getIntent().getExtras();

        this.place = bundle.getString("place");
        updateAlbergueInfo();
    }

    public void setTable(){
        this.header =  new TableRow(this);
        this.test = new TextView(this);
        this.test2 = new TextView(this);
        this.test.setText("RESERVA");
        this.test2.setText("CANTIDAD");
        this.test.setWidth(400);
        this.test2.setWidth(400);
        test.setTextColor(Color.BLACK);
        test2.setTextColor(Color.BLACK);
        this.header.addView(test);
        this.header.addView(test2);
        this.table.addView(header);
    }

    public void updateAlbergueInfo(){
        alberguesRef.orderByChild("nombre").equalTo(place)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            albergueKey = child.getKey().toString();
                            albergueNombre.setText(child.child("nombre").getValue().toString());
                            albergueDireccion.setText(child.child("direccion").getValue().toString());
                            albergueHorarios.setText(child.child("horarios").getValue().toString());
                            ocup = child.child("ocupamiento").getValue().toString();
                            cupo = child.child("capacidad").getValue().toString();
                            albergueCapacidad.setText(ocup+" / "+ cupo);
                            for (DataSnapshot reservas : child.child("codigos").getChildren()) {
                                TableRow entry = new TableRow(AlbergueAdmin.this);
                                TextView reserva = new TextView(AlbergueAdmin.this);
                                TextView cantidad = new TextView(AlbergueAdmin.this);
                                reserva.setText(reservas.getKey().toString());
                                cantidad.setText(reservas.getValue().toString());
                                reserva.setWidth(500);
                                cantidad.setWidth(500);
                                reserva.setTextColor(Color.BLACK);
                                cantidad.setTextColor(Color.BLACK);
                                entry.addView(reserva);
                                entry.addView(cantidad);
                                table.addView(entry);
                            }
                        }
                        Log.d("->","value:"+snapshot.toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
    }

    public void actualizar(View view){
        this.table.removeAllViews();
        setTable();
        updateAlbergueInfo();
        Map<String, Object> albergueMap = new HashMap<String, Object>();
        albergueMap.put("nombre",albergueNombre.getText().toString());
        albergueMap.put("direccion",albergueDireccion.getText().toString());
        albergueMap.put("horarios",albergueHorarios.getText().toString());
        alberguesRef.child(albergueKey).updateChildren(albergueMap);
    }
}
