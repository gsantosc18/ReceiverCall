package com.example.registercall;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.registercall.model.ChamadaDAO;
import com.example.registercall.model.ChamadaEntity;
import com.example.registercall.model.RegisterNotification;
import com.example.registercall.model.Status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Receiver extends BroadcastReceiver {

    private static long inicio = 0;
    private static long fim = 0;

    public static NotificationManager showGravacao = null;

    private final String ATENDIDO = "antendido";
    private final String PERDIDO = "perdido";
    private static String number = "";

    public static void addInicio() { inicio = Calendar.getInstance().getTimeInMillis(); }

    public static void addFim() { fim = Calendar.getInstance().getTimeInMillis(); }

    public static long durationRingCall() { return (fim-inicio)/1000; }

    private void resetDuration() { inicio = 0; fim = 0; }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        // Verifica se existe algum argumento para a chamada
        if(extras!=null){
            // Recuperar o status a ligacao
            String stateStr = extras.getString(TelephonyManager.EXTRA_STATE);
            // Verifica se a chamada esta sendo recebida
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING))
                // Recuperar o numero que esta realizando a ligacao
                number = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        }

        try{

            String stateStr = extras.getString(TelephonyManager.EXTRA_STATE);
            String data_inicio = String.valueOf(Calendar.getInstance().getTime());

            // Verifica se a chamada ja foi finalizada
            if(
                    stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE) &&
                    this.inicio != 0
            ) {
                // Adiciona uma hora de fim da chamada
                addFim();

                // Registra a chamada no banco
                registraChamada(context, number, Status.PERDIDA);
                // Abre a nova tela
                lancaNotificacao(context,number, PERDIDO);
                // Reseta as variaveis estaticas da classe
                resetDuration();
            }
            // Verifica se o usuario atendeu a chamada
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && this.inicio != 0){
                // Adiciona uma hora de fim para a acao
                addFim();

                // Registra a chamada no banco
                registraChamada(context, number, Status.ATENDIDA);

                lancaNotificacao(context,number, ATENDIDO);

                // Reseta as variaveis estaticas da classe
                resetDuration();
            }
            // Verifica se uma nova chamada foi recebida
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                // Registra o inicio da chamada
                addInicio();
            }

        } catch (Exception e){
            Toast.makeText(context,"Houve um erro no App RegisterCall. "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void lancaNotificacao(Context context, String number, String status) {
        // Criar a intent que sera enviada para a proxima tela
        Intent activity = new Intent(context.getApplicationContext(),HistoryActivity.class);

        // Adicionar as informacoes que serao envias para a proxima tela
        new RegisterNotification(context)
                .notification("Registro de chamada","nova(s) chamada foi registrada", activity);
    }

    private void registraChamada(Context context, String number, int status) {
        // Pega a data final da chamada perdida
        String data_fim = String.valueOf(Calendar.getInstance().getTime());

        // Cria a entidade que sera salva no banco de dados
        ChamadaEntity chamadaEntity = new ChamadaEntity();

        // Adiciona as informacoes que serao guardadas no banco de dados
        chamadaEntity.setNumero(number);
        chamadaEntity.setDuracao( (int) durationRingCall() );
        chamadaEntity.setData_inicio( formatDateHour(inicio) );
        chamadaEntity.setData_fim( formatDateHour(fim) );
        chamadaEntity.setStatus(status);

        // Classe responsavel para comunicacao com o banco de dados
        ChamadaDAO chamadaDAO = new ChamadaDAO(context);

        // Adiciona as informacoes no banco de fato
        chamadaDAO.add(chamadaEntity);
    }

    private String formatDateHour(long datetime) {
        Date date = new Date(datetime);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));
        return dateFormat.format(date);
    }

}
