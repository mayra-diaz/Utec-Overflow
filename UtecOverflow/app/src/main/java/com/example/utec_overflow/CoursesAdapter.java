package com.example.utec_overflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder>{

    private Context context;
    public JSONArray courses;
    public int userId;
    public String userUsername;

    public CoursesAdapter(JSONArray courses, Context context, int userId, String userUsername){
        this.courses = courses;
        this.context = context;
        this.userId = userId;
        this.userUsername = userUsername;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_element_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position, payloads);
        try {
            JSONObject course = courses.getJSONObject(position);
            final String coursename = course.getString("name");
            final String semester = "Ciclo" + course.getInt("semester");
            final int courseId = course.getInt("id");
            holder.firstLine.setText(semester);
            holder.secondLine.setText(coursename);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToQuestionsActivity(coursename, courseId, userId);
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void goToQuestionsActivity(String coursename, int courseId, int userID){
        Intent intent = new Intent(this.context, QuestionsActivity.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("courseName", coursename);
        intent.putExtra("userId", userID);
        intent.putExtra("userUsername", userUsername);
        this.context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return courses.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView firstLine, secondLine;
        RelativeLayout container;

        public ViewHolder(View itemView){
            super(itemView);
            firstLine  = itemView.findViewById(R.id.course_view_first_line);
            secondLine = itemView.findViewById(R.id.course_view_second_line);
            container  = itemView.findViewById(R.id.course_view_container);
        }
    }
}