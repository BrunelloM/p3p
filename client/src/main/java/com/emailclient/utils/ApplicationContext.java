package com.emailclient.utils;

import shared.User;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ApplicationContext {

    public static User getIdentity() {
        return new User(0, "Ugo", "Sbrinzi", "ugo_sbrinzi@sbrinzer.com");
    }

    public static String getServerAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
