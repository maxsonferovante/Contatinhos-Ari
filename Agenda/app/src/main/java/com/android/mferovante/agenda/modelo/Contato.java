package com.android.mferovante.agenda.modelo;

import java.io.Serializable;

/**
 * Created by mferovante on 02/03/17.
 */

public class Contato implements Serializable {
    public Contato(String id, String nome, String info, String telefone, int matriculado) {
        this.id = id;
        this.nome = nome;
        this.info = info;
        this.telefone = telefone;
        this.matriculado = matriculado;
    }

    private String id;
    private String nome;
    private String info;
    private String telefone;
    private int matriculado;

    public Contato(String s, String s1, String s2, int i) {
        this.nome = s;
        this.info = s1;
        this.telefone = s2;
        this.matriculado = i;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getMatriculado() {
        return matriculado;
    }

    public void setMatriculado(int matriculado) {
        this.matriculado = matriculado;
    }

    public Contato() {
        this.id = "0";
        this.matriculado = 0;
    }



}
