package com.labs.poziom.whereabouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rachit on 3/27/2017.
 */

public class ContactListAdapter extends ArrayAdapter<ContactModel> {
    public ContactListAdapter(Context context, ArrayList<ContactModel> users) {

        super(context, 0, users);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ContactModel contact_par = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wifiprofile, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.ssid);
        TextView tvAlias=(TextView)  convertView.findViewById(R.id.alias);
        // Populate the data into the template view using the data object
        tvName.setText(contact_par.phone.replaceAll("^\"|\"$", ""));
        tvAlias.setText(contact_par.id);
        // Return the completed view to render on screen
        return convertView;
    }
}
