package com.example.sistemagestioncuentas;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sistemagestioncuentas.model.Egresos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AgregarEgresos extends AppCompatActivity {

    private FirebaseAuth auth;
    private  String currentUserID;
    private DatabaseReference CategoriaRef;
    TextView nombreEgreso,descripcionEgreso,MontoEgreso,diasEgreso,fechaEgreso;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Spinner listaCategoria;
    Spinner ItemEgresoUsual;
    Button btnfecha,btnAgregarEgreso;
    EditText txtfecha;
    private int dia, mes,ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_egresos);

        listaCategoria = (Spinner)findViewById(R.id.ListaCategoriaEgresos) ;
        ItemEgresoUsual = (Spinner)findViewById(R.id.EgresoUsual) ;
        txtfecha = (EditText)findViewById(R.id.txtfechaEgresos);
        nombreEgreso = (EditText)findViewById(R.id.txt_nombreEgresos);
        descripcionEgreso = (EditText)findViewById(R.id.txtdescripcionEgresos);
        MontoEgreso = (EditText)findViewById(R.id.txtMontoEgresos);
        diasEgreso = (EditText)findViewById(R.id.txtdiasEgresoUsual);
        btnfecha = (Button)findViewById(R.id.btnfechaEgresos);
        btnAgregarEgreso = (Button) findViewById(R.id.btnAgregarEgreso);
        diasEgreso.setText("0");
// de agrea el ingreso usual
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.EgresoUsuales,android.R.layout.simple_spinner_item);
        ItemEgresoUsual.setAdapter(adapter);

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
                        if(areaSnapshot.child("tipo_cat").getValue().toString().equals("Egresos")) {
                            String areaName = areaSnapshot.child("nomb_cat").getValue(String.class);
                            areas.add(areaName);
                        }
                    }

                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AgregarEgresos.this, android.R.layout.simple_spinner_item, areas);
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
                final Calendar c =Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarEgresos.this, new DatePickerDialog.OnDateSetListener() {
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

        btnAgregarEgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarEgreso();
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logoicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

    }

    private void AgregarEgreso() {

        String nombreEgresos = nombreEgreso.getText().toString();
        String descripcionEgresos = descripcionEgreso.getText().toString();
        String categoria= listaCategoria.getSelectedItem().toString();
        String tipoEgreso= ItemEgresoUsual.getSelectedItem().toString();
        String MontoEgresos = MontoEgreso.getText().toString();
        String diasEgresos = diasEgreso.getText().toString();
        String fechaEgresos = txtfecha.getText().toString();
        Egresos i= new Egresos();
        i.setId_egreso(UUID.randomUUID().toString());
        i.setUid(currentUserID);
        i.setNomb_Egreso(nombreEgresos);
        i.setDesc_Egreso(descripcionEgresos);
        i.setTipo_cat(categoria);
        i.setmonto_Egreso(MontoEgresos);
        i.setTipo_egreso(tipoEgreso);
        i.setDias_Egreso(diasEgresos);
        i.setFecha_Egreso(fechaEgresos);
        //databaseReference.child("Categoria").child(c.getId_cat()).setValue(c);
        databaseReference.child("users").child(currentUserID).child("Egresos").child(i.getId_egreso()).setValue(i);
        limpiarcajas();
    }
    private void limpiarcajas() {
        nombreEgreso.setText("");
        descripcionEgreso.setText("");
        diasEgreso.setText("");
        MontoEgreso.setText("");
        txtfecha.setText("");

    }
}
