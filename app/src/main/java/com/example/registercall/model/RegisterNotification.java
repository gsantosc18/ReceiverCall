package com.example.registercall.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.registercall.R;

import java.util.List;

public class RegisterNotification {
    private NotificationManager notificationManager;
    private Context context;
    private NotificationCompat.Builder builder;
    private static String DEFAULT_CHANNEL_ID = "default_channel";
    private static String DEFAULT_CHANNEL_NAME = "Default";
    private String GROUP_KEY = "com.exemple.protok";
    private static int COUNT = 0;

    public RegisterNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(this.notificationManager);
        plussCount();
    }

    /**
     * @param titulo
     * @param mensagem
     */
    public void notification(String titulo, String mensagem){
        defaultHeader(titulo,mensagem);
        Notification notification = this.builder.build();
        this.notificationManager.notify(getNotificationId(),notification);
    }

    /**
     * @param titulo
     * @param mensagem
     * @param intent
     */
    public void notification(String titulo, String mensagem, Intent intent) {
        defaultHeader(titulo,mensagem);
        setIntent(intent);
        Notification notification = this.builder.build();
        this.notificationManager.notify(getNotificationId(),notification);
        setBadge(this.context, COUNT);
    }

    /**
     * @param titulo
     * @param mensagem
     */
    public void defaultHeader(String titulo, String mensagem) {
        this.builder = new NotificationCompat.Builder(context,DEFAULT_CHANNEL_ID)
                .setContentTitle(titulo)
                .setContentText(getMessage())
                .setSmallIcon(android.R.drawable.ic_menu_call)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_KEY);
    }

    /**
     * @param intent
     */
    public void setIntent(Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        this.builder.setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

    /**
     * @param icon
     * @param titulo
     * @param intent
     */
    public void addAccao( int icon, String titulo, PendingIntent intent){
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(icon, titulo, intent).build();
        this.builder.addAction(action);
    }

    public void launche()
    {
        Notification notification = this.builder.build();
        this.notificationManager.notify(getNotificationId(),notification);
    }

    public void notificationGravacao()
    {
        String titulo = "Teste titulo";
        String mensagem = "Teste de mensagem";
        Intent intent = new Intent();

        this.builder = new NotificationCompat.Builder(context,DEFAULT_CHANNEL_ID)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setSmallIcon(android.R.drawable.ic_menu_call)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_KEY);


        this.setIntent(new Intent());

        this.addAccao(
                R.drawable.ic_phone_white,
                "Gravar",
                PendingIntent.getBroadcast(this.context, 0, intent, 0)
        );

        this.launche();
    }

    /**
     * @param notificationManager
     */
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

    public static void plussCount()
    {
        COUNT++;
    }

    public static void stopCount()
    {
        COUNT = 0;
    }



    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
}
