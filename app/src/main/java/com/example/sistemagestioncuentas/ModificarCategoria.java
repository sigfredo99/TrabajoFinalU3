package com.example.sistemagestioncuentas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemagestioncuentas.model.categoriaG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class ModificarCategoria extends AppCompatActivity {

    public static final String idcategoria = "idcategoria";;

    TextView txtidCM,IDuserM;

    EditText nombreM,descripcionM;
    Spinner tipomovimientoU;
    Button ModificarCategoria;

    String currentId;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    DatabaseReference CategoriaRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_categoria);

        txtidCM = (TextView) findViewById(R.id.idCM);
        IDuserM = (TextView) findViewById(R.id.UIDM);
        nombreM = (EditText) findViewById(R.id.nombreCategoriaU);
        descripcionM = (EditText) findViewById(R.id.descripcionCategoriaU);
        tipomovimientoU =(Spinner) findViewById(R.id.spinnerU);

        ModificarCategoria = (Button)findViewById(R.id.btnModificarCategoria);


        //ID CATEGORIA
        String idcategoria = getIntent().getStringExtra("idcategoria");
        txtidCM.setText(idcategoria);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.tipomovimiento,android.R.layout.simple_spinner_item);
        tipomovimientoU.setAdapter(adapter);
        ////
        mAuth = FirebaseAuth.getInstance();
        currentId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");

        UserRef.child(currentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    IDuserM.setText(currentId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        CategoriaRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentId).child("Categoria");

        CategoriaRef.child(idcategoria).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String nombreC_U = dataSnapshot.child("nomb_cat").getValue().toString();
                    nombreM.setText(nombreC_U);
                    String descripcionC_U = dataSnapshot.child("desc_cat").getValue().toString();
                    descripcionM.setText(descripcionC_U);
                    String tipo_U = dataSnapshot.child("tipo_cat").getValue().toString();
                    txtidCM.setVisibility(View.INVISIBLE);
                    IDuserM.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ModificarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCategoria();
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logoicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    private void UpdateCategoria()
    {
        final String IdCategoriaU = txtidCM.getText().toString();
        final String nombreU = nombreM.getText().toString();
        final String descripcionU = descripcionM.getText().toString();
        final String tipoMovimientoU = tipomovimientoU.getSelectedItem().toString();
        if(TextUtils.isEmpty(nombreU))
        {
            Toast.makeText(this,"registre el nombre de la categoria",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(descripcionU))
        {
            Toast.makeText(this,"registre la descripcion de la categoria\"",Toast.LENGTH_LONG).show();
        }
        else
        {

            AlertDialog.Builder confirmacion1= new AlertDialog.Builder(ModificarCategoria.this);
            confirmacion1.setMessage("¿Seguro que quiere modificar esta categoría?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    categoriaG c= new categoriaG();
                    c.setId_cat(IdCategoriaU);
                    c.setUid(currentId);
                    c.setNomb_cat(nombreU);
                    c.setDesc_cat(descripcionU);
                    c.setTipo_cat(tipoMovimientoU);
                    //databaseReference.child("Categoria").child(c.getId_cat()).setValue(c);
                    databaseReference.child("users").child(currentId).child("Categoria").child(c.getId_cat()).setValue(c);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Categoría Modificada", Toast.LENGTH_SHORT);
                    toast1.show();

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert =confirmacion1.create();
            alert.setTitle("Modificar Categoría");
            alert.show();


        }

    }



}
