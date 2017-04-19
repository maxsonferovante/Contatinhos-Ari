package com.android.mferovante.agenda.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mferovante on 02/03/17.
 */

public class Database extends SQLiteOpenHelper {
    private static final String NOMEBANCO = "Agenda";
    private static final int VERSAOBANCO = 2;
    private static final String TABLECONTATO = "CREATE TABLE Contato (id INTEGER PRIMARY KEY, " +
            "nome TEXT, info TEXT, telefone TEXT);";

    public Database(Context context) {
        super(context, NOMEBANCO, null, VERSAOBANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLECONTATO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion>oldVersion){
            Log.i("ENTRANDO NO ONUPGRADE","------------------");
            db.execSQL("CREATE TEMP TABLE t1(id INTEGER PRIMARY KEY, n TEXT,i TEXT,t TEXT)");
            db.execSQL("INSERT INTO t1 SELECT id, nome,email, telefone FROM Contato");
            db.execSQL("DROP TABLE Contato");
            onCreate(db);
            db.execSQL("INSERT INTO Contato SELECT id, n,i,t FROM t1");
            db.execSQL("DROP TABLE t1");
            //db.execSQL("DROP TABLE IF EXISTS Contato;");
            //onCreate(db);
            Log.i("SAIND NO ONUPGRADE","------------------");
        }
    }
}
