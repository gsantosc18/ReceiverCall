package com.example.registercall.model;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registercall.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<LogCall> logCalls;

    public boolean hidden = true;

    private ArrayList<Integer> ids;

    public CustomAdapter(Context context, List<LogCall> logCalls) {
        this.context = context;
        this.logCalls = logCalls;
        this.ids = new ArrayList<>();
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

        final LogCall log = this.logCalls.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.card_log_call,null);

        TextView nome = convertView.findViewById(R.id.name_user_call_text);
        TextView duracao = convertView.findViewById(R.id.duration_call_user_text);
        TextView label = convertView.findViewById(R.id.label_call_text);
        TextView date = convertView.findViewById(R.id.date_call_user_text);

        nome.setText(log.getName());
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

    public void toggleVisible()
    {
        hidden = !hidden;
    }

    public void remove(LogCall logCall, Context context)
    {
        ChamadaEntity chamadaEntity = logCall.getChamada();
        ChamadaDAO chamadaDAO = new ChamadaDAO(context);

        try {
            chamadaDAO.remove(chamadaEntity);
        } catch (Exception ex) {
            Toast.makeText(context, "Houve uma falha ao apagar o registro da chamada.", Toast.LENGTH_SHORT)
                 .show();
        }
    }

    public void toggleSelect(int position){
        boolean finded = ids.indexOf( position ) > -1;
        selectView( position, finded );
    }

    public void selectView(int position, boolean selected) {
        if ( !selected ){
            ids.add(position);
        }
        else
            ids.remove( ids.indexOf(position) );
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getSelecteds() {
        return ids;
    }
}
