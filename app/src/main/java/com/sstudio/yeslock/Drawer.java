package com.sstudio.yeslock;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Drawer extends AppCompatActivity {
    LinearLayout linearLayout,linearLayout_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        linearLayout_main=(LinearLayout)findViewById(R.id.drawer_layout_main);
        Intent intent=getIntent();
        int res=intent.getIntExtra("CallingRes",-1);
        switch (res){
            case R.id.configure:
                diag();
                break;
            case R.id.drawersettings:
                drawerSetting(R.layout.drawer_setting);
                break;
            case R.id.change_lock_screen:
                lockScreen();
                Toast.makeText(this, "Not ready", Toast.LENGTH_SHORT).show();
                break;
        }

    }
    public void lockScreen(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 12);
    }
    public void drawerSetting(int i){
        final TinyDB tinyDB=new TinyDB(this);
        final View child = getLayoutInflater().inflate(i,null);
        child.setPadding(30,0,30,0);
        linearLayout_main.addView(child);
        SeekBar s=(SeekBar) child.findViewById(R.id.drawer_setting_seekbar);
        s.setProgress(tinyDB.getInt("seekbar_val"));
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Toast.makeText(Drawer.this, ""+i, Toast.LENGTH_SHORT).show();
                //TODO seekbar_val
                tinyDB.putInt("seekbar_val",i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((Button)child.findViewById(R.id.drawer_setting_choose_color_button))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AmbilWarnaDialog(Drawer.this, tinyDB.getInt("drawer_back_color"), true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                            @Override
                            public void onCancel(AmbilWarnaDialog dialog) {
                                //do nothing
                            }

                            @Override
                            public void onOk(AmbilWarnaDialog dialog, int color) {
                                //TODO drawer_back_color
                                Toast.makeText(Drawer.this, ""+color, Toast.LENGTH_SHORT).show();
                                tinyDB.putInt("drawer_back_color",color);
                            }
                        }).show();
                    }
                });
        Spinner sp = (Spinner)child.findViewById(R.id.drawer_setting_animation_choose_spinner);
        sp.setSelection(tinyDB.getInt("spinner_val"));
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO spinner_val
                Toast.makeText(Drawer.this, "spin val"+i, Toast.LENGTH_SHORT).show();
                tinyDB.putInt("spinner_val",i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });
    }

    public void addLay(int i){
        final TinyDB tinyDB=new TinyDB(this);
        final View child = getLayoutInflater().inflate(i,null);
        child.setPadding(30,0,30,0);
        linearLayout_main.addView(child);
        switch (i){
            case R.layout.textpass:
                tinyDB.putInt("Lock_type",R.layout.textpass);
                //TODO locktype
                final EditText editText=(EditText)(child.findViewById(R.id.text_pass_et1));
                final EditText editText1=(EditText)(child.findViewById(R.id.text_pass_et2));
                ((Button)child.findViewById(R.id.text_pass_save))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (editText.getText().toString().equals(editText1.getText().toString())){
                                    Toast.makeText(Drawer.this, "pass matches", Toast.LENGTH_SHORT).show();
                                    //TODO save matched password
                                    tinyDB.putString("TextPass",editText.getText().toString());
                                    Toast.makeText(Drawer.this, "Passs saved"+editText.getText().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                child.findViewById(R.id.text_pass_cancel)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                diag();
                                linearLayout_main.removeView(child);
                            }
                        });
                break;
            case R.layout.time_password:
                tinyDB.putInt("Lock_type",R.layout.time_password);
                //TODO locktype
                ((Button)child.findViewById(R.id.time_pass_save))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //TODO implement edittext.get text = system time hand
                                Toast.makeText(Drawer.this, "time pass coming soon", Toast.LENGTH_SHORT).show();
                                /*if ((child.findViewById(R.id.time_pass_et))
                                        ==(child.findViewById(R.id.text_pass_et2))){
                                    Toast.makeText(Drawer.this, "pass matches", Toast.LENGTH_SHORT).show();
                                }*/
                            }
                        });
                child.findViewById(R.id.time_pass_cancel)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                diag();
                                linearLayout_main.removeView(child);
                            }
                        });
                break;
            case R.layout.math_password:
                //TODO locktype
                tinyDB.putInt("Lock_type",R.layout.math_password);
                final EditText text=(EditText)child.findViewById(R.id.math_pass_et1);
                //final int mathpass= Integer.parseInt(text.getText().toString());
                ((Button)child.findViewById(R.id.math_pass_save))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //TODO implement textview.gettext()*(some pre saved integer)==edittext.getteext();
                                if (!text.getText().toString().equals("")){
                                    Toast.makeText(Drawer.this, ""+Integer.parseInt(text.getText().toString()), Toast.LENGTH_SHORT).show();
                                    tinyDB.putInt("mathpass", Integer.parseInt(text.getText().toString()));
                                }else{
                                    Toast.makeText(Drawer.this, "type a text", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                                //tinyDB.putInt("mathpass",mathpass);
                              /* AlertDialog.Builder d=  new AlertDialog.Builder(Drawer.this);
                                EditText et=new EditText(Drawer.this);
                                TextView tv=new TextView(Drawer.this);
                                d.setView(et);
                                d.setView(tv);
                                d.setPositiveButton("Set as pass", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //tinyDB.putInt("mathpass",);
                                    }
                                });
                                AlertDialog dialog=d.create();
                                dialog.show();*/
                            }
                        });
                child.findViewById(R.id.math_pass_cancel)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                diag();
                                linearLayout_main.removeView(child);
                            }
                        });

        }
    }
    public void diag(){
        new AlertDialog.Builder(this,R.style.diag)
                .setTitle("Select Lock type")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setItems(new CharSequence[]{"Password", "Time password", "Random Math","Remove password"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                addLay(R.layout.textpass);
                                Toast.makeText(Drawer.this, "Tet password", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                addLay(R.layout.time_password);
                                Toast.makeText(Drawer.this, "Time password", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                addLay(R.layout.math_password);
                                Toast.makeText(Drawer.this, "Random Math", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                TinyDB tiny=new TinyDB(Drawer.this);
                                tiny.putInt("Lock_type",0);
                                finish();
                        }
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TinyDB tinyDB=new TinyDB(this);
        if (requestCode==12){
            try{
                try{
                    InputStream i=getContentResolver().openInputStream(Uri.parse(tinyDB.getString("lBack")));
                    Drawable d=Drawable.createFromStream(i, String.valueOf(Uri.parse(tinyDB.getString("lBack"))));
                    //linearLayout_main.setBackground(d);
                    ((ImageView)findViewById(R.id.Lock_preview_drawer_lay)).setImageDrawable(d);
                }catch (Exception e){
                    Log.d("error",""+e);
                    Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                }
                tinyDB.putString("lBack",data.getData().toString());

                //TODO save the uri for lockscreen update -> Uri image=data.getData();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
