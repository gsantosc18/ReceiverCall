package com.example.registercall.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

public class RegisterNotification {
    private NotificationManager notificationManager;
    private Context context;
    private NotificationCompat.Builder builder;
    private static String DEFAULT_CHANNEL_ID = "default_channel";
    private static String DEFAULT_CHANNEL_NAME = "Default";
    private String GROUP_KEY = "com.exemple.registercall";
    private static int COUNT = 0;

    public RegisterNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(this.notificationManager);
        plussCount();
    }

    public void notification(String titulo, String mensagem){
        defaultHeader(titulo,mensagem);
        Notification notification = this.builder.build();
        this.notificationManager.notify(getNotificationId(),notification);
    }

    public void notification(String titulo, String mensagem, Intent intent) {
        defaultHeader(titulo,mensagem);
        setIntent(intent);
        Notification notification = this.builder.build();
        this.notificationManager.notify(getNotificationId(),notification);
    }

    private void defaultHeader(String titulo, String mensagem) {
        this.builder = new NotificationCompat.Builder(context,DEFAULT_CHANNEL_ID)
                .setContentTitle(titulo)
                .setContentText(getMessage())
                .setSmallIcon(android.R.drawable.ic_menu_call)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_KEY);
    }

    private void setIntent(Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        this.builder.setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

    private static void createNotificationChannel(NotificationManager notificationManager) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(
                        DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                ));
            }
        }
    }

    private static int getNotificationId()
    {
        return 1;
    }

    private String getMessage()
    {
        String message = " nova chamada foi registrada!";

        if (COUNT > 1) {
            message = " novas chamadas foram registradas";
        }

        return COUNT+message;
    }

    private void plussCount()
    {
        COUNT++;
    }

    public static void stopCount()
    {
        COUNT = 0;
    }
}
