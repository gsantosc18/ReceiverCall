package com.example.registercall.model;

import java.util.Calendar;

public class Cronometer {
    private static long time;
    private static boolean status;
    private static long inicio;

    public static void start()
    {
        inicio = Calendar.getInstance().getTimeInMillis();
    }

    public static long now()
    {
        long fim = Calendar.getInstance().getTimeInMillis();

        return fim-inicio;
    }

    public static String getTime()
    {
        Format formatar = new Format();

        return formatar.hour((int) now(),"mm:ss");
    }
}
