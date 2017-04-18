package com.labs.poziom.whereabouts;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.design.widget.FloatingActionButton;
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
import java.util.Set;

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
    static String[] status_array = new String[] {  "Broker List", "Intern Applications", "Bangalore " };
    static HashMap<String, String> wfAliasSSid = new HashMap<>();
    NumberPicker np;
    WifilistAdapter wifiProfiles;
    List<WifiAliasConf> wifiAliasConfs = new ArrayList<>();
    List<ContactModel> contacts_list = new ArrayList<>();
    static List<String> interim_list = new ArrayList<>();
    ContactListAdapter contactList;

    ListView listView;
    public static final String MyPREFERENCES = "myprefs";
    SharedPreferences prefs;
    SharedPreferences.Editor scoreEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_tag);
        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_init_tag);
        View inflated = stub.inflate();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        scoreEditor = prefs.edit();

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        np = (NumberPicker) findViewById(R.id.numberPicker1);


        np.setMinValue(0);
        np.setMaxValue(3);
        np.setValue(actual_status);
        np.setDisplayedValues( new String[] {  "Broker List", "Intern Applications", "Bangalore " , "Companies" });
        np.setWrapSelectorWheel(false);
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

        Intent intent = getIntent();
        String amber = intent.getStringExtra("numberInsert");

        if(amber != null)
        { contacts_list.add(new ContactModel("Unknown", "" ,amber));
        ((ContactListAdapter)listView.getAdapter()).notifyDataSetChanged();
        interim_list.add(amber);
            Set<String> set = new HashSet<String>();
            set.addAll(interim_list);
            scoreEditor.putStringSet("key", set);
            scoreEditor.commit();

        }

        FloatingActionButton floatButton = (FloatingActionButton) findViewById(R.id.fab2);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                contacts_list.add(new ContactModel("Unknown", "" ,item.getText().toString()));
                ((ContactListAdapter)listView.getAdapter()).notifyDataSetChanged();  */

                Intent showCallLog = new Intent();
                showCallLog.setAction(Intent.ACTION_VIEW);
                showCallLog.setType(CallLog.Calls.CONTENT_TYPE);
                showCallLog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(showCallLog);


            }
        });


    }


    protected void populateList() {


        Set<String> set = prefs.getStringSet("key", null);
        if(set != null) {
            contacts_list.clear();
            for (String prev_added : set) {
                contacts_list.add(new ContactModel("Unknown", "", prev_added));
            }
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifiList;

        //risky attempt to get the shared preferences


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
        wifiProfiles = new WifilistAdapter(getApplicationContext(), (ArrayList<WifiAliasConf>) wifiAliasConfs);
        listView = (ListView) findViewById(R.id.configuredWifiList);
        contactList = new ContactListAdapter(getApplicationContext(), (ArrayList<ContactModel>) contacts_list);

        listView.setAdapter(contactList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long rowId) {
                final EditText alias = new EditText(InitTagActivity.this);
                alias.setHint("Tag this Contact");
                if(wifiAliasConfs.get(position).alias!=""){
                    alias.setText(wifiAliasConfs.get(position).alias);
                }
                AlertDialog.Builder adb = new AlertDialog.Builder(InitTagActivity.this);
                adb.setTitle("Tag Alias");
                final ContactModel wiobj = (ContactModel) parent.getItemAtPosition(position);
                adb.setMessage(wiobj.phone.replaceAll("^\"|\"$", ""));
                adb.setView(alias);
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = alias.getText().toString();
                        db.insertWifi(wiobj.phone, name);
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
