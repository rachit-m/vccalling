package com.labs.poziom.whereabouts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import static android.R.attr.id;
import static android.R.attr.phoneNumber;



/**
 * Created by Rachit on 12/28/2016.
 */

public class ItemCursorAdapter extends CursorAdapter {

    ContentResolver extra1;

    public ItemCursorAdapter(Context context, Cursor cursor, ContentResolver extra) {
        super(context, cursor, 0);
        extra1 = extra;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView tvName = (TextView) view.findViewById(R.id.tvName);
        final TextView tvnumber = (TextView) view.findViewById(R.id.tvNumber);
        TextView tvID = (TextView) view.findViewById(R.id.tvID);

        String name =  cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
    //    String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));

    //   String phoneNumber=cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
    //    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        tvnumber.setText(contactId);
       // if(mimeType == "vnd.android.cursor.item/vnd.com.whatsapp.voip.call")
        tvID.setText(contactId);

        String rawContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts._ID));


        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)

        {

            String[] whereArgs = new String[]{String.valueOf(contactId)};


           Cursor cursor2 = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                    },
                    ContactsContract.CommonDataKinds.Phone._ID + " = ? ", whereArgs, null);

       /*     Cursor cursor2 = context.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.DATA3},
                    ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.Data.RAW_CONTACT_ID + " = ? ",
                    new String[]{"vnd.android.cursor.item/vnd.com.whatsapp.profile", rawContactId},
                    null);*/
            ArrayList<String> phones = new ArrayList<String>();

/*
        Cursor cursor1 = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = " + contactId,
                    null , null);
*/
            while (cursor2.moveToNext()) {
                phones.add(cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                tvnumber.setText(phones.get(0));
             //   tvnumber.setText(contactId);

            }

            cursor2.close();





        }
     /*  Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
       InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), my_contact_Uri);
      Cursor pCur = context.getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId}, null);
        while (pCur.moveToNext()) {
            phone = pCur.getString(
                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        pCur.close();*/
        tvName.setText(name);





    }

}
