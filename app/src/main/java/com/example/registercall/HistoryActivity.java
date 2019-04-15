package com.example.registercall;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.registercall.model.ChamadaDAO;
import com.example.registercall.model.ChamadaEntity;
import com.example.registercall.model.CustomAdapter;
import com.example.registercall.model.LogCall;
import com.example.registercall.model.RegisterNotification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        showHistorico();
    }

    private void showHistorico() {
        ListView listView = (ListView) findViewById(R.id.listLogCall);

        List<LogCall> logCallList = null;

        try{

            logCallList = listHistoryCalls();

        }catch (Exception ex){
            ex.printStackTrace();
            Log.e("Error ao exibir", ex.getMessage());
        }

        CustomAdapter adapter = new CustomAdapter(HistoryActivity.this,logCallList);

        listView.setAdapter(adapter);
    }

    /**
     * @return
     */
    private List<LogCall> listHistoryCalls() {
        List<LogCall> logCallList = new ArrayList<>();
        try {
            // Pega o historico do celular
//            logCallList = getCallDetails();

            // Instancia a comunicacao com o banco de dados
            ChamadaDAO chamadaDAO = new ChamadaDAO(HistoryActivity.this);

            // Recupera a lista de chamadas registradas no banco
            List<ChamadaEntity> chamadaEntityList = chamadaDAO.all();

            if(chamadaEntityList != null) {
                for (ChamadaEntity chamada : chamadaEntityList) {
                    LogCall logCall = new LogCall(
                            chamada.getNumero(),
                            chamada.getDuracao(),
                            chamada.getData_inicio()
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
    }

    /**
     * @return
     */
    private List<LogCall> getCallDetails() {

        List<LogCall> logCallList = new ArrayList<>();

        try {

            String permission = Manifest.permission.READ_CALL_LOG;

            if (ActivityCompat.checkSelfPermission(this, permission ) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                        getApplicationContext(),
                        "O aplicativo precisa de algumas permissões para funcionar!",
                        Toast.LENGTH_LONG
                ).show();
                return null;
            }

            Cursor managedCursor =
                    getContentResolver()
                    .query(
                            CallLog.Calls.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );

            int number =
                    managedCursor
                            .getColumnIndex(
                                    CallLog.Calls.NUMBER
                            );
            int duration = managedCursor.getColumnIndex(
                    CallLog.Calls.DURATION
            );

            int date = managedCursor.getColumnIndex(
                    CallLog.Calls.DATE
            );

            while(managedCursor.moveToNext()) {
                String telefone_numero = managedCursor.getString(number);
                String data_chamada = managedCursor.getString(date);
                int duracao_chamada = managedCursor.getInt(duration);

                LogCall logCall = new LogCall(telefone_numero, duracao_chamada, data_chamada);

                logCallList.add(logCall);
            }
        } catch (Exception ex) {
            Log.e("Erro listagem", ex.getMessage());
        }

        return logCallList;
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
}
