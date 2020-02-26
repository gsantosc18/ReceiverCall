package com.example.registercall.model;

import java.util.ArrayList;
import java.util.List;

public class AgroupCall {
    private String name;
    private List<LogCall> listLogCalls;

    public AgroupCall() {
        listLogCalls = new ArrayList<>();
    }

    public AgroupCall(LogCall logCall) {
        this();
        addCall( logCall );
        setName( logCall.getNumber() );
    }

    public int getCount() {
        return listLogCalls.size();
    }

    public void addCall(LogCall logCall) {
        this.listLogCalls.add( logCall );
        if ( name.trim().isEmpty() ) {
            setName( logCall.getNumber() );
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
