package com.pub.expensecalculator.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by prabhu on 9/2/18.
 */


public class SharedPrefManager {
    public static SharedPreferences getSharedPref(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return pref;
    }

    public static void setTotalBalance(Context mContext, String value) {
        SharedPreferences.Editor edit = getSharedPref(mContext).edit();
        edit.putString(Constants.BALANCE, value);
        edit.commit();
    }

    public static void setIntPrefVal(Context mContext, String key, int value) {
        SharedPreferences.Editor edit = getSharedPref(mContext).edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void setBooleanPrefVal(Context mContext, String key, boolean value){
        SharedPreferences.Editor edit = getSharedPref(mContext).edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBooleanPrefVal(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        boolean val = false;
        if(pref.contains(key))
            val = pref.getBoolean(key,false);
        else
            val = false;
        return val;
    }

    public static String getTotalBalance(Context mContext) {
        SharedPreferences pref = getSharedPref(mContext);
        String val = "";
        if(pref.contains(Constants.BALANCE))
            val = pref.getString(Constants.BALANCE, "");
        else
            val = "0.0";
        return val;
    }

    public static int getIntPrefVal(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        int val = 0;
        if(pref.contains(key)) val = pref.getInt(key, 0);
        return val;
    }
}
