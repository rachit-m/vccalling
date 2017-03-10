package com.poziomlabs.plugged;

import org.tinyradius.util.RadiusServer;

import java.net.InetSocketAddress;

/**
 * Created by guest on 9/7/16.
 */
public class MyRadiusServer extends RadiusServer {


    @Override
    public String getSharedSecret(InetSocketAddress inetSocketAddress) {
        return null;
    }

    @Override
    public String getUserPassword(String s) {
        return null;
    }
}
