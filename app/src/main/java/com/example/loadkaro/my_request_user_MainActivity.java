package com.example.loadkaro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class my_request_user_MainActivity extends AppCompatActivity {
RecyclerView recyclerView;
FirebaseUser currentuser;

//adapter for recycler view
    my_request_user_recyclerview_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request_user_main);

        //for variable initialization
        initialize();

        FirebaseRecyclerOptions<my_request_user_model_class> options =
                new FirebaseRecyclerOptions.Builder<my_request_user_model_class>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("User").child(currentuser.getUid()).child("My request"), my_request_user_model_class.class)
                        .build();
        adapter=new my_request_user_recyclerview_adapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void initialize() {
        recyclerView=findViewById(R.id.my_request_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //for currentuser
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
    }
}