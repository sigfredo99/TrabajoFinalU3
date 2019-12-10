package com.example.sistemagestioncuentas;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sistemagestioncuentas.Fragmento.ListarCategoriaFragment;
import com.example.sistemagestioncuentas.model.Ingresos;
import com.example.sistemagestioncuentas.model.categoriaG;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class AgregarIngresos extends AppCompatActivity {

    private FirebaseAuth auth;
    private  String currentUserID;
    private DatabaseReference CategoriaRef;
    TextView nombreIngreso,descripcionIngreso,MontoIngreso,diasIngreso,fechaIngreso;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Spinner listaCategoria;
    Spinner ItemIngresoUsual;
    Button btnfecha,btnAgregarIngreso;
    EditText txtfecha;
    private int dia, mes,ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ingresos);

        listaCategoria = (Spinner)findViewById(R.id.ListaCategoriaIngresos) ;
        ItemIngresoUsual = (Spinner)findViewById(R.id.IngresoUsual) ;
        txtfecha = (EditText)findViewById(R.id.txtfechaIngresos);
        nombreIngreso = (EditText)findViewById(R.id.txt_nombreIngresos);
        descripcionIngreso = (EditText)findViewById(R.id.txtdescripcionIngresos);
        MontoIngreso = (EditText)findViewById(R.id.txtMontoIngresos);
        diasIngreso = (EditText)findViewById(R.id.txtdiasIngresoUsual);
        btnfecha = (Button)findViewById(R.id.btnfecha);
        btnAgregarIngreso = (Button) findViewById(R.id.btnAgregarIngreso);
        diasIngreso.setEnabled(true);
        diasIngreso.setText("0");
// de agrea el ingreso usual
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.IngresoUsuales,android.R.layout.simple_spinner_item);
        ItemIngresoUsual.setAdapter(adapter);


        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        CategoriaRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("Categoria");
      ///listar categoria
        CategoriaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    final List<String> areas = new ArrayList<String>();

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        if(areaSnapshot.child("tipo_cat").getValue().toString().equals("Ingresos")) {
                            String areaName = areaSnapshot.child("nomb_cat").getValue(String.class);
                            areas.add(areaName);
                        }
                    }
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AgregarIngresos.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    listaCategoria.setAdapter(areasAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//obtener la fecha
        btnfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Calendar c =Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarIngresos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtfecha.setText(dayOfMonth+"/"+(month+1)+"/"+(year));
                    }
                }
                ,dia,mes,ano);
                datePickerDialog.show();

            }
        });
        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        btnAgregarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarIngreso();


            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logoicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


    }

    private void AgregarIngreso() {


        String nombreIngresos = nombreIngreso.getText().toString();
        String descripcionIngresos = descripcionIngreso.getText().toString();
        String categoria= listaCategoria.getSelectedItem().toString();
        String tipoIngreso= ItemIngresoUsual.getSelectedItem().toString();
        String MontoIngresos = MontoIngreso.getText().toString();
        String diasIngresos = diasIngreso.getText().toString();
        String fechaIngresos = txtfecha.getText().toString();
        Ingresos i= new Ingresos();
        i.setId_ingreso(UUID.randomUUID().toString());
        i.setUid(currentUserID);
        i.setNomb_Ingreso(nombreIngresos);
        i.setDesc_Ingreso(descripcionIngresos);
        i.setTipo_cat(categoria);
        i.setMonto_Ingreso(MontoIngresos);
        i.setTipo_Ingreso(tipoIngreso);
        i.setDias_Ingreso(diasIngresos);
        i.setFecha_Ingreso(fechaIngresos);
        //databaseReference.child("Categoria").child(c.getId_cat()).setValue(c);
        databaseReference.child("users").child(currentUserID).child("Ingresos").child(i.getId_ingreso()).setValue(i);
        limpiarcajas();
    }
    private void limpiarcajas() {
        nombreIngreso.setText("");
        descripcionIngreso.setText("");
        diasIngreso.setText("");
        MontoIngreso.setText("");
        txtfecha.setText("");

    }
}
