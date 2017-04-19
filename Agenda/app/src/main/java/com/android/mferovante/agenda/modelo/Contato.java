package com.android.mferovante.agenda.modelo;

import java.io.Serializable;

/**
 * Created by mferovante on 02/03/17.
 */

public class Contato implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String nome;
    private String info;
    private String telefone;

    public Contato() {
        this.id = "0";
    }

    public Contato(String id, String nome, String info, String telefone) {
        this.id = id;
        this.nome = nome;
        this.info = info;
        this.telefone = telefone;
    }

    public Contato(String nome, String info, String telefone) {
        this.nome = nome;
        this.info = info;
        this.telefone = telefone;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String email) {
        this.info = info;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}
