package com.sstudio.yeslock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LockView extends AppCompatActivity {
    ArrayList<String> locklist;
    List<ResolveInfo> pkgAppsList;
    Drawable[] val1;        //Global values
    String[] val2;          //Global values
    YesAdapter arrayAdapter;        //Global values
    JazzyGridView jazzyGridView;
    Context context = this;
    TinyDB tinyDB;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_view);
        jazzyGridView = (JazzyGridView) findViewById(R.id.Lock_grid);
        tinyDB = new TinyDB(context);

        Intent intent = new Intent();
        intent.setAction("setPass");
        sendBroadcast(intent);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                PackageManager p = getPackageManager();
                pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
                for (int y = 0; y < pkgAppsList.size(); y++) {
                    if (pkgAppsList.get(y).activityInfo.packageName.equals("com.sstudio.yeslock")) {
                        pkgAppsList.remove(y);
                    }
                }
                Collections.sort(pkgAppsList, new ResolveInfo.DisplayNameComparator(p));
                locklist = new ArrayList<>();
                val1 = new Drawable[pkgAppsList.size()];
                val2 = new String[pkgAppsList.size()];
                for (int i = 0; i < pkgAppsList.size(); i++) {
                    //TODO "Done"add if statement to check if a saved list can bbe found from shared preferences
                    val1[i] = ((pkgAppsList.get(i).activityInfo.loadIcon(p)));
                    val2[i] = String.valueOf(pkgAppsList.get(i).loadLabel(p));
                    arrayAdapter = new YesAdapter(LockView.this, val2, val1);
                }
            }
        });
        t.start();
        try {
            t.join();
            jazzyGridView.setAdapter(arrayAdapter);
            jazzyGridView.setNumColumns(tinyDB.getInt("seekbar_val"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Toast.makeText(context, "" + !tinyDB.getBoolean("notempty"), Toast.LENGTH_SHORT).show();
        if (tinyDB.getBoolean("notempty")) {
            Toast.makeText(context, "List empty", Toast.LENGTH_SHORT).show();
            locklist = tinyDB.getListString("lockList");


        } else {
            Toast.makeText(context, "Not empty List", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < pkgAppsList.size(); i++) {
                locklist.add(i, "Empty");
            }
            tinyDB.putBoolean("notempty", true);
        }


        ((Button) findViewById(R.id.save_locklist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!locklist.isEmpty()) {
                    tinyDB.putListString("lockList", locklist);
                    Intent i = new Intent();
                    i.setAction("updateList");
                    sendBroadcast(i);
                    //Toast.makeText(context, "broadcast sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        jazzyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemId = i;
                final TextView tv = (TextView) findViewById(R.id.lock_app_count_tv);
                final PackageManager p = getPackageManager();
               /* if (!locklist.get(itemId).equals(pkgAppsList.get(itemId).activityInfo.packageName)) {

                    tv.setText(String.valueOf(pkgAppsList.get(itemId).loadLabel(p)) + " Not Locked");
                } else {
                    tv.setText(String.valueOf(pkgAppsList.get(itemId).loadLabel(p)) + " Locked");
                }*/
                new AlertDialog.Builder(context)
                        .setTitle("Lock " + String.valueOf(pkgAppsList.get(itemId).loadLabel(p)))
                        .setIcon(((pkgAppsList.get(itemId).activityInfo.loadIcon(p))))
                        .setPositiveButton("Lock", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                locklist.set(itemId, pkgAppsList.get(itemId).activityInfo.packageName);
                                tinyDB.putListString("lockList", locklist);
                                Intent update = new Intent();
                                update.setAction("updateList");
                                sendBroadcast(update);
                                tv.setText(String.valueOf(pkgAppsList.get(itemId).loadLabel(p)) + " Locked");
                            }
                        })
                        .setNegativeButton("Unlock", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    locklist.set(itemId, "Empty");
                                    //locklist.remove(itemId);
                                    tinyDB.putListString("lockList", locklist);
                                    Intent update = new Intent();
                                    update.setAction("updateList");
                                    sendBroadcast(update);
                                    tv.setText(String.valueOf(pkgAppsList.get(itemId).loadLabel(p)) + " Not Locked");
                                } catch (Exception e) {
                                    Toast.makeText(LockView.this, "Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).show();

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //the following code prevents the recent app button interference
           /* ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(getTaskId(), 0);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
