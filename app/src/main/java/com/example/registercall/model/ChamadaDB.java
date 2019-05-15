package com.example.registercall.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ChamadaDB extends SQLiteOpenHelper{
    private static String TABLE = "registercall";
    private static int VERSION = 1;

    public ChamadaDB(Context ctx){
        super(ctx, TABLE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE chamada (\n" +
                "\tid_chamada integer PRIMARY KEY AUTOINCREMENT,\n" +
                "\tnumero varchar(250),\n" +
                "\tduracao integer,\n" +
                "\tdata_inicio varchar(250),\n" +
                "\tdata_fim varchar(250)\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table chamada");
        onCreate(db);
    }
}
