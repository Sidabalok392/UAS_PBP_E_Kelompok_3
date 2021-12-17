package com.example.easy_learning;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ProfilActivity extends AppCompatActivity {

    private PorterShapeImageView iv_foto_profil_edit;
    private EditText et_nama_profil_edit;
    private EditText et_telepon_profil_edit;
    private EditText et_umur_profil_edit;
    private Button btn_back_edit_profil;
    private Button btn_edit_profil_edit;
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;
    private Bitmap bitmap = null;
    private Uri uri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        iv_foto_profil_edit = findViewById(R.id.iv_foto_profil_edit);
        et_nama_profil_edit = findViewById(R.id.et_nama_profil_edit);
        et_telepon_profil_edit = findViewById(R.id.et_telepon_profil_edit);
        et_umur_profil_edit = findViewById(R.id.et_umur_profil_edit);
        btn_back_edit_profil = findViewById(R.id.btn_back_edit_profil);
        btn_edit_profil_edit = findViewById(R.id.btn_edit_profil_edit);

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

        getSupportActionBar().setTitle("Edit Profil");
        iv_foto_profil_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.
                        from(ProfilActivity.this);
                View selectMediaView = layoutInflater.
                        inflate(R.layout.layout_select_media, null);
                final android.app.AlertDialog alertDialog =
                        new android.app.AlertDialog.
                                Builder(selectMediaView.getContext()).create();
                Button btn_kamera = selectMediaView.findViewById(R.id.btn_kamera);
                Button btn_galeri = selectMediaView.findViewById(R.id.btn_galeri);
                btn_kamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (checkSelfPermission(
                                Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA};
                            requestPermissions(permission,
                                    PERMISSION_REQUEST_CAMERA);
                        } else {
                            Intent intent = new Intent(MediaStore
                                    .ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,
                                    CAMERA_REQUEST);
                        }
                        alertDialog.dismiss();
                    }
                });btn_galeri.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.
                                ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_PICTURE);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(selectMediaView);
                alertDialog.show();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                et_nama_profil_edit.setText(snapshot.child("0")
                        .getValue().toString());
                et_telepon_profil_edit.setText(snapshot.child("1")
                        .getValue().toString());
                et_umur_profil_edit.setText(snapshot.child("2")
                        .getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),
                        "Gagal Ambil Profil !", Toast.LENGTH_SHORT).show();
            }
        });
        storageReference.getDownloadUrl().addOnCompleteListener(
                new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Gagal Ambil Foto !", Toast.LENGTH_SHORT).show();
                        }else{
                            path = task.getResult().toString();
                            Glide.with(getApplicationContext()).
                                    load(task.getResult().
                                            toString()).into(iv_foto_profil_edit);
                        }
                    }
                });
        btn_back_edit_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_edit_profil_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_nama_profil_edit.getText().toString().isEmpty() ||
                        et_telepon_profil_edit.getText().toString().isEmpty()||
                        et_umur_profil_edit.getText().toString().isEmpty()){
                    if(et_nama_profil_edit.getText().toString().isEmpty())
                        et_nama_profil_edit.setError("Nama Kosong !");
                    if(et_telepon_profil_edit.getText().toString().isEmpty())
                        et_telepon_profil_edit.setError("Telepon Kosong !");
                    if(et_umur_profil_edit.getText().toString().isEmpty())
                        et_umur_profil_edit.setError("Umur Kosong !");
                }else{ if(path!=null || uri!=null){
                    databaseReference.child("0").
                            setValue(et_nama_profil_edit.
                                    getText().toString());
                    databaseReference.child("1").
                            setValue(et_telepon_profil_edit.
                                    getText().toString());
                    databaseReference.child("2").
                            setValue(et_umur_profil_edit.
                                    getText().toString());
                    if(uri!=null){
                        storageReference.putFile(uri);
                    }

                    Toast.makeText(getApplicationContext(),
                            "Berhasil Edit Data !",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(),
                            "Belum Memilih Foto Profil !",
                            Toast.LENGTH_SHORT).show();
                }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "Permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            Uri selectedImage = data.getData();
            uri=data.getData();
            try {
                InputStream inputStream = getContentResolver()
                        .openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(
                    this.getContentResolver(),bitmap,
                    null,null);
            uri = Uri.parse(path);
        }

        bitmap = getResizedBitmap(bitmap, 512);
        iv_foto_profil_edit.setImageBitmap(bitmap);
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap,
                width, height, true);
    }
}