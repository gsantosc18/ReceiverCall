package com.example.registercall.model;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.IOException;

public class RecorderAudio {
    public static final String PREFIX = "pro_audio";
    public static final String EXTENSION = "3gp";

    private static String name;

    private static MediaRecorder mediaRecorder = null;

    public static void start() throws IOException {
            if (mediaRecorder == null) {
                config();
            }
            mediaRecorder.setOutputFile(
                    getNameAbsolute()
            );
            mediaRecorder.prepare();
            mediaRecorder.start();
    }

    public static void stop() throws Exception {
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder = null;
        }
        else
            throw new Exception("O Gravador de audio não está ativo.");
    }

    public static void setName(String date)
    {
        name = PREFIX+"_"+date+"."+EXTENSION;
    }

    public static String getName()
    {
        return name;
    }

    public static String getNameAbsolute()
    {
        return pathAbsolute()+"/"+getName();
    }

    private static void config() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
    }

    private static String pathAbsolute()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
