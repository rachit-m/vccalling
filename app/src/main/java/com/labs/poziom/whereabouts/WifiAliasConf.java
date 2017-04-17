package com.labs.poziom.whereabouts;

import android.net.wifi.WifiConfiguration;

/**
 * Created by Sandy on 11-01-2017.
 */

public class WifiAliasConf {
        WifiConfiguration wificonf;

        String alias;
        WifiAliasConf(WifiConfiguration wifiProf,String name){
            this.wificonf=wifiProf;
            this.alias=name;
        }
}
