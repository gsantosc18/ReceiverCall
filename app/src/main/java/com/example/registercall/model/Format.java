package com.example.registercall.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {

    public String hour(int time, String format)
    {
        Date date = new Date( time );
        return new SimpleDateFormat(format).format(date);
    }

    public String datetime(String datetime, String formatFrom, String formatTo) throws ParseException {

        SimpleDateFormat formato = new SimpleDateFormat(formatFrom);

        Date date = formato.parse( datetime );

        return new SimpleDateFormat(formatTo).format(date);
    }
}
