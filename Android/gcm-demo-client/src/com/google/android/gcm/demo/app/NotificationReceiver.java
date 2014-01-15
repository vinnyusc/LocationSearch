package com.google.android.gcm.demo.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

public class NotificationReceiver extends Service {

    /*
     * @Override protected void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * setContentView(R.layout.activity_notification_receiver);
     * 
     * Log.d("NotificationReceiver","started"); }
     */

    private Notification noti;
    public static final String MYACTION = "com.nodovitt.myalerttest.MYACTION";

    /*
     * @Override public void onReceive(Context context, Intent intent) {
     * Log.d("NR","onReceive() called"); }
     */

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("Service", "onCreate()");
    }

    @Override
    public void onDestroy() {
        Log.d("Service", "onDestroy()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String userText = intent.getStringExtra("NOTIFY");

        //for snooze
        Intent intentBack = new Intent("com.nodovitt.myalerttest.ALERTSETTER");
        intentBack.putExtra("NOTIFY", userText+" ".toString());
        //Log.d("ALERT", intentBack.getStringExtra("NOTIFY"));
        intentBack.putExtra("SNOOZE", "snoozed");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intentBack, PendingIntent.FLAG_CANCEL_CURRENT);
        
        //for something else
        Intent somethingElseIntent = new Intent("com.nodovitt.myalerttest.ALERTSETTER");
        somethingElseIntent.putExtra("SNOOZE", "somethingElse");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 2, somethingElseIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        
      //for something else
        Intent somethingElseIntent2 = new Intent("com.nodovitt.myalerttest.ALERTSETTER");
        somethingElseIntent2.putExtra("SNOOZE", "somethingElse");
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this, 3, somethingElseIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
        
        noti = new Notification.Builder(this)
                .setContentTitle("ALERT")
                .setContentText(userText)
                .setSmallIcon(R.drawable.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .addAction(R.drawable.ic_launcher, "S", pendingIntent)
                .addAction(R.drawable.ic_launcher, "A", pendingIntent2)
                .addAction(R.drawable.ic_launcher, "B", pendingIntent3)
                .build();

        
        NotificationManager nM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nM.notify(0, noti);
        stopSelf();
        return START_STICKY;
    }

}
