package com.example.registercall.model;

import java.util.Date;

public class LogCall {
    private String number;
    private int duration;
    private String date;
    private int qtd;

    public LogCall(String number, int duration, String date) {
        this.number = number;
        this.duration = duration;
        this.date = date;
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
}
