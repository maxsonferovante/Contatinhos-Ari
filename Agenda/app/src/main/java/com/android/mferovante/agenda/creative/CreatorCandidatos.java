package com.android.mferovante.agenda.creative;

import android.content.Context;

import com.android.mferovante.agenda.database.ContatoDatabase;
import com.android.mferovante.agenda.database.Database;
import com.android.mferovante.agenda.modelo.Contato;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

/**
 * Created by mferovante on 24/05/17.
 */

public class CreatorCandidatos {
    private ContatoDatabase contatoDatabas;
    private List<Contato> contatoList = null;

    public CreatorCandidatos(Context context){
        contatoDatabas = new ContatoDatabase(context);
        contatoList = new ArrayList<>();
    }

    public void creation(int quant) {
        long quantItem = contatoDatabas.quantItem();
        if (quantItem < quant)
        {
            Random random = new Random();
            for (int i = 0; i< quant - quantItem; i++){
                contatoList.add(
                        new Contato(
                                Integer.toString(random.nextInt())+"-Nome ",
                                Integer.toString(random.nextInt())+"-Info",
                                Integer.toString(random.nextInt())+"-Telefone",
                                random.nextInt()%2)
                );
            }
            contatoDatabas.insertList(contatoList);
        }
    }
}
