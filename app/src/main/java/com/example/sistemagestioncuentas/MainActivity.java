package com.example.sistemagestioncuentas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sistemagestioncuentas.Fragmento.ActualizarDatosFragment;
import com.example.sistemagestioncuentas.Fragmento.ListarCategoriaFragment;
import com.example.sistemagestioncuentas.Fragmento.ListarEgresosFragment;
import com.example.sistemagestioncuentas.Fragmento.ListarEgresosPendientesFragmnent;
import com.example.sistemagestioncuentas.Fragmento.ListarIngresosFragment;
import com.example.sistemagestioncuentas.Fragmento.ListarIngresosPendientesFragment;
import com.example.sistemagestioncuentas.Fragmento.PrincipalFragment;
import com.example.sistemagestioncuentas.Fragmento.ReportesFragment;
import com.example.sistemagestioncuentas.model.Egresos;
import com.facebook.AccessToken;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {



    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;

    private CircleImageView NavProfileImage;
    private TextView NavProfileUserEmail;
    private TextView NavProfileUserName;
    private TextView NavProfileUserId;

    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference UserRef;

    //
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    String currentUserID;
    public static  String idcategoria= "idcategoria";

    //notificacion

    private DatabaseReference EgresosRef;
    private DatabaseReference IngresosRef;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth =  FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =(NavigationView)findViewById(R.id.nav_view);
        View navView = navigationView.getHeaderView(0);

        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menulat, getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        VerificarUusario();




        ///LOGIN GOOGLE
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.photoImageView);
        nameTextView = (TextView) navView.findViewById(R.id.name);
        emailTextView = (TextView) navView.findViewById(R.id.emailTextView);
        idTextView = (TextView) navView.findViewById(R.id.idTextView);
        navigationView.setNavigationItemSelectedListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);
                } else {
                    goLogInScreen();
                }
            }
        };




//dias
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

                                NotificationCompat.Builder mBuilder;
                                NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

                            int icono = R.mipmap.ic_launcher;
                            Intent i=new Intent(MainActivity.this, MostrarNotificacion.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, 0);
                            // Intent actionIntent = new Intent(MainActivity.this, MostrarNotificacion.class);
                            //PendingIntent actionPendingIntent = PendingIntent.getActivity(MainActivity.this, 222, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            mBuilder =new NotificationCompat.Builder(getApplicationContext())
                                    .setContentIntent(pendingIntent)
                                    .setSmallIcon(icono)
                                    .setContentTitle("Tienes un pago pendiente apunto de vencer")
                                    .setContentText("te queda"+ " " + dias + " " +"dias para realizar el pago")
                                    .setVibrate(new long[] {100, 250, 100, 500})
                                    //   .addAction(R.drawable.ic_launcher_background, "¡ACEPTAR!", actionPendingIntent)
                                    // .addAction(R.drawable.ic_launcher_background, "¡ACEPTAR!", actionPendingIntent)
                                    .setAutoCancel(true);
                            mNotifyMgr.notify(1, mBuilder.build());
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

                            String idingreso = areaSnapshot.child("id_ingreso").getValue(String.class);
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
                            NotificationCompat.Builder mnBuilder;
                            NotificationManager mNotifyMgrr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

                            int icono = R.mipmap.ic_launcher;

                            idcategoria = idingreso;
                            Intent intent = new Intent(MainActivity.this, MostrarNotificacion.class);
                            intent.putExtra(MostrarNotificacion.idcategoria,idcategoria);

                            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

                            // Intent actionIntent = new Intent(MainActivity.this, MostrarNotificacion.class);
                            //PendingIntent actionPendingIntent = PendingIntent.getActivity(MainActivity.this, 222, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            mnBuilder =new NotificationCompat.Builder(getApplicationContext())
                                    .setContentIntent(pendingIntent)
                                    .setSmallIcon(icono)
                                    .setContentTitle("Tienes un pago pendiente apunto de vencer")
                                    .setContentText("te queda"+ " " + dias + " " +"dias para confirmar tu ingreso")
                                    .setVibrate(new long[] {100, 250, 100, 500})
                                    //   .addAction(R.drawable.ic_launcher_background, "¡ACEPTAR!", actionPendingIntent)
                                    // .addAction(R.drawable.ic_launcher_background, "¡ACEPTAR!", actionPendingIntent)
                                    .setAutoCancel(true);
                            mNotifyMgrr.notify(1, mnBuilder.build());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new PrincipalFragment()).commit();










//boton inicio
        NavProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction().replace(R.id.container,new PrincipalFragment()).commit();
            }
        });

        ///////////////login
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logoicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }



    public  void VerificarUusario()
    {
        navigationView =(NavigationView)findViewById(R.id.nav_view);
        View navView = navigationView.getHeaderView(0);

        NavProfileImage = (CircleImageView)navView.findViewById(R.id.photoImageView);
        NavProfileUserId = (TextView) navView.findViewById(R.id.idTextView);
        NavProfileUserName = (TextView) navView.findViewById(R.id.name);
        NavProfileUserEmail = (TextView) navView.findViewById(R.id.emailTextView);

        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String Email = firebaseAuth.getCurrentUser().getEmail();
                    String UID = firebaseAuth.getCurrentUser().getUid();
                    NavProfileUserName.setText(name);
                    NavProfileUserId.setText(UID);
                    NavProfileUserEmail.setText(Email);
                    Picasso.get().load(image).into(NavProfileImage);
                  //  Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.com_facebook_auth_dialog_background).into(NavProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //DATOS DEL USUARIO
    private void setUserData(FirebaseUser user){
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        idTextView.setText(user.getUid());
        Glide.with(this).load(user.getPhotoUrl()).into(NavProfileImage);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth !=null)
        {
            firebaseAuth.addAuthStateListener(firebaseAuthListener);
            VerificarUusario();
        }


    }


    private void goLogInScreen() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void logOut(View view) {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_usuario) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ActualizarDatosFragment()).commit();
        } else if (id == R.id.nav_categoria) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListarCategoriaFragment()).commit();

        } else if (id == R.id.nav_Ingresos) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListarIngresosFragment()).commit();

        } else if (id == R.id.nav_Egresos) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListarEgresosFragment()).commit();

        } else if (id == R.id.nav_Reportes) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ReportesFragment()).commit();

        } else if (id == R.id.nav_IngresosPendientes) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListarIngresosPendientesFragment()).commit();

        }
        else if (id == R.id.nav_EgresosPendientes) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListarEgresosPendientesFragmnent()).commit();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opciones_menu,menu);


        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.id_update){
            SendUserUpdateactivity();

        }
        if(item.getItemId() == R.id.salir){
            firebaseAuth.signOut();
            SendUserLoginActivity();

        }
        return true;
    }


    private void SendUserUpdateactivity()
    {
        Intent loginintent = new Intent(MainActivity.this,ActualizarUsuario.class);
        startActivity(loginintent);
    }

    private void SendUserLoginActivity()
    {
        Intent loginintent = new Intent(MainActivity.this,Login.class);
        startActivity(loginintent);
    }
}

