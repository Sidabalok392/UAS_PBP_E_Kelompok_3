package com.example.easy_learning;

import static com.example.easy_learning.MyApplication.CHANNEL_1_ID;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_nama_register;
    private EditText et_telepon_register;
    private EditText et_umur_register;
    private EditText et_email_register;
    private EditText et_password_register;
    private Button btn_login_register;
    private Button btn_register_register;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button btn_eye_register;
    private boolean eye = false;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_nama_register = findViewById(R.id.et_nama_register);
        et_telepon_register = findViewById(R.id.et_telepon_register);
        et_umur_register = findViewById(R.id.et_umur_register);
        et_email_register = findViewById(R.id.et_email_register);
        et_password_register = findViewById(R.id.et_password_register);
        btn_login_register = findViewById(R.id.btn_login_register);
        btn_register_register = findViewById(R.id.btn_register_register);
        btn_eye_register = findViewById(R.id.btn_eye_register);
        notificationManager = NotificationManagerCompat.from(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getSupportActionBar().setTitle("Register");

        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,
                        LoginActivity.class));
                finish();
            }
        });

        btn_eye_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eye == false){
                    et_password_register.setInputType(InputType.TYPE_CLASS_TEXT);
                    btn_eye_register.setBackgroundResource
                            (R.drawable.ic_baseline_remove_red_eye_24);
                    eye = true;
                }else{
                    et_password_register.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btn_eye_register.setBackgroundResource
                            (R.drawable.ic_baseline_visibility_off_24);
                    eye = false;
                }

            }
        });

        btn_register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_nama_register.getText().toString().isEmpty() ||
                        et_telepon_register.getText().toString().isEmpty()||
                        et_umur_register.getText().toString().isEmpty()||
                        et_email_register.getText().toString().isEmpty()||
                        et_password_register.getText().toString().isEmpty()){
                    if(et_nama_register.getText().toString().isEmpty())
                        et_nama_register.setError("Nama tidak boleh Kosong !");
                    if(et_telepon_register.getText().toString().isEmpty())
                        et_telepon_register.setError("Telepon tidak boleh Kosong !");
                    if(et_umur_register.getText().toString().isEmpty())
                        et_umur_register.setError("Umur tidak boleh Kosong !");
                    if(et_email_register.getText().toString().isEmpty())
                        et_email_register.setError("Email tidak boleh Kosong !");
                    if(et_password_register.getText().toString().isEmpty())
                        et_password_register.setError("Password tidak boleh kosong Kosong !");
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(
                            et_email_register.getText().toString(),
                            et_password_register.getText().toString())
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(!task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),
                                                        "Gagal Register !", Toast.LENGTH_SHORT).show();
                                            }else{
                                                firebaseUser=firebaseAuth.getCurrentUser();
                                                firebaseUser.sendEmailVerification().addOnCompleteListener(
                                                        new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(!task.isSuccessful()){
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "Gagal Register !",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }else{
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "Register Berhasil !",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    String title = "Sign UP";
                                                                    String message = "Selamat! " +
                                                                            "Anda berhasil melakukan pendaftaran";
                                                                    Notification notification = new
                                                                            NotificationCompat.
                                                                                    Builder(RegisterActivity.this,
                                                                            CHANNEL_1_ID)
                                                                            .setSmallIcon(R.drawable.hi)
                                                                            .setContentTitle(title)
                                                                            .setContentText(message)
                                                                            .setPriority(NotificationCompat
                                                                                    .PRIORITY_HIGH)
                                                                            .setCategory(NotificationCompat
                                                                                    .CATEGORY_MESSAGE)
                                                                            .setColor(Color.BLUE)
                                                                            .setAutoCancel(true)
                                                                            .setOnlyAlertOnce(true)
                                                                            .build();
                                                                    notificationManager.notify(1,notification);
                                                                    startActivity(new Intent(
                                                                            RegisterActivity.this,
                                                                            LoginActivity.class)
                                                                            .putExtra("nama",
                                                                                    et_nama_register
                                                                                            .getText().toString())
                                                                            .putExtra("telepon",
                                                                                    et_telepon_register
                                                                                            .getText().toString())
                                                                            .putExtra("umur",
                                                                                    et_umur_register
                                                                                            .getText().toString())
                                                                            .putExtra("email",
                                                                                    et_email_register.
                                                                                            getText().toString())
                                                                            .putExtra("password",
                                                                                    et_password_register
                                                                                            .getText().toString()));
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            startActivity(new Intent(
                    RegisterActivity.this,
                    LoginActivity.class));
            finish();
        }
    }
}