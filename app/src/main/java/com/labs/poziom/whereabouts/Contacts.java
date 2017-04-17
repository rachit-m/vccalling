package com.labs.poziom.whereabouts;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Contacts extends AppCompatActivity {
    DbContacts db = new DbContacts(this);
    private AutoCompleteTextView mTxtPhoneNo;
    ContentResolver localContentResolver;
    Cursor contactLookupCursor;
    ContactsAdapter adapter;
    ArrayList<ContactModel> uniques = new ArrayList<ContactModel>();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        SharedPreferences sharedPreferences= getSharedPreferences("phone", Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= 23) {

            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }


        }

        db.insertContact("12351", "You are Plugged!", sharedPreferences.getString("phone","9999999999"));
        createList();
        mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.contactSearch);

        //ListView lv = (ListView) findViewById(R.id.lvContacts);


        localContentResolver = getApplication().getContentResolver();

        contactLookupCursor =
                localContentResolver.query(
                        Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                                Uri.encode("")),
                        new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.RawContacts._ID },
                        null,
                        null,
                        null);
        final ItemCursorAdapter itemCursorAdapter = new ItemCursorAdapter(this, contactLookupCursor, localContentResolver);
        if(uniques.size()<2)
        new updateFriends().execute();


        mTxtPhoneNo.setAdapter(itemCursorAdapter);
       // lv.setAdapter(itemCursorAdapter);

        mTxtPhoneNo.setThreshold(2);


        mTxtPhoneNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             //   itemCursorAdapter.swapCursor(contactLookupCursor);

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {


             //

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        //write db insertion logic here...
                        contactLookupCursor =
                                localContentResolver.query(
                                        Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                                                Uri.encode(editable.toString())),
                                        new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.RawContacts._ID},
                                        null,
                                        null,
                                        null);
                        itemCursorAdapter.swapCursor(contactLookupCursor);
                    }},900);



            }

        });

        mTxtPhoneNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String name = ((TextView) (((LinearLayout) arg1).getChildAt(0))).getText().toString();
                String cid = ((TextView) (((LinearLayout) arg1).getChildAt(1))).getText().toString();
                String phone = ((TextView) (((LinearLayout) arg1).getChildAt(2))).getText().toString();
                db.insertContact(cid, name, phone);
                createList();
                mTxtPhoneNo.setText("");
            }
        });

        FloatingActionButton floatButton = (FloatingActionButton) findViewById(R.id.fab);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), InitTagActivity.class));
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    // Permission Denied
                    Toast.makeText(Contacts.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void createList() {
        ArrayList<ContactModel> contact = new ArrayList<>();
        Cursor cursor = db.getAllContacts();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(DbContacts.CONTACT_ID));
                String contacts = cursor.getString(cursor.getColumnIndexOrThrow(DbContacts.CONTACT_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(DbContacts.CONTACT_NUMBER));

                contact.add(new ContactModel(contacts, id, number));
            } while (cursor.moveToNext());
        }
        cursor.close();
        for (ContactModel element : contact) {
            if (!uniques.contains(element)) {
                uniques.add(element);
            }
        }

        ListView lv = (ListView) findViewById(R.id.lvContacts);
        adapter = new ContactsAdapter(getApplicationContext(), (ArrayList<ContactModel>) uniques);
        lv.setAdapter(adapter);
    }

    private class updateFriends extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            final ArrayList<ContactModel> contact = new ArrayList<>();


            WiFiInfo service;
            Call<String> call;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    final String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    final String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            final String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String spacefreePhone = phoneNo.replace(" ","").replace("-","");
                            service = retrofit.create(WiFiInfo.class);
                            call = service.knowWiFi(spacefreePhone.substring(Math.max(spacefreePhone.length()-10,0)));
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    if (response.body().equals("unknown"))
                                    {



                                    }
                                    else
                                    {
                                        ContactModel goodFriend = new ContactModel(name, id, phoneNo);
                                        if (!uniques.contains(goodFriend) && phoneNo.length()>=10) {
                                            uniques.add(goodFriend);
                                            adapter.notifyDataSetChanged();
                                            db.insertContact(id, name, phoneNo);

                                        }
                                        //    createList();


                                    }


                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }


                        pCur.close();
                    }
                }
            }

            /*

            while (contactLookupCursor.moveToNext()) {
                final String name =  contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                final String contactId = contactLookupCursor.getString(contactLookupCursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(contactLookupCursor.getString(contactLookupCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    String[] whereArgs = new String[]{String.valueOf(contactId)};
                    Cursor cursor2 = getApplicationContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                            },
                            ContactsContract.CommonDataKinds.Phone._ID + " = ? ", whereArgs, null);

                    final ArrayList<String> phones = new ArrayList<String>();

                    while (cursor2.moveToNext()) {
                        phones.add(cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        String spacefreePhone = phones.get(0).replace(" ","").replace("-","");
                        if(spacefreePhone.substring(Math.max(spacefreePhone.length()-10,0)).equals("9836844004") || name.equals("Baba"))
                            Log.d("hello","world");
                        service = retrofit.create(WiFiInfo.class);
                        call = service.knowWiFi(spacefreePhone.substring(Math.max(spacefreePhone.length()-10,0)));
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                                if (response.body().equals("unknown"))
                                {



                                }
                                else
                                {
                                    ContactModel goodFriend = new ContactModel(name, contactId, phones.get(0));
                                    if (!uniques.contains(goodFriend)) {
                                        uniques.add(goodFriend);
                                    }
                                    //    createList();

                                }


                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }

                    cursor2.close();



                }
            }
*/


        return null;
        }
        @Override
        protected void onPostExecute(Void voila) {

        //    createList();

        }


    }


}



























