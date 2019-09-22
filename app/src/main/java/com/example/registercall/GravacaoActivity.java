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
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.registercall.model.GravacaoAdapter;
import com.example.registercall.model.GravacaoDAO;
import com.example.registercall.model.GravacaoEntity;
import com.example.registercall.model.RecorderAudio;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int pos = parent.getPositionForView(view);

            final GravacaoEntity gravacao = (GravacaoEntity) listView.getAdapter().getItem(pos);

            PopupMenu popup = new PopupMenu(GravacaoActivity.this, view);
            popup.getMenuInflater().inflate(R.menu.menu_gravacao, popup.getMenu());
            popup.show();

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.btnOuvirGravacao:
                            RecorderAudio.setName( gravacao.getNome() );
                            String path = RecorderAudio.getNameAbsolute();

                            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(path);
                            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                            Log.e("Audio",path+" tipo: "+mimetype+" extensao: "+extension);

                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(path), mimetype);
                            startActivity(intent);
                    }
                    return true;
                }
            });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent); break;
        }

        return true;
    }
}
