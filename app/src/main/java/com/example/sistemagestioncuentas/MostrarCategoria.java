package com.example.sistemagestioncuentas;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MostrarCategoria extends AppCompatActivity {


    public static final String idcategoria = "idcategoria";;

    TextView txtidCM,IDuserM;

    TextView nombreM,descripcionM;
    TextView tipomovimientoU;
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
        setContentView(R.layout.activity_mostrar_categoria);
        txtidCM = (TextView) findViewById(R.id.idCM);
        IDuserM = (TextView) findViewById(R.id.UIDM);
        nombreM = (TextView) findViewById(R.id.nombreCategoriaU);
        descripcionM = (TextView) findViewById(R.id.descripcionCategoriaU);
        tipomovimientoU =(TextView) findViewById(R.id.tipo);

        String idcategoria = getIntent().getStringExtra("idcategoria");

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
                    nombreM.setText( "Nombre : "+nombreC_U);
                    String descripcionC_U = dataSnapshot.child("desc_cat").getValue().toString();
                    descripcionM.setText("Descripcion: "+descripcionC_U);
                    String tipo_U = dataSnapshot.child("tipo_cat").getValue().toString();
                    tipomovimientoU.setText("Tipo: "+tipo_U);
                    txtidCM.setVisibility(View.INVISIBLE);
                    IDuserM.setVisibility(View.INVISIBLE);
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
