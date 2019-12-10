package com.example.sistemagestioncuentas.Fragmento;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sistemagestioncuentas.ActualizarUsuario;
import com.example.sistemagestioncuentas.AgregarCategoria;
import com.example.sistemagestioncuentas.R;
import com.example.sistemagestioncuentas.model.Ingresos;
import com.example.sistemagestioncuentas.model.Usuario;
import com.example.sistemagestioncuentas.model.categoriaG;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActualizarDatosFragment extends Fragment {

    private  View IngresosView;
    private FirebaseAuth mAuth;
    private  String currentUserID;
    private DatabaseReference UsuarioRef;
    private RecyclerView myUsuarioList;
    private List<Ingresos> lstUsuario;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView nombre,Email,Estado,Informacion;
    CircleImageView imagen;


    public ActualizarDatosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        IngresosView=inflater.inflate(R.layout.fragment_actualizar_datos, container , false);
        nombre = (TextView)IngresosView.findViewById(R.id.txtnombreU);
        Email = (TextView)IngresosView.findViewById(R.id.txtEmailU);
        Estado = (TextView)IngresosView.findViewById(R.id.txtestadoU);
        Informacion = (TextView)IngresosView.findViewById(R.id.txtinformacionU);
        imagen = (CircleImageView) IngresosView.findViewById(R.id.imageU);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsuarioRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        UsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    String EmailU = mAuth.getCurrentUser().getEmail();
                    String name = dataSnapshot.child("name").getValue().toString();
                    if(dataSnapshot.child("status").exists())
                    {
                        String status = dataSnapshot.child("status").getValue().toString();
                        Estado.setText("Estado: "+status);
                    }

                    if(dataSnapshot.child("information").exists())
                    {
                        String info = dataSnapshot.child("information").getValue().toString();
                        Informacion.setText("Informacion: "+info);
                    }

                    String image = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(imagen);
                    Email.setText("Email: "+EmailU);
                    nombre.setText("Nombre: "+name);
                    Email.setEnabled(false);
                    nombre.setEnabled(false);
                    Estado.setEnabled(false);
                    Informacion.setEnabled(false);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //hABILITAR CAMPOS
        FloatingActionButton fab = IngresosView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity() , ActualizarUsuario.class);
                startActivity(intent);

            }
        });

        return IngresosView;

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
