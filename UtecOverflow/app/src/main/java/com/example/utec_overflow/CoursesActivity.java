package com.example.utec_overflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class CoursesActivity extends AppCompatActivity {

    RecyclerView mRecycleView;
    RecyclerView.Adapter mAdapter;

    public int userId;
    public String userUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        this.userUsername = this.getIntent().getExtras().getString("username");
        this.userId = this.getIntent().getExtras().getInt("id");
        this.setTitle(userUsername);

        mRecycleView = findViewById(R.id.courses_recycler_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mRecycleView.setLayoutManager(new GridLayoutManager(this, 2));
        getCourses();
    }

    public Activity getActivity(){
        return this;
    }

    public void showMessage(String message){
        Toast.makeText(this , message, Toast.LENGTH_LONG).show();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void onHomeClicked(){
        showMessage("!Ya estás en los cursos!");
    }

    public void onProfileClicked(){
        goToProfileActivity();
    }

    public void onLogoutClicked(View view){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "http://10.0.2.2:8080/logout",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // to do on chat response
                        goToMainActivity();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // to do on error
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void getCourses() {
        String url = "http://10.0.2.2:8080/courses";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // to do on users response
                        mAdapter = new CoursesAdapter(response, getActivity(), userId, userUsername);
                        mRecycleView.setAdapter(mAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // to do on error
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}