package com.example.registercall.model;

public class GravacaoEntity {
    private int id_gravacao;
    private String arquivo;
    private String data_cadastro;

    public GravacaoEntity(int id_gravacao, String arquivo, String data_cadastro) {
        this.id_gravacao = id_gravacao;
        this.arquivo = arquivo;
        this.data_cadastro = data_cadastro;
    }

    public GravacaoEntity(String arquivo, String data_cadastro) {
        this.arquivo = arquivo;
        this.data_cadastro = data_cadastro;
    }

    public int getId_gravacao() {
        return id_gravacao;
    }

    public void setId_gravacao(int id_gravacao) {
        this.id_gravacao = id_gravacao;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        this.data_cadastro = data_cadastro;
    }
}
