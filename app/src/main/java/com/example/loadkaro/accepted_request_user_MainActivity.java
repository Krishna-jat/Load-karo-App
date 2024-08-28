package com.example.loadkaro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class accepted_request_user_MainActivity extends AppCompatActivity {
RecyclerView recyclerView;
FirebaseRecyclerOptions options;

FirebaseUser currentuser;
accepted_request_user_recyclerview_adapter_class adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_request_user_main);

        //to initialize variables
        initialize();

        //for seting initial data
        setdata();
    }

    private void setdata() {



        options =
                new FirebaseRecyclerOptions.Builder<accepted_request_user_model_class>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("User").child(currentuser.getUid()).child("Accepted Request"), accepted_request_user_model_class.class)
                        .build();
        adapter=new accepted_request_user_recyclerview_adapter_class(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void initialize() {
        recyclerView=findViewById(R.id.accepted_request_user_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentuser= FirebaseAuth.getInstance().getCurrentUser();
    }
}