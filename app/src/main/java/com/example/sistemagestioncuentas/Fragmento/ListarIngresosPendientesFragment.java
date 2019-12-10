package com.example.sistemagestioncuentas.Fragmento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemagestioncuentas.AgregarIngresos;
import com.example.sistemagestioncuentas.R;
import com.example.sistemagestioncuentas.model.Egresos;
import com.example.sistemagestioncuentas.model.Ingresos;
import com.example.sistemagestioncuentas.model.IngresosPendientes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListarIngresosPendientesFragment extends Fragment {

    private FirebaseAuth auth;
    private  String currentUserID;
    private DatabaseReference IngresosRef;
    private RecyclerView myIngresosPendientesList;
    private  View IngresosPendientesView;
    private List<Ingresos> lstIngresosPendientes;
    DatabaseReference IngresosPendientesRef;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    public ListarIngresosPendientesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        IngresosPendientesView=inflater.inflate(R.layout.fragment_listar_ingresos_pendientes, container , false);

        myIngresosPendientesList = (RecyclerView) IngresosPendientesView.findViewById(R.id.lista_IngresosPendientes);
        myIngresosPendientesList.setLayoutManager(new LinearLayoutManager(getContext()));
        lstIngresosPendientes = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        IngresosRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("Ingresos");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        return IngresosPendientesView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Ingresos> options = new FirebaseRecyclerOptions.Builder<Ingresos>()
                        .setQuery(IngresosRef, Ingresos.class)
                        .build();

        FirebaseRecyclerAdapter<Ingresos,IngresosPendientesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Ingresos, IngresosPendientesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull IngresosPendientesViewHolder holder, int position, @NonNull final Ingresos model) {


                        if(model.getTipo_Ingreso().equals("Ingreso Usual"))
                        {

                            Date date = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            final String fechaA = dateFormat.format(date);

                            Date fechaInicial= null;
                            try {
                                fechaInicial = dateFormat.parse(model.getFecha_Ingreso());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date fechaFinal= null;
                            try {
                                fechaFinal = dateFormat.parse(fechaA);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            int DAYi = Integer.parseInt(model.getDias_Ingreso());

                            // calcular la fecha limite de acuerdo a los dias establecidos
                            Date nuevaFecha = new Date();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(fechaInicial);
                            cal.add(Calendar.DAY_OF_YEAR ,DAYi);
                            nuevaFecha = cal.getTime();
                            SimpleDateFormat formatoFecha = new SimpleDateFormat();
                            formatoFecha.applyPattern("dd/MM/yyyy");
                           final String fechaRespuesta = formatoFecha.format(nuevaFecha);

                            Date fechaRespuestap= null;
                            try {
                                fechaRespuestap = dateFormat.parse(fechaRespuesta);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            int dias=(int) ((fechaRespuestap.getTime()-fechaFinal.getTime())/86400000);

                            holder.nameIP.setText(model.getNomb_Ingreso());
                            holder.fechaIP.setText("Fecha Limite: "+fechaRespuesta);
                            holder.diasIP.setText(dias+" Dias"+"\n"+" Restantes");
                            holder.precioIP.setText("Monto: "+model.getMonto_Ingreso());
                            holder.fecha.setText("Fecha Inicial: "+model.getFecha_Ingreso());

                            holder.PagarIP.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder confirmacion= new AlertDialog.Builder(getActivity());
                                    confirmacion.setMessage("¿Estas seguro Recibir su Ingreso?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            String nombreIngresosPendientes = model.getNomb_Ingreso();
                                            String descripcionIngresosPendientes = model.getDesc_Ingreso();
                                            String categoria= model.getTipo_cat();
                                            String tipoIngresoPendiente= model.getTipo_Ingreso();
                                            String MontoIngresosPendiente = model.getMonto_Ingreso();
                                            String diasIngresosPendiente = model.getDias_Ingreso();
                                            String fechaIngresoPendiente = model.getFecha_Ingreso();

                                            IngresosPendientes IP= new IngresosPendientes();

                                            IP.setId_ingresoPendiente(UUID.randomUUID().toString());
                                            IP.setUid(currentUserID);
                                            IP.setNomb_IngresoP(nombreIngresosPendientes);
                                            IP.setDesc_IngresoP(descripcionIngresosPendientes);
                                            IP.setTipo_catP(categoria);
                                            IP.setMonto_IngresoP(MontoIngresosPendiente);
                                            IP.setTipo_IngresoP(tipoIngresoPendiente);
                                            IP.setDias(diasIngresosPendiente);
                                            IP.setFechaInicial(fechaIngresoPendiente);
                                            IP.setFecha_Final(fechaRespuesta);
                                            IP.setFechaPago(fechaA);
                                            databaseReference.child("users").child(currentUserID).child("IngresosPendientes").child(IP.getId_ingresoPendiente()).setValue(IP);


                                            Ingresos i= new Ingresos();
                                            i.setId_ingreso(model.getId_ingreso());
                                            i.setUid(currentUserID);
                                            i.setNomb_Ingreso(model.getNomb_Ingreso());
                                            i.setDesc_Ingreso(model.getDesc_Ingreso());
                                            i.setTipo_cat(model.getTipo_cat());
                                            i.setMonto_Ingreso(model.getMonto_Ingreso());
                                            i.setTipo_Ingreso(model.getTipo_Ingreso());
                                            i.setDias_Ingreso(model.getDias_Ingreso());
                                            i.setFecha_Ingreso(fechaRespuesta);
                                            databaseReference.child("users").child(currentUserID).child("Ingresos").child(i.getId_ingreso()).setValue(i);

                                            Toast toast1 = Toast.makeText(getApplicationContext(), "Ingreso Recibido", Toast.LENGTH_SHORT);
                                            toast1.show();

                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alert =confirmacion.create();
                                    alert.setTitle("Ingreso Recibido");
                                    alert.show();

                                }
                            });





                            ///ELiminar Ingreso
                            holder.Eliminar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder confirmacion= new AlertDialog.Builder(getActivity());
                                    confirmacion.setMessage("¿Estas seguro que quieres eliminar tu Ingreso?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
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
                        else
                        {
                            holder.nameIP.setText(null);
                            holder.fechaIP.setText(null);
                            holder.diasIP.setText(null);
                            holder.precioIP.setText(null);

                        }
                    }
                    @NonNull
                    @Override
                    public IngresosPendientesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View  view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ingresospendientes,viewGroup,false);
                        IngresosPendientesViewHolder viewHolder = new IngresosPendientesViewHolder(view);
                        return viewHolder;
                    }
                };
        myIngresosPendientesList.setAdapter(adapter);
        adapter.startListening();


    }

    public static class IngresosPendientesViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameIP, fechaIP,diasIP,precioIP,fecha;

        ImageButton Modificar, Eliminar,PagarIP;

        public IngresosPendientesViewHolder(@NonNull View itemView) {
            super(itemView);
            Modificar = (ImageButton) itemView.findViewById(R.id.Modificar);
            Eliminar = (ImageButton) itemView.findViewById(R.id.Eliminar);
            PagarIP = (ImageButton) itemView.findViewById(R.id.Pagar);
            nameIP = itemView.findViewById(R.id.nombreIP);
            fechaIP = itemView.findViewById(R.id.fechaIP);
            precioIP = itemView.findViewById(R.id.precioIP);
            diasIP = itemView.findViewById(R.id.diasIP);
            fecha = itemView.findViewById(R.id.fechactual);
        }


    }


}
