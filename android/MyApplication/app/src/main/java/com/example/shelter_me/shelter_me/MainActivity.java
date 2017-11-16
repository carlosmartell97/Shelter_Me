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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    View mainView;
    EditText usernameTextBox, passwordTextBox;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference usersRef = database.getReference("admins");
    final DatabaseReference alberguesRef = database.getReference("albergues");
    final DatabaseReference acopiosRef = database.getReference("centros_acopio");

    public static class Admin {
        String id;
        String password;
        public Admin(){}
        public Admin(String id, String password){
            this.id = id;
            this.password = password;
        }
        public String getUsername(){
            return this.id;
        }
        public String getPassword(){
            return this.password;
        }
        /*@Override
        public String toString() {
            return "User{" +
                    "username='" + this.username + '\'' +
                    ", password=" + this.password +
                    '}';
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mainView = findViewById(android.R.id.content);
        this.usernameTextBox = (EditText)findViewById(R.id.usernameTextbox);
        this.passwordTextBox = (EditText)findViewById(R.id.passwordTextbox);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void register(View view){
        Snackbar.make(view, "Account created, " + usernameTextBox.getText().toString(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Admin newUser = new Admin(usernameTextBox.getText().toString(),passwordTextBox.getText().toString());
        newUser.id = usernameTextBox.getText().toString();
        newUser.password = passwordTextBox.getText().toString();
        usersRef.push().setValue(newUser);
    }*/

    public void login(View view){
        usersRef.orderByChild("id").equalTo(usernameTextBox.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Log.e("->", "======="+child.getValue(Admin.class).getPassword());
                            if(child.getValue(Admin.class).getPassword().equals(passwordTextBox.getText().toString())){
                                Snackbar.make(findViewById(android.R.id.content), "LOGIN successful", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                alberguesRef.orderByChild("id").equalTo(usernameTextBox.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot albergues) {
                                        if(albergues.exists()){
                                            for (DataSnapshot albergue : albergues.getChildren()) {
                                                Intent albergueView = new Intent(MainActivity.this, Albergue.class);
                                                albergueView.putExtra("place",albergue.child("nombre").getValue().toString());
                                                startActivity(albergueView);
                                                Log.d("-->","albergues "+albergue.child("nombre").getValue().toString());
                                            }
                                        } else {
                                            Log.d("->",usernameTextBox.getText().toString()+" ALBERGUE doesn't exist ");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("->","couldn't read");
                                    }
                                });
                                acopiosRef.orderByChild("id").equalTo(usernameTextBox.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot acopios) {
                                        if(acopios.exists()){
                                            for (DataSnapshot acopio : acopios.getChildren()) {
                                                Intent acopioView = new Intent(MainActivity.this, Acopio.class);
                                                acopioView.putExtra("place",acopio.child("nombre").getValue().toString());
                                                Log.d("-->","centros_acopio "+acopio.child("nombre").getValue().toString());
                                                startActivity(acopioView);
                                            }
                                        } else {
                                            Log.d("->",usernameTextBox.getText().toString()+" ACOPIO doesn't exist ");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("-->","couldn't read");
                                    }
                                });
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), "incorrect password", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
    }
}
