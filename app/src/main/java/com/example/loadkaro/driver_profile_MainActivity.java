package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class driver_profile_MainActivity extends AppCompatActivity {
ImageView img_logout;
ShapeableImageView profile_dp;
TextView txt_username,txt_user_gmail,txt_user_contact,txt_user_experience;
LinearLayout img_edit_profile,img_reset_password;

RecyclerView vehicle_recyclerview;

FirebaseAuth auth;
FirebaseUser currentuser;
GoogleSignInClient googleSignInClient;

//firebase recycler view adapter class vehicle
    vehicle_Firebase_Recyclerview_adapter_class adapter_class;

Uri profile_dp_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile_main);

        //initialize variable
        initialize();

        //seting data while loading
        setdata();



        //to logout
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              auth.signOut();
              googleSignInClient.signOut();

                Intent intent=new Intent(driver_profile_MainActivity.this,login_MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        //to go to edit profile
        img_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(driver_profile_MainActivity.this,edit_profile_MainActivity.class);
                startActivity(intent);

            }
        });

        //to reset password
        img_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(driver_profile_MainActivity.this);
                dialog.setContentView(R.layout.dialog_box_reset_password);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.linear_layout_dialog_style));
                Button yes = dialog.findViewById(R.id.dialog_btn_yes);
                Button no = dialog.findViewById(R.id.dialog_btn_no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        auth.sendPasswordResetEmail(currentuser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(driver_profile_MainActivity.this, "Reset password - Email send", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(driver_profile_MainActivity.this, "Reset password - Email not send", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }


            });
    }


            private void setdata() {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    //snapshot currentuser in driver
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild("Profile")) {
                            profile_diver_model_class profile_model=snapshot.child("Profile").getValue(profile_diver_model_class.class);
                            String username = snapshot.child("Profile").child("name").getValue().toString();
                            String usergmail = snapshot.child("Profile").child("email").getValue().toString();
                            String usercontact = snapshot.child("Profile").child("contact").getValue().toString();
                            String userexperience = snapshot.child("Profile").child("experience").getValue().toString();

//                            String profile_url=snapshot.child("Profile").child("profile_dp_url").getValue().toString();

                            if (!(userexperience.equals(""))) {
                                txt_user_experience.setText(userexperience);
                            } else {
                                txt_user_experience.setText("no data");
                            }
                            txt_username.setText(username);
                            txt_user_gmail.setText(usergmail);
                            txt_user_contact.setText(usercontact);

                            if(profile_model.getProfile_dp_url()!=null)
                            {
                                profile_dp_url = Uri.parse(profile_model.getProfile_dp_url());
                            }
                            if(profile_dp_url!=null)
                            {
                                Picasso.get().load(profile_dp_url).into(profile_dp);
                            }

                            if(profile_dp_url==null)
                            {
                                if(profile_model.getProfile_dp_url()!=null) {
                                    profile_dp_url = Uri.parse(profile_model.getProfile_dp_url());
                                    Picasso.get().load(profile_dp_url).into(profile_dp);
                                }
                            }



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //vehicle show
                if(FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid()).child("Vehicles")!=null) {
                    vehicle_recyclerview_setdata();
                }



            }


            //recycler view vehicle show process
    private void vehicle_recyclerview_setdata() {
        FirebaseRecyclerOptions<driver_vehicle_model_class> options =
                new FirebaseRecyclerOptions.Builder<driver_vehicle_model_class>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid()).child("Vehicles"), driver_vehicle_model_class.class)
                        .build();

         adapter_class=new vehicle_Firebase_Recyclerview_adapter_class(options);
        vehicle_recyclerview.setAdapter(adapter_class);
        adapter_class.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter_class.startListening();
        adapter_class.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter_class.stopListening();
    }

    private void initialize() {
                img_logout = findViewById(R.id.profile_logout);
                img_edit_profile = findViewById(R.id.profile_img_edit_profile);
                txt_user_gmail = findViewById(R.id.profile_txt_gmail);
                txt_username = findViewById(R.id.profile_txt_username);
                txt_user_contact = findViewById(R.id.profile_txt_user_contact);
                txt_user_experience = findViewById(R.id.profile_txt_user_experience);
                img_reset_password = findViewById(R.id.profile_img_reset_password);
                profile_dp=findViewById(R.id.profile_profile_dp);

                //recycler view
                vehicle_recyclerview=findViewById(R.id.profile_recyclerview_vehicle);
                vehicle_recyclerview.setLayoutManager(new LinearLayoutManager(driver_profile_MainActivity.this));

                //firebase auth
                auth = FirebaseAuth.getInstance();

                //current user
                currentuser = auth.getCurrentUser();

                //google sign
                GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                googleSignInClient = GoogleSignIn.getClient(this, options);
            }
        }
