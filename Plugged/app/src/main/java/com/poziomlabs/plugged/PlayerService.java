package com.poziomlabs.plugged;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Telephony;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PlayerService extends Service {
    public static final String EXTRA_PLAYLIST="EXTRA_PLAYLIST";
    public static final String EXTRA_SHUFFLE="EXTRA_SHUFFLE";
    private boolean isPlaying=false;
    SharedPreferences sharedPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String playlist=intent.getStringExtra(EXTRA_PLAYLIST);
        boolean useShuffle=intent.getBooleanExtra(EXTRA_SHUFFLE, false);

        play(playlist, useShuffle);

        return(START_STICKY);
    }

    @Override
    public void onDestroy() {
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return(null);
    }

    private void play(String playlist, boolean useShuffle) {
        if (!isPlaying) {
            Log.w(getClass().getName(), "Got to play()!");
            isPlaying=true;

            Notification note=new Notification(R.drawable.ic_launcher, "Can you hear the music?", System.currentTimeMillis());
            Intent i=new Intent(this, MainActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pi=PendingIntent.getActivity(this, 0,i, 0);
            note.setLatestEventInfo(this, "Plugged!", "Connected to shared Wifi",pi);
            note.flags|=Notification.FLAG_NO_CLEAR;


            sharedPreferences = getSharedPreferences("splash", Context.MODE_PRIVATE);

            new RetrieveFeedTask().execute(sharedPreferences.getString("phone","notregistered"));


            startForeground(1337, note);
        }
    }

    private void stop() {
        if (isPlaying) {
            Log.w(getClass().getName(), "Got to stop()!");
            isPlaying=false;
            stopForeground(true);
          //sendSMS("7506184927","Test SMS");
/*
            final String myPackageName = getPackageName();
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
                // App is not default.
                // Show the "not currently set as the default SMS app" interface

                Intent intent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        myPackageName);
                startActivity(intent);




            }*/
        }

        }
/*
    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    */

}


class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

    private Exception exception;



    protected Void doInBackground(String... urls) {
     /*   try {

            String host="52.27.54.85";
            String shared="router";

            String user=urls[0];
            String pass="test";
            RadiusClient rc = new RadiusClient(host, shared);

            long l1 = readFile(RX_FILE);
            long l2 = readFile(RX_FILE);
            int i=0;
            int session_time =0;

            while(WifiReceiver.wifi_state>0) {

                AccountingRequest acc;

                if(i==0)
                { acc = new AccountingRequest(user, AccountingRequest.ACCT_STATUS_TYPE_START);
                    acc.addAttribute("Acct-Input-Octets","50");
                    session_time = 0;
                    i++;
                }
                else
                {
                    l2 = readFile(RX_FILE);
                    acc = new AccountingRequest(user, AccountingRequest.ACCT_STATUS_TYPE_INTERIM_UPDATE);
                    session_time = session_time + 10;
                    acc.addAttribute("Acct-Session-Time",String.valueOf(10));
                    acc.addAttribute("Acct-Input-Octets",String.valueOf(l2-l1));
                    l1 =l2;
                }
                //AccessRequest ar = new AccessRequest(user, pass);

                //ar.setAuthProtocol(AccessRequest.AUTH_PAP);
                //ar.addAttribute("NAS-Identifier", "this.is.my.nas-identifier.de");
                //ar.addAttribute("NAS-IP-Address", "192.168.1.3");
                //ar.addAttribute("Service-Type","1000");
                try {

                    rc.account(acc);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RadiusException e) {
                    e.printStackTrace();
                }

            }




                    /*rc.authenticaccate(ar);
                if (response.getPacketType() == RadiusPacket.ACCESS_ACCEPT) {
                    System.out.println("AUTHENTICATED USER");
                }
                else
                {
                    System.out.println("INCORRECT LOGIN ATTEMPT");
                }


        } catch (Exception e) {
            this.exception = e;

        }*/
        return null;
    }

    protected void onPostExecute() {

        //   Log.d("Result", result);


        // TODO: check this.exception
        // TODO: do something with the feed
    }
    private final String RX_FILE = "/sys/class/net/wlan0/statistics/rx_bytes";
    private final String TX_FILE = "/sys/class/net/wlan0/statistics/tx_bytes";

    private long readFile(String fileName){
        File file = new File(fileName);
        BufferedReader br = null;
        long bytes = 0;
        try{
            br = new BufferedReader(new FileReader(file));
            String line = "";
            line = br.readLine();
            bytes = Long.parseLong(line);
        }  catch (Exception e){
            e.printStackTrace();
            return 0;

        } finally{
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return bytes/(1024*1024);
    }
}
