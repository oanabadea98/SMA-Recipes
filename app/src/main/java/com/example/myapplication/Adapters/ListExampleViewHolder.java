package com.example.myapplication.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class ListExampleViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTv;
    public TextView firstnameTv;

    public ListExampleViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
    }

    private void initializeViews()
    {
        nameTv = itemView.findViewById(R.id.tv_row_example_name);
        firstnameTv = itemView.findViewById(R.id.tv_row_example_firstname);
    }

    public void setValues(String name, String firstname){
        nameTv.setText(name);
        firstnameTv.setText(firstname);
    }
}
