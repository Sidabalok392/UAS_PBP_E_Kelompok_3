package com.example.easy_learning.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.easy_learning.ClassActivity;
import com.example.easy_learning.LocationActivity;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.easy_learning.ProfilActivity;
import com.example.easy_learning.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {
    private PorterShapeImageView iv_foto_profil;
    private TextView tv_nama_profil;
    private TextView tv_telepon_profil;
    private TextView tv_umur_profil;
    private TextView tv_email_profil;
    private Button btn_edit_profil;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView btn_class_profil;
    private ImageView btn_location_profil;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);


        iv_foto_profil = view.findViewById(R.id.iv_foto_profil);
        tv_nama_profil = view.findViewById(R.id.tv_nama_profil);
        tv_telepon_profil = view.findViewById(R.id.tv_telepon_profil);
        tv_umur_profil = view.findViewById(R.id.tv_umur_profil);
        tv_email_profil = view.findViewById(R.id.tv_email_profil);
        btn_edit_profil = view.findViewById(R.id.btn_edit_profil);
        btn_class_profil = view.findViewById(R.id.btn_class_profil);
        btn_location_profil = view.findViewById(R.id.btn_location_profil);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("users/"+firebaseUser.getEmail()
                        .toString().replace('.','&'));
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                "gs://easy-learning-faffd.appspot.com/users/"+
                        firebaseUser.getEmail().toString()
                                .replace('.','&'));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_nama_profil.setText(snapshot.child("0").getValue().toString());
                tv_telepon_profil.setText(snapshot.child("1").getValue().toString());
                tv_umur_profil.setText(snapshot.child("2").getValue().toString());
                tv_email_profil.setText(snapshot.child("3").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Gagal Ambil Profil !",
                        Toast.LENGTH_SHORT).show();
            }
        });

        storageReference.getDownloadUrl().addOnCompleteListener(
                new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getContext(),
                                    "Gagal Ambil Foto !",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            if (getActivity() == null) {
                                return;
                            }else{
                                Glide.with(getContext()).load(
                                        task.getResult().toString())
                                        .into(iv_foto_profil);
                            }
                        }
                    }
                });


        btn_class_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ClassActivity.class));
            }
        });

        btn_location_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LocationActivity.class));
            }
        });

        btn_edit_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfilActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}