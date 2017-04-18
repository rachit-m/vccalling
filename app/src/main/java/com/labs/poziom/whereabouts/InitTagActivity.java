package com.labs.poziom.whereabouts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.labs.poziom.whereabouts.OutgoingBroadcastReceiver.getPhoneNumber;

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
    private DatabaseReference mReference;
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
        stub.inflate();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFirebase();

        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        scoreEditor = prefs.edit();

        if(getSupportActionBar() != null)   getSupportActionBar().setDisplayShowTitleEnabled(false);


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

        //Intent intent = getIntent();
        //String phoneNumber = intent.getStringExtra("numberInsert");
        //System.out.println(sPhoneNumber);

        String phoneNumber;
        if((phoneNumber = getPhoneNumber()) != null) {
            ContactModel contactModel = new ContactModel("Unknown", phoneNumber);
            contactModel.setComment("Nice");
            System.out.println(contactModel);
            mReference.child(contactModel.getRecordId()).setValue(contactModel);
            contacts_list.add(contactModel);
            ((ContactListAdapter)listView.getAdapter()).notifyDataSetChanged();
            Set<String> set = new HashSet<>();
            for(ContactModel contact: contacts_list) {
                set.add(new Gson().toJson(contact));
            }
            scoreEditor.putStringSet("key", set);
            scoreEditor.apply();
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
                ContactModel contactModel = new Gson().fromJson(prev_added, ContactModel.class);
                contacts_list.add(contactModel);
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
                if(!wifiAliasConfs.get(position).alias.equals("")){
                    alias.setText(wifiAliasConfs.get(position).alias);
                }
                AlertDialog.Builder adb = new AlertDialog.Builder(InitTagActivity.this);
                adb.setTitle("Tag Alias");
                final ContactModel wiobj = (ContactModel) parent.getItemAtPosition(position);
                adb.setMessage(wiobj.getPhone().replaceAll("^\"|\"$", ""));
                adb.setView(alias);
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = alias.getText().toString();
                        db.insertWifi(wiobj.getPhone(), name);
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

    private void initFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference().child("Lists");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ContactModel contactModel = snapshot.getValue(ContactModel.class);
                        if(!contacts_list.contains(contactModel)) {
                            contacts_list.add(contactModel);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


}
