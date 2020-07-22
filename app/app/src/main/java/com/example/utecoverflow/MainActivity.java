package com.example.utecoverflow;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showMessage(String message){
        Toast.makeText(this , message, Toast.LENGTH_LONG).show();

    }

    private void gotoIndex(String username, int id){
        Intent intent = new Intent(this, Index.class);
        intent.putExtra("username", username);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void onRegisterClicked(View view) {
        Intent intent = new Intent(this, RegisteruserActivity.class);
        startActivity(intent);
    }
    public void onLoginClicked(View view){
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
                "http://10.0.2.2:8080/authenticatemobile",
                jsonMessage,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TO DO when  OK response
                        showMessage(response.toString());
                        try {
                            String username = response.getString("username");
                            int id = response.getInt("id");
                            gotoIndex(username, id);
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

        //4. Send request to Server
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}