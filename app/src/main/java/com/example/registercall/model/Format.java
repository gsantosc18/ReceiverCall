package com.example.registercall.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {

    public String hour(int time, String format)
    {
        Date date = new Date( time );
        String hourFormated = new SimpleDateFormat(format).format(date);
        return hourFormated;
    }
}
