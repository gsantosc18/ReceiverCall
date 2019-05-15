package com.example.registercall.model;

import java.util.Date;

public class LogCall {

    private String name;
    private String number;
    private int duration;
    private String date;
    private int qtd;

    private ChamadaEntity chamada;

    public LogCall(String name,String number, int duration, String date) {
        this.number = number;
        this.duration = duration;
        this.date = date;
        this.name = name;
    }

    public LogCall(String name, String number, int duration, String date, ChamadaEntity chamada) {
        this.number = number;
        this.duration = duration;
        this.date = date;
        this.chamada = chamada;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public ChamadaEntity getChamada() {
        return chamada;
    }

    public void setChamada(ChamadaEntity chamada) {
        this.chamada = chamada;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
