package com.example.utec_overflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.userId = this.getIntent().getExtras().getInt("userId");
        this.setTitle("Perfil");

    }

    private void goToProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void onHomeClicked(){
        goToCoursesActivity();
    }

    public void onProfileClicked(){
        goToProfileActivity();
    }

    private void goToCoursesActivity(){
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }
}