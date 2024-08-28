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


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Dashboard_MainActivity extends AppCompatActivity {

ShapeableImageView profile_img_dp;

LinearLayout layout_request_trip,layout_booked_trip;
ImageView profile_visit_icon;

TextView txt_hello,txt_username,txt_live_address_from,txt_live_address_to,txt_live_unloading_date;

FirebaseAuth auth;
FirebaseUser currentuser;
//reference for firebase to set data.
    DatabaseReference reference;

    Uri profile_dp_url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);

    //to initialize variable
        initialize();

    //setting data while loading
        setdata();


        //image dp click listener
        profile_img_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_MainActivity.this,driver_profile_MainActivity.class);
                Pair[] pairs=new Pair[3];
                pairs[0]=new Pair<View,String>(profile_img_dp,"profile_dp");
                pairs[1]=new Pair<View,String>(txt_hello,"hello");
                pairs[2]=new Pair<View,String>(txt_username,"username");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Dashboard_MainActivity.this,pairs);
                startActivity(intent,options.toBundle());

            }
        });

        //profile visit icon click listener
        profile_visit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_MainActivity.this,driver_profile_MainActivity.class);
                Pair[] pairs=new Pair[3];
                pairs[0]=new Pair<View,String>(profile_img_dp,"profile_dp");
                pairs[1]=new Pair<View,String>(txt_hello,"hello");
                pairs[2]=new Pair<View,String>(txt_username,"username");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Dashboard_MainActivity.this,pairs);
                startActivity(intent,options.toBundle());



            }
        });

        //transfer activity to request trip
        layout_request_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_MainActivity.this,request_trip_driver_MainActivity.class);
                startActivity(intent);
            }
        });

        //transfer activity to booked trip
        layout_booked_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_MainActivity.this,Booked_request_driver_MainActivity.class);
                startActivity(intent);
            }
        });





    }

    private void setdata() {
         reference=FirebaseDatabase.getInstance().getReference("Driver");
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
                                profile_diver_model_class profile_model=snapshot.child("Profile").getValue(profile_diver_model_class.class);
                                String name=snapshot.child("Profile").child("name").getValue().toString();
                                txt_username.setText(name);

                                if(profile_model.getProfile_dp_url()!=null)
                                {
                                    profile_dp_url = Uri.parse(profile_model.getProfile_dp_url());
                                }
                                if(profile_dp_url!=null)
                                {
                                    Picasso.get().load(profile_dp_url).placeholder(R.drawable.user_icon).into(profile_img_dp);
                                }

                                if(profile_dp_url==null)
                                {
                                    if(profile_model.getProfile_dp_url()!=null) {
                                        profile_dp_url = Uri.parse(profile_model.getProfile_dp_url());
                                        Picasso.get().load(profile_dp_url).placeholder(R.drawable.user_icon).into(profile_img_dp);
                                    }
                                }




                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    reference.child(currentuser.getUid()).child("Live Trip").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                txt_live_address_from.setText(snapshot.child("address_from").getValue().toString());
                                txt_live_address_to.setText(snapshot.child("address_to").getValue().toString());
                                txt_live_unloading_date.setText(snapshot.child("date_of_unloading").getValue().toString());
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
        profile_img_dp=findViewById(R.id.dashboard_profile_dp);
        profile_visit_icon=findViewById(R.id.dashboard_profile_visit);
        txt_hello=findViewById(R.id.dashboard_txt_hello);
        txt_username=findViewById(R.id.dashboard_txt_username);
        layout_request_trip=findViewById(R.id.dashboard_icon_request_trip);
        layout_booked_trip=findViewById(R.id.dashboard_icon_booked_Trip);
        txt_live_address_from=findViewById(R.id.dashboard_live_from_address);
        txt_live_address_to=findViewById(R.id.dashboard_live_to_address);
        txt_live_unloading_date=findViewById(R.id.dashboard_live_expected_time_to_reach);


        //auth
        auth=FirebaseAuth.getInstance();
        //currentuser
        currentuser=auth.getCurrentUser();

    }
}