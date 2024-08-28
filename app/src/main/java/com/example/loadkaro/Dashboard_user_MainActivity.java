package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Dashboard_user_MainActivity extends AppCompatActivity {

    ShapeableImageView user_profile_img;
    ImageView img_profile_visit_icon;

    TextView txt_hello,txt_username;

    //firebase database reference
    DatabaseReference reference;
    //current user auth
    FirebaseUser currentuser;

    LinearLayout layout_generate_request,layout_my_request,layout_request_accepted;

    Uri profile_dp_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user_main);

        //to initialize variables
       initialize();

       //set data on loading
        setdata();

        //img dp click listener
        user_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_user_MainActivity.this,user_profile_MainActivity.class);
                Pair[] pairs=new Pair[3];
                pairs[0]=new Pair<View,String>(user_profile_img,"user_profile_dp");
                pairs[1]=new Pair<View,String>(txt_hello,"user_hello");
                pairs[2]=new Pair<View,String>(txt_username,"user_username");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Dashboard_user_MainActivity.this,pairs);
                startActivity(intent,options.toBundle());

            }
        });

        //profile visit icon clicklistener
       img_profile_visit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_user_MainActivity.this,user_profile_MainActivity.class);
                Pair[] pairs=new Pair[3];
                pairs[0]=new Pair<View,String>(user_profile_img,"user_profile_dp");
                pairs[1]=new Pair<View,String>(txt_hello,"user_hello");
                pairs[2]=new Pair<View,String>(txt_username,"user_username");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Dashboard_user_MainActivity.this,pairs);
                startActivity(intent,options.toBundle());



            }
        });

       //request generat activity transfer
        layout_generate_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_user_MainActivity.this,user_request_generate_MainActivity.class);
                startActivity(intent);
            }
        });

        // myrequest activity transfer
        layout_my_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_user_MainActivity.this,my_request_user_MainActivity.class);
                startActivity(intent);
            }
        });

        //request accepted activity transfer
        layout_request_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_user_MainActivity.this,accepted_request_user_MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setdata() {

        reference= FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            //snapshot driver
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(currentuser.getUid()))
                {
                    reference.child(currentuser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        //snapshot current driver
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("Profile"))
                            {
                                profile_user_model_class profile_model=snapshot.child("Profile").getValue(profile_user_model_class.class);
                                String name=snapshot.child("Profile").child("name").getValue().toString();
                                txt_username.setText(name);

                                if(profile_model.getProfile_dp_url()!=null)
                                {
                                    profile_dp_url = Uri.parse(profile_model.getProfile_dp_url());
                                }
                                if(profile_dp_url!=null)
                                {
                                    Picasso.get().load(profile_dp_url).placeholder(R.drawable.user_icon).into(user_profile_img);
                                }

                                if(profile_dp_url==null)
                                {
                                    if(profile_model.getProfile_dp_url()!=null) {
                                        profile_dp_url = Uri.parse(profile_model.getProfile_dp_url());
                                        Picasso.get().load(profile_dp_url).placeholder(R.drawable.user_icon).into(user_profile_img);
                                    }
                                }




                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void initialize() {
        user_profile_img=findViewById(R.id.dashboard_user_profile_dp);
        txt_hello=findViewById(R.id.dashboard_user_txt_hello);
        txt_username=findViewById(R.id.dashboard_user_txt_username);
        img_profile_visit_icon=findViewById(R.id.dashboard_user_profile_visit);
        layout_generate_request=findViewById(R.id.dashboard_user_icon_generate_request);
        layout_my_request=findViewById(R.id.dashboard_user_icon_my_request);
        layout_request_accepted=findViewById(R.id.dashboard_user_icon_accepted_request);

        //current user
        currentuser=FirebaseAuth.getInstance().getCurrentUser();
    }
}