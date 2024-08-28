package com.example.loadkaro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_MainActivity extends AppCompatActivity {

    TextInputLayout edit_email,edit_password;

    TextView btn_forget_password,login_txt_forget_password;
    AppCompatButton btn_login;
    FirebaseAuth auth;

    LinearLayout btn_google_signin;

    GoogleSignInClient googleclint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        edit_email=findViewById(R.id.login_txt_layoutemail);
        edit_password=findViewById(R.id.login_txt_layoutpassword);
        btn_forget_password=findViewById(R.id.btn_forget_password);
        login_txt_forget_password=findViewById(R.id.login_txt_forget_password);

        btn_login=findViewById(R.id.login_btn_login);
        btn_google_signin=findViewById(R.id.login_btn_google_signin);

         auth=FirebaseAuth.getInstance();

    //on text change to resolve error field
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

//login button lisitner
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=edit_email.getEditText().getText().toString();
                String password=edit_password.getEditText().getText().toString();

                if(!(email.equals("")||password.equals("")))
                {
                    loginthroughcrediential(email,password);
                }

                else if(email.equals("")&&password.equals(""))
                {
                    edit_email.setError("Enter valid email");
                    edit_password.setError("Enter valid password");
                }
                else if(password.equals(""))
                {
                    edit_password.setError("Enter valid password");
                }
                else
                {
                    edit_email.setError("Enter valid email");
                }


            }
        });

        //google sing in
        GoogleSignInOptions options=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleclint= GoogleSignIn.getClient(this,options);


        btn_google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginthroughgoogle();
            }
        });

        //forget password process
        btn_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit_password.setVisibility(View.GONE);
                btn_login.setVisibility(View.GONE);
                btn_forget_password.setText("Send email");
                String email=edit_email.getEditText().getText().toString();
                if((email.equals("")))
                {
                    edit_email.setError("enter valid email");
                }
                else
                {
                    edit_email.setVisibility(View.GONE);
                    edit_email.getEditText().setText("");
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                CountDownTimer countDown =  new CountDownTimer(59000, 1000) {
                                    @Override
                                    public void onTick(long l) {
                                        long sec=l/1000;
                                        btn_forget_password.setText("0:"+sec+".");
                                    }

                                    @Override
                                    public void onFinish() {
                                        edit_email.setVisibility(View.VISIBLE);
                                        btn_forget_password.setText("Resend email");
                                        login_txt_forget_password.setText("Don't remember password? ");


                                    }
                                }.start();
                                Toast.makeText(login_MainActivity.this, "email send", Toast.LENGTH_SHORT).show();
                                login_txt_forget_password.setText("Email send; wait to resend email in ");
                            }
                            else
                            {

                                Toast.makeText(login_MainActivity.this, "Email not send"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    edit_email.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }
        });


    }

    private void loginthroughgoogle() {

        Intent intent = googleclint.getSignInIntent();
        startActivityForResult(intent,101);

    }

    private void loginthroughcrediential(String email, String password) {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        edit_email.getEditText().setText("");
                        edit_password.getEditText().setText("");

                        if(task.isSuccessful())
                        {
                            FirebaseUser user= auth.getCurrentUser();
                            DatabaseReference referenceuser = FirebaseDatabase.getInstance().getReference("Driver");
                            referenceuser.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.hasChild(user.getUid())) {
                                        Intent intent = new Intent(login_MainActivity.this, Dashboard_MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        Intent intent = new Intent(login_MainActivity.this, Dashboard_user_MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(login_MainActivity.this, "cancle login"+error, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(login_MainActivity.this, "login not successfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            if(requestCode==101)
            {
                Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account=task.getResult(ApiException.class);
                    signinwithgoogleauth(account.getIdToken());
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void signinwithgoogleauth(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(login_MainActivity.this,Dashboard_MainActivity.class);
                            intent.putExtra("email",auth.getCurrentUser().getEmail());
                            startActivity(intent);
                            finishAffinity();
                        }
                        else {
                            Toast.makeText(login_MainActivity.this, "signin unsucessfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}