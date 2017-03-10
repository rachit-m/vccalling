package com.labs.poziom.whereabouts;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

/**
 * Created by Sandy on 12-01-2017.
 */

public class ServiceExample  extends Service {

    private BroadcastReceiver mReceiver;
           onWifiReceiver wfrcvr= new onWifiReceiver();
    @Override
    public IBinder onBind(Intent arg0) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

    //    mReceiver = new onWifiReceiver();
        registerReceiver(wfrcvr, filter);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //handleStart(intent, startId);
       // sendToServer();
        getApplicationContext().registerReceiver(wfrcvr, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
        Log.d("Service", "FirstService started");
        return Service.START_STICKY;
    }

    public void sendToServer(){
        Thread triggerService = new Thread(new Runnable(){
            public void run(){
                try{
                    Looper.prepare();//Initialise the current thread as a looper.

                    Log.d("Wifi_SERVICE", "Service RUNNING!");
                    Looper.loop();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }, "WifiService");
        triggerService.start();
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // Unregister the receiver when unbinding the service
        unregisterReceiver(wfrcvr);
        wfrcvr = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "FirstService destroyed");
    }



}
