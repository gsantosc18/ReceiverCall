package com.example.registercall.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GravacaoDB extends SQLiteOpenHelper {
    private static String TABLE = "gravacao";
    private static int VERSION = 2;

    public GravacaoDB(Context ctx){
        super(ctx, TABLE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE gravacao (\n" +
                "\t`id_gravacao`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`arquivo`\tTEXT,\n" +
                "\t`data_cadastro`\tTEXT\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table gravacao");
        onCreate(db);
    }
}
