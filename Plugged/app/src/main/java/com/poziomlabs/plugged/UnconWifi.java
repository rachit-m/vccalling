package com.poziomlabs.plugged;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;

/**
 * Created by guest on 19/12/16.
 */
public class UnconWifi extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION))
        {
            SupplicantState state = (SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String ssid = wm.getConnectionInfo().getSSID();
            String bssid = wm.getConnectionInfo().getBSSID();

            if(state == SupplicantState.ASSOCIATED)
                WifiReceiver.showNotification(context, ssid.split("\"")[1] );



/*
            switch(state)
            {

                case ASSOCIATED:
                    if(ssid == "Uno_Test")
                    WifiReceiver.showNotification(context);
                    break;
                case COMPLETED:
                case DISCONNECTED:

            }

            */
        }

    }
}
