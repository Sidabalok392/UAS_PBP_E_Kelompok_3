package com.example.easy_learning;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.example.easy_learning.adapter.AdminAdapter;
import com.example.easy_learning.api.CourseApi;
import com.example.easy_learning.model.CourseResponse;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainAdminActivity extends AppCompatActivity {

    public static final int LAUNCH_ADD_ACTIVITY = 123;

    private AdminAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private RecyclerView rvAdmin;
    private SearchView svAdmin;
    private SwipeRefreshLayout srAdmin;
    private LinearLayout layoutLoading;
    private FloatingActionButton fabAdd;
    private RequestQueue queue;
    private MenuInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        queue = Volley.newRequestQueue(this);

        rvAdmin = findViewById(R.id.rv_admin);
        svAdmin = findViewById(R.id.sv_admin);
        srAdmin = findViewById(R.id.sr_admin);
        layoutLoading = findViewById(R.id.layout_loading);
        fabAdd = findViewById(R.id.fab_add);

        svAdmin.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        srAdmin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllAdmin();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEditCourseActivity.class);
                startActivityForResult(intent, LAUNCH_ADD_ACTIVITY);
            }
        });
        adapter = new AdminAdapter(new ArrayList<>(), this);
        manager = new GridLayoutManager(this,2);
        rvAdmin.setLayoutManager(manager);
        rvAdmin.setAdapter(adapter);

        getAllAdmin();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == Activity.RESULT_OK)
            getAllAdmin();
    }

    private void getAllAdmin() {
        srAdmin.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(GET, CourseApi.GET_ALL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CourseResponse courseResponse = gson.fromJson(response, CourseResponse.class);
                adapter.setCourseList(courseResponse.getCourseList());
                adapter.getFilter().filter(svAdmin.getQuery());
                Toast.makeText(getApplicationContext(), "Ambil data admin berhasil", Toast.LENGTH_SHORT).show();
                srAdmin.setRefreshing(false);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srAdmin.setRefreshing(false);
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

    public void deleteCourse(long id) {
        StringRequest stringRequest = new StringRequest(DELETE, CourseApi.DELETE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainAdminActivity.this, "Delete Berhasil", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(MainAdminActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Toast.makeText(MainAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = new MenuInflater(this);
        inflater.inflate(R.menu.home_app_bar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            startActivity(new Intent(MainAdminActivity.this,
                    LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    
}