package com.myidea.sih.reportpothole.util;

public class MyRandom {

    private static String inventory = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_";

    public static String makeRandom(int n) {
        String toReturn = "";
        int i = 1;
        while (i <= n) {
            toReturn += "" + inventory.charAt((int)(inventory.length() * Math.random()));
            i++;
        }
        return toReturn;
    }

    public static String makeRandomForStorage() {
        String toReturn = "";
        int i = 1;
        while (i <= 30) {
            toReturn += "" + inventory.charAt((int)(inventory.length() * Math.random()));
            i++;
        }
        return toReturn;
    }

}
