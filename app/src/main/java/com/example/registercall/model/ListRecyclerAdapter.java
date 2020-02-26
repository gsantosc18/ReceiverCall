package com.example.registercall.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.registercall.R;

import java.util.List;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListHolder> {
    private List<LogCall> logCallList;

    public ListRecyclerAdapter(List<LogCall> logCallList) {
        this.logCallList = logCallList;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View card = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.card_log_call, viewGroup, false );
        return new ListHolder( card );
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
