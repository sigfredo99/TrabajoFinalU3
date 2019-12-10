package com.example.sistemagestioncuentas;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MostrarEgresos extends AppCompatActivity {

    public static final String idcategoria = "idcategoria";


    TextView txtidCM,IDuserM;

    TextView nombre,descripcion,tipo,monto,tipoIngreso,dias,fecha;
    TextView tipomovimientoU;
    Button ModificarCategoria;

    String currentId;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    DatabaseReference IngresoRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_egresos);
        nombre = (TextView) findViewById(R.id.nombreIngreso);
        descripcion = (TextView) findViewById(R.id.descripcionIngreso);
        tipo =(TextView) findViewById(R.id.tipocategoriaIngreso);
        monto = (TextView) findViewById(R.id.montoIngreso);
        tipoIngreso = (TextView) findViewById(R.id.TipoingresoUsual);
        dias =(TextView) findViewById(R.id.DiasIngreso);
        fecha =(TextView) findViewById(R.id.fechaIngreso);

        final String idcategoria = getIntent().getStringExtra("idcategoria");


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
                    nombre.setText( "Nombre : "+nombreC_U);
                    descripcion.setText("Descripcion: "+descripcionn);
                    tipo.setText("Categoria:"+Categoria);
                    monto.setText("Monto: "+montoo);
                    tipoIngreso.setText("Tipo: "+Tipo);
                    dias.setText("Dias: "+Dias);
                    fecha.setText("fecha: "+Fecha);

//                    txtidCM.setVisibility(View.INVISIBLE);
//                    IDuserM.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logoicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

    }
}