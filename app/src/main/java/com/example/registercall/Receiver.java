package com.example.registercall;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.registercall.MainActivity;
import com.example.registercall.model.ChamadaDAO;
import com.example.registercall.model.ChamadaEntity;
import com.example.registercall.model.RegisterNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Receiver extends BroadcastReceiver {

    private static long inicio = 0;
    private static long fim = 0;

    public static void addInicio() { inicio = Calendar.getInstance().getTimeInMillis(); }

    public static void addFim() { fim = Calendar.getInstance().getTimeInMillis(); }

    public static long durationRingCall() { return (fim-inicio)/1000; }

    private void resetDuration() { inicio = 0; fim = 0; }

    @Override
    public void onReceive(Context context, Intent intent) {
        try{

            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String data_inicio = String.valueOf(Calendar.getInstance().getTime());

            // Verifica se a chamada ja foi finalizada
            if(
                    stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE) &&
                    this.inicio != 0
            ) {
                // Adiciona uma hora de fim da chamada
                addFim();

                // Pega as informacoes do numero de ligou
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                Toast.makeText(context,"A ligação foi finalizada!",Toast.LENGTH_LONG).show();

                // Registra a chamada no banco
                registraChamada(context, number);
                // Abre a nova tela
                abreTela(context,number);
                // Reseta as variaveis estaticas da classe
                resetDuration();
            }
            // Verifica se o usuario atendeu a chamada
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && this.inicio != 0){
                // Adiciona uma hora de fim para a acao
                addFim();

                // Pega as informacoes do numero de ligou
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                // Registra a duração da chamada
                long duration = durationRingCall();

                // Registra a chamada no banco
                registraChamada(context, number);

                // Criar a intent que sera acionada ao clicar na mensagem
                Intent activity = new Intent(context.getApplicationContext(),ShowReceivedCall.class);

                // Adicionar as informacoes que serao envias para a proxima tela
                activity.putExtra("number", number);
                activity.putExtra("duration", durationRingCall() );
                activity.putExtra("status","atendido");

                // Cria a notificação
                new RegisterNotification(context)
                        .notification("Registro de chamada","Uma nova chamada foi registrada", activity);
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

    private void abreTela(Context context, String number) {
        // Criar a intent que sera enviada para a proxima tela
        Intent activity = new Intent(context.getApplicationContext(),ShowReceivedCall.class);

        // Adicionar as informacoes que serao envias para a proxima tela
        activity.putExtra("number", number);
        activity.putExtra("duration", durationRingCall() );

        // Abre a proxima tela
        context.startActivity(activity);
    }

    private void registraChamada(Context context, String number) {
        // Pega a data final da chamada perdida
        String data_fim = String.valueOf(Calendar.getInstance().getTime());

        // Cria a entidade que sera salva no banco de dados
        ChamadaEntity chamadaEntity = new ChamadaEntity();

        // Adiciona as informacoes que serao guardadas no banco de dados
        chamadaEntity.setNumero(number);
        chamadaEntity.setDuracao( (int) durationRingCall() );
        chamadaEntity.setData_inicio( formatDateHour(inicio) );
        chamadaEntity.setData_fim( formatDateHour(fim) );

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
