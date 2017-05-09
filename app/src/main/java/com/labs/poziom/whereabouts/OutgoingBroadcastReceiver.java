package com.labs.poziom.whereabouts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rachit on 3/20/2017.
 */

public class OutgoingBroadcastReceiver extends BroadcastReceiver {
    private static String sPhoneNumber;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
// If it is to call (outgoing)
            showNotification(context,intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));

            Toast.makeText(context, "Call Outgoing Detected!", Toast.LENGTH_LONG).show();
       /*     Intent i = new Intent(context, OutgoingCallScreenDisplay.class);
            i.putExtras(intent);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/
            WiFiInfo service;
            Call<String> call;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(WiFiInfo.class);
            String spacefreephone = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).replace(" ","");
            call = service.knowWiFi(spacefreephone.substring(spacefreephone.length()-10));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response.body().equals("unknown")) {

                    } else {

                        long min = 1800;
                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                        Date currentLocalTime = cal.getTime();
                        try {
                            Date lastSeenTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.body());
                            min = ((currentLocalTime.getTime() - lastSeenTime.getTime()) / (1000 * 60)) - 330;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //  Date d = new Date(response.body());
                        if (min < 10) {


                        //    showNotification(context,"7506184927");
                            Toast.makeText(context, "Use VC Calling!", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(context, "Do not Use VC Calling!", Toast.LENGTH_LONG).show();

                        }


                    }


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }


            });

        }
    }
    private void showNotification(Context context, String phone) {


        Intent i = new Intent(context, InitTagActivity.class);
        sPhoneNumber = phone;
        //i.putExtra("numberInsert", phone);

        PendingIntent contentIntent = PendingIntent.getActivity(context,1, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.vc_splash1)
                        .setContentTitle("You might add "+phone+" to a list")
                        .setContentText("VC Calling");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());




      /*  Uri uri = Uri.parse("smsto:" + "+91"+ phone);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        //startActivity(i);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,i, 0);
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
        mNotificationManager.notify(1, mBuilder.build());   */

    }

    public static String getPhoneNumber() { return sPhoneNumber; }
}