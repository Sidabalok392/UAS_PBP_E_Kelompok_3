package com.example.easy_learning;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easy_learning.adapter.ClassAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassActivity extends AppCompatActivity {

    private RecyclerView rv_class;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ClassAdapter classAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        rv_class = findViewById(R.id.rv_class);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("class/"+firebaseUser.getEmail()
                        .toString().replace('.','&'));

        List<AddClass> list = new ArrayList<>();
        for(int i=0;i<500;i++){
            int finalI = i;
            databaseReference.child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){

                        String nama = snapshot.child("0").getValue().toString();
                        String kode = snapshot.child("1").getValue().toString();
                        String desc = snapshot.child("2").getValue().toString();
                        String url = snapshot.child("3").getValue().toString();

                        list.add(new AddClass(nama,kode,desc,url));
                        classAdapter = new ClassAdapter(list,getApplicationContext());

                        rv_class.setAdapter(classAdapter);
                        rv_class.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),
                            "Gagal Ambil Profil !", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}