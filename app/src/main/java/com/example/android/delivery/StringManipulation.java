package com.example.android.delivery;

/**
 * Created by ASUS on 07/05/2018.
 */

public class StringManipulation {
    public static String expandUsername(String username){
        return username.replace(".", " ");
    }

    public static String condenseUsername(String username){
        return username.replace(" " , ".");
    }
}
