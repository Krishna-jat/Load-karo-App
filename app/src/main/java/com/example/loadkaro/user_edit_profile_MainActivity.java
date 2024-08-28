package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;
import java.util.List;

public class user_edit_profile_MainActivity extends AppCompatActivity {
    ShapeableImageView img_profile_dp;

    ImageView img_change_profile_dp;

    TextView txt_username,txt_usercontact,txt_usercontact2,txt_user_address;
    AppCompatButton btn_save_changed;

    //firebase realtime reference
    DatabaseReference reference1;

    //current user
    FirebaseUser currentuser;

    //first model call of user detail contain detail before update
    profile_user_model_class firstmodel;
    //profile uri
    Uri profile_main_url_dp;

    int REQUEST_CODE_IMAGE_GALARY=102;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile_main);
        
        
        //initialize variables
        initialize();
        
        //set data o loading
        setdata();

        //update values in firebase
        btn_save_changed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=txt_username.getText().toString();
                String contact=txt_usercontact.getText().toString();
                String contact2=txt_usercontact2.getText().toString();
                String address=txt_user_address.getText().toString();
                if((!(name.equals("")))&&(!(contact.equals("")))&&(!(address.equals(""))))
                {
                    HashMap<String,Object> update_details=new HashMap();
                    update_details.put("name",name);
                    update_details.put("contact",contact);
                    update_details.put("contact2",contact2);
                    update_details.put("address",address);
                    reference1=FirebaseDatabase.getInstance().getReference("User").child(currentuser.getUid()).child("Profile");
                    reference1.updateChildren(update_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(user_edit_profile_MainActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(user_edit_profile_MainActivity.this, "unsuccessful update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(user_edit_profile_MainActivity.this, "fill all data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //upload and change profile dp
        img_change_profile_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(user_edit_profile_MainActivity.this)
                        .withPermissions(android.Manifest.permission.READ_MEDIA_IMAGES,
                                android.Manifest.permission.READ_MEDIA_VIDEO
                                , Manifest.permission.READ_MEDIA_AUDIO)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if(!(multiplePermissionsReport.isAnyPermissionPermanentlyDenied()))
                                {
                                    Intent intent=new Intent(Intent.ACTION_PICK);
                                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                    startActivityForResult(intent,REQUEST_CODE_IMAGE_GALARY);
                                }
                                else {
                                    Toast.makeText(user_edit_profile_MainActivity.this, "Media permission are denied change from app setting", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();



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
                ProgressDialog progressDialog=new ProgressDialog(user_edit_profile_MainActivity.this);;
                progressDialog.setMessage("uploading wait....");
                progressDialog.setTitle("Image uploade!");
                progressDialog.show();


                Uri image_url=data.getData();
                StorageReference referencestorage= FirebaseStorage.getInstance().getReference("User");
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
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentuser.getUid())
                                                            .child("Profile");
                                                    databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                img_profile_dp.setImageURI(null);

                                                                Picasso.get().load(uri).into(img_profile_dp);
                                                                progressDialog.dismiss();
                                                                Toast.makeText(user_edit_profile_MainActivity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(user_edit_profile_MainActivity.this, "unable to upload file in profile", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(user_edit_profile_MainActivity.this, "unable to upload file in profile", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else
                            {progressDialog.dismiss();
                                Toast.makeText(user_edit_profile_MainActivity.this, "unable to upload file", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        }
    }

    private void setdata() {

        reference1= FirebaseDatabase.getInstance().getReference("User").child(currentuser.getUid()).child("Profile");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstmodel=snapshot.getValue(profile_user_model_class.class);
                if(firstmodel!=null)
                {
                    if(firstmodel.getName()!=null)
                    {
                        txt_username.setText(firstmodel.getName());
                    }
                    if(firstmodel.getContact()!=null)
                    {
                        txt_usercontact.setText(firstmodel.getContact());
                    }
                    if(firstmodel.getContact2()!=null)
                    {
                        txt_usercontact2.setText(firstmodel.getContact2());
                    }
                    if(!(firstmodel.getAddress().equals("")))
                    {
                        txt_user_address.setText(firstmodel.getAddress());
                    }
                    if(profile_main_url_dp!=null)
                    {
                        Picasso.get().load(profile_main_url_dp).into(img_profile_dp);

                    }
                    if(profile_main_url_dp==null)
                    {
                        if(!(firstmodel.getProfile_dp_url().equals(""))) {
                            String profile_url = firstmodel.getProfile_dp_url();
                            profile_main_url_dp= Uri.parse(profile_url);
                            Picasso.get().load(profile_main_url_dp).into(img_profile_dp);

                        }

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initialize() {
        txt_username=findViewById(R.id.edit_profile_user_edit_username);
        txt_usercontact=findViewById(R.id.edit_profile_user_edit_contact);
        txt_usercontact2=findViewById(R.id.edit_profile_user_edit_contact2);
        txt_user_address=findViewById(R.id.edit_profile_user_edit_address);
        btn_save_changed=findViewById(R.id.edit_profile_user_btn_save_changes);
        img_profile_dp=findViewById(R.id.edit_profile_user_profile_dp);
        img_change_profile_dp=findViewById(R.id.edit_profile_user_img_change_dp);

        //curent user firebase auth
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
    }
}