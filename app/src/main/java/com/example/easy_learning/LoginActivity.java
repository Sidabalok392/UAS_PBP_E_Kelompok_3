package com.example.easy_learning;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email_login;
    private EditText et_password_login;
    private Button btn_register_login;
    private Button btn_login_login;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button btn_eye_login;
    private boolean eye = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_email_login = findViewById(R.id.et_email_login);
        et_password_login = findViewById(R.id.et_password_login);
        btn_login_login = findViewById(R.id.btn_login_login);
        btn_register_login = findViewById(R.id.btn_register_login);
        btn_eye_login = findViewById(R.id.btn_eye_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btn_eye_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eye == false){
                    et_password_login.setInputType(InputType.TYPE_CLASS_TEXT);
                    btn_eye_login.setBackgroundResource
                            (R.drawable.ic_baseline_remove_red_eye_24);
                    eye = true;
                }else{
                    et_password_login.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btn_eye_login.setBackgroundResource
                            (R.drawable.ic_baseline_visibility_off_24);
                    eye = false;
                }

            }
        });

        getSupportActionBar().setTitle("Login");
        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_email_login.getText().toString().equals("admin")
                        && et_password_login.getText().toString().equals("admin")){
                    startActivity(new Intent(LoginActivity.this,MainAdminActivity.class));
                    finish();
                }else{
                    if(et_email_login.getText().toString().isEmpty() ||
                            et_password_login.getText().toString().isEmpty()){
                        if(et_email_login.getText().toString().isEmpty())
                            et_email_login.setError("Email Kosong !");
                        if(et_password_login.getText().toString().isEmpty())
                            et_password_login.setError("Password Kosong !");
                    }else{
                        firebaseUser = firebaseAuth.getCurrentUser();
                        firebaseAuth.signInWithEmailAndPassword(
                                et_email_login.getText().toString(),
                                et_password_login.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),
                                                    "Gagal Login !", Toast.LENGTH_SHORT).show();
                                        }else{
                                            firebaseUser=firebaseAuth.getCurrentUser();
                                            if(firebaseUser.isEmailVerified()){
                                                if(getIntent().getStringExtra("nama")==null){
                                                }else{
                                                    ArrayList<String> list = new ArrayList<>();
                                                    list.add(getIntent().getStringExtra("nama"));
                                                    list.add(getIntent().getStringExtra("telepon"));
                                                    list.add(getIntent().getStringExtra("umur"));
                                                    list.add(getIntent().getStringExtra("email"));
                                                    list.add(getIntent().getStringExtra("password"));
                                                    databaseReference.child("users")
                                                            .child(et_email_login.getText().toString()
                                                                    .replace('.','&'))
                                                            .setValue(list);
                                                }
                                                startActivity(new Intent(
                                                        LoginActivity.this,
                                                        MainActivity.class));
                                                finish();

                                            }else{
                                                Toast.makeText(getApplicationContext(),
                                                        "Email belum diverif",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        });

        btn_register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(LoginActivity.this,
                        RegisterActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null && firebaseUser.isEmailVerified()){
            int regex=0;
            int length=firebaseUser.getEmail().length();
            for(int i=0;i<firebaseUser.getEmail().length();i++){
                if(firebaseUser.getEmail().charAt(i) == '@'){
                    regex=i;
                    break;
                }
            }
            String email = firebaseUser.getEmail().substring(0,regex);
            startActivity(new Intent(
                    LoginActivity.this,
                    MainActivity.class).putExtra("email",email));
            finish();
        }
    }
}