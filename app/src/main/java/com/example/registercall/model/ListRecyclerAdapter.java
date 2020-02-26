package com.example.registercall.model;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.registercall.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
        LogCall log = this.logCallList.get(i);
        Format formatar = new Format();

        listHolder.nome.setText(log.getName());
        listHolder.duracao.setText( formatar.hour(log.getDuration()*1000,"mm:ss") );

        if (!log.getFoto().trim().isEmpty())
            listHolder.foto_contato.setImageURI( Uri.parse( log.getFoto() ) );

        switch (log.getChamada().getStatus()) {
            case Status.ATENDIDA:
                listHolder.action_contato.setImageResource(R.drawable.ic_call_received_green_24dp);
                break;
            case Status.PERDIDA:
                listHolder.action_contato.setImageResource(R.drawable.ic_call_missed_red_24dp);
                break;
        }

        try {
            listHolder.date.setText( formatDateHour( log.getDate() ) );
        } catch (ParseException e) {
            Log.e("Erro datetime", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return logCallList != null ? logCallList.size() : 0;
    }

    public void add(LogCall logCall)
    {
        logCallList.add(logCall);
    }

    private String formatDateHour(String datetime) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormat.setTimeZone( TimeZone.getTimeZone("GMT-03:00"));
        Date date = dateFormat.parse(datetime);
        return dateFormat.format(date);
    }
}
