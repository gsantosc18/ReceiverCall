package com.example.registercall;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.registercall.model.ChamadaDAO;
import com.example.registercall.model.ChamadaEntity;
import com.example.registercall.model.Contato;
import com.example.registercall.model.CustomAdapter;
import com.example.registercall.model.LogCall;
import com.example.registercall.model.RegisterNotification;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private ListView listView;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        checkPermission();

        RegisterNotification.stopCount();

        showHistorico();

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("PROTOK");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionBar.setDisplayShowTitleEnabled(false);

        notificationManager = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private void checkPermission() {
        boolean canCallPhone = hasPermission(Manifest.permission.CALL_PHONE);
        boolean canReadPhoneState = hasPermission(Manifest.permission.READ_PHONE_STATE);
        boolean canReadContact = hasPermission(Manifest.permission.READ_CONTACTS);
        boolean canReadCallLog = hasPermission(Manifest.permission.READ_CALL_LOG);
        boolean canWriteCallLog = hasPermission(Manifest.permission.WRITE_CALL_LOG);

        if (canCallPhone) {
            requestPermission(Manifest.permission.CALL_PHONE);
        }

        if (canReadPhoneState) {
            requestPermission(Manifest.permission.READ_PHONE_STATE);
        }

        if (canReadContact) {
            requestPermission(Manifest.permission.READ_CONTACTS);
        }

        if (canReadCallLog) {
            requestPermission(Manifest.permission.READ_CONTACTS);
        }

        if (canWriteCallLog) {
            requestPermission(Manifest.permission.READ_CONTACTS);
        }
    }

    private boolean hasPermission(String permission) {
        return ContextCompat
                .checkSelfPermission(
                        HistoryActivity.this,
                        permission ) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission) {
        ActivityCompat.requestPermissions(HistoryActivity.this,
                new String[]{permission},1);
    }

    private void showHistorico() {
        listView = (ListView) findViewById(R.id.listLogCall);

        List<LogCall> logCallList = null;

        try{

            logCallList = listHistoryCalls();

        }catch (Exception ex){
            ex.printStackTrace();
            Log.e("Error ao exibir", ex.getMessage());
        }

        adapter = new CustomAdapter(HistoryActivity.this,logCallList);

        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();

                if ( checkedCount > 1 )
                    mode.setTitle(checkedCount + " selecionados");
                else mode.setTitle("1 item selecionado");

                adapter.toggleSelect(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.deleteItem:
                        if ( listView.getCheckedItemCount() > 0 ) {
                            for (int posicao : adapter.getSelecteds()) {
                                LogCall logCall = (LogCall) adapter.getItem(posicao);

                                ChamadaEntity chamadaEntity = logCall.getChamada();
                                ChamadaDAO chamadaDAO  = new ChamadaDAO(HistoryActivity.this);

                                chamadaDAO.remove(chamadaEntity);

                                adapter.notifyDataSetChanged();
                            }
                            recreate();
                        };
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = parent.getPositionForView(view);

                final LogCall log = (LogCall) listView.getAdapter().getItem(pos);

                PopupMenu popup = new PopupMenu(HistoryActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_item, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.btnDiscar:
                                dialContactNumber(log.getNumber());
                                ; break;
                            case R.id.btnChamar:
                                callContactNumber( log.getNumber() )
                                ; break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.btnrefresh:
                Toast
                    .makeText(
                            HistoryActivity.this,
                            "Lista atualizada!",
                            Toast.LENGTH_SHORT
                    )
                    .show();
                recreate();
                break;
            case R.id.btnAbrirAgenda:
                abrirAgenda();
                break;
            case R.id.btnAbrirDiscagem:
                abrirDiscagem();
                break;
        }

        return true;
    }

    /**
     * @return
     */
    private List<LogCall> listHistoryCalls() {
        List<LogCall> logCallList = new ArrayList<>();
        try {

            // Instancia a comunicacao com o banco de dados
            ChamadaDAO chamadaDAO = new ChamadaDAO(HistoryActivity.this);

            Contato info = new Contato(HistoryActivity.this);

            // Recupera a lista de chamadas registradas no banco
            List<ChamadaEntity> chamadaEntityList = chamadaDAO.all();
            if(chamadaEntityList != null) {
                for (ChamadaEntity chamada : chamadaEntityList) {

                    String name_contact = info.getINomeByNumero( chamada.getNumero() );
                    String foto_contact = info.getImagemByNumero( chamada.getNumero() );

                    LogCall logCall = new LogCall(
                            name_contact,
                            chamada.getNumero(),
                            chamada.getDuracao(),
                            chamada.getData_inicio(),
                            foto_contact,
                            chamada
                    );
                    logCallList.add(logCall);
                }
            }

        } catch (Exception ex) { }

        return logCallList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showHistorico();
        RegisterNotification.stopCount();
        notificationManager.cancelAll();
    }

    /**
     * @param lista
     * @return
     */
    private List<LogCall> agrupa(List<LogCall> lista) {
        List<LogCall> listaAgrupada = new ArrayList<>();

        for (int i = 1; i<lista.size(); i++) {

            String anterior = lista.get(i-1).getNumber();
            String atual = lista.get(i).getNumber();

            if( anterior.equalsIgnoreCase(atual) ) {

                int posicao = listaAgrupada.size() - 1;

                LogCall ultimoLog = listaAgrupada.get( posicao );
                ultimoLog.setQtd( ultimoLog.getQtd() + 1 );

                listaAgrupada.set( posicao, ultimoLog );
            }
        }

        return listaAgrupada;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void dialContactNumber(String number)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    private void callContactNumber(String number)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    private void abrirAgenda() {
        Intent intent = new Intent(Intent.ACTION_DEFAULT, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void abrirDiscagem()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"));
        startActivity(intent);
    }
}
