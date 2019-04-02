package com.example.registercall.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.registercall.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<LogCall> logCalls;

    public CustomAdapter(Context context, List<LogCall> logCalls) {
        this.context = context;
        this.logCalls = logCalls;
    }

    @Override
    public int getCount() {
        return this.logCalls==null?0:logCalls.size();
    }

    @Override
    public Object getItem(int position) {
        return logCalls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return logCalls.indexOf( getItem(position) );
    }

    private String formatDateHour(String datetime) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));
        Date date = dateFormat.parse(datetime);
        return dateFormat.format(date);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LogCall log = this.logCalls.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.card_log_call,null);

        TextView nome = convertView.findViewById(R.id.name_user_call_text);
        TextView duracao = convertView.findViewById(R.id.duration_call_user_text);
        TextView label = convertView.findViewById(R.id.label_call_text);
        TextView date = convertView.findViewById(R.id.date_call_user_text);

        nome.setText(log.getNumber());
        duracao.setText(String.valueOf(log.getDuration())+"s");

        try {
            date.setText( formatDateHour( log.getDate() ) );
        } catch (ParseException e) {
            Log.e("Erro datetime", e.getMessage());
        }

        label.setText(" Toque");

        if(log.getDuration() > 5) {
            label.setText(" Chamada");
            label.setTextColor(Color.GREEN);
        }


        return convertView;
    }
}
