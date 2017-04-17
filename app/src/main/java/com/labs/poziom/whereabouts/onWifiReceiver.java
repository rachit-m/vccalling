package com.labs.poziom.whereabouts;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Converter.Factory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Url;

import static android.R.attr.password;
import static android.content.ContentValues.TAG;
import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static com.labs.poziom.whereabouts.InitTagActivity.actual_status;
import static com.labs.poziom.whereabouts.InitTagActivity.status;
import static com.labs.poziom.whereabouts.InitTagActivity.status_array;
import static com.labs.poziom.whereabouts.InitTagActivity.wfAliasSSid;

/**
 * Created by Sandy on 13-01-2017.
 */

public class onWifiReceiver extends BroadcastReceiver {
    String SERVER_URL = "http://52.27.54.85/blog/rad_app";

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      //  status = "Not on WiFi";
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            /*WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            Log.d("SSID", ssid);*/
            JobScheduler js = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                JobInfo job = new JobInfo.Builder(3 , new ComponentName(context, MyJobService.class))
                        .setPeriodic(2*10000)
                        .build();
                js.schedule(job);
               // showNotification(context);
            }
            else {

                AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent in = new Intent(context, onWifiReceiver.class);
          //      in.putExtra(ONE_TIME, Boolean.TRUE);
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 5), pi);


                SharedPreferences sharedPreferences= context.getSharedPreferences("phone",Context.MODE_PRIVATE);

                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();



             /*   DBStorage db = new DBStorage(context);
                HashMap<String, String> wfAliasSSid = new HashMap<>();

                final Cursor cursor = db.getAllProfiles();
                if (cursor.moveToFirst()) {
                    do {
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(DBStorage.WIFI_COLUMN_ID));
                        String ssid1 = cursor.getString(cursor.getColumnIndexOrThrow(DBStorage.WIFI_SSID));
                        String alias = cursor.getString(cursor.getColumnIndexOrThrow(DBStorage.WIFI_ALIAS));
                        wfAliasSSid.put(ssid1, alias);
                    } while (cursor.moveToNext());
                }
                cursor.close();  */
/*
                if(actual_status > 0)
                    ssid = status_array[actual_status-1];
                else {

                if (wfAliasSSid.containsKey(ssid)) {
                        ssid = wfAliasSSid.get(ssid);
                    }


                    status = "Connected to " + ssid;
//All this logic for knowing the WiFi one is connected to
                }*/


                String bssid = wifiInfo.getBSSID();
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<String> call = apiService.createTask(sharedPreferences.getString("phone","9999999999"), ssid, bssid, "redmi");
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(onWifiReceiver.class.getSimpleName(), response.body() + " onresponse");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("failed", t.toString());
                    }
                });

            }


        }
       /* else
        {
            actual_status = 0;
            status = "Not Connected";
        }
        */
    }

    private void showNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, Contacts.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.vc_splash1)
                        .setContentTitle("You might call contacts who are online")
                        .setContentText("");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}




