package com.example.registercall;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.registercall.model.Cronometer;
import com.example.registercall.model.Format;
import com.example.registercall.model.GravacaoDAO;
import com.example.registercall.model.GravacaoEntity;
import com.example.registercall.model.RecorderAudio;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GavacaoActionActivity extends AppCompatActivity {

    private boolean isGravando = false;

    private ImageButton btnGravar;
    private TextView labelGravacao;
    private TextView labelNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gavacao_action);

        btnGravar = (ImageButton) findViewById(R.id.btnGravar);
        labelGravacao = (TextView) findViewById(R.id.statusGravacaoTxt);
        labelNotification = (TextView) findViewById(R.id.labelNotification);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if ( !isGravando ){
                        startGravacao();
                        startCronometer();
                        labelNotification.setText("");
                    }
                    else {
                        stopGravacao();
                        labelNotification.setText("Gravação salva em: "+RecorderAudio.getName());
                    }

                } catch (IOException e) {
                    Log.e("Erro Gravação",e.getMessage());
                } catch (Exception e) {
                    Log.e("Erro Gravação",e.getMessage());
                }
            }
        });
    }

    private void startGravacao() throws IOException {

        RecorderAudio.setName(generateLabel());
        RecorderAudio.start();

        btnGravar.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
        labelGravacao.setText("PARAR");
        isGravando = true;
    }

    private void stopGravacao() throws Exception {
        RecorderAudio.stop();
        registerGravacao();
        btnGravar.setBackgroundResource(R.drawable.ic_play_circle_filled_white_24dp);
        labelGravacao.setText("INICIAR");
        isGravando = false;
    }

    private String generateLabel()
    {
        return (
                new SimpleDateFormat(
                        "yyyyMMddHHmmss"
                )
        ).format(new Date());
    }

    private void startCronometer()
    {
        final TextView timer = (TextView) findViewById(R.id.labelSegundo);
        Cronometer.start();
        ( new Thread() {
            @Override
            public void run() {
                while (true) {
                    if(!isGravando) break;
                    timer.setText(Cronometer.getTime());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void registerGravacao()
    {
        Format format = new Format();
        GravacaoEntity gravacaoEntity = new GravacaoEntity(
                RecorderAudio.getNameAbsolute(),
                RecorderAudio.getName(),
                (
                        new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                ).format(new Date())
        );

        GravacaoDAO gravacaoDAO = new GravacaoDAO( this );

        gravacaoDAO.add( gravacaoEntity );
    }
}
