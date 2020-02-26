package com.example.registercall.model;

import java.util.ArrayList;
import java.util.List;

public class LogCallAgroup {
    private List<LogCall> logCallList;

    public LogCallAgroup(List<LogCall> logCallList) {
        this.logCallList = logCallList;
    }

    public void exec() {
        ArrayList<AgroupCall> agroupCallList = new ArrayList<>();
        AgroupCall agroupCall = null;
        for(LogCall logCall : logCallList ) {
            if ( agroupCall == null ) {
                agroupCall = new AgroupCall( logCall );
                continue;
            }
            if ( agroupCall.getName() == logCall.getNumber() ) {
                agroupCall.addCall( logCall );
            } else {
                agroupCallList.add( agroupCall );
                agroupCall = new AgroupCall( logCall );
            }
        }
    }
}
