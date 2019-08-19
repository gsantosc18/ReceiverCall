package com.example.registercall;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.registercall.model.GravacaoAdapter;
import com.example.registercall.model.GravacaoDAO;
import com.example.registercall.model.GravacaoEntity;

public class GravacaoActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravacao);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Gravações");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionBar.setDisplayShowTitleEnabled(true);

        final ListView listView = (ListView) findViewById(R.id.listGravacoes);

        GravacaoDAO gravacaoDAO = new GravacaoDAO(this);

        final GravacaoAdapter gravacaoAdapter = new GravacaoAdapter(this, gravacaoDAO.all());

        listView.setAdapter(gravacaoAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();

                if ( checkedCount > 1 )
                    mode.setTitle(checkedCount + " selecionados");
                else mode.setTitle("1 item selecionado");

                gravacaoAdapter.toggleSelect(position);
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
                            for (int posicao : gravacaoAdapter.getSelecteds()) {
                                GravacaoEntity gravacaoEntity = (GravacaoEntity) gravacaoAdapter.getItem(posicao);

                                GravacaoDAO chamadaDAO  = new GravacaoDAO(GravacaoActivity.this);

                                chamadaDAO.remove(gravacaoEntity);

                                gravacaoAdapter.notifyDataSetChanged();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.btnrefresh:
                Toast
                        .makeText(
                                GravacaoActivity.this,
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
            case R.id.btnRecord:
                abrirGravacoes();
                break;
        }

        return true;
    }

    private void abrirAgenda()
    {
        Intent intent = new Intent(Intent.ACTION_DEFAULT, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void abrirDiscagem()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"));
        startActivity(intent);
    }

    private void abrirGravacoes()
    {
        Intent intent = new Intent(GravacaoActivity.this, GravacaoActivity.class);
        startActivity(intent);
    }
}
