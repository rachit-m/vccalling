package com.labs.poziom.whereabouts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;

public class Contacts extends AppCompatActivity {
    DbContacts db = new DbContacts(this);
    private AutoCompleteTextView mTxtPhoneNo;
    ContentResolver localContentResolver;
    Cursor contactLookupCursor;

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        SharedPreferences sharedPreferences= getSharedPreferences("phone", Context.MODE_PRIVATE);

        db.insertContact("12351", "You are Plugged!", sharedPreferences.getString("phone","9999999999"));
        createList();
        mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.contactSearch);

        //ListView lv = (ListView) findViewById(R.id.lvContacts);


        localContentResolver = getApplication().getContentResolver();
        contactLookupCursor =
                localContentResolver.query(
                        Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                                Uri.encode("98")),
                        new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.RawContacts._ID },
                        null,
                        null,
                        null);
        final ItemCursorAdapter itemCursorAdapter = new ItemCursorAdapter(this, contactLookupCursor, localContentResolver);


        mTxtPhoneNo.setAdapter(itemCursorAdapter);
       // lv.setAdapter(itemCursorAdapter);

        mTxtPhoneNo.setThreshold(2);

        mTxtPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contactLookupCursor =
                        localContentResolver.query(
                                Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                                        Uri.encode(charSequence.toString())),
                                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.RawContacts._ID},
                                null,
                                null,
                                null);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemCursorAdapter.swapCursor(contactLookupCursor);
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
        ArrayList<ContactModel> uniques = new ArrayList<ContactModel>();
        for (ContactModel element : contact) {
            if (!uniques.contains(element)) {
                uniques.add(element);
            }
        }

        ListView lv = (ListView) findViewById(R.id.lvContacts);
        ContactsAdapter adapter = new ContactsAdapter(getApplicationContext(), (ArrayList<ContactModel>) uniques);
        lv.setAdapter(adapter);
    }


}



























