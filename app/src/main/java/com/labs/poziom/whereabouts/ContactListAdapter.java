package com.labs.poziom.whereabouts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import static com.labs.poziom.whereabouts.InitTagActivity.actual_status;
import static com.labs.poziom.whereabouts.InitTagActivity.status_array;
import static com.labs.poziom.whereabouts.InitTagActivity.wfAliasSSid;

/**
 * Created by Rachit on 3/27/2017.
 */

public class ContactListAdapter extends ArrayAdapter<ContactModel> {
    ContactModel contact_par;
    Context mContext;

    public ContactListAdapter(Context context, ArrayList<ContactModel> users) {

        super(context, 0, users);
        mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        contact_par = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wifiprofile, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.ssid);
        final TextView tvAlias=(TextView)  convertView.findViewById(R.id.alias);

        // Populate the data into the template view using the data object
        if(contact_par.getName().equals("Unknown"))
        tvName.setText(contact_par.getPhone().replaceAll("^\"|\"$", ""));
        else
        tvName.setText(contact_par.getName());

        if(wfAliasSSid.get(contact_par.getPhone()) != null && !wfAliasSSid.get(contact_par.getPhone()).equals("Alias"))
        tvAlias.setText(wfAliasSSid.get(contact_par.getPhone()));
        // Return the completed view to render on screen


        final ImageButton adder = (ImageButton) convertView.findViewById(R.id.adder);
        if(contact_par.getComment().equals("Nice")) {
            adder.setVisibility(View.VISIBLE);
            tvAlias.setVisibility(View.GONE);
        }
        adder.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            if(mContext instanceof InitTagActivity){


                final EditText alias = new EditText(getContext());
                final String number = ((TextView)((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                alias.setHint(status_array[actual_status]);
                /*if(!wifiAliasConfs.get(position).alias.equals("")){
                    alias.setText(wifiAliasConfs.get(position).alias);
                }*/
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Name this contact");
                //final ContactModel wiobj = (ContactModel) parent.getItemAtPosition(position);
                adb.setMessage(((TextView)((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString());
             //   alias.setText(((TextView)(((RelativeLayout) view).getChildAt(1))).getText());
                adb.setView(alias);
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = alias.getText().toString();

                        tvAlias.setVisibility(View.VISIBLE);
                        adder.setVisibility(View.GONE);
                        ((InitTagActivity)mContext).addToList(number , name);

                        //       db.insertWifi(wiobj.getPhone(), name);
                  //      populateList();
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                adb.show();

               // tvAlias.setVisibility(View.VISIBLE);
               // adder.setVisibility(View.GONE);
              //  ((InitTagActivity)mContext).addToList(((TextView)((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString());

            }

        }
});
        return convertView;
    }
}
