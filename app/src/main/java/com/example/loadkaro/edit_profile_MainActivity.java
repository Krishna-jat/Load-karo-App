package com.example.loadkaro;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.material.imageview.ShapeableImageView;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.List;

public class edit_profile_MainActivity extends AppCompatActivity {
EditText username,user_contact,user_experience,vehicle_type,vehicle_capacity,vehicle_number;
ImageView change_dp;
ShapeableImageView profile_dp;
AppCompatButton btn_submit_user_detail,btn_add_new_vehicle;
FirebaseAuth auth;
FirebaseUser currentuser;

Uri profile_main_url_dp;

int REQUEST_CODE_IMAGE_GALARY=101;

//this model contain current and before update data from firebase.
    profile_diver_model_class firstmodel;


    DatabaseReference reference1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_main);

        //initialize variables
        initialize();

        //setting data while loading
        setdata();

        //update details
        btn_submit_user_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=username.getText().toString();
                String contact=user_contact.getText().toString();
                String experience=user_experience.getText().toString();
                if((!(name.equals("")))&&(!(contact.equals("")))&&(!(experience.equals(""))))
                {
                    HashMap<String,Object> update_details=new HashMap();
                    update_details.put("name",name);
                    update_details.put("contact",contact);
                    update_details.put("experience",experience);
                    reference1=FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid()).child("Profile");
                    reference1.updateChildren(update_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(edit_profile_MainActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(edit_profile_MainActivity.this, "unsuccessful update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(edit_profile_MainActivity.this, "fill all data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //upload and change profile dp
        change_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(edit_profile_MainActivity.this)
                        .withPermissions(Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO
                        ,Manifest.permission.READ_MEDIA_AUDIO)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if(multiplePermissionsReport.areAllPermissionsGranted())
                                {
                                    Intent intent=new Intent(Intent.ACTION_PICK);
                                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                    startActivityForResult(intent,REQUEST_CODE_IMAGE_GALARY);
                                }
                                else {
                                    Toast.makeText(edit_profile_MainActivity.this, "Media permission are denied change from app setting", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                            }
                        }).check();



            }
        });

        //add new vehicle
        btn_add_new_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type=vehicle_type.getText().toString();
                String capacity=vehicle_capacity.getText().toString();
                String number=vehicle_number.getText().toString();

                if((!(type.equals("")))&&(!(capacity.equals("")))&&(!(number.equals(""))))
                {

                    driver_vehicle_model_class vehicle_model=new driver_vehicle_model_class(type,number,capacity);

                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid()).child("Vehicles").child(number);
                    reference.setValue(vehicle_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            vehicle_type.setText("");
                            vehicle_number.setText("");
                            vehicle_capacity.setText("");

                            if(task.isSuccessful())
                            {
                                Toast.makeText(edit_profile_MainActivity.this, "vehicle successfully added", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(edit_profile_MainActivity.this, "unable to add vehicle", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(edit_profile_MainActivity.this, "enter all data of vehicle", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void setdata() {

        reference1= FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid()).child("Profile");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 firstmodel=snapshot.getValue(profile_diver_model_class.class);
                if(firstmodel!=null)
                {
                    if(firstmodel.getName()!=null)
                    {
                        username.setText(firstmodel.getName());
                    }
                    if(firstmodel.getContact()!=null)
                    {
                        user_contact.setText(firstmodel.getContact());
                    }
                    if(!(firstmodel.getExperience().equals("")))
                    {
                        user_experience.setText(firstmodel.getExperience());
                    }
                    if(profile_main_url_dp!=null)
                    {
                        Picasso.get().load(profile_main_url_dp).into(profile_dp);

                    }
                    if(profile_main_url_dp==null)
                    {
                        if(firstmodel.getProfile_dp_url()!=null) {
                            String profile_url = firstmodel.getProfile_dp_url();
                            profile_main_url_dp=Uri.parse(profile_url);
                            Picasso.get().load(profile_main_url_dp).into(profile_dp);

                        }

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {

            if(requestCode==REQUEST_CODE_IMAGE_GALARY)
            {
                ProgressDialog progressDialog=new ProgressDialog(edit_profile_MainActivity.this);;
                progressDialog.setMessage("uploading wait....");
                progressDialog.setTitle("Image uploade!");
                progressDialog.show();


                Uri image_url=data.getData();
                StorageReference referencestorage=FirebaseStorage.getInstance().getReference("Driver");
                if(image_url!=null)
                {
                    referencestorage.child(currentuser.getUid()).child("profile_image").putFile(image_url).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                referencestorage.child(currentuser.getUid()).child("profile_image").getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                if (uri != null) {
                                                    String file_path = uri.toString();

                                                    HashMap<String, Object> map = new HashMap<>();
                                                    map.put("profile_dp_url", file_path);
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid())
                                                            .child("Profile");
                                                    databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                profile_dp.setImageURI(null);

                                                                Picasso.get().load(uri).into(profile_dp);
                                                                progressDialog.dismiss();
                                                                Toast.makeText(edit_profile_MainActivity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(edit_profile_MainActivity.this, "unable to upload file in profile", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(edit_profile_MainActivity.this, "unable to upload file in profile", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else
                            {progressDialog.dismiss();
                                Toast.makeText(edit_profile_MainActivity.this, "unable to upload file", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        }

    }

    private void initialize() {
        username=findViewById(R.id.edit_profile_edit_username);
        user_contact=findViewById(R.id.edit_profile_edit_contact);
        user_experience=findViewById(R.id.edit_profile_edit_experience);
        btn_submit_user_detail=findViewById(R.id.edit_profile_btn_save_changes);
        profile_dp=findViewById(R.id.edit_profile_profile_dp);
        change_dp=findViewById(R.id.edit_profile_img_change_dp);
        btn_add_new_vehicle=findViewById(R.id.edit_profile_btn_add_new_vehicle);
        vehicle_type=findViewById(R.id.edit_profile_edit_row_vehicle_type);
        vehicle_capacity=findViewById(R.id.edit_profile_edit_row_vehicle_capacity);
        vehicle_number=findViewById(R.id.edit_profile_edit_row_vehicle_number);

        //auth
        auth=FirebaseAuth.getInstance();
        //currentuser
        currentuser=auth.getCurrentUser();
    }
}


