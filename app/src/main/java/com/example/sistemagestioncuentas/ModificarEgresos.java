package com.example.sistemagestioncuentas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemagestioncuentas.model.Egresos;
import com.example.sistemagestioncuentas.model.Ingresos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ModificarEgresos extends AppCompatActivity {

    public static final String idcategoria = "idcategoria";


    TextView txtid;

    EditText nombre,descripcion,monto,dias,fecha;
    TextView tipomovimientoU;
    Button ModificarCategoria;
    Spinner listaCategoria;
    Spinner ItemIngresoUsual;
    Button btnfecha,btnAgregarIngreso;
    EditText txtfecha;
    private int dia, mes,ano;
    String currentId;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    DatabaseReference IngresoRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private  String currentUserID;
    private DatabaseReference CategoriaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_ingresos);

        txtid = (TextView) findViewById(R.id.ID);
        listaCategoria = (Spinner)findViewById(R.id.ListaCategoriaIngresosM) ;
        ItemIngresoUsual = (Spinner)findViewById(R.id.IngresoUsualM) ;
        txtfecha = (EditText)findViewById(R.id.fechaModificar);
        nombre = (EditText)findViewById(R.id.txt_nombreIngresosM);
        descripcion = (EditText)findViewById(R.id.txtdescripcionIngresosM);
        monto = (EditText)findViewById(R.id.txtMontoIngresosM);
        dias = (EditText)findViewById(R.id.txtdiasIngresoUsualM);
        btnfecha = (Button)findViewById(R.id.btnfechaModificar);
        btnAgregarIngreso = (Button) findViewById(R.id.btnModificarIngreso);



// de agrea el ingreso usual
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.EgresoUsuales,android.R.layout.simple_spinner_item);
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
                        if(areaSnapshot.child("tipo_cat").getValue().toString().equals("Egresos")) {
                            String areaName = areaSnapshot.child("nomb_cat").getValue(String.class);
                            areas.add(areaName);
                        }
                    }
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(ModificarEgresos.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    listaCategoria.setAdapter(areasAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String idcategoria = getIntent().getStringExtra("idcategoria");
        txtid.setText(idcategoria);
        txtid.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        currentId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        IngresoRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentId).child("Egresos");
        IngresoRef.child(idcategoria).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String nombreC_U = dataSnapshot.child("nomb_Egreso").getValue().toString();
                    String descripcionn = dataSnapshot.child("desc_Egreso").getValue().toString();
                    String Categoria = dataSnapshot.child("tipo_cat").getValue().toString();
                    String montoo = dataSnapshot.child("monto_Egreso").getValue().toString();
                    String Tipo = dataSnapshot.child("tipo_egreso").getValue().toString();
                    String Dias = dataSnapshot.child("dias_Egreso").getValue().toString();
                    String Fecha = dataSnapshot.child("fecha_Egreso").getValue().toString();
                    nombre.setText( nombreC_U);
                    descripcion.setText(descripcionn);
                    monto.setText(montoo);
                    dias.setText(Dias);
                    txtfecha.setText(Fecha);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ModificarEgresos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtfecha.setText(dayOfMonth+"/"+(month+1)+"/"+(year));
                    }
                }
                        ,dia,mes,ano);
                datePickerDialog.show();
            }
        });

        btnAgregarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateEgreso();
            }
        });


    }


    private void UpdateEgreso()
    {
        final String IdIEgreso = txtid.getText().toString();
        final String nombreEgresos = nombre.getText().toString();
        final String descripcionEgresos = descripcion.getText().toString();
        final String categoria= listaCategoria.getSelectedItem().toString();
        final String tipoEgreso= ItemIngresoUsual.getSelectedItem().toString();
        final String MontoEgresos = monto.getText().toString();
        final String diasEgresos = dias.getText().toString();
        final String fechaEgresos = txtfecha.getText().toString();

        if(TextUtils.isEmpty(nombreEgresos))
        {
            Toast.makeText(getApplicationContext(),"registre el nombre",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(descripcionEgresos))
        {
            Toast.makeText(getApplicationContext(),"registre la descripcion ",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(MontoEgresos))
        {
            Toast.makeText(getApplicationContext(),"registre el Monto ",Toast.LENGTH_LONG).show();
        }
        else
        {

            AlertDialog.Builder confirmacion1= new AlertDialog.Builder(ModificarEgresos.this);
            confirmacion1.setMessage("¿Seguro que modificar?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Egresos c= new Egresos();
                    c.setId_egreso(IdIEgreso);
                    c.setUid(currentId);
                    c.setNomb_Egreso(nombreEgresos);
                    c.setDesc_Egreso(descripcionEgresos);
                    c.setTipo_cat(categoria);
                    c.setTipo_egreso(tipoEgreso);
                    c.setmonto_Egreso(MontoEgresos);
                    c.setDias_Egreso(diasEgresos);
                    c.setFecha_Egreso(fechaEgresos);
                    //databaseReference.child("Categoria").child(c.getId_cat()).setValue(c);
                    databaseReference.child("users").child(currentId).child("Egresos").child(c.getId_egreso()).setValue(c);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Egreso Modificada", Toast.LENGTH_SHORT);
                    toast1.show();

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert =confirmacion1.create();
            alert.setTitle("Modificar Egreso");
            alert.show();










        }

    }



}
