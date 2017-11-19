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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Acopio extends AppCompatActivity implements  OnMapReadyCallback {
    View mainView;
    private GoogleMap mMap;
    MapView mapView1;
    final DatabaseReference acopiosRef = MainActivity.database.getReference("centros_acopio");
    TextView acopioNombre, acopioDireccion, acopioHorarios;
    TableLayout table;
    TableRow header;
    TextView test,test2;
    String place;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acopio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();

        mapView1 = findViewById(R.id.mapView2);
        mapView1.onCreate(savedInstanceState);
        mapView1.getMapAsync(this);

        this.place = bundle.getString("place");
        this.acopioNombre = findViewById(R.id.acopio_nombre);
        this.acopioDireccion = findViewById(R.id.acopioDireccion);
        this.acopioHorarios = findViewById(R.id.acopioHorarios);

        this.table = findViewById(R.id.items);
        this.header =  new TableRow(this);
        this.test = new TextView(this);
        this.test2 = new TextView(this);
        this.test.setText("ITEM");
        this.test2.setText("CANTIDAD");
        this.test.setWidth(400);
        this.test2.setWidth(400);
        test.setTextColor(Color.BLACK);
        test2.setTextColor(Color.BLACK);
        this.header.addView(test);
        this.header.addView(test2);
        this.table.addView(header);

        this.mainView = findViewById(android.R.id.content);
        acopiosRef.orderByChild("nombre").equalTo(place)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            acopioNombre.setText(child.child("nombre").getValue().toString());
                            acopioDireccion.setText(child.child("direccion").getValue().toString());
                            acopioHorarios.setText(child.child("horarios").getValue().toString());
                            for (DataSnapshot items : child.child("items").getChildren()) {
                                TableRow entry = new TableRow(Acopio.this);
                                TextView item = new TextView(Acopio.this);
                                TextView cantidad = new TextView(Acopio.this);
                                item.setText(items.getKey().toString());
                                cantidad.setText(items.getValue().toString());
                                item.setWidth(500);
                                cantidad.setWidth(500);
                                item.setTextColor(Color.BLACK);
                                cantidad.setTextColor(Color.BLACK);
                                entry.addView(item);
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

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        acopiosRef.orderByChild("nombre").equalTo(place).addListenerForSingleValueEvent(new ValueEventListener() {
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
