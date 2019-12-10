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

import com.example.sistemagestioncuentas.AgregarEgresos;
import com.example.sistemagestioncuentas.ModificarEgresos;
import com.example.sistemagestioncuentas.ModificarIngresos;
import com.example.sistemagestioncuentas.MostrarEgresos;
import com.example.sistemagestioncuentas.MostrarIngreso;
import com.example.sistemagestioncuentas.R;
import com.example.sistemagestioncuentas.model.Egresos;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListarEgresosFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView myCategoriaList;
    private  View EgresosView;
    private List<Egresos> lstEgresos;
    private FirebaseAuth auth;
    private  String currentUserID;
    private DatabaseReference EgresosRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static  String idcategoria= "idcategoria";

    public ListarEgresosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EgresosView=inflater.inflate(R.layout.fragment_listar_egresos, container , false);

        myCategoriaList = (RecyclerView) EgresosView.findViewById(R.id.lista_egresos);
        myCategoriaList.setLayoutManager(new LinearLayoutManager(getContext()));
        lstEgresos = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        //opcion mas Ingresos
        FloatingActionButton fab = EgresosView.findViewById(R.id.fabegresos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity() , AgregarEgresos.class);
                startActivity(intent);

            }
        });



        EgresosRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("Egresos");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        return EgresosView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Egresos> options =
                new FirebaseRecyclerOptions.Builder<Egresos>()
                        .setQuery(EgresosRef, Egresos.class)
                        .build();


        FirebaseRecyclerAdapter<Egresos,EgresosViewHolder> adapter =
                new FirebaseRecyclerAdapter<Egresos, EgresosViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EgresosViewHolder holder, int position, @NonNull final Egresos model) {
                        holder.nameE.setText(model.getNomb_Egreso());
                        holder.fechaE.setText("Realizado el: "+model.getFecha_Egreso());
                        holder.categoriaE.setText("Categoria: "+model.getTipo_cat());
                        holder.precioE.setText("S/. "+model.getmonto_Egreso());

                        holder.Ver.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                idcategoria =   model.getId_egreso();
                                Intent intent = new Intent(getActivity(), MostrarEgresos.class);
                                intent.putExtra(MostrarEgresos.idcategoria,idcategoria);
                                startActivity(intent);
                            }
                        });

                        holder.Modificar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                idcategoria =   model.getId_egreso();
                                Intent intent = new Intent(getActivity(), ModificarEgresos.class);
                                intent.putExtra(ModificarIngresos.idcategoria,idcategoria);
                                startActivity(intent);
                            }
                        });

                        holder.Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder confirmacion= new AlertDialog.Builder(getActivity());
                                confirmacion.setMessage("¿Seguro que quiere eliminar el Egreso?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        databaseReference.child("users").child(currentUserID).child("Egresos").child(model.getId_egreso()).removeValue();
                                        Toast toast1 = Toast.makeText(getApplicationContext(), "Egreso Eliminado", Toast.LENGTH_SHORT);
                                        toast1.show();

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert =confirmacion.create();
                                alert.setTitle("Eliminar Egreso");
                                alert.show();

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ListarEgresosFragment.EgresosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View  view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_egresos,viewGroup,false);
                        ListarEgresosFragment.EgresosViewHolder viewHolder = new ListarEgresosFragment.EgresosViewHolder(view);
                        return viewHolder;
                    }
                };


        myCategoriaList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class EgresosViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameE, fechaE,categoriaE,precioE;

        ImageButton Modificar, Eliminar,Ver;

        public EgresosViewHolder(@NonNull View itemView) {
            super(itemView);

            Modificar = (ImageButton) itemView.findViewById(R.id.ModificarE);
            Eliminar = (ImageButton) itemView.findViewById(R.id.EliminarE);
            Ver = (ImageButton) itemView.findViewById(R.id.Ver);

            nameE = itemView.findViewById(R.id.nombreE);
            fechaE = itemView.findViewById(R.id.fechaE);
            categoriaE = itemView.findViewById(R.id.categoriaE);
            precioE = itemView.findViewById(R.id.PrecioE);
        }


    }





}
