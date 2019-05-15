package com.example.registercall;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowReceivedCall extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_received_call);

        Bundle extra =  (Bundle) getIntent().getExtras();

        TextView numberCallReceiver =  (TextView) findViewById(R.id.numberCallReceiver);
        TextView durationCall =  (TextView) findViewById(R.id.durationCall);

        numberCallReceiver.setText( extra.getString("number") );
        durationCall.setText( String.valueOf( extra.getLong("duration") ) );

        String status = extra.getString("status");

        if(status!=null && !status.trim().isEmpty() && status.equalsIgnoreCase("atendido") ) {
            TextView title = (TextView) findViewById(R.id.title_show_text);
            title.setText("Chamada Atendida");
        }

        Button btnHistorico = findViewById(R.id.btnBackHistorico);

        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowReceivedCall.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
