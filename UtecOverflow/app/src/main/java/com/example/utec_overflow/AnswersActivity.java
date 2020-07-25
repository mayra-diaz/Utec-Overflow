package com.example.utec_overflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnswersActivity extends AppCompatActivity {

    RecyclerView mRecycleView;
    RecyclerView.Adapter mAdapter;

    public int userId;
    public String userUsername;
    public int courseId;
    public String courseName;
    public String questionContent;
    public int questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        this.userUsername = this.getIntent().getExtras().getString("userUsername");
        this.userId = this.getIntent().getExtras().getInt("userId");
        this.courseId = this.getIntent().getExtras().getInt("courseId");
        this.courseName = this.getIntent().getExtras().getString("courseName");
        this.questionContent = this.getIntent().getExtras().getString ("questionContent");
        this.questionId = this.getIntent().getExtras().getInt("questionId");
        this.setTitle(userUsername);

        mRecycleView = findViewById(R.id.answers_recycler_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        getAnswers();
    }

    public Activity getActivity(){
        return this;
    }

    private void goToProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userUsername", userUsername);
        startActivity(intent);
    }

    private void goToCoursesActivity(){
        Intent intent = new Intent(this, CoursesActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userUsername", userUsername);
        startActivity(intent);
    }

    public void onHomeClicked(View view){
        goToCoursesActivity();
    }

    public void onProfileClicked(View view){
        goToProfileActivity();
    }

    public void getAnswers() {
        String url = "http://10.0.2.2:8080/answers/" + questionId;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // to do on users response
                        mAdapter = new AnswersAdapter(response, getActivity());
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

    public void postAnswer(View view){
        //1. Get inputs
        EditText questionContent = (EditText)findViewById(R.id.post_answer_content);
        final String content = questionContent.getText().toString();

        //2. Create JSON message

        Map<String, String> message = new HashMap<>();
        message.put("content", content);
        message.put("user_id", String.valueOf(userId));
        message.put("question_id", String.valueOf(questionId));
        JSONObject jsonMessage = new JSONObject(message); // convert to json

        //3. Sent request Message to Server
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8080/new_answer",
                jsonMessage,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TO DO when  OK response
                        getAnswers();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TO DO when  FAIL response
                        error.printStackTrace();
                    }
                }
        );

        //4. Send request to Server
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}