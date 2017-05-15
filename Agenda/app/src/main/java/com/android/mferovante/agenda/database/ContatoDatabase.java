package com.android.mferovante.agenda.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.mferovante.agenda.modelo.Contato;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mferovante on 02/03/17.
 */

public class ContatoDatabase {
    Context context;
    Database database;

    private static final String NOMETABELA = "Contato";

    public ContatoDatabase(Context context) {
        this.context = context;
    }

    public long insere(Contato contato) {
        database = new Database(context);

        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues dados = pegaDadosDoContato(contato);

        long inserir = db.insert(NOMETABELA, null, dados);

        db.close();

        Log.i(NOMETABELA, inserir + "");
        return inserir;
    }

    public List<Contato> buscaContatos(int matriculado) {
        Cursor cursor;
        SQLiteDatabase db;
        String sql = "SELECT * FROM " + NOMETABELA +" WHERE MATRICULADO == "+ Integer.toString(matriculado);
        List<Contato> contatos = new ArrayList<Contato>();

        database = new Database(context);
        db = database.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            contatos.add(
                    new Contato(
                            Integer.toString(cursor.getInt(cursor.getColumnIndex("id"))),
                            cursor.getString(cursor.getColumnIndex("nome")),
                            cursor.getString(cursor.getColumnIndex("info")),
                            cursor.getString(cursor.getColumnIndex("telefone")),
                            cursor.getInt(cursor.getColumnIndex("matriculado"))
                    )
            );
        }
        cursor.close();
        db.close();

        return contatos;
    }

    public void alterar(Contato contato) {
        database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        ContentValues dados = pegaDadosDoContato(contato);

        String[] params = {String.valueOf(contato.getId())};
        db.update("Contato", dados, "id=?", params);

    }

    public void deletar(Contato contato) {
        database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();
        String whereClause = "id=?";
        String[] whereArgs = new String[]{String.valueOf(contato.getId())};
        db.delete(NOMETABELA, whereClause, whereArgs);
        db.close();
    }

    private ContentValues pegaDadosDoContato(Contato contato) {
        ContentValues dados = new ContentValues();
        dados.put("nome", contato.getNome());
        dados.put("info", contato.getInfo());
        dados.put("telefone", contato.getTelefone());
        dados.put("matriculado", contato.getMatriculado());
        return dados;
    }
}
