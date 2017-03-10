package com.poziomlabs.plugged;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Browser;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusException;
import org.tinyradius.util.RadiusServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UnconWifi handler = new UnconWifi();
        getApplicationContext().registerReceiver(handler, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));



       // RadiusServer server = new MyRadiusServer();
       // server.start(true, true);
       // server.stop();





    //    WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
  //      List<WifiConfiguration> getConfiguredNetworks = wifiManager.getConfiguredNetworks();
//        Iterator<WifiConfiguration> iter =  getConfiguredNetworks.iterator();

        saveWepConfig("MR.PAWAN", "CE2760F3BF6");
        saveWepConfig("GOPAL", "CE2760F3BF6");
        saveWepConfig("acharya", "CE2760F3BF6");
        saveWepConfig("De", "CE2760F3BF6");
        saveWepConfig("SB VISION 05", "CE2760F3BF6");
        saveWepConfig("Rabin", "CE2760F3BF6");
        saveWepConfig("Gold Fish", "CE2760F3BF6");
        saveWepConfig("Cycle_Count", "CE2760F3BF6");
        saveWepConfig("raviPG", "CE2760F3BF6");
        saveWepConfig("LimeMoose", "CE2760F3BF6");
        saveWepConfig("HIND INOX", "CE2760F3BF6");




        // new RetrieveFeedTask().execute();




    }

    //Is this for saving Wifi configuration WPA2
    void copyWPAConfig(String SSID_char, String passkey, WifiConfiguration ws )
    {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wc = new WifiConfiguration();
        wc = ws;
        wc.SSID = "\"" + SSID_char + "\"";
        wc.preSharedKey = "\"" + passkey + "\"";
        WifiManager  wifiManag = (WifiManager) this.getSystemService(WIFI_SERVICE);
        boolean res1 = wifiManag.setWifiEnabled(true);
        int res = wifi.addNetwork(wc);
        boolean es = wifi.saveConfiguration();
        boolean b = wifi.enableNetwork(res, true);

    }
    public void startPlayer(View v) {
        Intent i=new Intent(this, PlayerService.class);

        i.putExtra(PlayerService.EXTRA_PLAYLIST, "main");
        i.putExtra(PlayerService.EXTRA_SHUFFLE, true);

        startService(i);
    }

    public void stopPlayer(View v) {
        stopService(new Intent(this, PlayerService.class));
    }


    void saveWepConfig(String SSID_char, String passkey )
    {
      /*  WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\""+SSID_char+"\""; //IMP! This should be in Quotes!!
        wc.hiddenSSID = true;
        wc.status = WifiConfiguration.Status.DISABLED;
        wc.priority = 40;
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

        wc.wepKeys[0] = "\"aaabbb1234\""; //This is the WEP Password
        wc.wepTxKeyIndex = 0;
        wc.preSharedKey =  "\"" + passkey + "\"";
        WifiManager  wifiManag = (WifiManager) this.getSystemService(WIFI_SERVICE);
        boolean res1 = wifiManag.setWifiEnabled(true);
        int res = wifi.addNetwork(wc);
        Log.d("WifiPreference", "add Network returned " + res );
        boolean es = wifi.saveConfiguration();
        Log.d("WifiPreference", "saveConfiguration returned " + es );
        boolean b = wifi.enableNetwork(res, true);
        Log.d("WifiPreference", "enableNetwork returned " + b ); */

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\""+SSID_char+"\"";
        conf.preSharedKey = "\"" + passkey + "\"";
        conf.status = WifiConfiguration.Status.ENABLED;

        conf.status = WifiConfiguration.Status.ENABLED;
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);


        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int res = wifi.addNetwork(conf);
        boolean b = wifi.enableNetwork(res, true);
        wifi.reconnect();


    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


