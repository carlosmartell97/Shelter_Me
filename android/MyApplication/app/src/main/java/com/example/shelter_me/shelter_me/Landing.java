package com.example.shelter_me.shelter_me;

import android.os.Bundle;
import android.provider.Telephony;
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
import android.content.*;

public class Landing extends AppCompatActivity {
    View mainView;
    EditText usernameTextBox, passwordTextBox;
    String type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mainView = findViewById(android.R.id.content);

    }

    public void acopio(View view) {
        type = "centros_acopio";
        change();
    }

    public void albergue (View view) {
        type = "albergues";
        change();
    }

    public void change(){
        Intent change = new Intent(Landing.this,MapsActivity.class);
        change.putExtra("type", type);
        startActivity(change);
    }

    public void admin(View view){
        Intent change = new Intent(Landing.this,MainActivity.class);
        startActivity(change);
    }


}
