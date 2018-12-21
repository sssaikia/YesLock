package com.sstudio.yeslock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Alan on 7/13/2017.
 */

class Setpass {
    Context context;
    private TinyDB tinyDB;

    Setpass(Context c) {
        this.context = c;
        tinyDB = new TinyDB(c);
    }

    private AlertDialog alertDialogdlgM, alertDialogdlgT, alertDialogdlgS;

    private void dlgM() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.warn_diag);
        View i = View.inflate(context, R.layout.math_password, null);
        alertDialogBuilder.setView(i);
        final EditText editText = (EditText) i.findViewById(R.id.math_pass_et1);
        final TextView textView = (TextView) i.findViewById(R.id.math_pass_tv);
        textView.setText("Enter the key number.");
        Button button = (Button) i.findViewById(R.id.math_pass_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tinyDB.putInt("mathpass", Integer.parseInt(editText.getText().toString()));
                tinyDB.putInt("Lock_type", R.layout.math_password);
                alertDialogdlgM.cancel();
            }
        });
        ((Button) i.findViewById(R.id.math_pass_cancel))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogdlgT.cancel();
                    }
                });
        alertDialogBuilder
                .setCancelable(false);
        alertDialogdlgM = alertDialogBuilder.create();
        alertDialogdlgM.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialogdlgM.show();
    }

    private void dlgT() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.warn_diag);
        View i = View.inflate(context, R.layout.textpass, null);
        alertDialogBuilder.setView(i);
        final EditText editText = (EditText) i.findViewById(R.id.text_pass_et1);
        final EditText editText2 = (EditText) i.findViewById(R.id.text_pass_et2);

        ((Button) i.findViewById(R.id.text_pass_save))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText.getText().toString().equals(editText2.getText().toString())) {
                            tinyDB.putString("TextPass", editText.getText().toString());
                            tinyDB.putInt("Lock_type", R.layout.textpass);
                            alertDialogdlgT.cancel();
                        }

                    }
                });
        ((Button) i.findViewById(R.id.text_pass_cancel))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogdlgT.cancel();
                    }
                });
        alertDialogBuilder
                .setCancelable(false);
        alertDialogdlgT = alertDialogBuilder.create();
        alertDialogdlgT.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialogdlgT.show();
    }

    void diag() {
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setTitle("Select Lock type");
        a.setItems(new CharSequence[]{"Password", "Time password", "Random Math", "Remove password"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        dlgT();
                        tinyDB.putInt("DolockNo", 1);
                        Toast.makeText(context, "Text password", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        dlgS();
                        Toast.makeText(context, "Time password", Toast.LENGTH_SHORT).show();
                        tinyDB.putInt("DolockNo", 1);
                        break;
                    case 2:
                        dlgM();
                        tinyDB.putInt("DolockNo", 1);
                        Toast.makeText(context, "Random Math", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        tinyDB.putInt("Lock_type", 0);
                        tinyDB.putInt("DolockNo", 1);
                        break;
                }
            }
        });
        a.setCancelable(false);
        AlertDialog bk = a.create();
        bk.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        bk.show();
    }

    private void dlgS() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.warn_diag);
        alertDialogBuilder.setItems(new CharSequence[]{"mmDD -minute and date", "DDmm -day and" +
                " minute", "DDhh -da and hour", "hhDD -hour and date"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        timeLockTest(0);
                        tinyDB.putInt("DolockNo", 1);

                        Toast.makeText(context, "mmDD selected", Toast.LENGTH_SHORT).show();
                        alertDialogdlgS.cancel();
                        break;
                    case 1:
                        timeLockTest(1);
                        tinyDB.putInt("DolockNo", 1);
                        Toast.makeText(context, "DDmm selected", Toast.LENGTH_SHORT).show();
                        alertDialogdlgS.cancel();
                        break;
                    case 2:
                        timeLockTest(2);
                        tinyDB.putInt("DolockNo", 1);
                        Toast.makeText(context, "DDhh selected", Toast.LENGTH_SHORT).show();
                        alertDialogdlgS.cancel();
                        break;
                    case 3:
                        timeLockTest(3);
                        tinyDB.putInt("DolockNo", 1);
                        Toast.makeText(context, "hhDD selected", Toast.LENGTH_SHORT).show();
                        alertDialogdlgS.cancel();
                        break;
                    default:
                        tinyDB.putInt("DolockNo", 1);
                        tinyDB.putInt("timePassFormat", 1);
                        Toast.makeText(context, "default mmDD selected", Toast.LENGTH_SHORT).show();
                        alertDialogdlgS.cancel();
                        break;
                }
            }
        });
        alertDialogBuilder
                .setCancelable(false);
        alertDialogdlgS = alertDialogBuilder.create();
        alertDialogdlgS.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialogdlgS.show();
    }
    private void timeLockTest(final int i1){
        String str=null;
        switch (i1){
            case 0:str="mmDD";
                break;
            case 1:str="DDmm";
                break;
            case 2:str="DDhh";
                break;
            case 3:str="hhDD";
                break;
        }
        final String finalStr = str;
        new AlertDialog.Builder(context)
                .setTitle("Attention!!")
                .setMessage("You have selected time password in the "+finalStr+"format.\n Please note:\n" +
                        "DD - date. eg: for 13-1-1989 it is 13\n\n" +
                        "mm - minute hand from your clock visible on the status bar\n\n" +
                        "hh - hour hand from your clock visible on the status bar. " +
                        "\n\nPlease note that hh should be in 12hr format.\n\n\n" +
                        "Warning:\n" +
                        "If you can't see your clock on your status bar properly than click on Reset password right now.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tinyDB.putInt("Lock_type", R.layout.time_password);
                        tinyDB.putInt("timePassFormat", i1+1);
                        Toast.makeText(context, "password set in "+ finalStr +" format.", Toast.LENGTH_SHORT).show();
                        Log.d("::time format:",""+tinyDB.getInt("timePassFormat"));

                    }
                })
                .setNegativeButton("Reset password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tinyDB.putInt("Lock_type", 0);
                        Toast.makeText(context, "Password removed.", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

}
