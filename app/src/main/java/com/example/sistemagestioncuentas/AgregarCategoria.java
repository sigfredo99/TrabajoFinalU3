package com.example.sistemagestioncuentas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemagestioncuentas.Fragmento.ListarCategoriaFragment;
import com.example.sistemagestioncuentas.model.categoriaG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class AgregarCategoria extends AppCompatActivity {

    EditText nombre,descripcion;
    EditText idUser;
    ListView listacategoria;
    Spinner tipomovimiento;
    Button AgregarCategoria;



    DatabaseReference UserRef;
    DatabaseReference CategoriaRef;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String currentId;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_categoria);

        tipomovimiento =(Spinner) findViewById(R.id.spinner);
        idUser = (EditText) findViewById(R.id.idusers);
        nombre = (EditText) findViewById(R.id.txt_nombreCategoria);
        descripcion = (EditText) findViewById(R.id.txt_descripcionCategoria);
        //BTN
        AgregarCategoria = (Button)findViewById(R.id.btnAgregarCategoria);

        mAuth = FirebaseAuth.getInstance();
        currentId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.tipomovimiento,android.R.layout.simple_spinner_item);
        tipomovimiento.setAdapter(adapter);

        UserRef.child(currentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String UID = mAuth.getCurrentUser().getUid();
                    idUser.setText(UID);
                    idUser.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        AgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddUser();
            }
        });

        CategoriaRef = FirebaseDatabase.getInstance().getReference().child("Categoria");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logoicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
    private void AddUser()
    {

        String userID = idUser.getText().toString();
        String nombrecat = nombre.getText().toString();
        String desccat = descripcion.getText().toString();
        String tipocat= tipomovimiento.getSelectedItem().toString();
        categoriaG c= new categoriaG();
        c.setId_cat(UUID.randomUUID().toString());
        c.setUid(currentId);
        c.setNomb_cat(nombrecat);
        c.setDesc_cat(desccat);
        c.setTipo_cat(tipocat);
        //databaseReference.child("Categoria").child(c.getId_cat()).setValue(c);
        databaseReference.child("users").child(currentId).child("Categoria").child(c.getId_cat()).setValue(c);
        limpiarcajas();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListarCategoriaFragment()).commit()
    }

    private void limpiarcajas() {
        nombre.setText("");
        descripcion.setText("");
    }

    private void validacion() {
        String nombrecat = nombre.getText().toString();
        String desccat = descripcion.getText().toString();
        if(nombrecat.equals("")){
            nombre.setError("Required");
        }else{
            if(desccat.equals("")){
                descripcion.setError("Required");}
        }
    }
}
