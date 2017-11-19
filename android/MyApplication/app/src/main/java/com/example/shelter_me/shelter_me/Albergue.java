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

public class Albergue extends AppCompatActivity implements OnMapReadyCallback {
    View mainView;
    private GoogleMap mMap;
    MapView mapView1;
    final DatabaseReference alberguesRef = MainActivity.database.getReference("albergues");
    TextView albergueNombre, albergueDireccion, albergueHorarios, lugares, albergueCapacidad, codigoRes;
    String place,cupo,ocup,albergueKey;
    int codigo, reservas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albergue);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.albergueNombre = findViewById(R.id.albergueNombre);
        this.albergueDireccion = findViewById(R.id.albergueDireccion);
        this.albergueHorarios = findViewById(R.id.albergueHorarios);
        this.albergueCapacidad = findViewById(R.id.albergueCapacidad);
        this.lugares = findViewById(R.id.numLug);
        this.codigoRes = findViewById(R.id.codigoRes);

        mapView1 = findViewById(R.id.mapView3);
        mapView1.onCreate(savedInstanceState);
        mapView1.getMapAsync(this);

        this.mainView = findViewById(android.R.id.content);

        Bundle bundle = getIntent().getExtras();

        this.place = bundle.getString("place");
        updateAlbergueInfo();
    }

    public void updateAlbergueInfo(){
        alberguesRef.orderByChild("nombre").equalTo(place)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            albergueKey = child.getKey().toString();
                            reservas = Integer.parseInt(child.child("reservas").getValue().toString());
                            albergueNombre.setText(child.child("nombre").getValue().toString());
                            albergueDireccion.setText(child.child("direccion").getValue().toString());
                            albergueHorarios.setText(child.child("horarios").getValue().toString());
                            ocup = child.child("ocupamiento").getValue().toString();
                            cupo = child.child("capacidad").getValue().toString();
                            albergueCapacidad.setText(ocup+" / "+ cupo);
                        }
                        Log.d("->","value:"+snapshot.toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
    }

    public void reservar(View view){
        if(lugares.getText().toString().equals("")){
            return;
        }
        updateAlbergueInfo();
        if (Integer.parseInt(this.ocup) + Integer.parseInt(lugares.getText().toString())> Integer.parseInt(this.cupo)){
            Toast.makeText(this,"ocupacion maxima",Toast.LENGTH_SHORT).show();
        }else{
            Random r = new Random();
            codigo = r.nextInt(1000000 - 1) + 1;
            codigoRes.setText("CODIGO: "+codigo);
            Map<String, Object> codigosMap = new HashMap<String, Object>();
            codigosMap.put(reservas+10+"-"+codigo, lugares.getText().toString());
            Map<String, Object> albergueMap = new HashMap<String, Object>();
            albergueMap.put("reservas", ++reservas);
            albergueMap.put("ocupamiento",Integer.parseInt(this.ocup)+Integer.parseInt(lugares.getText().toString()));
            alberguesRef.child(albergueKey).child("codigos").updateChildren(codigosMap);
            alberguesRef.child(albergueKey).updateChildren(albergueMap);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        alberguesRef.orderByChild("nombre").equalTo(place).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(String.valueOf(child.child("lat").getValue())), Double.parseDouble(String.valueOf(child.child("lon").getValue())))).title(child.child("nombre").getValue().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(String.valueOf(child.child("lat").getValue())), Double.parseDouble(String.valueOf(child.child("lon").getValue()))), 16));
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        // Add a marker in Sydney and move the camera



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView1.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView1.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView1.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView1.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView1.onSaveInstanceState(outState);
    }
}
