package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup_MainActivity extends AppCompatActivity {
TextInputLayout edit_email,edit_user_name,edit_contact,edit_password,edit_confirm_password;
AppCompatCheckBox check_user,check_driver;
TextView checkbox_caution;
AppCompatButton btn_signup;

FirebaseAuth auth;
DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_main);


        edit_email=findViewById(R.id.signup_txt_layoutemail);
        edit_user_name=findViewById(R.id.signup_txt_username);
        edit_contact=findViewById(R.id.signup_txt_contact);
        edit_password=findViewById(R.id.signup_txt_layoutpassword);
        edit_confirm_password=findViewById(R.id.signup_txt_layout_confirmpassword);
        btn_signup=findViewById(R.id.signup_btn_signup);
        check_driver=findViewById(R.id.check_driver);
        check_user=findViewById(R.id.check_user);
        checkbox_caution=findViewById(R.id.txt_caution_checkbox);
        checkbox_caution.setVisibility(View.GONE);

        auth=FirebaseAuth.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupprocess();
            }
        });

        //reset text fields
        edit_email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_user_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_user_name.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_contact.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_contact.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_confirm_password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_confirm_password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //allow only one check box to be selected
        check_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_user.isChecked())
                {
                    check_user.setChecked(false);

                }
                checkbox_caution.setVisibility(View.GONE);
            }
        });

        check_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_driver.isChecked())
                {
                    check_driver.setChecked(false);
                }
                checkbox_caution.setVisibility(View.GONE);
            }
        });






    }

    private void signupprocess() {

        String email=edit_email.getEditText().getText().toString();
        String user_name=edit_user_name.getEditText().getText().toString();
        String contact=edit_contact.getEditText().getText().toString();
        String password=edit_password.getEditText().getText().toString();
        String confirm_password=edit_confirm_password.getEditText().getText().toString();

      if(checkvalidation(email,user_name,contact,password,confirm_password)==1)
      {
          auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()) {
                      if (check_driver.isChecked()) {
                          FirebaseUser user=auth.getCurrentUser();
                          reference = FirebaseDatabase.getInstance().getReference("Driver");
                          String experience="";
                          String url="";
                          profile_diver_model_class model = new profile_diver_model_class(user_name, contact,experience,email,url);
                          reference.child(user.getUid()).child("Profile").setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if (task.isSuccessful()) {
                                      Toast.makeText(signup_MainActivity.this, "successfully added", Toast.LENGTH_SHORT).show();
                                      Intent intent = new Intent(signup_MainActivity.this, Dashboard_MainActivity.class);
                                      startActivity(intent);
                                      finishAffinity();
                                  } else {
                                      Toast.makeText(signup_MainActivity.this, "not added->", Toast.LENGTH_SHORT).show();
                                  }
                              }
                          });


                      }
                      else if(check_user.isChecked())
                      {
                          FirebaseUser user=auth.getCurrentUser();

                          reference=FirebaseDatabase.getInstance().getReference("User");
                          String address="";
                          String profile_dp_url="";
                          String contact2="";
                            profile_user_model_class model=new profile_user_model_class(user_name,contact,contact2,email,address,profile_dp_url);
                          reference.child(user.getUid()).child("Profile").setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(signup_MainActivity.this, "successfully added", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(signup_MainActivity.this, Dashboard_user_MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                                else {
                                    Toast.makeText(signup_MainActivity.this, "not added->", Toast.LENGTH_SHORT).show();
                                }
                              }
                          });
                      }
                      else {
                          Toast.makeText(signup_MainActivity.this, "unable to sign up", Toast.LENGTH_SHORT).show();
                      }
                  }
              }
          });

      }



    }

    private int checkvalidation(String email, String user_name, String contact, String password, String confirm_password) {
        int valid=1;
        if(email.equals(""))
        {
            edit_email.setError("Enter valid email");
            valid=0;
        }
        if(user_name.equals(""))
        {
            edit_user_name.setError("Enter valid username");
            valid=0;
        }
        if(contact.equals("")||contact.length()!=10)
        {
            edit_contact.setError("Enter valid contact");
            valid=0;
        }
        if(password.equals(""))
        {
            edit_password.setError("Enter valid password");
            valid=0;

        }
        if(confirm_password.equals(""))
        {
            edit_confirm_password.setError("Enter valid password");
            valid=0;
        }

        if((password.equals(confirm_password))==false)
        {
            edit_confirm_password.setError("Doesn't match password");
            valid=0;
        }
        if((!(check_user.isChecked()))&&(!(check_driver.isChecked())))
        {
            checkbox_caution.setVisibility(View.VISIBLE);
            valid=0;
        }
        return valid;
    }
}