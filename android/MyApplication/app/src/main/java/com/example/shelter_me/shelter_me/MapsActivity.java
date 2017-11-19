package com.example.shelter_me.shelter_me;

import android.content.Intent;
import android.os.Debug;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseReference acopiosRef;
    private GoogleMap mMap;
    MapView mapView;
    String type,place;
    TextView mapType;
    Button info;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        info = findViewById(R.id.button5);

        Bundle bundle = getIntent().getExtras();

        this.type = bundle.getString("type");
        acopiosRef = MainActivity.database.getReference(type);
    }


    public void info(View view) {
        if (this.type.compareTo("centros_acopio") == 0) {
            Intent acopioView = new Intent(MapsActivity.this, Acopio.class);
            acopioView.putExtra("place",place);
            Log.d("-->",place);
            startActivity(acopioView);
        } else {
            Intent albergueView = new Intent(MapsActivity.this, Albergue.class);
            albergueView.putExtra("place",place);
            Log.d("-->",place);
            startActivity(albergueView);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        acopiosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(String.valueOf(child.child("lat").getValue())), Double.parseDouble(String.valueOf(child.child("lon").getValue())))).title(child.child("nombre").getValue().toString()));
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.43, -99.13), 14));

        //LatLng sydney = new LatLng(-34, 151);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                place = marker.getTitle();
                info.setEnabled(true);
                info.setText("Ver: "+ place);
                return false;
            }
        });

    }


}
