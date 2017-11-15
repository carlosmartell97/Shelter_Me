package com.example.shelter_me.shelter_me;

import android.content.Intent;
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

public class Map extends AppCompatActivity {
    View mainView;
    EditText usernameTextBox, passwordTextBox;
    String type;
    TextView mapType;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mainView = findViewById(android.R.id.content);

        Bundle bundle = getIntent().getExtras();

        this.type = bundle.getString("type");

        this.mapType = findViewById(R.id.mapType);

        this.mapType.setText(this.type);


    }

    public void info (View view) {
        if (this.type.compareTo("acopio")==0 ) {
            Intent change = new Intent(Map.this, Acopio.class);
            startActivity(change);
        }else{
            Intent change = new Intent(Map.this, Albergue.class);
            startActivity(change);
        }
    }

}
