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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    View mainView;
    EditText usernameTextBox, passwordTextBox;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference usersRef = database.getReference("users");


    public static class User {
        String username;
        String password;
        public User(){}
        public User(String username, String password){
            this.username = username;
            this.password = password;
        }
        public String getUsername(){
            return this.username;
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

    public void register(View view){
        Snackbar.make(view, "Account created, " + usernameTextBox.getText().toString(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        User newUser = new User(usernameTextBox.getText().toString(),passwordTextBox.getText().toString());
        newUser.username = usernameTextBox.getText().toString();
        newUser.password = passwordTextBox.getText().toString();
        usersRef.push().setValue(newUser);
    }

    public void login(View view){
        usersRef.orderByChild("username").equalTo(usernameTextBox.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Log.e("->", "======="+child.getValue(User.class).getPassword());
                            if(child.getValue(User.class).getPassword().equals(passwordTextBox.getText().toString())){
                                Snackbar.make(findViewById(android.R.id.content), "LOGIN successful", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
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
