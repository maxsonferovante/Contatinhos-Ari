package com.android.mferovante.agenda.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mferovante on 02/03/17.
 */

public class Database extends SQLiteOpenHelper {
    private static final String NOMEBANCO = "Agenda";
    private static final int VERSAOBANCO = 1;
    private static final String TABLECONTATO = "CREATE TABLE Contato (id INTEGER PRIMARY KEY, " +
            "nome TEXT, email TEXT, telefone TEXT);";

    public Database(Context context) {
        super(context, NOMEBANCO, null, VERSAOBANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLECONTATO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Contato;");
        db.execSQL(TABLECONTATO);
    }
}
