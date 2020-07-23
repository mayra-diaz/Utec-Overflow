package com.example.utec_overflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CoursesActivity extends AppCompatActivity {

    RecyclerView mRecycleView;
    RecyclerView.Adapter mAdapter;

    public int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        String username = this.getIntent().getExtras().getString("username");
        this.userId = this.getIntent().getExtras().getInt("id");
        this.setTitle(username);

        mRecycleView = findViewById(R.id.courses_recycler_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        getUsers();
    }

    public Activity getActivity(){
        return this;
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

    public void getUsers() {
        String url = "http://10.0.2.2:8080/users";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // to do on users response
                        mAdapter = new ContactsAdapter(response, getActivity(), userId);
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