package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class user_request_generate_MainActivity extends AppCompatActivity {
TextInputLayout address_from,adddress_to,distance,material_name,material_quantity,layout_loading_dd,layout_loading_mm,layout_loading_yyyy,layout_unloading_dd,layout_unloading_mm,layout_unloading_yyyy;
AutoCompleteTextView loading_dd,loading_mm,loading_yyyy,unloading_dd,unloading_mm,unloading_yyyy;
AppCompatButton btn_submit;
TextView txt_estimated_price;

FirebaseUser currentuser;

// count for no of trips count
    int count=1;
//for holding user profile data
    profile_user_model_class user_profile_model;

    String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request_generate_main);

        //initialize variable
        initialize();
        //setting up date spinner
        date_spinner();

        //resetting field after error...
        resetfield();

        //submit button work..
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(validatesubmission()==true)
                {
                    ProgressDialog dialog=new ProgressDialog(user_request_generate_MainActivity.this);
                    dialog.setTitle("Processing request");
                    dialog.setMessage("please wait...");
                    dialog.show();

                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (task.isSuccessful()) {
                                        token = task.getResult();

                                    }
                                    else
                                    {
                                        Log.d("tokennnnn exxxecptionnnn", "onComplete: "+task.getException());
                                    }

                                }
                            });
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if(!(token.equals(""))) {

                                String to_address=adddress_to.getEditText().getText().toString();
                                String from_address=address_from.getEditText().getText().toString();
                                String loading_date=layout_loading_dd.getEditText().getText().toString()+"/"+layout_loading_mm.getEditText().getText().toString()+"/"+layout_loading_yyyy.getEditText().getText().toString();
                                String unloading_date=layout_unloading_dd.getEditText().getText().toString()+"/"+layout_unloading_mm.getEditText().getText().toString()+"/"+layout_unloading_yyyy.getEditText().getText().toString();
                                String distancee=distance.getEditText().getText().toString();
                                String name_material=material_name.getEditText().getText().toString();
                                String quantity_material=material_quantity.getEditText().getText().toString();
                                String price=txt_estimated_price.getText().toString();



                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(currentuser.getUid());
                                reference.child("Request data").child("no of request").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        reference.child("Request data").child("no of request").removeEventListener(this);
                                        if (snapshot.getValue() == null) {
                                            reference.child("Request data").child("no of request").setValue("1");
                                            count = 1;

                                        } else {
                                            String no_of_trip = snapshot.getValue().toString();
                                            Log.d("no of trippppppppp", "onDataChange: " + no_of_trip);
                                            count = Integer.parseInt(no_of_trip);
                                            count++;
                                            HashMap<String, Object> map = new HashMap();
                                            map.put("no of request", "" + count);

                                            reference.child("Request data").updateChildren(map);
                                        }
                                        String request_no = "" + count;
                                        my_request_user_model_class model = new my_request_user_model_class(request_no, from_address, to_address, loading_date, unloading_date, distancee, name_material, quantity_material, price);

                                        reference.child("My request").child("" + count).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    DatabaseReference reference_user_profile = FirebaseDatabase.getInstance().getReference("User").child(currentuser.getUid()).child("Profile");
                                                    reference_user_profile.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            user_profile_model = snapshot.getValue(profile_user_model_class.class);
                                                            if (user_profile_model != null) {
                                                                String user_contact = user_profile_model.getContact();
                                                                String user_name = user_profile_model.getName();
                                                                String user_pic_url = user_profile_model.getProfile_dp_url();
                                                                String request_no = "" + count;


                                                                all_trip_request_model_class modelall = new all_trip_request_model_class(token, request_no, from_address, to_address, loading_date, unloading_date, distancee, name_material, quantity_material, price, user_contact, user_name, user_pic_url, currentuser.getUid());

                                                                DatabaseReference referenceall = FirebaseDatabase.getInstance().getReference("Admin").child("All trip");
                                                                referenceall.child(currentuser.getUid() + count).setValue(modelall).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(user_request_generate_MainActivity.this, "request generated successfully", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Toast.makeText(user_request_generate_MainActivity.this, "Unsuccessful request", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        adddress_to.getEditText().setText("");
                                                                        address_from.getEditText().setText("");
                                                                        material_name.getEditText().setText("");
                                                                        material_quantity.getEditText().setText("");
                                                                        distance.getEditText().setText("");
                                                                        layout_loading_dd.getEditText().setText("");
                                                                        layout_loading_mm.getEditText().setText("");
                                                                        layout_loading_yyyy.getEditText().setText("");
                                                                        layout_unloading_dd.getEditText().setText("");
                                                                        layout_unloading_mm.getEditText().setText("");
                                                                        layout_unloading_yyyy.getEditText().setText("");
                                                                        dialog.dismiss();

                                                                    }
                                                                });
                                                            } else {
                                                                Log.d("user profile model", "onComplete: model class is null");
                                                                dialog.dismiss();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                                } else {
                                                    Toast.makeText(user_request_generate_MainActivity.this, "Unsuccessful request", Toast.LENGTH_SHORT).show();
                                                    adddress_to.getEditText().setText("");
                                                    address_from.getEditText().setText("");
                                                    material_name.getEditText().setText("");
                                                    material_quantity.getEditText().setText("");
                                                    distance.getEditText().setText("");
                                                    layout_loading_dd.getEditText().setText("");
                                                    layout_loading_mm.getEditText().setText("");
                                                    layout_loading_yyyy.getEditText().setText("");
                                                    layout_unloading_dd.getEditText().setText("");
                                                    layout_unloading_mm.getEditText().setText("");
                                                    layout_unloading_yyyy.getEditText().setText("");

                                                    reference.child("Request data").child("no of request").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            reference.child("Request data").child("no of request").removeEventListener(this);
                                                            String no_of_trip = snapshot.getValue().toString();
                                                            count = Integer.parseInt(no_of_trip);
                                                            count--;
                                                            HashMap<String, Object> map = new HashMap();
                                                            map.put("no of request", "" + count);

                                                            reference.child("Request data").child("no of request").updateChildren(map);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                    dialog.dismiss();
                                                }
                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(user_request_generate_MainActivity.this, "unable to generate request", Toast.LENGTH_SHORT).show();
                                Log.e("Token error", "onClick: error in receiving token");
                            }


                        }
                    },3000);





                }

            }
        });
    }

    private void resetfield() {
        address_from.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                address_from.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        adddress_to.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adddress_to.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        distance.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               distance.setError(null);
               if((!(distance.getEditText().getText().toString().equals("")))&&(!(material_quantity.getEditText().getText().toString().equals(""))))
               {
                   String distancee=distance.getEditText().getText().toString();
                   String quantity=material_quantity.getEditText().getText().toString();
                   int dist=Integer.parseInt(distancee);
                   int quant=Integer.parseInt(quantity);
                   double total=(dist/100.0)*(quant/1000.0)*750.0;
                   txt_estimated_price.setText(""+total);

               }
               else
               {
                   txt_estimated_price.setText("");
               }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        material_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                material_name.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        material_quantity.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                material_quantity.setError(null);
                if((!(distance.getEditText().getText().toString().equals("")))&&(!(material_quantity.getEditText().getText().toString().equals(""))))
                {
                    String distancee=distance.getEditText().getText().toString();
                    String quantity=material_quantity.getEditText().getText().toString();
                    int dist=Integer.parseInt(distancee);
                    int quant=Integer.parseInt(quantity);
                    double total=(dist/100.0)*(quant/1000.0)*750.0;
                    txt_estimated_price.setText(""+total);

                }
                else
                {
                    txt_estimated_price.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        layout_loading_dd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_loading_dd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        layout_loading_mm.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_loading_mm.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        layout_loading_yyyy.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_loading_yyyy.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        layout_unloading_dd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_unloading_dd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        layout_unloading_mm.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_unloading_mm.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        layout_unloading_yyyy.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_unloading_yyyy.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean validatesubmission() {

        int flag=1;
        if(adddress_to.getEditText().getText().toString().equals(""))
        {
            flag=0;
           adddress_to.setError("enter address");

        }
        if(address_from.getEditText().getText().toString().equals(""))
        { flag=0;
            address_from.setError("enter address");

        }
        if(distance.getEditText().getText().toString().equals(""))
        {
            distance.setError("enter distance");
            flag=0;
        }
        if(material_name.getEditText().getText().toString().equals(""))
        {
            material_name.setError("enter material name");
            flag=0;
        }
        if(material_quantity.getEditText().getText().toString().equals(""))
        {
            material_quantity.setError("enter quantity");
            flag=0;
        }
        if(layout_loading_dd.getEditText().getText().toString().equals(""))
        {
            layout_loading_dd.setError("enter dd");
            flag=0;
        }
        if(layout_loading_mm.getEditText().getText().toString().equals(""))
        {
            layout_loading_mm.setError("enter mm");
            flag=0;
        }
        if(layout_loading_yyyy.getEditText().getText().toString().equals(""))
        {
            layout_loading_yyyy.setError("enter yyyy");
            flag=0;
        }
        if(layout_unloading_dd.getEditText().getText().toString().equals(""))
        {
            layout_unloading_dd.setError("enter dd");
            flag=0;
        }
        if(layout_unloading_mm.getEditText().getText().toString().equals(""))
        {
            layout_unloading_mm.setError("enter mm");
            flag=0;
        }
        if(layout_unloading_yyyy.getEditText().getText().toString().equals(""))
        {
            layout_unloading_yyyy.setError("enter yyyy");
            flag=0;
        }
        if(txt_estimated_price.getText().toString().equals(""))
        {
            flag=0;

        }

        if(flag==1)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private void date_spinner() {
        ArrayList<String> dd=new ArrayList<>();
      dd.add("1");dd.add("2");dd.add("3");dd.add("4");dd.add("5");dd.add("6");dd.add("7");dd.add("8");dd.add("9");dd.add("10");dd.add("11");dd.add("12");dd.add("13");dd.add("14");dd.add("15");dd.add("16");dd.add("17");dd.add("18");dd.add("19");dd.add("20");dd.add("21");dd.add("22");dd.add("23");dd.add("24");dd.add("25");dd.add("26");dd.add("27");dd.add("28");dd.add("29");dd.add("30");dd.add("31");
        ArrayAdapter adapter_dd=new ArrayAdapter(this,R.layout.dropdown_menu_dates,dd);
        loading_dd.setAdapter(adapter_dd);
        unloading_dd.setAdapter(adapter_dd);


        ArrayList<String> mm=new ArrayList<>();
        mm.add("1");mm.add("2");mm.add("3");mm.add("4");mm.add("5");mm.add("6");mm.add("7");mm.add("8");mm.add("9");mm.add("10");mm.add("11");mm.add("12");
        ArrayAdapter adapter_mm=new ArrayAdapter(this,R.layout.dropdown_menu_dates,mm);
        loading_mm.setAdapter(adapter_mm);
        unloading_mm.setAdapter(adapter_mm);


        ArrayList<String> yyyy=new ArrayList<>();
        yyyy.add("2023");yyyy.add("2024");yyyy.add("2025");yyyy.add("2026");yyyy.add("2027");yyyy.add("2028");yyyy.add("2029");yyyy.add("2030");
        ArrayAdapter adapter_yyyy=new ArrayAdapter(this,R.layout.dropdown_menu_dates,yyyy);
        loading_yyyy.setAdapter(adapter_yyyy);
        unloading_yyyy.setAdapter(adapter_yyyy);




    }

    private void initialize() {
        address_from=findViewById(R.id.request_generat_textinput_address_from);
        adddress_to=findViewById(R.id.request_generat_textinput_address_to);
        distance=findViewById(R.id.request_generat_textinput_distance);
        material_name=findViewById(R.id.request_generat_textinput_material_name);
        material_quantity=findViewById(R.id.request_generat_textinput_material_quantity);
        loading_dd=findViewById(R.id.request_generat_autocomplete_loading_dd);
        loading_mm=findViewById(R.id.request_generat_autocomplete_loading_mm);
        loading_yyyy=findViewById(R.id.request_generat_autocomplete_loading_yyyy);
        unloading_dd=findViewById(R.id.request_generat_autocomplete_unloading_dd);
        unloading_mm=findViewById(R.id.request_generat_autocomplete_unloading_mm);
        unloading_yyyy=findViewById(R.id.request_generat_autocomplete_unloading_yyyy);
        btn_submit=findViewById(R.id.request_generat_btn_submit);
        txt_estimated_price=findViewById(R.id.request_generat_txt_estimated_price);
        layout_loading_dd=findViewById(R.id.request_generat_textinput_loading_dd);
        layout_loading_mm=findViewById(R.id.request_generat_textinput_loading_mm);
        layout_loading_yyyy=findViewById(R.id.request_generat_textinput_loading_yyyy);
        layout_unloading_dd=findViewById(R.id.request_generat_textinput_unloading_dd);
        layout_unloading_mm=findViewById(R.id.request_generat_textinput_unloading_mm);
        layout_unloading_yyyy=findViewById(R.id.request_generat_textinput_unloading_yyyy);


        //current user
        currentuser=FirebaseAuth.getInstance().getCurrentUser();
    }
}