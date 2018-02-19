package com.pub.expensecalculator.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.pub.expensecalculator.database.DBHelper;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by prabhu on 24/1/18.
 */

public class CommonUtilities {
    private final String TAG = CommonUtilities.class.toString();

    /**
     * Check if singleton object of DB is null and not open; in the case
     * reinitialize and open DB.
     *
     * @param mContext
     */
    public static DBHelper getDBObject(Context mContext) {
        DBHelper dbhelper = DBHelper.getInstance(mContext);
        return dbhelper;
    }

    // This method will return current date in String
    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    // This method will return last month date with current date in String
    public static String getLastMonth(int lastMonthPeriod) {
        Date dt = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MONTH, lastMonthPeriod);
        dt = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(dt);
        return formattedDate;
    }

    // This method will return byte array of Drawable
    public static byte[] getByteArray(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // This method will return Bitmap of byteArray
    public static Bitmap getBitmap(byte[] byteAttay) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteAttay, 0, byteAttay.length);
        return bitmap;
    }
}
