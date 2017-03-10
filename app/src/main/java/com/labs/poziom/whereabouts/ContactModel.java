package com.labs.poziom.whereabouts;

import android.net.wifi.WifiConfiguration;

/**
 * Created by Sandy on 19-01-2017.
 */

public class ContactModel {
    String name;
    String id;
    String phone;

    ContactModel(String name, String id, String phone) {
        this.name = name;
        this.id = id;
        this.phone = phone;
    }

    public boolean equals(Object object2) {
        return object2 instanceof ContactModel && id.equals(((ContactModel)object2).id) && name.equals(((ContactModel)object2).name);
    }
}

