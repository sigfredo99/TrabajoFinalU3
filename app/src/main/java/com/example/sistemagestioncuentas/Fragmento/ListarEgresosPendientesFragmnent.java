package com.example.sistemagestioncuentas.Fragmento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemagestioncuentas.R;
import com.example.sistemagestioncuentas.model.Egresos;
import com.example.sistemagestioncuentas.model.EgresosPendientes;
import com.example.sistemagestioncuentas.model.Ingresos;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListarEgresosPendientesFragmnent extends Fragment {

    private FirebaseAuth auth;
    private  String currentUserID;
    private DatabaseReference EgresosRef;
    private RecyclerView myEgresosPendientesList;
    private  View EgresosPendientesView;
    private List<Egresos> lstEgresosPendientes;
    DatabaseReference EgresosPendientesRef;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    public ListarEgresosPendientesFragmnent() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EgresosPendientesView=inflater.inflate(R.layout.fragment_listar_egresos_pendientes_fragmnent, container , false);

        myEgresosPendientesList = (RecyclerView) EgresosPendientesView.findViewById(R.id.lista_EgresosPendientes);
        myEgresosPendientesList.setLayoutManager(new LinearLayoutManager(getContext()));
        lstEgresosPendientes = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        EgresosRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("Egresos");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        return EgresosPendientesView;



    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Egresos> options =
                new FirebaseRecyclerOptions.Builder<Egresos>()
                        .setQuery(EgresosRef, Egresos.class)
                        .build();

        FirebaseRecyclerAdapter<Egresos,EgresosPendientesViewHolder> adapter
                = new FirebaseRecyclerAdapter<Egresos, EgresosPendientesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EgresosPendientesViewHolder holder, int position, @NonNull final Egresos model) {




                if(model.getTipo_egreso().equals("Egreso Usual"))
                {

                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    final String fechaA = dateFormat.format(date);

                    Date fechaInicial= null;
                    try {
                        fechaInicial = dateFormat.parse(model.getFecha_Egreso());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Date fechaFinal= null;
                    try {
                        fechaFinal = dateFormat.parse(fechaA);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int DAYi = Integer.parseInt(model.getDias_Egreso());

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
                    if(dias>0)
                    {
                        holder.nameIP.setText(model.getNomb_Egreso());
                        holder.fechaIP.setText("Fecha Limite: "+fechaRespuesta);
                        holder.diasIP.setText(dias+" Dias"+"\n"+" Restantes");
                        holder.diasIP.setTextColor(Color.GREEN);
                        holder.precioIP.setText("Monto: "+model.getmonto_Egreso());
                        holder.fecha.setText("Fecha Inicial: "+model.getFecha_Egreso());
                    }
                    else
                    {
                        holder.nameIP.setText(model.getNomb_Egreso());
                        holder.fechaIP.setText("Fecha Limite: "+fechaRespuesta);
                        holder.diasIP.setText(-dias+" Dias"+"\n"+" Retrasados");
                        holder.diasIP.setTextColor(Color.RED);
                        holder.precioIP.setText("Monto: "+model.getmonto_Egreso());
                        holder.fecha.setText("Fecha Inicial: "+model.getFecha_Egreso());
                    }

                    holder.PagarIP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder confirmacion= new AlertDialog.Builder(getActivity());
                            confirmacion.setMessage("¿Estas seguro de Pagar su Egreso?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String nombreIngresosPendientes = model.getNomb_Egreso();
                                    String descripcionIngresosPendientes = model.getDesc_Egreso();
                                    String categoria= model.getTipo_cat();
                                    String tipoIngresoPendiente= model.getTipo_egreso();
                                    String MontoIngresosPendiente = model.getmonto_Egreso();
                                    String diasIngresosPendiente = model.getDias_Egreso();
                                    String fechaIngresoPendiente = model.getFecha_Egreso();


                                    EgresosPendientes EP= new EgresosPendientes();
                                    EP.setId_EgresoPendiente(UUID.randomUUID().toString());
                                    EP.setUid(currentUserID);
                                    EP.setNomb_EgresoP(nombreIngresosPendientes);
                                    EP.setDesc_EgresoP(descripcionIngresosPendientes);
                                    EP.setTipo_catP(categoria);
                                    EP.setMonto_EgresoP(MontoIngresosPendiente);
                                    EP.setTipo_EgresoP(tipoIngresoPendiente);
                                    EP.setDias(diasIngresosPendiente);
                                    EP.setFechaInicial(fechaIngresoPendiente);
                                    EP.setFecha_Final(fechaRespuesta);
                                    EP.setFechaPago(fechaA);
                                    databaseReference.child("users").child(currentUserID).child("EgresosPendientes").child(EP.getId_EgresoPendiente()).setValue(EP);

                                    Egresos e= new Egresos();
                                    e.setId_egreso(model.getId_egreso());
                                    e.setUid(currentUserID);
                                    e.setNomb_Egreso(model.getNomb_Egreso());
                                    e.setDesc_Egreso(model.getDesc_Egreso());
                                    e.setTipo_cat(model.getTipo_cat());
                                    e.setmonto_Egreso(model.getmonto_Egreso());
                                    e.setTipo_egreso(model.getTipo_egreso());
                                    e.setDias_Egreso(model.getDias_Egreso());
                                    e.setFecha_Egreso(fechaRespuesta);
                                    databaseReference.child("users").child(currentUserID).child("Egresos").child(e.getId_egreso()).setValue(e);

                                    Toast toast1 = Toast.makeText(getApplicationContext(), "Egreso Pagado", Toast.LENGTH_SHORT);
                                    toast1.show();

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert =confirmacion.create();
                            alert.setTitle("Pagar Egreso");
                            alert.show();

                        }
                    });





                    ///ELiminar Ingreso
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
            public EgresosPendientesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View  view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_egresospendientes,viewGroup,false);
                EgresosPendientesViewHolder viewHolder = new EgresosPendientesViewHolder(view);
                return viewHolder;
            }
        };


        myEgresosPendientesList.setAdapter(adapter);
        adapter.startListening();




    }

    public static class EgresosPendientesViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameIP, fechaIP,diasIP,precioIP,fecha;

        ImageButton Modificar, Eliminar,PagarIP;

        public EgresosPendientesViewHolder(@NonNull View itemView) {
            super(itemView);
            Modificar = (ImageButton) itemView.findViewById(R.id.Modificar);
            Eliminar = (ImageButton) itemView.findViewById(R.id.Eliminar);
            PagarIP = (ImageButton) itemView.findViewById(R.id.Pagar);
            nameIP = itemView.findViewById(R.id.nombreEP);
            fechaIP = itemView.findViewById(R.id.fechaEP);
            precioIP = itemView.findViewById(R.id.precioEP);
            diasIP = itemView.findViewById(R.id.diasEP);
            fecha = itemView.findViewById(R.id.fechactual);
        }


    }




}
