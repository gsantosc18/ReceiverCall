package com.example.registercall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.registercall.MainActivity;

import java.util.Calendar;

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

            if( stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                addFim();

                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                long duration = durationRingCall();

                Intent activity = new Intent(context.getApplicationContext(),ShowReceivedCall.class);

                activity.putExtra("number",number);
                activity.putExtra("duration", duration);

                context.startActivity(activity);
                Toast.makeText(context,"A ligação foi finalizada!",Toast.LENGTH_LONG).show();

                resetDuration();
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                Toast.makeText(context,"A ligação foi atendida!",Toast.LENGTH_LONG).show();
                resetDuration();
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                addInicio();
                Toast.makeText(context,"Uma nova ligação foi recebida.",Toast.LENGTH_LONG).show();
            }

        } catch (Exception e){

        }
    }
}
