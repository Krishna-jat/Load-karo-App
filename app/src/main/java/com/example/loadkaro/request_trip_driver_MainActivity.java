package com.example.loadkaro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class request_trip_driver_MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    EditText txt_search_source_location,txt_search_destination_location;

    //firebase current user
    FirebaseUser currentuser;

    request_all_trip_driver_adapter adapter;

//firebase recyclerview
    FirebaseRecyclerOptions<all_trip_request_model_class> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_trip_driver_main);

        //method for initialization
        initialize();

        //set data basiclly recyclerview initial data
        setdata();

//for searching process
        txt_search_source_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                options =
                        new FirebaseRecyclerOptions.Builder<all_trip_request_model_class>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Admin").child("All trip").orderByChild("address_from").startAt(charSequence.toString()).endAt(charSequence.toString()+"\uf8ff"), all_trip_request_model_class.class)
                                .build();
                adapter=new request_all_trip_driver_adapter(options,request_trip_driver_MainActivity.this);
                recyclerView.setAdapter(adapter);

                adapter.startListening();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txt_search_destination_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                options =
                        new FirebaseRecyclerOptions.Builder<all_trip_request_model_class>()
                                .setQuery(FirebaseDatabase.getInstance().getReference("Admin").child("All trip").orderByChild("address_to").startAt(charSequence.toString()).endAt(charSequence.toString()+"\uf8ff"), all_trip_request_model_class.class)
                                .build();
                adapter=new request_all_trip_driver_adapter(options,request_trip_driver_MainActivity.this);
                recyclerView.setAdapter(adapter);
               adapter.startListening();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





    }



    private void setdata() {
        options =
                new FirebaseRecyclerOptions.Builder<all_trip_request_model_class>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Admin").child("All trip"), all_trip_request_model_class.class)
                        .build();
        adapter=new request_all_trip_driver_adapter(options,request_trip_driver_MainActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void initialize() {
        recyclerView=findViewById(R.id.request_trip_driver_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txt_search_source_location=findViewById(R.id.request_trip_driver_search_source_location);
        txt_search_destination_location=findViewById(R.id.request_trip_driver_search_destination_location);

        //current user
        currentuser= FirebaseAuth.getInstance().getCurrentUser();

    }
}