package com.example.sistemagestioncuentas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sistemagestioncuentas.model.Ingresos;
import com.example.sistemagestioncuentas.model.IngresosPendientes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MostrarNotificacion extends AppCompatActivity {
    public static final String idcategoria = "idcategoria";

    private DatabaseReference EgresosRef;
    private DatabaseReference IngresosRef;
    DatabaseReference databaseReference;
    String currentUserID;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_notificacion);

        firebaseAuth =  FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        //notificacion de EGRESOS

        EgresosRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("Egresos");
        IngresosRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("Ingresos");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        EgresosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String fecha = areaSnapshot.child("fecha_Egreso").getValue(String.class);
                        String dia = areaSnapshot.child("dias_Egreso").getValue(String.class);

                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        final String fechaA = dateFormat.format(date);

                        Date fechaInicial= null;
                        try {
                            fechaInicial = dateFormat.parse(fecha);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Date fechaFinal= null;
                        try {
                            fechaFinal = dateFormat.parse(fechaA);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int DAYi = Integer.parseInt(dia);
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
                        if(dias <= 2 && dias>=-2 ) {
                            final String idcategoriaa = getIntent().getStringExtra("idcategoria");


                            AlertDialog.Builder alerta = new AlertDialog.Builder(MostrarNotificacion.this);
                            alerta.setMessage("Estas a punto de confirmar el pago de la Luz ¿Deseas Confirmar?"+idcategoriaa).setCancelable(false)
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            EgresosRef.child(idcategoriaa).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if(dataSnapshot.exists())
                                                    {
                                                        String nombreIngresosPendientes = dataSnapshot.child("fecha_Egreso").getValue(String.class);
                                                        String descripcionIngresosPendientes = dataSnapshot.child("dias_Egreso").getValue(String.class);
                                                        String categoria = dataSnapshot.child("fecha_Egreso").getValue(String.class);
                                                        String tipoIngresoPendiente = dataSnapshot.child("dias_Egreso").getValue(String.class);
                                                        String MontoIngresosPendiente = dataSnapshot.child("fecha_Egreso").getValue(String.class);
                                                        String diasIngresosPendiente = dataSnapshot.child("dias_Egreso").getValue(String.class);
                                                        String fechaIngresoPendiente = dataSnapshot.child("dias_Egreso").getValue(String.class);

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
                                                        i.setId_ingreso(idcategoriaa);
                                                        i.setUid(currentUserID);
                                                        i.setNomb_Ingreso(nombreIngresosPendientes);
                                                        i.setDesc_Ingreso(descripcionIngresosPendientes);
                                                        i.setTipo_cat(categoria);
                                                        i.setMonto_Ingreso(MontoIngresosPendiente);
                                                        i.setTipo_Ingreso(tipoIngresoPendiente);
                                                        i.setDias_Ingreso(diasIngresosPendiente);
                                                        i.setFecha_Ingreso(fechaRespuesta);
                                                        databaseReference.child("users").child(currentUserID).child("Ingresos").child(i.getId_ingreso()).setValue(i);

                                                        Toast toast1 = Toast.makeText(getApplicationContext(), "Ingreso Recibido", Toast.LENGTH_SHORT);
                                                        toast1.show();








                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            Intent intent = new Intent(MostrarNotificacion.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("CONFIRMACION DE PAGO (Egreso)");
                            titulo.show();


                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//notificacion de INGRESOS
        IngresosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {



                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {

                        String nombreIngreso = areaSnapshot.child("nomb_Ingreso").getValue(String.class);
                        String fecha = areaSnapshot.child("fecha_Ingreso").getValue(String.class);
                        String dia = areaSnapshot.child("dias_Ingreso").getValue(String.class);
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        final String fechaA = dateFormat.format(date);

                        Date fechaInicial= null;
                        try {
                            fechaInicial = dateFormat.parse(fecha);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Date fechaFinal= null;
                        try {
                            fechaFinal = dateFormat.parse(fechaA);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int DAYi = Integer.parseInt(dia);
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
                        if(dias <= 2 && dias>=-5 ) {
                            final String idcategoria = getIntent().getStringExtra("idcategoria");


                            AlertDialog.Builder alerta = new AlertDialog.Builder(MostrarNotificacion.this);
                            alerta.setMessage("Estas a punto de confirmar"+" "+ nombreIngreso +" "+" ¿Deseas Confirmar?").setCancelable(false)
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            Intent intent = new Intent(MostrarNotificacion.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("CONFIRMACION DE PAGO(Ingreso)");
                            titulo.show();


                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
