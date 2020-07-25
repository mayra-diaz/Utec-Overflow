package com.example.utec_overflow;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder>{

    private Context context;
    public JSONArray questions;
    public int courseId;
    public int userId;
    public String userUsername;

    public QuestionsAdapter(JSONArray questions, Context context, int courseId, int userId, String userUsername){
        this.questions = questions;
        this.context = context;
        this.userId = userId;
        this.courseId = courseId;
        this.userUsername = userUsername;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_element_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position, payloads);
        try {
            JSONObject question = questions.getJSONObject(position);
            final String questionContent = question.getString("content");
            final int questionId = question.getInt("id");
            JSONObject questionUser = question.getJSONObject("user");
            String questionUserUsername = questionUser.getString("username");
            holder.firstLine.setText(questionUserUsername);
            holder.secondLine.setText(questionContent);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToAnswersActivity(questionContent, questionId, courseId, userId);
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void goToAnswersActivity(String questionContent, int questionId, int courseId, int userID){
        Intent intent = new Intent(this.context, AnswersActivity.class);
        intent.putExtra("questionContent", questionContent);
        intent.putExtra("questionId", questionId);
        intent.putExtra("courseId", courseId);
        intent.putExtra("userId", userID);
        intent.putExtra("userUsername", userUsername);
        this.context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return questions.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView firstLine, secondLine;
        RelativeLayout container;

        public ViewHolder(View itemView){
            super(itemView);
            firstLine  = itemView.findViewById(R.id.question_view_user);
            secondLine = itemView.findViewById(R.id.question_view_content);
            container  = itemView.findViewById(R.id.questions_view_container);
        }
    }
}
