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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListHolder> {
    private List<LogCall> logCallList;
    private List<AgroupCall> agroupCall;

    public ListRecyclerAdapter() {
        this.logCallList = logCallList = new ArrayList<>(  );
        this.agroupCall = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View card = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.card_log_call, viewGroup, false );
        return new ListHolder( card );
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {
        AgroupCall agroupCall2 = this.agroupCall.get(i);
        LogCall log = agroupCall2.all().get( agroupCall2.getCount()-1 );
//        LogCall log = logCallList.get(i);
        Format formatar = new Format();
        String title = log.getName().trim().isEmpty()?log.getNumber():log.getName();
        int size = agroupCall2.getCount();

        Log.e("AgroupCall", agroupCall2.getName());

        if ( size > 1 ) {
            title = title+" ("+size+")";
        }

        listHolder.nome.setText(title);
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
            listHolder.date.setText( log.getDate() );
        } catch (Exception e) {
            Log.e("Erro datetime", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return agroupCall != null ? agroupCall.size() : 0;
    }

    public void add(LogCall logCall)
    {
        AgroupCall agc = new AgroupCall( logCall );

        if ( agroupCall.size() > 0 ) {
            AgroupCall ag = agroupCall.get( agroupCall.size()-1 );
            LogCall logCall1 = ag.all().get( 0 );
            if (
                  ( ag.getName().equals( logCall.getNumber() ) ||
                  ag.getName().equals( logCall.getNumber() ) ) &&
                  logCall.getChamada().getStatus() == logCall1.getChamada().getStatus()
            ) {
                agroupCall.get( agroupCall.size()-1 ).addCall( logCall );
            } else {
                agroupCall.add( agc );
            }
        } else {
            agroupCall.add( agc );
        }

//        Log.e("Agroup Call", agroupCall.size()+"");
//        logCallList.add(logCall);
    }

    private static String formatDateHour(String datetime) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormat.setTimeZone( TimeZone.getTimeZone("GMT-03:00"));
        Date date = dateFormat.parse(datetime);
        return dateFormat.format(date);
    }
}
