package com.example.registercall.model;

import android.content.Context;

public class ShowLigacao {

    private Context context;
    private AgroupCall agroupCall;

    public ShowLigacao(Context context) {
        this.context = context;
    }

    public ShowLigacao setAgroupCall(AgroupCall agroupCall)
    {
        this.agroupCall = agroupCall;
        return this;
    }

    public void result() {
        new Dialog(context).ShowDialog(agroupCall.all());
    }
}
