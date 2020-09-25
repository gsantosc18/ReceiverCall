package com.example.registercall.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private ListRecyclerSelectItem listener;
    public final List<Integer> selectedItems = new ArrayList<>(  );
    private int currentSelectedPos;
    public boolean inSelection = false;
    private Context context;

    public ListRecyclerAdapter(Context context) {
        this.logCallList = logCallList = new ArrayList<>(  );
        this.agroupCall = new ArrayList<>();
        this.context = context;
    }

    public void setListener(ListRecyclerSelectItem listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View card = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.card_log_call, viewGroup, false );
        return new ListHolder( card );
    }

    @Override
    public void onBindViewHolder(@NonNull final ListHolder listHolder, final int i) {
        final AgroupCall agroupCall2 = this.agroupCall.get(i);
        final LogCall log = agroupCall2.all().get( agroupCall2.getCount()-1 );
        String title = log.getName().trim().isEmpty()?log.getNumber():log.getName();
        int size = agroupCall2.getCount();

        if ( size > 1 ) {
            title = title+" ("+size+")";
        }

        listHolder.nome.setText(title);
        listHolder.duracao.setText( log.formatedDuration() );

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


        listHolder.date.setText( log.formatedHour() );

        if( agroupCall2.getSelected() )
        {
            listHolder.wrapp.setActivated( true );
        } else {
            listHolder.wrapp.setActivated( false );
        }

        listHolder.btn_call_number.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callContactNumber( log.getNumber() );
            }
        } );

        listHolder.btn_discage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactNumber( log.getNumber() );
            }
        } );

        listHolder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItems.size() > 0 && listener != null) {
                    listener.onItemClick( i, listHolder );
                }

                if(!inSelection) {
                    new ShowLigacao( context ).setAgroupCall( agroupCall2 ).result();
                }
            }
        } );

        listHolder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if ( listener != null ) {
                    listener.onItemLongClick( i, listHolder );
                }
                return true;
            }
        } );

        if (currentSelectedPos == i) currentSelectedPos = -1;
    }

    @Override
    public int getItemCount() {
        return agroupCall != null ? agroupCall.size() : 0;
    }

    public void add(LogCall logCall) {
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

    public void deleteSelected() {
        Log.e("DeleteSelected","Deletados os itens selecionados");
        for(int i : selectedItems) {
            AgroupCall agroupCall2 = this.agroupCall.get(i);
            List<LogCall> logs = agroupCall2.all();
            for (LogCall logCall : logs) {
                ChamadaEntity chamadaEntity = logCall.getChamada();
                ChamadaDAO chamadaDAO  = new ChamadaDAO(context);
                try {
                    Log.e("DeleteSelected","Removido: "+logCall.getNumber());
                    chamadaDAO.remove( chamadaEntity );
                } catch (Exception e) {
                    Log.e("DeleteSelected ERROR",e.getMessage());
                    Toast.makeText( context, "Houve um erro ao remover os registros", Toast.LENGTH_LONG ).show();
                }
            }
        }
    }

    public void toggleSelection(int position) {
        currentSelectedPos = position;
        inSelection = true;
        int positionSelecteds = getPosition( position );
        if (positionSelecteds != -1) {
            selectedItems.remove(positionSelecteds);
            agroupCall.get( position ).setSelected( false );
        } else {
            selectedItems.add(position);
            agroupCall.get( position ).setSelected( true );
        }
        notifyItemChanged(position);
    }

    public int getPosition(int position) {
        for( int i = 0; i < selectedItems.size(); i++) {
            if( position == selectedItems.get(i) ) return i;
        }
        return -1;
    }

    public void unSelectedItens() {
        for (AgroupCall agroupCall : this.agroupCall) {
            agroupCall.setSelected( false );
        }
        inSelection = false;
    }

    private void dialContactNumber(String number)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData( Uri.parse("tel:"+number));
        context.startActivity(intent);
    }

    private void callContactNumber(String number)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+number));
        context.startActivity(intent);
    }
}
