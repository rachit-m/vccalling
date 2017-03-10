package com.labs.poziom.whereabouts;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.labs.poziom.whereabouts.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Sandy on 09-01-2017.
 */

public class WifilistAdapter extends ArrayAdapter  <WifiAliasConf>{
    public WifilistAdapter(Context context, ArrayList<WifiAliasConf> users) {

        super(context, 0, users);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WifiAliasConf wificonfig = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wifiprofile, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.ssid);
        TextView tvAlias=(TextView)  convertView.findViewById(R.id.alias);
        // Populate the data into the template view using the data object
        tvName.setText(wificonfig.wificonf.SSID.replaceAll("^\"|\"$", ""));
        tvAlias.setText(wificonfig.alias);
        // Return the completed view to render on screen
        return convertView;
    }
}
