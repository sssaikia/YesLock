package com.sstudio.yeslock;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    List<ResolveInfo> pkgAppsList;
    JazzyGridView gridView;
    FloatingActionButton fab;
    ImageView im;
    Random r;
    TextClock t;
    int lastX, lastY;
    int id;
    Context con = MainActivity.this;
    AlertDialog dialog = null;
    long lastPressTime;
    boolean mHasDoubleClicked;
    //ImageView fav1,fav2,fav3,fav4,fav5;

    Drawable[] val1;        //Global values
    String[] val2;          //Global values
    YesAdapter arrayAdapter,favarry;        //Global values
    BroadcastMain broadcastMain;
    //DraggableGridView draggableGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //draggableGridView=(DraggableGridView)findViewById(R.id.homeGrid);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //to make the status bar and navigation bar transparent
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/

        t = (TextClock) findViewById(R.id.clock);
        gridView = (JazzyGridView) findViewById(R.id.grid);
        im = (ImageView) findViewById(R.id.wallpaper);
        fab = (FloatingActionButton) findViewById(R.id.fab);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        checkPermission();
        broadcastMain = new BroadcastMain();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        registerReceiver(broadcastMain, intentFilter);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                pkgAppsList = MainActivity.this.getPackageManager().queryIntentActivities(mainIntent, 0);
                for (int y = 0; y < pkgAppsList.size(); y++) {
                    if (pkgAppsList.get(y).activityInfo.packageName.equals("com.sstudio.yeslock")) {
                        pkgAppsList.remove(y);
                    }
                }
                PackageManager p = getPackageManager();
                Collections.sort(pkgAppsList, new ResolveInfo.DisplayNameComparator(p));
                val1 = new Drawable[pkgAppsList.size()];
                val2 = new String[pkgAppsList.size()];
                for (int i = 0; i < pkgAppsList.size(); i++) {
                    //TODO "Done"add if statement to check if a saved list can bbe found from shared preferences

                    val1[i] = ((pkgAppsList.get(i).activityInfo.loadIcon(p)));
                    val2[i] = String.valueOf(pkgAppsList.get(i).loadLabel(p));
                    arrayAdapter = new YesAdapter(MainActivity.this, val2, val1);
                }
            }
        }).start();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                gridClose();
                // Toast.makeText(MainActivity.this, "" + list.get(i), Toast.LENGTH_SHORT).show();
                Intent mIntent = getPackageManager().getLaunchIntentForPackage(pkgAppsList.get(i).activityInfo.packageName);
                startActivity(mIntent);
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        fab = (FloatingActionButton) findViewById(R.id.fab);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                long pressTime = System.currentTimeMillis();

                // If double click...
                if (pressTime - lastPressTime <= 200) {
                    //TODO ad stuff here
                    /*TinyDB tinyDB = new TinyDB(con);
                    if (!isAccessibilitySettingsOn(con)) {
                        Intent acc = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(acc, 0);
                    } else if (tinyDB.getInt("Lock_type") == 0 || tinyDB.getInt("Lock_type") == 4) {
                        Toast.makeText(con, "" + tinyDB.getInt("Lock_type"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction("openRecent");
                        sendBroadcast(intent);
                        Log.d("fab clicked  ", "broadcast sent  ");
                    }*/
                    //Toast.makeText(getApplicationContext(), "Double Click Event", Toast.LENGTH_SHORT).show();
                    mHasDoubleClicked = true;
                } else {     // If not double click....
                    mHasDoubleClicked = false;
                    Handler myHandler = new Handler() {
                        public void handleMessage(Message m) {
                            if (!mHasDoubleClicked) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // get the center for the clipping circle
                                    int centerX = (view.getLeft() + view.getRight()) / 2;
                                    int centerY = (view.getTop() + view.getBottom()) / 2;
                                    int startRadius = 10;
                                    // get the final radius for the clipping circle
                                    int endRadius = Math.max(gridView.getWidth(), gridView.getHeight());
                                    // create the animator for this view (the start radius is zero)
                                    Animator anim;
                                    if (lastX != 0 && lastY != 0) {
                                        anim = ViewAnimationUtils.createCircularReveal(gridView, lastX, lastY, startRadius, endRadius);
                                    } else {
                                        anim = ViewAnimationUtils.createCircularReveal(gridView, centerX, centerY, startRadius, endRadius);
                                    }
                                    // make the view visible and start the animation
                                    gridView.setVisibility(View.VISIBLE);
                                    anim.start();
                                    openGrid();
                                    //Toast.makeText(getApplicationContext(), "Single Click Event", Toast.LENGTH_SHORT).show();
                                } else {
                                    gridView.setVisibility(View.VISIBLE);
                                    openGrid();
                                }
                            }
                        }
                    };
                    Message m = new Message();
                    myHandler.sendMessageDelayed(m, 200);
                }
                lastPressTime = pressTime;
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        t.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                t.setOnTouchListener(new View.OnTouchListener() {

                    float startX;
                    float startY;
                    float startRawY;
                    float startRawX;
                    float distanceX;
                    float distanceY;
                    int lastAction;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                startX = view.getX() - event.getRawX();
                                startY = view.getY() - event.getRawY();
                                startRawX = event.getRawX();
                                startRawY = event.getRawY();
                                lastAction = MotionEvent.ACTION_DOWN;
                                break;

                            case MotionEvent.ACTION_MOVE:
                                view.setX(event.getRawX() + startX);
                                view.setY(event.getRawY() + startY);
                                lastAction = MotionEvent.ACTION_MOVE;
                                break;

                            case MotionEvent.ACTION_UP:
                                distanceX = event.getRawX() - startRawX;
                                distanceY = event.getRawY() - startRawY;
                                if (Math.abs(distanceX) < 10) {
                                    Intent mIntent = getPackageManager().getLaunchIntentForPackage("com.android.deskclock");
                                    startActivity(mIntent);
                                }
                                break;
                            case MotionEvent.ACTION_BUTTON_PRESS:

                            default:
                                return false;
                        }
                        return true;
                    }
                });
                return false;
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                fab.setOnTouchListener(new View.OnTouchListener() {

                    float startX;
                    float startY;
                    float startRawY;
                    float startRawX;
                    float distanceX;
                    float distanceY;
                    int lastAction;

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                Log.d("fab : onTouch", "Action down");
                                startX = view.getX() - event.getRawX();
                                startY = view.getY() - event.getRawY();
                                startRawX = event.getRawX();
                                startRawY = event.getRawY();
                                lastAction = MotionEvent.ACTION_DOWN;
                                break;

                            case MotionEvent.ACTION_MOVE:
                                Log.d("fab : onTouch", "Action move");
                                view.setX(event.getRawX() + startX);
                                view.setY(event.getRawY() + startY);
                                lastX = (int) ((event.getRawX() + startX));
                                lastY = (int) ((event.getRawY() + startY));
                                lastAction = MotionEvent.ACTION_MOVE;
                                break;

                            case MotionEvent.ACTION_UP:
                                Log.d("fab : onTouch", "Action up");
                                distanceX = event.getRawX() - startRawX;
                                distanceY = event.getRawY() - startRawY;
                                if (Math.abs(distanceX) < 10) {
                                    fab.callOnClick();
                                }
                                break;
                            case MotionEvent.ACTION_BUTTON_PRESS:

                            default:
                                return false;
                        }
                        return true;
                    }
                });
                return false;
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                TinyDB t = new TinyDB(con);
                r = new Random();
                i = r.nextInt(14);
                int anim = t.getInt("spinner_val");
                if (anim == 15) {
                    gridView.setTransitionEffect(i);
                } else {
                    gridView.setTransitionEffect(anim);
                }

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                //gridView.setTransitionEffect(i);
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                view.setAnimation(shake);
                id = i;
                shake.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this, R.style.diag);
                        View v = getLayoutInflater().inflate(R.layout.diag, null);
                        b.setView(v);
                        v.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    //Open the specific App Info page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + pkgAppsList.get(id).activityInfo.packageName));
                                    startActivity(intent);

                                } catch (ActivityNotFoundException e) {
                                    //e.printStackTrace();

                                    //Open the generic Apps page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);

                                }
                                dialog.cancel();
                            }
                        });
                        v.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gridClose();
                                Uri packageUri = Uri.parse("package:" + pkgAppsList.get(id).activityInfo.packageName);
                                Intent uninstall = new Intent(Intent.ACTION_DELETE, packageUri);
                                startActivity(uninstall);

                                dialog.cancel();
                            }
                        });
                        v.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(con, LockView.class);
                                //intent.putExtra("CallingRes", R.id.change_lock_screen);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });
                        v.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(con, LockView.class);
                                //intent.putExtra("CallingRes", R.id.change_lock_screen);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });
                    /*    v.findViewById(R.id.favlist).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PackageManager p=getPackageManager();
                                View v=View.inflate(con,R.layout.grid,null);
                                ((ImageView)v.findViewById(R.id.giv)).setImageDrawable(pkgAppsList.get(id).activityInfo.loadIcon(p));
                                //((TextView)v.findViewById(R.id.gtv)).setText(String.valueOf(pkgAppsList.get(id).loadLabel(p)));
                                ImageView iv=new ImageView(con);
                                iv.setMaxHeight(50);
                                iv.setMaxWidth(50);
                                iv.setImageDrawable(pkgAppsList.get(id).activityInfo.loadIcon(p));
                                draggableGridView.addView(iv);
                            }
                        });*/
                        //TODO add fav feature
                        dialog = b.create();
                        dialog.show();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                /*Toast.makeText(MainActivity.this, "name :"+pkgAppsList.get(i).activityInfo.packageName+
                        "permision :"+pkgAppsList.get(i).activityInfo.permission, Toast.LENGTH_SHORT).show();
                */
                return true;
            }

        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent intent = new Intent(MainActivity.this, YesLockService.class);
        startService(intent);

 /*       fav1=(ImageView)findViewById(R.id.fav1);
        fav2=(ImageView)findViewById(R.id.fav2);
        fav3=(ImageView)findViewById(R.id.fav3);
        fav4=(ImageView)findViewById(R.id.fav4);
        fav5=(ImageView)findViewById(R.id.fav5);*/

    }

    public void openGrid() {
        TinyDB ti = new TinyDB(this);
        fab.setVisibility(View.GONE);
        gridView.setBackgroundResource(R.drawable.back);
        try {
            if (ti.getInt("drawer_back_color") == 0) {
                gridView.setBackgroundColor(Color.parseColor("#b3b9b6"));
                ti.putInt("drawer_back_color", Color.parseColor("#b3b9b6"));
            }
            gridView.setBackgroundColor(ti.getInt("drawer_back_color"));
        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
///////////////////////////////////////////////////////////////////////////////////

        gridView.setAdapter(arrayAdapter);
        if (ti.getInt("seekbar_val") == 0) {
            ti.putInt("seekbar_val", 4);
        }
        gridView.setNumColumns(ti.getInt("seekbar_val"));
///////////////////////////////////////////////////////////////////////////////////
    }

    private void uPdateList() {
        Log.d("on Updatelist","add the new package");
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager p = getPackageManager();
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                pkgAppsList = MainActivity.this.getPackageManager().queryIntentActivities(mainIntent, 0);
                Collections.sort(pkgAppsList, new ResolveInfo.DisplayNameComparator(p));
                for (int y = 0; y < pkgAppsList.size(); y++) {
                    if (pkgAppsList.get(y).activityInfo.packageName.equals("com.sstudio.yeslock")) {
                        pkgAppsList.remove(y);
                    }
                }
                val1 = new Drawable[pkgAppsList.size()];
                val2 = new String[pkgAppsList.size()];
                int j = pkgAppsList.size(), k = 0;
                for (int i = 0; i < j; i++) {
                    val1[i] = ((pkgAppsList.get(i).activityInfo.loadIcon(p)));
                    val2[i] = String.valueOf(pkgAppsList.get(i).loadLabel(p));
                    arrayAdapter = new YesAdapter(MainActivity.this, val2, val1);
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            gridClose();
        }
    }

    public void gridClose() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = (fab.getLeft() + fab.getRight()) / 2;
            int cy = (fab.getTop() + fab.getBottom()) / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(gridView.getWidth(), gridView.getHeight());
            // i just swapped from radius, to radius arguments
            Animator animator;
            if (lastX != 0 && lastY != 0) {
                animator = ViewAnimationUtils.createCircularReveal(gridView, lastX, lastY, finalRadius, 0);
            } else {
                animator = ViewAnimationUtils.createCircularReveal(gridView, cx, cy, finalRadius, 0);
            }
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            //animator.setDuration(1500);
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    fab.setVisibility(View.VISIBLE);
                    gridView.setAdapter(null);
                    gridView.setBackgroundColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        } else {
            fab.setVisibility(View.VISIBLE);
            gridView.setAdapter(null);
            gridView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    boolean showRecent = false;       //Global values

    @Override
    protected void onResume() {
        super.onResume();
        uPdateList();
        showRecent = false;
        // TinyDB tinyDB=new TinyDB(con);
        //tinyDB.putInt("DolockNo", 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*TinyDB tinyDB = new TinyDB(this);
        if (tinyDB.getBoolean("pauseLock")) {
            if (showRecent) {
                showRecent = false;
            } else {
                //the following code prevents the recent app button interference
                ActivityManager activityManager = (ActivityManager) getApplicationContext()
                        .getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.moveTaskToFront(getTaskId(), 0);
            }
        }*/
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Uri u = Uri.parse(MainActivity.this.getApplicationInfo().dataDir);
            Intent intent = new Intent(Intent.ACTION_VIEW, u);
            intent.setType("image/*");
            try {
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
            startActivity(Intent.createChooser(intent, "Select Wallpaper"));
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            startActivity(intent);
        } else if (id == R.id.configure) {
            Intent intent = new Intent(this, PassChangeActivity.class);
            //intent.putExtra("package", "null");
            startActivity(intent);
            TinyDB tinyDB = new TinyDB(con);
            tinyDB.putInt("DolockNo", 1);
        } else if (id == R.id.drawersettings) {
            gridClose();
            Intent intent = new Intent(this, Drawer.class);
            intent.putExtra("CallingRes", R.id.drawersettings);
            startActivity(intent);
        } else if (id == R.id.change_lock_screen) {
            Intent intent = new Intent(this, LockView.class);
            TinyDB tinyDB = new TinyDB(con);
            tinyDB.putInt("DolockNo", 1);
            //intent.putExtra("CallingRes", R.id.change_lock_screen);
            startActivity(intent);
        } else if (id == R.id.help) {
            View v = getLayoutInflater().inflate(R.layout.helptext, null);
            new AlertDialog.Builder(con)
                    .setTitle("Help")
                    .setView(v)
                    .setNeutralButton("OK", null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            try {
                im.setImageURI(data.getData());
            } catch (Exception e) {
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        /*switch (keyCode) {
            case KeyEvent.KEYCODE_APP_SWITCH:
            {
                showRecent=true;
                Toast.makeText(this, "show recent = true", Toast.LENGTH_SHORT).show();
                onPause();
                return true;
            }
        }*/
        return super.onKeyDown(keyCode, event);
    }

  /*  // To check if service is enabled
    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + MyAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            //Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            //Log.e(TAG, "Error finding setting, default accessibility to not found: "+ e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            //Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    //Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        //Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            // Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }*/

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private class BroadcastMain extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Mainactivity broadcast:",intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)||intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                uPdateList();
            }
        }
    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(broadcastMain);
        super.onDestroy();
    }
}
