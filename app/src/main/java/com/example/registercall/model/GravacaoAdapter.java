package com.example.registercall.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.registercall.R;

import java.util.ArrayList;
import java.util.List;

public class GravacaoAdapter extends BaseAdapter {

    private Context context;
    private List<GravacaoEntity> gravacoes;
    public boolean hidden = true;
    private ArrayList<Integer> ids;

    public GravacaoAdapter(Context context, List<GravacaoEntity> gravacoes) {
        this.context = context;
        this.gravacoes = gravacoes;
        ids = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return gravacoes.size();
    }

    @Override
    public Object getItem(int position) {
        return gravacoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return gravacoes.indexOf( getItem(position) );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GravacaoEntity gravacaoEntity = gravacoes.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.card_log_gravacao,null);

        TextView nameGravacaoText = convertView.findViewById(R.id.name_gravacao_text);
        TextView dateGravacaoText = convertView.findViewById(R.id.date_gravacao_text);

        nameGravacaoText.setText(gravacaoEntity.getNome());
        dateGravacaoText.setText(gravacaoEntity.getData_cadastro());

        return convertView;
    }

    public void toggleVisible()
    {
        hidden = !hidden;
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
