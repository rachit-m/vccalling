package com.poziomlabs.plugged;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by guest on 2/7/16.
 */

public class WifiReceiver extends BroadcastReceiver {

    static int wifi_state = 0;
    static SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

    ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo netInfo = conMan.getActiveNetworkInfo();
    if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
        {wifi_state = 1;
        Intent i=new Intent(context, PlayerService.class);

        i.putExtra(PlayerService.EXTRA_PLAYLIST, "main");
        i.putExtra(PlayerService.EXTRA_SHUFFLE, true);

        context.startService(i);

        }
    else
        {   if(wifi_state == 1)
        {
            context.stopService(new Intent(context, PlayerService.class));

        }

            wifi_state = 0;}



    }
    static public void showNotification(Context context, String display_r) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        sharedPreferences = context.getSharedPreferences("splash", Context.MODE_PRIVATE);

        String display = sharedPreferences.getString(display_r, display_r);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(display)
                        .setContentText("Hello World!");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
    public void showToast(Context context)
    {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.d("WifiReceiver", "Have Wifi Connection");
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo ();
            info.getBSSID ();


            Toast.makeText(context,"Connected",Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, MainActivity.class));
            Intent intnt = new Intent(context, MainActivity.class);
            intnt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intnt);



        }
        else {
            Toast.makeText(context,"Disconnected",Toast.LENGTH_LONG).show();

            Log.d("WifiReceiver", "Don't have Wifi Connection");
        }
    }


}