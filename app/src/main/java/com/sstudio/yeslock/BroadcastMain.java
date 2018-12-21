package com.sstudio.yeslock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Alan on 7/18/2017.
 */
class BroadcastMain extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Mainactivity broadcast:",intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)||intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            //uPdateList();

        }
    }
}