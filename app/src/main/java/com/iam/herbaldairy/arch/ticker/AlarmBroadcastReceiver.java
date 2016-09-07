package com.iam.herbaldairy.arch.ticker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.util.Time;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.Date;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("did")
                .setContentText(new Date(System.currentTimeMillis()).toString())
                .setSmallIcon(R.drawable.mint)
                .build();
        NotificationManagerCompat.from(context).notify(45, notification);

        wl.release();
    }

    public void SetAlarm(Context context)
    {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.finish_dates_sp), Context.MODE_PRIVATE);
        String sDates = preferences.getString(context.getString(R.string.finish_dates), "[]");
        Date date = new Date(0);
        Log.d("date", date + "");
        try {
            JSONArray jDates = new JSONArray(sDates);
            final int datesCount = jDates.length();
            Date[] dates = new Date[datesCount];
            for (int i = 0; i < datesCount; i++) {
                dates[i] = Time.dateFormat.parse(jDates.getString(i));
            }
            if (dates.length > 0) date = Time.earliestOfDates(dates);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        am.set(AlarmManager.RTC, date.getTime(), pi);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
