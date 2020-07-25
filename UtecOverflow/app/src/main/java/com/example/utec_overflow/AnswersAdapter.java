package com.example.utec_overflow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private Context context;
    public JSONArray answers;

    public AnswersAdapter(JSONArray answers, Context context){
        this.answers = answers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_element_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position, payloads);
        try {
            JSONObject question = answers.getJSONObject(position);
            final String questionContent = question.getString("content");
            final int questionId = question.getInt("id");
            holder.firstLine.setText(String.valueOf(questionId));
            holder.secondLine.setText(questionContent);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return answers.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView firstLine, secondLine;
        RelativeLayout container;

        public ViewHolder(View itemView){
            super(itemView);
            firstLine  = itemView.findViewById(R.id.answer_view_user);
            secondLine = itemView.findViewById(R.id.answer_view_content);
            container  = itemView.findViewById(R.id.answer_view_container);
        }
    }
}
