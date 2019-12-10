package com.example.sistemagestioncuentas;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActualizarUsuario extends AppCompatActivity {


    private Button Actualizar;
    private  EditText idupdate;
    private EditText nameUpdate,emailUpdate, userStatus;
    private  EditText userinformation;
    private  EditText URL;



    private  String currentId;
    private  String currentName;
    private  String currentStatus;
    private  String currentInfo;


    private  FirebaseAuth mAuth;
    private  CircleImageView NavProfileImage;
    private  DatabaseReference UserRef;
    private  FirebaseAuth.AuthStateListener firebaseAuthListener;
    private StorageReference UserProfeileImageRef;


//
    private  static final int gallerypick =1;
    private  StorageReference UserProfileImagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_usuario);

        Actualizar = (Button)findViewById(R.id.btnActualizar);
        idupdate = (EditText) findViewById(R.id.txtidU);
        nameUpdate = (EditText) findViewById(R.id.txtnombreU);
        emailUpdate = (EditText) findViewById(R.id.txtEmailU);
        userStatus = (EditText) findViewById(R.id.txtestadoU);
        userinformation = (EditText) findViewById(R.id.txtinformacionU);
        NavProfileImage = (CircleImageView)findViewById(R.id.imageU);
        URL = (EditText) findViewById(R.id.url);

        mAuth = FirebaseAuth.getInstance();
        currentId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        UserProfeileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        UserRef.child(currentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String UID = mAuth.getCurrentUser().getUid();
                    String Email = mAuth.getCurrentUser().getEmail();
                    String name = dataSnapshot.child("name").getValue().toString();
                    if(dataSnapshot.child("status").exists())
                    {
                        String status = dataSnapshot.child("status").getValue().toString();
                        userStatus.setText(status);
                    }

                    if(dataSnapshot.child("information").exists())
                    {
                        String info = dataSnapshot.child("information").getValue().toString();
                        userinformation.setText(info);
                    }

                    String image = dataSnapshot.child("image").getValue().toString();
                    URL.setText(image);
                    Picasso.get().load(image).into(NavProfileImage);
                    idupdate.setText(UID);
                    emailUpdate.setText(Email);
                    nameUpdate.setText(name);
                    emailUpdate.setEnabled(false);
                    idupdate.setVisibility(View.INVISIBLE);
                    URL.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUser();
            }
        });


        NavProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryInten = new Intent();
                galleryInten.setAction(Intent.ACTION_GET_CONTENT);
                galleryInten.setType("image/*");
                startActivityForResult(galleryInten,gallerypick);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallerypick && resultCode == RESULT_OK && data != null){

            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                final StorageReference filepath = UserProfeileImageRef.child(currentId + ".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String imagen = uri.toString();
                                UserRef.child(currentId).child("image")
                                        .setValue(imagen);
                            }
                        });



                    }
                });


            }

        }

    }

    private void UpdateUser()
    {
        String setUserName = nameUpdate.getText().toString();
        String setStatus = userStatus.getText().toString();
        String setinformation = userinformation.getText().toString();
        String setimagen = URL.getText().toString();
        if(TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(this,"registre su nombre",Toast.LENGTH_LONG).show();
        }
        else
        {
            HashMap<String, String> ProfileMap = new HashMap<>();
            if(currentId.equals(idupdate.getText().toString()))
            {


                //registrar los datos a firebase
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                currentUserDB.child("name").setValue(setUserName);
                currentUserDB.child("status").setValue(setStatus);
                currentUserDB.child("information").setValue(setinformation);
                currentUserDB.child("image").setValue(setimagen);

                //una notificacion de que se registro corrctamente y se enia a la vista de MainActivity
                Intent intent = new Intent(ActualizarUsuario.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(ActualizarUsuario.this,"se Actualizo correctamente",Toast.LENGTH_LONG).show();


            }


        }

    }




}
