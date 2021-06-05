package com.adglobi.ads;

public class Credentials {
    public static String key;
    public static String aid;
    public static String mid;

    public Credentials(String key, String aid, String mid) {
        this.key = key;
        this.aid = aid;
        this.mid = mid;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        Credentials.key = key;
    }

    public static String getAid() {
        return aid;
    }

    public static void setAid(String aid) {
        Credentials.aid = aid;
    }

    public static String getMid() {
        return mid;
    }

    public static void setMid(String mid) {
        Credentials.mid = mid;
    }
}
