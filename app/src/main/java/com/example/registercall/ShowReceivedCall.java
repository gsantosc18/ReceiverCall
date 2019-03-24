package com.example.registercall;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }
}
