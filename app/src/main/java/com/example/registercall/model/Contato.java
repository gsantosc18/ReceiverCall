package com.example.registercall.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class Contato {

    private Context context;

    public Contato(Context context)
    {
        this.context = context;
    }

    public String getINomeByNumero(String numero)
    {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(numero));
        String propertie = ContactsContract.PhoneLookup.DISPLAY_NAME;

        String nome = getPropertie(propertie,uri);

        if( nome != null && !nome.trim().equals("") )
            return nome;

        return numero;
    }

    public String getImagemByNumero(String numero)
    {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(numero));
        String propertie = ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI;

        String imagem = getPropertie(propertie,uri);

        if( imagem != null )
            return imagem;

        return "";
    }

    private String getPropertie(String propertie, Uri uri) {
        String[] projection = new String[]{ propertie };
        String response = null;
        Cursor cursor = null;

        cursor = this.context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                response = cursor.getString(0);
            }
            cursor.close();
        }

        return response;
    }

}
