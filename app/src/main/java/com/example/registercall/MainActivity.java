package com.example.registercall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.registercall.model.ChamadaDAO;

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

    @Override
    protected void onResume() {
        super.onResume();

        TextView qtdLigacoes = (TextView) findViewById(R.id.qtdLigacoes);
        qtdLigacoes.setText(String.valueOf(countLigacoes()));
    }

    private int countLigacoes() {
        // Instancia a comunicacao com o banco de dados
        ChamadaDAO chamadaDAO = new ChamadaDAO(MainActivity.this);
        // Recupera a lista de chamadas registradas no banco
        return chamadaDAO.all().size();
    }
}
