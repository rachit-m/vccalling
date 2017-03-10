package com.labs.poziom.whereabouts;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.name;
import static android.R.attr.theme;
import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static com.labs.poziom.whereabouts.DBStorage.DATABASE_NAME;
import static com.labs.poziom.whereabouts.DBStorage.TABLE_NAME;
import static com.labs.poziom.whereabouts.DBStorage.WIFI_ALIAS;
import static com.labs.poziom.whereabouts.DBStorage.WIFI_SSID;
import static com.labs.poziom.whereabouts.R.id.ssid;
import static java.security.AccessController.getContext;

public class InitTagActivity extends AppCompatActivity {

    private static final String TAG = InitTagActivity.class.getSimpleName();
    DBStorage db = new DBStorage(this); // data storage for storing aliases
    static  String status = "Not Connected";
    static Integer actual_status = 0;
    static String[] status_array = new String[] {  "At the movies", "In a meeting", "Battery about to die" };
    static HashMap<String, String> wfAliasSSid = new HashMap<>();
    NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_tag);
        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_init_tag);
        View inflated = stub.inflate();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(false);


        np = (NumberPicker) findViewById(R.id.numberPicker1);


        np.setMinValue(0);
        np.setMaxValue(3);
        np.setValue(actual_status);
        np.setDisplayedValues( new String[] { status, "At the movies", "In a meeting", "Battery about to die" } );
        np.setWrapSelectorWheel(false);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                actual_status = newVal;
            }
        });

        // to display a the list of configured wifi profiles and their aliases
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            populateList();
        }
        else
            Toast.makeText(getApplication(),"Turn on WIFI",Toast.LENGTH_SHORT).show();

        }


    protected void populateList() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        final List<WifiAliasConf> wifiAliasConfs = new ArrayList<>();
        List<WifiConfiguration> wifiList;
        wifiList = wifiManager.getConfiguredNetworks();
//        HashMap<String, String> wfAliasSSid = new HashMap<>();

        final Cursor cursor = db.getAllProfiles();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(DBStorage.WIFI_COLUMN_ID));
                String ssid = cursor.getString(cursor.getColumnIndexOrThrow(DBStorage.WIFI_SSID));
                String alias = cursor.getString(cursor.getColumnIndexOrThrow(DBStorage.WIFI_ALIAS));
                wfAliasSSid.put(ssid, alias);
            } while (cursor.moveToNext());
        }
        cursor.close();
        for (WifiConfiguration wificonf : wifiList) {
            if (wfAliasSSid.containsKey(wificonf.SSID)) {
                wifiAliasConfs.add(new WifiAliasConf(wificonf, wfAliasSSid.get(wificonf.SSID)));
            } else {
                wifiAliasConfs.add(new WifiAliasConf(wificonf, ""));
            }

        }
        Collections.sort(wifiAliasConfs, new Comparator<WifiAliasConf>() {
            @Override
            public int compare(WifiAliasConf t1, WifiAliasConf t2) {
                return -100*t1.alias.compareToIgnoreCase(t2.alias)+t1.wificonf.SSID.compareToIgnoreCase(t2.wificonf.SSID);
            }

        });
        WifilistAdapter wifiProfiles = new WifilistAdapter(getApplicationContext(), (ArrayList<WifiAliasConf>) wifiAliasConfs);
        final ListView listView = (ListView) findViewById(R.id.configuredWifiList);

        listView.setAdapter(wifiProfiles);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long rowId) {
                final EditText alias = new EditText(InitTagActivity.this);
                alias.setHint("Name your Wifi");
                if(wifiAliasConfs.get(position).alias!=""){
                    alias.setText(wifiAliasConfs.get(position).alias);
                }
                AlertDialog.Builder adb = new AlertDialog.Builder(InitTagActivity.this);
                adb.setTitle("Tag Alias");
                final WifiAliasConf wiobj = (WifiAliasConf) parent.getItemAtPosition(position);
                adb.setMessage(wiobj.wificonf.SSID.replaceAll("^\"|\"$", ""));
                adb.setView(alias);
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = alias.getText().toString();
                        db.insertWifi(wiobj.wificonf.SSID, name);
                        populateList();
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                adb.show();
            }
        });
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }*/
    }

}
