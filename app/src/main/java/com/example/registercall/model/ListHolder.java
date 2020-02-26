package com.example.registercall.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.registercall.R;

public class ListHolder extends RecyclerView.ViewHolder {
    public TextView nome;
    public TextView duracao;
    public TextView date;

    public ImageView foto_contato;
    public ImageView action_contato;

    public View lineOption;
    public LinearLayout optionsItem;

    public ListHolder(@NonNull View itemView) {
        super( itemView );
        nome = itemView.findViewById( R.id.name_user_call_text);
        duracao = itemView.findViewById(R.id.duration_call_user_text);
        date = itemView.findViewById(R.id.date_call_user_text);

        foto_contato = itemView.findViewById(R.id.foto_contato);
        action_contato = itemView.findViewById(R.id.action_call);

        lineOption = itemView.findViewById(R.id.lineItem);
        optionsItem = itemView.findViewById(R.id.optionsItem);
    }
}
