package com.example.loadkaro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Booked_request_driver_MainActivity extends AppCompatActivity {

RecyclerView recyclerView;
FirebaseRecyclerOptions options;

FirebaseUser currentuser;

//adapter for recyclerview
booked_request_driver_recyclerview_adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_request_driver_main);

        //to initialize variable
        initialize();

        //for seting initial data
        setdata();

    }

    private void setdata() {
        options =
                new FirebaseRecyclerOptions.Builder<all_trip_request_model_class>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid()).child("Booked request"), all_trip_request_model_class.class)
                        .build();
                adapter=new booked_request_driver_recyclerview_adapter(options);
                recyclerView.setAdapter(adapter);
                adapter.startListening();



    }
    private void initialize() {
        recyclerView=findViewById(R.id.booked_trip_driver_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //for currentuser
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
    }
}