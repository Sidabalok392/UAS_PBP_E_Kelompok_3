package com.example.easy_learning;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.example.easy_learning.api.CourseApi;
import com.example.easy_learning.api.LiveApi;
import com.example.easy_learning.model.CourseResponse;
import com.example.easy_learning.model.Live;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddEditLiveActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;

    private TextView tv_title_live;
    private CardView cv_gambar_live;
    private ImageView iv_gambar;
    private AutoCompleteTextView et_nama_live;
    private AutoCompleteTextView et_sesi_live;
    private AutoCompleteTextView et_tanggal_live;
    private Button btn_cancel_live;
    private Button btn_save_live_live;
    private LinearLayout layoutLoading;
    private Bitmap bitmap = null;
    private RequestQueue queue;
    private EditText ambil_url;
    private String url;
    private String[] list = new String[]{};
    private List<Long> index;
    private List<String> nama_modul;
    private List<String> url_modul;
    private Calendar newDate;
    private Intent intent;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_live);
        queue = Volley.newRequestQueue(this);

        iv_gambar = findViewById(R.id.iv_gambar_live_edit);
        et_nama_live = findViewById(R.id.et_nama_live);
        et_sesi_live = findViewById(R.id.et_sesi_live);
        et_tanggal_live = findViewById(R.id.et_tanggal_live);
        layoutLoading = findViewById(R.id.layout_loading);

        getAllCourse();

        Button btnCancel = findViewById(R.id.btn_cancel_live);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button btnSave = findViewById(R.id.btn_save_live);
        TextView tvTitle = findViewById(R.id.tv_title_live);
        long id = getIntent().getLongExtra("id", -1);
        if (id == -1) {
            tvTitle.setText("Tambah Live");
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createLive();
                }
            });
        } else {
            tvTitle.setText("Edit Live");
            getLiveById(id);
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("url")).into(iv_gambar);
            et_nama_live.setText(getIntent().getStringExtra("nama"));
            et_sesi_live.setText(getIntent().getStringExtra("sesi"));
            et_tanggal_live.setText(getIntent().getStringExtra("tanggal"));
            url=getIntent().getStringExtra("url");

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateLive(id);
                }
            });
        }

        String[] SESI_LIST = new String[]{"Sesi 1", "Sesi 2", "Sesi 3", "Sesi 4", "Sesi 5"};
        ArrayAdapter<String> sesi = new ArrayAdapter<>(this, R.layout.list_item,SESI_LIST );
        et_sesi_live.setAdapter(sesi);

        et_nama_live.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                for(int i=0;i<index.size();i++){
                    if(et_nama_live.getText().toString().equals(nama_modul.get(i))){
                        Glide.with(getApplicationContext()).load(url_modul.get(i)).into(iv_gambar);
                        url=url_modul.get(i);
                        break;
                    }
                }
            }
        });

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        et_tanggal_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditLiveActivity.this, (datePicker, year, month, day) ->
                {
                    newDate = Calendar.getInstance();
                    newDate.set(year,month,day);
                    et_tanggal_live.setText(simpleDateFormat.format(newDate.getTime()));
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                Toast.makeText(AddEditLiveActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            Uri selectedImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(AddEditLiveActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        bitmap = getResizedBitmap(bitmap, 512);
        iv_gambar.setImageBitmap(bitmap);
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

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private void getLiveById(long id) {
        setLoading(true);
        StringRequest stringRequest = new StringRequest(GET, LiveApi.GET_BY_ID_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AddEditLiveActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditLiveActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private void createLive() {
        setLoading(true);
        Live live = new Live(
                et_nama_live.getText().toString(),
                et_sesi_live.getText().toString(),
                et_tanggal_live.getText().toString(),
                url);


        StringRequest stringRequest = new StringRequest(POST, LiveApi.ADD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(AddEditLiveActivity.this, "Berhasil Tambah Live", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);

                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AddEditLiveActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditLiveActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                String requestBody = gson.toJson(live);
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(stringRequest);
    }

    private void updateLive(long id) {
        setLoading(true);

        Live live = new Live(
                et_nama_live.getText().toString(),
                et_sesi_live.getText().toString(),
                et_tanggal_live.getText().toString(),
                url);

        StringRequest stringRequest = new StringRequest(PUT, LiveApi.UPDATE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddEditLiveActivity.this, "Berhasil Update Data", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AddEditLiveActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditLiveActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                String requestBody = gson.toJson(live);
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(stringRequest);
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.INVISIBLE);
        }
    }

    private void getAllCourse() {
        StringRequest stringRequest = new StringRequest(GET, CourseApi.GET_ALL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CourseResponse courseResponse = gson.fromJson(response, CourseResponse.class);
//                adapter.setCourseList(courseResponse.getCourseList());
//                adapter.getFilter().filter(svCourse.getQuery());
//                Toast.makeText(getContext(), "Ambil data course berhasil", Toast.LENGTH_SHORT).show();

                index = new ArrayList<>();
                nama_modul = new ArrayList<>();
                url_modul = new ArrayList<>();

                for(int i=0;i<courseResponse.getCourseList().size();i++){
                    index.add(courseResponse.getCourseList().get(i).getId());
                    nama_modul.add(courseResponse.getCourseList().get(i).getNama_modul());
                    url_modul.add(courseResponse.getCourseList().get(i).getUrl());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item,nama_modul);
                et_nama_live.setAdapter(adapter);


            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(getApplicationContext(), errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        queue.add(stringRequest);
    }
}