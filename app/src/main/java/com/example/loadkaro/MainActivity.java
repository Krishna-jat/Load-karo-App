package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
ShapeableImageView img_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        img_logo=findViewById(R.id.splash_logo);
        Animation anim_logo= AnimationUtils.loadAnimation(this,R.anim.splash_logo_anim);
        img_logo.startAnimation(anim_logo);


        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                checking user driver or customer
                checkinguser();

            }
        },3000);

    }
    private void checkinguser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null) {
            DatabaseReference referenceuser = FirebaseDatabase.getInstance().getReference("Driver");
            referenceuser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild(user.getUid())) {

                        Intent intent = new Intent(MainActivity.this, Dashboard_MainActivity.class);
                        startActivity(intent);
                        referenceuser.removeEventListener(this);
                        finish();
                    }
                    else
                    {

                                    Intent intent = new Intent(MainActivity.this, Dashboard_user_MainActivity.class);
                                    startActivity(intent);
                                    referenceuser.removeEventListener(this);
                                    finish();
                                }


                    }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else
        {
            Intent intent=new Intent(MainActivity.this,login_or_signup_MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}