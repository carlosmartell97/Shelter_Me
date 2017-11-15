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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
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
    TableLayout table;
    TableRow header;
    TextView test,test2;
    TableRow entry;
    TextView item;
    TextView cantidad;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acopio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        entry = new TableRow(this);
        item = new TextView(this);
        cantidad = new TextView(this);

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
        this.header.addView(test);
        this.header.addView(test2);
        this.table.addView(header);

        this.mainView = findViewById(android.R.id.content);

    }

    public void acopio1(View view){
        Log.d("->","acopio1()");
        updateInfo("Comida Para Todos");
        //table.removeAllViews();

    }

    public void acopio2(View view){
        Log.d("->","acopio2()");
        updateInfo("Juntos Podemos");
        //table.removeAllViews();
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
                            item.setText(child.child("nivel_abastecimiento").getKey().toString());
                            cantidad.setText(child.child("nivel_abastecimiento").getValue().toString());
                            entry.removeAllViews();
                            entry.addView(item);
                            entry.addView(cantidad);
                            table.removeAllViews();
                            table.addView(entry);




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
