package com.example.registercall.model;

import java.sql.Date;

public class ChamadaEntity {
    private int id_chamada;
    private String numero;
    private int duracao;
    private String data_inicio;
    private String data_fim;

    private int status;


    public ChamadaEntity() {}

    public ChamadaEntity(int id_chamada, String numero, int duracao, String data_inicio,String data_fim, int status) {
        setId_chamada(id_chamada);
        setNumero(numero);
        setDuracao(duracao);
        setData_inicio(data_inicio);
        setData_fim(data_fim);
        setStatus(status);
    }

    /**
     * @return
     */
    public String getData_fim() {
        return data_fim;
    }

    /**
     * @param data_fim
     */
    public void setData_fim(String data_fim) {
        this.data_fim = data_fim;
    }

    /**
     * @return
     */
    public int getId_chamada() {
        return id_chamada;
    }

    /**
     * @param id_chamada
     */
    public void setId_chamada(int id_chamada) {
        this.id_chamada = id_chamada;
    }

    /**
     * @return
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return
     */
    public int getDuracao() {
        return duracao;
    }

    /**
     * @param duracao
     */
    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    /**
     * @return
     */
    public String getData_inicio() {
        return data_inicio;
    }

    /**
     * @param data_inicio
     */
    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    /**
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
