package com.example.easy_learning;

import static com.android.volley.Request.Method.DELETE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.easy_learning.api.CourseApi;
import com.example.easy_learning.api.LiveApi;
import com.example.easy_learning.fragment.LiveFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.easy_learning.fragment.HomeFragment;
import com.example.easy_learning.fragment.ProfilFragment;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int LAUNCH_ADD_ACTIVITY = 123;
    private BottomNavigationView bottom_navigation;
    private CardView view_fragment;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private int itemSelect = 0;
    private MenuInflater inflater;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        view_fragment = findViewById(R.id.view_fragment);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        queue = Volley.newRequestQueue(this);

        if(itemSelect == 0)
        {
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.view_fragment,fragment)
                    .commit();
        }


        bottom_navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if(item.getItemId()==R.id.home){
                            if(itemSelect != 1){
                                HomeFragment fragment = new HomeFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.view_fragment,fragment)
                                        .commit();
                            }
                            itemSelect = 1;
                            getSupportActionBar().setTitle("Home");
                        }else if(item.getItemId()==R.id.live){
                            if(itemSelect != 2){
                                LiveFragment fragment = new LiveFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.view_fragment,fragment)
                                        .commit();
                            }
                            itemSelect = 2;
                            getSupportActionBar().setTitle("Tentor");
                        }else if(item.getItemId()==R.id.profil){
                            if(itemSelect != 3){
                                ProfilFragment fragment = new ProfilFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.view_fragment,fragment)
                                        .commit();
                            }
                            itemSelect = 3;
                            getSupportActionBar().setTitle("Profil");
                        }
                        return true;
                    }
                });

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
            startActivity(new Intent(MainActivity.this,
                    LoginActivity.class));
            firebaseAuth.signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteCourse(long id) {
        StringRequest stringRequest = new StringRequest(DELETE, CourseApi.DELETE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "Delete Berhasil", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(MainActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void deleteLive(long id) {
        StringRequest stringRequest = new StringRequest(DELETE, LiveApi.DELETE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "Delete Berhasil", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(MainActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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