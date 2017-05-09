package com.labs.poziom.whereabouts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
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
    static Integer total_lists = 1;
    static String[] status_array;
    static HashMap<String, String> wfAliasSSid = new HashMap<>();
    NumberPicker np;
    WifilistAdapter wifiProfiles;
    List<WifiAliasConf> wifiAliasConfs = new ArrayList<>();
    static List<ContactModel> contacts_list = new ArrayList<>();
    static ContactModel interim;

    private DatabaseReference mReference;
    ContactListAdapter contactList;


    ListView listView;
    public static final String MyPREFERENCES = "myprefs";
    SharedPreferences prefs;
    SharedPreferences.Editor scoreEditor;
    private static final int RESULT_PICK_CONTACT = 850;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_tag);
        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_init_tag);
        stub.inflate();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   initFirebase();

        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        scoreEditor = prefs.edit();
   //      prefs.edit().remove("lists").commit();

        Set<String> listnames = prefs.getStringSet("lists", null);



        if(getSupportActionBar() != null)   getSupportActionBar().setDisplayShowTitleEnabled(false);


        np = (NumberPicker) findViewById(R.id.numberPicker1);


        np.setMinValue(0);
        if(listnames != null)
        {   //status_array = status_array + listnames.toArray(new String[listnames.size()]);
         //   listnames.add("New");
           status_array = listnames.toArray(new String[listnames.size()]);
            total_lists = status_array.length;
           status_array = append(status_array,"New");
            status_array[total_lists-1] = status_array[total_lists];
            status_array[total_lists] = "New";
            //listnames does not contain new element

          // status_array[total_lists] = "New";

           // System.arraycopy(status_array, 0, bigArray, 0, status_array.length);
          //  System.arraycopy(listnames.toArray(new String[listnames.size()]), 0, bigArray, status_array.length, listnames.size());

         //   status_array = bigArray;
/*
            np.setMaxValue(status_array.length-1);
            np.setValue(actual_status);

            np.setDisplayedValues( bigArray );*/


        }
        else
        {

            status_array = new String[] {  "Default", "New" };
            total_lists = 1;
        }

        np.setMaxValue(total_lists);
        np.setValue(actual_status);
        np.setDisplayedValues(status_array);

        np.setWrapSelectorWheel(false);
        np.setWrapSelectorWheel(false);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if(newVal == total_lists)
                {
                    final EditText newlist = new EditText(InitTagActivity.this);
                    AlertDialog.Builder adb = new AlertDialog.Builder(InitTagActivity.this);
                    adb.setTitle("Create a new list");
                    adb.setView(newlist);
                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String name = newlist.getText().toString();

                         //   status_array[total_lists+1] = name;
                            status_array = append(status_array, name);
                            total_lists++;
                            np.setDisplayedValues(null);
                            np.setMaxValue(total_lists);

                            np.setDisplayedValues(status_array);
                            scoreEditor.putStringSet("lists", new HashSet<String>(Arrays.asList(Arrays.copyOfRange(status_array, 0, total_lists))));
                            scoreEditor.apply();


                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    adb.show();

                }
                // TODO Auto-generated method stub
                else {
                    actual_status = newVal;
                    populateList();
                    if (interim != null && interim.getComment().equals("Nice") && !contacts_list.contains(interim))
                        contacts_list.add(interim);
                    ((ContactListAdapter) listView.getAdapter()).notifyDataSetChanged();
                }

            }
        });




        // to display a the list of configured wifi profiles and their aliases
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

       // if (mWifi.isConnected()) {
            populateList();
       // }
       // else
       //     Toast.makeText(getApplication(),"Turn on WIFI",Toast.LENGTH_SHORT).show();

        //Intent intent = getIntent();
        //String phoneNumber = intent.getStringExtra("numberInsert");
        //System.out.println(sPhoneNumber);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String phoneNumber;
       // if(clipboard.getText()!= null && (phoneNumber = clipboard.getText().toString()) != null) {
         if((phoneNumber = getPhoneNumber()) != null || (clipboard.getText()!= null && clipboard.getText().toString().replaceAll("[^\\dA-Za-z ]", "").replace(" ","").matches("-?\\d+(\\.\\d+)?") && (phoneNumber = clipboard.getText().toString()) != null) )
         { ContactModel contactModel = new ContactModel("Unknown", phoneNumber);
            contactModel.setComment("Nice");
            System.out.println(contactModel);
             if(!contacts_list.contains(contactModel))
            contacts_list.add(contactModel);
            ((ContactListAdapter)listView.getAdapter()).notifyDataSetChanged();
            interim = contactModel;
/*
            mReference.child(ContactModel.generateRecordId()).setValue(contactModel);
            contacts_list.add(contactModel);
            ((ContactListAdapter)listView.getAdapter()).notifyDataSetChanged();
            Set<String> set = new HashSet<>();
            for(ContactModel contact: contacts_list) {
                set.add(new Gson().toJson(contact));
            }
            scoreEditor.putStringSet("key", set);
            scoreEditor.apply();*/
        }

        FloatingActionButton floatButton = (FloatingActionButton) findViewById(R.id.fab2);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 /*   ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                contacts_list.add(new ContactModel("Unknown", "" ,item.getText().toString()));
                ((ContactListAdapter)listView.getAdapter()).notifyDataSetChanged();  */

                Intent myIntent = new Intent(InitTagActivity.this, Contacts.class);
                myIntent.putExtra("key", "value");
                InitTagActivity.this.startActivity(myIntent);

                //Contact picker
                /*
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);

                */


              /*  Intent showCallLog = new Intent();
                showCallLog.setAction(Intent.ACTION_VIEW);
                showCallLog.setType(CallLog.Calls.CONTENT_TYPE);
                showCallLog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(showCallLog);  */


            }
        });


    }

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N+1);
        arr[N] = arr[N-1];
        arr[N-1] = element;
        return arr;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
       //     int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
        //    int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
           //     phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


            ContactModel contactModel = new ContactModel(name, phoneNo);
            contactModel.setComment("Nice");
            System.out.println(contactModel);
            contacts_list.add(contactModel);
            ((ContactListAdapter)listView.getAdapter()).notifyDataSetChanged();
            interim = contactModel;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToList(String phoneNumber, String name)
    {
        interim.setComment("Added");
        ContactModel contactModel = new ContactModel(name, phoneNumber);
        contactModel.setComment("Added");
        contactModel.setId(status_array[actual_status]);
        System.out.println(contactModel);
        mReference.child(ContactModel.generateRecordId()).setValue(contactModel);

        Set<String> set = new HashSet<>();
        for(ContactModel contact: contacts_list) {
            if(contact.getComment().equals("Added"))
            set.add(new Gson().toJson(contact));
        }
        set.add(new Gson().toJson(contactModel));     //ContactModel is like a employee, Contact list is like a manager, Shared preference is like a COO, Db is like CEO & Firebase is like a VC
        scoreEditor.putStringSet("key", set);  //Contact is associated with comment, list, and users. Create provision for these three.
        scoreEditor.apply();
    }


    protected void populateList() {

        Set<String> set = prefs.getStringSet("key", null);
        if(set != null) {
            contacts_list.clear();
            for (String prev_added : set) {
                ContactModel contactModel = new Gson().fromJson(prev_added, ContactModel.class);
                if(!contacts_list.contains(contactModel) && (contactModel.getId().equals(status_array[actual_status])))    contacts_list.add(contactModel);
            }
        }

        initFirebase();

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
        /*   //This is about sorting the list wrt comments
        for (ContactModel contactModel : contacts_list) {
            if (wfAliasSSid.containsKey(contactModel.getPhone())) {
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
        */
        listView = (ListView) findViewById(R.id.configuredWifiList);


        contactList = new ContactListAdapter(this , (ArrayList<ContactModel>) contacts_list);

        listView.setAdapter(contactList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long rowId) {
                final EditText alias = new EditText(InitTagActivity.this);
                alias.setHint("Tag this Contact");
                /*if(!wifiAliasConfs.get(position).alias.equals("")){
                    alias.setText(wifiAliasConfs.get(position).alias);
                }*/
                AlertDialog.Builder adb = new AlertDialog.Builder(InitTagActivity.this);
                adb.setTitle("Tag Alias");
                final ContactModel wiobj = (ContactModel) parent.getItemAtPosition(position);
                adb.setMessage(wiobj.getPhone().replaceAll("^\"|\"$", ""));
                alias.setText(((TextView)(((RelativeLayout) view).getChildAt(1))).getText());
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
                        if(!contacts_list.contains(contactModel) && contactModel.getId().equals(status_array[actual_status])) {
                            contacts_list.add(contactModel);
                            ((ContactListAdapter)listView.getAdapter()).notifyDataSetChanged();
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
