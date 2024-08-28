package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class user_profile_MainActivity extends AppCompatActivity {

    ShapeableImageView img_profile_dp;
    TextView txt_username,txt_usergmail,txt_user_contact,txt_user_contact2,txt_user_address;
    ImageView img_logout;

    LinearLayout img_edit_profile,img_password_reset;
    
    //current user
    FirebaseUser currentuser;

    //firebase auth
    FirebaseAuth auth;

    //uri of profile dp
    Uri profile_dp_url;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_main);
        
        
        //to initialize variable
        initialize();
        
        //set data on loading
        setdata();

        //edit profile  on click listener to go to edit window
        img_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(user_profile_MainActivity.this,user_edit_profile_MainActivity.class);
                startActivity(intent);

            }
        });

        //for logout
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();

                Intent intent=new Intent(user_profile_MainActivity.this,login_MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        //reset password
        img_password_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(user_profile_MainActivity.this);
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
                                    Toast.makeText(user_profile_MainActivity.this, "Reset password - Email send", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(user_profile_MainActivity.this, "Reset password - Email not send", Toast.LENGTH_SHORT).show();

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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(currentuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            //snapshot currentuser in driver
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("Profile")) {
                    profile_user_model_class profile_model=snapshot.child("Profile").getValue(profile_user_model_class.class);
                    String username = snapshot.child("Profile").child("name").getValue().toString();
                    String usergmail = snapshot.child("Profile").child("email").getValue().toString();
                    String usercontact = snapshot.child("Profile").child("contact").getValue().toString();
                    String usercontact2 = snapshot.child("Profile").child("contact2").getValue().toString();
                    String useraddress = snapshot.child("Profile").child("address").getValue().toString();

//                            String profile_url=snapshot.child("Profile").child("profile_dp_url").getValue().toString();

                    if (!(useraddress.equals(""))) {
                        txt_user_address.setText(useraddress);
                    } else {
                        txt_user_address.setText("no data");
                    }
                    if(!(usercontact2.equals("")))
                    {
                        txt_user_contact2.setText(usercontact2);
                    }
                    else
                    {
                        txt_user_contact2.setText("no data");
                    }
                    txt_username.setText(username);
                    txt_usergmail.setText(usergmail);
                    txt_user_contact.setText(usercontact);

                    if(!(profile_model.getProfile_dp_url().equals("")))
                    {
                        profile_dp_url = Uri.parse(profile_model.getProfile_dp_url());
                    }
                    if(profile_dp_url!=null)
                    {
                        Picasso.get().load(profile_dp_url).into(img_profile_dp);
                    }

                    if(profile_dp_url==null)
                    {
                        if(!(profile_model.getProfile_dp_url().equals(""))) {
                            profile_dp_url = Uri.parse(profile_model.getProfile_dp_url());
                            Picasso.get().load(profile_dp_url).into(img_profile_dp);
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
        img_profile_dp=findViewById(R.id.profile_user_profile_dp);
        txt_username=findViewById(R.id.profile_user_txt_username);
        txt_usergmail=findViewById(R.id.profile_user_txt_gmail);
        txt_user_contact=findViewById(R.id.profile_user_txt_user_contact);
        txt_user_contact2=findViewById(R.id.profile_user_txt_user_contact2);
        txt_user_address=findViewById(R.id.profile_user_txt_user_address);
        img_edit_profile=findViewById(R.id.profile_user_img_edit_profile);
        img_password_reset=findViewById(R.id.profile_user_img_reset_password);
        img_logout=findViewById(R.id.profile_user_logout);


        //Firebase auth
        auth=FirebaseAuth.getInstance();

        
        //current user
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
    }
}