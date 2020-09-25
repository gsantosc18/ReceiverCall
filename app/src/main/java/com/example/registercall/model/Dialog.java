package com.example.registercall.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registercall.R;

import java.util.List;

public class Dialog {

    private Context context;

    public Dialog(Context context) {
        this.context = context;
    }

    public void ShowDialog(List<LogCall> logs)
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(context);

        LinearLayout linearLayout = new LinearLayout(context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //add ratingBar to linearLayout
        linearLayout.addView( createViewDetail( logs ) );


//        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("DETALHES");

        //add linearLayout to dailog
        popDialog.setView( linearLayout );



        // Button OK
        popDialog.setNegativeButton("FECHAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = popDialog.create();

        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor( context.getResources().getColor( R.color.backgroundDark_light ) );
            }
        });


        alert.show();

    }

    private View createViewDetail(List<LogCall> logs) {
        View card = LayoutInflater.from(context).inflate( R.layout.card_call_detail, null );
        final ListView lista = card.findViewById( R.id.list_card_datail );

        CardAdapter adapter = new CardAdapter( logs, context );

        lista.setAdapter( adapter );

        return card;
    }


    class CardAdapter extends BaseAdapter {
        private Context context;
        private List<LogCall> logs;

        public CardAdapter(List<LogCall> logs, Context context) {
            this.logs = logs;
            this.context = context;
        }

        @Override
        public int getCount() {
            return logs.size();
        }

        @Override
        public Object getItem(int position) {
            return logs.get( position );
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = convertView.inflate( context, R.layout.item_card_detail, null );
            final LogCall logCall = logs.get(position);

            TextView number = view.findViewById( R.id.number_item );
            TextView date = view.findViewById( R.id.date_item );
            TextView duracao = view.findViewById( R.id.duracao_item );
            final ImageButton button = view.findViewById( R.id.item_option );

            number.setText( logCall.getNumber() );
            date.setText( logCall.formatedHour() );
            duracao.setText( logCall.formatedDuration() );

            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, button);
                    popup.getMenuInflater().inflate(R.menu.menu_item, popup.getMenu());

                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.btnApagarItem:
                                    delete(logCall, position);
                                    ; break;
                            }
                            return true;
                        }
                    });
                }
            } );

            return view;
        }

        private void delete(LogCall logCall, int position) {

            ChamadaEntity chamadaEntity = logCall.getChamada();
            ChamadaDAO chamadaDAO  = new ChamadaDAO(context);
            try {
                Log.e("Dialog","Removido: "+logCall.getNumber());
                chamadaDAO.remove( chamadaEntity );
                logs.remove( position );
                notifyDataSetChanged();
            } catch (Exception e) {
                Log.e("Dialog ERROR",e.getMessage());
                Toast.makeText( context, "Houve um erro ao remover o registro", Toast.LENGTH_LONG ).show();
            }
        }
    }
}
