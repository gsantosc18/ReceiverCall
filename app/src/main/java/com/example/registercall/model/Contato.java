package com.example.registercall.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.BufferedInputStream;
import java.io.InputStream;

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
        String propertie = ContactsContract.PhoneLookup.PHOTO_URI;

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





    /**
     * @return the photo URI
     */
    public Bitmap getPhotoUri(String numero) {
        try {
            Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, Uri.encode(numero));
            InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(this.context.getContentResolver(),my_contact_Uri);
            BufferedInputStream buf = new BufferedInputStream(photo_stream);
            Bitmap my_btmp = BitmapFactory.decodeStream(buf);
            return my_btmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
