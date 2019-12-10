package com.example.sistemagestioncuentas.Fragmento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemagestioncuentas.AgregarCategoria;
import com.example.sistemagestioncuentas.AgregarIngresos;
import com.example.sistemagestioncuentas.ModificarCategoria;
import com.example.sistemagestioncuentas.ModificarIngresos;
import com.example.sistemagestioncuentas.MostrarIngreso;
import com.example.sistemagestioncuentas.R;
import com.example.sistemagestioncuentas.model.Ingresos;
import com.example.sistemagestioncuentas.model.categoriaG;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListarIngresosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView myCategoriaList;
    private  View IngresosView;
    private List<Ingresos> lstIngresos;
    private FirebaseAuth auth;
    private  String currentUserID;
    private DatabaseReference IngresosRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static  String idcategoria= "idcategoria";



    public ListarIngresosFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        IngresosView=inflater.inflate(R.layout.fragment_listar_ingresos, container , false);

        myCategoriaList = (RecyclerView) IngresosView.findViewById(R.id.lista_Ingresos);
        myCategoriaList.setLayoutManager(new LinearLayoutManager(getContext()));
        lstIngresos = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        //opcion mas Ingresos
        FloatingActionButton fab = IngresosView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity() , AgregarIngresos.class);
                startActivity(intent);

            }
        });



        IngresosRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("Ingresos");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        return IngresosView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Ingresos> options =
                new FirebaseRecyclerOptions.Builder<Ingresos>()
                        .setQuery(IngresosRef, Ingresos.class)
                        .build();


        FirebaseRecyclerAdapter<Ingresos,IngresosViewHolder> adapter =
                new FirebaseRecyclerAdapter<Ingresos, IngresosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull IngresosViewHolder holder, int position, @NonNull final Ingresos model) {
                holder.nameI.setText(model.getNomb_Ingreso());
                holder.fechaI.setText("Realizado el: "+model.getFecha_Ingreso());
                holder.categoriaI.setText("Categoria: "+model.getTipo_cat());
                holder.precioI.setText("S/. "+model.getMonto_Ingreso());


                holder.Ver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idcategoria =   model.getId_ingreso();
                        Intent intent = new Intent(getActivity(), MostrarIngreso.class);
                        intent.putExtra(MostrarIngreso.idcategoria,idcategoria);
                        startActivity(intent);
                    }
                });

                holder.Modificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idcategoria =   model.getId_ingreso();
                        Intent intent = new Intent(getActivity(), ModificarIngresos.class);
                        intent.putExtra(ModificarIngresos.idcategoria,idcategoria);
                        startActivity(intent);
                    }
                });

                holder.Eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder confirmacion= new AlertDialog.Builder(getActivity());
                        confirmacion.setMessage("¿Seguro que quiere eliminar el Ingreso?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                databaseReference.child("users").child(currentUserID).child("Ingresos").child(model.getId_ingreso()).removeValue();
                                Toast toast1 = Toast.makeText(getApplicationContext(), "Ingreso Eliminado", Toast.LENGTH_SHORT);
                                toast1.show();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert =confirmacion.create();
                        alert.setTitle("Eliminar Ingreso");
                        alert.show();

                    }
                });

            }

            @NonNull
            @Override
            public IngresosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View  view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ingresos,viewGroup,false);
                IngresosViewHolder viewHolder = new IngresosViewHolder(view);
                return viewHolder;
            }
        };


        myCategoriaList.setAdapter(adapter);
        adapter.startListening();

    }







    public static class IngresosViewHolder extends RecyclerView.ViewHolder
    {
        TextView  nameI, fechaI,categoriaI,precioI;

        ImageButton Modificar, Eliminar,Ver;

        public IngresosViewHolder(@NonNull View itemView) {
            super(itemView);

            Modificar = (ImageButton) itemView.findViewById(R.id.Modificar);
            Eliminar = (ImageButton) itemView.findViewById(R.id.Eliminar);
            Ver = (ImageButton) itemView.findViewById(R.id.Ver);

            nameI = itemView.findViewById(R.id.nombreI);
            fechaI = itemView.findViewById(R.id.fechaI);
            categoriaI = itemView.findViewById(R.id.categoriaI);
            precioI = itemView.findViewById(R.id.Precio);
        }


    }


}
