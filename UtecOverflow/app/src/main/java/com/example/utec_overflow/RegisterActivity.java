package com.example.utec_overflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void showMessage(String message){
        Toast.makeText(this , message, Toast.LENGTH_LONG).show();

    }

    public void onRegisterClicked(View view){
        //1. Implement Register
        EditText txtUsername = (EditText)findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);
        EditText txtName = (EditText)findViewById(R.id.txtName);
        EditText txtFullname = (EditText)findViewById(R.id.txtFullname);


        final String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        final String name = txtName.getText().toString();
        final String fullname = txtFullname.getText().toString();

        Toast.makeText(this,"Register succeed ", Toast.LENGTH_LONG).show();

        //2. Create JSON message

        Map<String, String> message = new HashMap<>(); //{}
        message.put("username", username );
        message.put("password", password);
        message.put("name", name);
        message.put("fullname", fullname);
        JSONObject jsonMessage = new JSONObject(message); // convert to json

        //3. Sent request Message to Server
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8080/new_user",
                jsonMessage,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TO DO when  OK response
                        showMessage(response.toString());
                        try {
                            String name = response.getString("username");
                            String fullname = response.getString("username");
                            String username = response.getString("username");
                            int id = response.getInt("id");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TO DO when  FAIL response

                        error.printStackTrace();
                        showMessage(error.getMessage());
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}