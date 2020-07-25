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

public class ProfileActivity extends AppCompatActivity {

    int userId;
    String userUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.userId = this.getIntent().getExtras().getInt("userId");
        this.userUsername = this.getIntent().getExtras().getString("userUsername");
        this.setTitle("Perfil");

    }

    public void showMessage(String message){
        Toast.makeText(this , message, Toast.LENGTH_LONG).show();
    }

    private void goToCoursesActivity(){
        Intent intent = new Intent(this, CoursesActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userUsername", userUsername);
        startActivity(intent);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onHomeClicked(View view){
        goToCoursesActivity();
    }

    public void onProfileClicked(View view){
        showMessage("¡Ya estás en tu perfil!");
    }


    public void onLogoutClicked(View view){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "http://10.0.2.2:8080/logout",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response.getString("msg");
                            goToMainActivity();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
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

    public void onUpdatePassword(View view){
        //1. Implement Login
        EditText txtUsername = (EditText)findViewById(R.id.loginusername);
        EditText txtPassword = findViewById(R.id.loginpassword);
        final String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        Toast.makeText(this,"Bienvenido a UtecOverflow", Toast.LENGTH_LONG).show();

        //2. Create JSON message

        Map<String, String> message = new HashMap<>(); //{}
        message.put("username", username ); // { "username": "ale.sr"}
        message.put("password", password); //{"password": "ale123"}
        JSONObject jsonMessage = new JSONObject(message); // convert to json

        //3. Sent request Message to Server
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8080/update_user",
                jsonMessage,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TO DO when  OK response
                        showMessage("¡Usuario actualizado!");

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

        //4. Send request to Server
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

}