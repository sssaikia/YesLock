package com.sstudio.yeslock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PassChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_diag);
        PassView passView = new PassView(this);
        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putInt("DolockNo", 1);
        passView.Ldiag();
        ((Button) findViewById(R.id.warn_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Setpass setpass = new Setpass(PassChangeActivity.this);
                setpass.diag();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
       /* ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);*/
    }
}
