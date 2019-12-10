package com.example.sistemagestioncuentas.Fragmento;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.sistemagestioncuentas.ActualizarUsuario;
import com.example.sistemagestioncuentas.AgregarCategoria;
import com.example.sistemagestioncuentas.GestionarCategoria;
import com.example.sistemagestioncuentas.MainActivity;
import com.example.sistemagestioncuentas.ModificarCategoria;
import com.example.sistemagestioncuentas.MostrarCategoria;
import com.example.sistemagestioncuentas.R;
import com.example.sistemagestioncuentas.model.categoriaG;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListarCategoriaFragment extends Fragment {




    private List<categoriaG> lstCategoria;

    //
    private  View CategoriaView;
    private RecyclerView myCategoriaList;

    private  DatabaseReference userRef;
    private FirebaseAuth auth;
    private  String currentUserID;
    private DatabaseReference CategoriaRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static  String idcategoria= "idcategoria";

    public ListarCategoriaFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        CategoriaView=inflater.inflate(R.layout.fragment_listar_categoria, container , false);

        myCategoriaList = (RecyclerView) CategoriaView.findViewById(R.id.list_categoria);
        myCategoriaList.setLayoutManager(new LinearLayoutManager(getContext()));
        lstCategoria = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();


//nueva Categoria
        FloatingActionButton fab = CategoriaView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity() , AgregarCategoria.class);
                startActivity(intent);

            }
        });

///////////////////////
//CategoriaRef = FirebaseDatabase.getInstance().getReference().child("Categoria");
        CategoriaRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("Categoria");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        return CategoriaView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<categoriaG> options =
                new FirebaseRecyclerOptions.Builder<categoriaG>()
                .setQuery(CategoriaRef, categoriaG.class)
                .build();

        FirebaseRecyclerAdapter<categoriaG,CategoriaViewHolder> adapter =
                new FirebaseRecyclerAdapter<categoriaG, CategoriaViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position, @NonNull final categoriaG model) {
                        holder.uid.setText(model.getUid());
                        holder.idC.setText(model.getId_cat());
                        holder.nameC.setText(model.getNomb_cat());
                        holder.descripcionC.setText(model.getDesc_cat());
                        holder.tipoC.setText(model.getTipo_cat());
                        holder.uid.setVisibility(View.INVISIBLE);
                        holder.idC.setVisibility(View.INVISIBLE);




                        holder.Ver.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                idcategoria =   model.getId_cat();
                                Intent intent = new Intent(getActivity(), MostrarCategoria.class);
                                intent.putExtra(MostrarCategoria.idcategoria,idcategoria);
                                startActivity(intent);
                            }
                        });


                        holder.Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder confirmacion= new AlertDialog.Builder(getActivity());
                                confirmacion.setMessage("¿Seguro que quiere eliminar esta categoría?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        databaseReference.child("users").child(currentUserID).child("Categoria").child(model.getId_cat()).removeValue();
                                        Toast toast1 = Toast.makeText(getApplicationContext(), "Categoría Borrada", Toast.LENGTH_SHORT);
                                        toast1.show();

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert =confirmacion.create();
                                alert.setTitle("Eliminar Categoría");
                                alert.show();

                            }
                        });


                        holder.Modificar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                              idcategoria =   model.getId_cat();
                              Intent intent = new Intent(getActivity(), ModificarCategoria.class);
                              intent.putExtra(ModificarCategoria.idcategoria,idcategoria);
                              startActivity(intent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View  view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_categoria,viewGroup,false);
                        CategoriaViewHolder viewHolder = new CategoriaViewHolder(view);
                        return viewHolder;


                    }
                };
        myCategoriaList.setAdapter(adapter);
        adapter.startListening();

    }



    public static class CategoriaViewHolder extends RecyclerView.ViewHolder
    {
        TextView idC, nameC, descripcionC,tipoC,uid;

        ImageButton Modificar, Eliminar,Ver;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);

            Modificar = (ImageButton) itemView.findViewById(R.id.Modificar);
            Eliminar = (ImageButton) itemView.findViewById(R.id.Eliminar);
            Ver =(ImageButton)itemView.findViewById(R.id.Ver);
            uid = itemView.findViewById(R.id.idcategoria_a);
            idC = itemView.findViewById(R.id.idcategoria);
            nameC = itemView.findViewById(R.id.nombrecategoria);
            descripcionC = itemView.findViewById(R.id.descripcionCategoriaa);
            tipoC = itemView.findViewById(R.id.tipocategoria);
        }


    }

}
