package com.example.registercall.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.registercall.R;

import java.util.List;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LogCall log = this.logCalls.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.card_log_call,null);

        TextView nome = convertView.findViewById(R.id.name_user_call_text);
        TextView duracao = convertView.findViewById(R.id.duration_call_user_text);

        nome.setText(log.getNumber());
        duracao.setText(String.valueOf(log.getDuration())+"s");

        return convertView;
    }
}
