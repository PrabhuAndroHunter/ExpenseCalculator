package com.pub.expensecalculator.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.pub.expensecalculator.R;

/**
 * Created by prabhu on 13/2/18.
 */

public class FutureTransactionReceiver extends BroadcastReceiver {
    MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {

       /* mp=MediaPlayer.create(context, R.raw.alrm   );
        mp.start();*/
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();

    }
}
