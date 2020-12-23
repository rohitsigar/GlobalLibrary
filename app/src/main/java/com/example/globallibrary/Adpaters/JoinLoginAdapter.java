package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.R;

public class JoinLoginAdapter extends RecyclerView.Adapter<JoinLoginAdapter.JoinHolder> {

    Context context;

    public JoinLoginAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public JoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_branch,parent,false);

        return new JoinHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class JoinHolder extends RecyclerView.ViewHolder {

        public JoinHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
