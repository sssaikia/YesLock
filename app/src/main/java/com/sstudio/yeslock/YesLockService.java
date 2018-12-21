package com.sstudio.yeslock;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.rvalerio.fgchecker.AppChecker;

import java.util.ArrayList;

public class YesLockService extends Service {
    public YesLockService() {
    }

    Broadcast broadcast;
    String lockedAppName = "xyz";
    Context context;
    ArrayList<String> list;
    AlertDialog alertDialogdlgM, alertDialogdlgT;
    TinyDB tinyDB;
    boolean show = true;
    Setpass setpass;
    PassView passView;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        broadcast = new Broadcast();
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        screenStateFilter.addAction("lock");
        screenStateFilter.addAction("setPass");
        screenStateFilter.addAction("updateList");
        screenStateFilter.addAction("Home");
        registerReceiver(broadcast, screenStateFilter);
        AppChecker appChecker = new AppChecker();
        tinyDB = new TinyDB(context);
        list = tinyDB.getListString("lockList");
        setpass = new Setpass(context);
        passView = new PassView(context);
        requestUsageStatsPermission();

        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...
                Log.d("Home pressed","working");
               /* Intent intent=new Intent();
                intent.setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                sendBroadcast(intent);*/
               passView.closeDialog();
            }
            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();



        appChecker.other(new AppChecker.Listener() {
            @Override
            public void onForeground(String packageName) {
                //Log.d("foreground package : ", "" + packageName);
                //Log.d("if list contains : ", "" + list.contains(packageName));
                try {
                    if (list.contains(packageName) && show) {
                        show = false;
                        lockedAppName = packageName;
                        passView.Ldiag();
                    }
                    if (!lockedAppName.equals(packageName)) {
                        show = true;
                        list = tinyDB.getListString("lockList");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).timeout(1000).start(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
        // TODO: Return the communication channel to the service.
    }

    public class Broadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d("intent : ", "" + intent.getAction());
            /*if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)
                    *//*|| intent.getAction().equals(Intent.ACTION_SCREEN_OFF)*//*) {
                if (tinyDB.getInt("Lock_type") != 0 && tinyDB.getInt("Lock_type") != 1) {
                    passView.Ldiag();
                    tinyDB.putString("screenON", "no");
                    //Log.d("on recieve : ", " screen off ");
                }
            }*/
            if (intent.getAction().equals("lock")) {
                passView.Ldiag();
                try {
                    list.add(lockedAppName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (intent.getAction().equals("setPass")) {
                passView.Ldiag();
            }
            if (intent.getAction().equals("updateList")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        list = tinyDB.getListString("lockList");
                    }
                }).start();

            }
            if (intent.getAction().equals("Home")) {
                /*Intent intent1=new Intent(context,MainActivity.class);
                startActivity(intent1);*/
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        }
    }

    void requestUsageStatsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this)) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
          /*  new AlertDialog.Builder(context)
                    .setTitle("Permission needed")
                    .setMessage("Allow Yes lock to Read Usage Access.")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            }).show();*/
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcast);
        super.onDestroy();
    }
}
