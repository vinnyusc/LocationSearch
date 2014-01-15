package com.google.android.gcm.demo.app;

import java.net.URL;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class AlertSetterReceiver extends BroadcastReceiver {

    Context c;
    String notificationText = "some default text";
    AsyncTask<Void, Void, Void>snoozeTask;
    @Override
    public void onReceive(Context context, Intent intent) {

        c = context;
        if(intent.getAction().equals("com.nodovitt.myalerttest.ALERTSETTER")){
            notificationText = intent.getStringExtra("NOTIFY");
            
            //Log.d("Extra", intent.getStringExtra("SNOOZE")+"");
            if(intent.getStringExtra("SNOOZE")==null){
                callServiceToSetAlarm();
            }
            else if(intent.getStringExtra("SNOOZE").equals("snoozed")){
                //snooze alarm
                callServiceToSetAlarm();
                Log.d("Extra","snoozed");
                // call new Async task to update your server
                snoozeTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        
                        //URL url = new URL("http://192.168.0.9:8080/my-demo-server/update?&ID=blahblah&messageid=xyz");
                        //url.openConnection();                        
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                       //do nothing
                    }

                };
                snoozeTask.execute(null, null, null);
            }else{
                // notify server that it's not snoozed.
                Log.d("Alert","not snoozed");
            }
            //Log.d("Alert", notificationText+"blah");
            
        }
    }

    public void callServiceToSetAlarm() {
        AlarmManager alarm = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(c,NotificationReceiver.class);
        i.putExtra("NOTIFY", notificationText);

        PendingIntent pendingIntent = PendingIntent.getService(c, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
       
        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 4000,
                pendingIntent);
       //Log.d("Alert","callServiceTosetAlarm()");
    }
}
