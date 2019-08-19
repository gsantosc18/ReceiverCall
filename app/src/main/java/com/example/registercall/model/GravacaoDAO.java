package com.example.registercall.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GravacaoDAO {
    private SQLiteDatabase db;

    public GravacaoDAO(Context ctx){
        GravacaoDB gravacaoDB = new GravacaoDB(ctx);
        db = gravacaoDB.getWritableDatabase();
    }

    public void add(GravacaoEntity gravacaoEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("arquivo",gravacaoEntity.getArquivo());
        contentValues.put("nome",gravacaoEntity.getNome());
        contentValues.put("data_cadastro",gravacaoEntity.getData_cadastro());
        db.insert("gravacao",null,contentValues);
    }

    public List<GravacaoEntity> all() {
        List<GravacaoEntity> gravacoes = new ArrayList<>();
        String query = "select * from gravacao order by data_cadastro desc";
        Cursor cursor = this.db.rawQuery(query,null);

        if (cursor!=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                GravacaoEntity chamada = new GravacaoEntity(
                        cursor.getInt(cursor.getColumnIndex("id_gravacao")),
                        cursor.getString(cursor.getColumnIndex("arquivo")),
                        cursor.getString(cursor.getColumnIndex("nome")),
                        cursor.getString(cursor.getColumnIndex("data_cadastro"))
                );
                gravacoes.add(chamada);
            } while (cursor.moveToNext());
        }

        return gravacoes;
    }

    public boolean remove(GravacaoEntity gravacaoEntity) {
        try {
            return db.delete("gravacao", "id_gravacao=?", new String[]{String.valueOf(gravacaoEntity.getId_gravacao())}) > 0;
        } catch (Exception ex) {
            return false;
        }
    }
}
