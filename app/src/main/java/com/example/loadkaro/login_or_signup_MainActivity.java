package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_or_signup_MainActivity extends AppCompatActivity {
AppCompatButton btn_login,btn_signup;
TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_signup_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_login=findViewById(R.id.btn_login);
        btn_signup=findViewById(R.id.btn_signup);

        textView=findViewById(R.id.text_bigtext);



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_or_signup_MainActivity.this,login_MainActivity.class);

                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(btn_login,"login");
                pairs[1]=new Pair<View,String>(textView,"logo");

              ActivityOptions options=  ActivityOptions.makeSceneTransitionAnimation(login_or_signup_MainActivity.this,pairs);

                startActivity(intent,options.toBundle());
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_or_signup_MainActivity.this,signup_MainActivity.class);
                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(btn_signup,"signup");
                pairs[1]=new Pair<View,String>(textView,"logo");

                ActivityOptions options=  ActivityOptions.makeSceneTransitionAnimation(login_or_signup_MainActivity.this,pairs);

                startActivity(intent,options.toBundle());
            }
        });


    }


}