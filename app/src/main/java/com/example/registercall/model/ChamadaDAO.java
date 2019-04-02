package com.example.registercall.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChamadaDAO {
    private SQLiteDatabase db;

    public ChamadaDAO(Context ctx){
        ChamadaDB chamadaDB = new ChamadaDB(ctx);
        db = chamadaDB.getWritableDatabase();
    }

    public void add(ChamadaEntity chamadaEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("numero",chamadaEntity.getNumero());
        contentValues.put("duracao",chamadaEntity.getDuracao());
        contentValues.put("data_inicio",chamadaEntity.getData_inicio());
        contentValues.put("data_fim",chamadaEntity.getData_inicio());
        db.insert("chamada",null,contentValues);
    }

    public List<ChamadaEntity> all() {
        List<ChamadaEntity> chamadas = new ArrayList<>();
        String query = "select * from chamada order by data_inicio desc";
        Cursor cursor = this.db.rawQuery(query,null);

        if (cursor!=null && cursor.getCount() > 0) {
           cursor.moveToFirst();
           do {
               ChamadaEntity chamada = new ChamadaEntity(
                       cursor.getInt(cursor.getColumnIndex("id_chamada")),
                       cursor.getString(cursor.getColumnIndex("numero")),
                       cursor.getInt(cursor.getColumnIndex("duracao")),
                       cursor.getString(cursor.getColumnIndex("data_inicio")),
                       cursor.getString(cursor.getColumnIndex("data_fim"))
               );
               chamadas.add(chamada);
           } while (cursor.moveToNext());
        }

        return chamadas;
    }
}