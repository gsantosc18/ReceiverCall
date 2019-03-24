package com.example.registercall;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registercall.model.LogCall;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.buttonClicavel);
        TextView qtdLigacoes = (TextView) findViewById(R.id.qtdLigacoes);

        qtdLigacoes.setText(String.valueOf(countLigacoes()));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private int countLigacoes() {
        int qtdLigacoes = 0;
        try {

            String permission = Manifest.permission.READ_CALL_LOG;

            if (ActivityCompat.checkSelfPermission(this, permission ) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                        getApplicationContext(),
                        "O aplicativo precisa de algumas permissões para funcionar!",
                        Toast.LENGTH_LONG
                ).show();
                return 0;
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
            qtdLigacoes = managedCursor.getCount();
        } catch (Exception ex) {
            Log.e("Erro Recuperar ligações", ex.getMessage());
        }

        return qtdLigacoes;
    }
}
