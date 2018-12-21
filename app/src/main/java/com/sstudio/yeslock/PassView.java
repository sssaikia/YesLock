package com.sstudio.yeslock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Alan on 7/13/2017.
 */

class PassView {
    Context context;
    TinyDB tinyDB;
    public AlertDialog alertDialogdlgM, alertDialogdlgT, alertDialogdlgS;

    PassView(Context context1) {
        this.context = context1;
        tinyDB = new TinyDB(context);
    }

    private void dlgM() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.warn_diag);
        View i = View.inflate(context, R.layout.math_password, null);
        alertDialogBuilder.setView(i);
        final EditText editText = (EditText) i.findViewById(R.id.math_pass_et1);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        Random r = new Random();
        final int rndm = r.nextInt(100);
        final TextView textView = (TextView) i.findViewById(R.id.math_pass_tv);
        String str = rndm + "";
        textView.setText(str);
        Button button = (Button) i.findViewById(R.id.math_pass_save);
        Button button1 = (Button) i.findViewById(R.id.math_pass_cancel);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinyDB.getString("screenON").equals("ok")){
                tinyDB.putInt("DolockNo", 1);
                Intent intent = new Intent();
                intent.setAction("Home");
                context.sendBroadcast(intent);
                alertDialogdlgM.cancel();}

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Integer.parseInt(editText.getText().toString()) == (rndm + (tinyDB.getInt("mathpass")))
                            && !editText.getText().equals("")) {
                        alertDialogdlgM.cancel();
                        tinyDB.putInt("DolockNo", 1);
                        tinyDB.putString("screenON","ok");
                    } else {
                        editText.setText("");
                        editText.setHint("Try again.");
                        Toast.makeText(context, "Wrong entry", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e1) {
                    Toast.makeText(context, "" + e1, Toast.LENGTH_SHORT).show();
                }

            }
        });
       /* alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                tinyDB.putInt("DolockNo", 1);
                Intent intent = new Intent();
                intent.setAction("Home");
                context.sendBroadcast(intent);
            }
        });*/
        alertDialogBuilder
                .setCancelable(false);
        alertDialogdlgM = alertDialogBuilder.create();
        alertDialogdlgM.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialogdlgM.show();
    }

    private void dlgMS() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.warn_diag);
        View i = View.inflate(context, R.layout.time_password, null);
        alertDialogBuilder.setView(i);
        final EditText editText = (EditText) i.findViewById(R.id.time_pass_et);
        editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD|InputType.TYPE_CLASS_NUMBER);
        Button button = (Button) i.findViewById(R.id.time_pass_save);
        Button button1 = (Button) i.findViewById(R.id.time_pass_cancel);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinyDB.getString("screenON").equals("ok")){
                tinyDB.putInt("DolockNo", 1);
                Intent intent = new Intent();
                intent.setAction("Home");
                context.sendBroadcast(intent);
                alertDialogdlgS.cancel();}

            }
        });
        Calendar c = Calendar.getInstance();
        final int min = c.get(Calendar.MINUTE);
        final int date = c.get(Calendar.DAY_OF_MONTH);
        final int hour = c.get(Calendar.HOUR);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int sum = Integer.parseInt(editText.getText().toString());
                Log.d("time password ::: ", "from ET : " + sum +"  time format : "+tinyDB.getInt("timePassFormat"));
                try {
                    switch (tinyDB.getInt("timePassFormat")) {
                        case 1:
                            Log.d("time password ::: ", "from ET : " + sum + " from sys : " + ((min * 100) + date));
                            if (sum == ((min * 100) + date)) {
                                alertDialogdlgS.cancel();
                                tinyDB.putString("screenON","ok");
                                tinyDB.putInt("DolockNo", 1);
                            } else {
                                editText.setText("");
                                editText.setHint("Try again.");
                                Toast.makeText(context, "Wrong entry", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            Log.d("time password ::: ", "from ET : " + sum + " from sys : " + ((date * 100) + min));
                            if (sum == ((date * 100) + min)) {
                                alertDialogdlgS.cancel();
                                tinyDB.putString("screenON","ok");
                                tinyDB.putInt("DolockNo", 1);
                            } else {
                                editText.setText("");
                                editText.setHint("Try again.");
                                Toast.makeText(context, "Wrong entry", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 3:
                            Log.d("time password ::: ", "from ET : " + sum + " from sys : " + ((date * 100) + hour));
                            if (sum == ((date * 100) + hour)) {
                                alertDialogdlgS.cancel();
                                tinyDB.putString("screenON","ok");
                                tinyDB.putInt("DolockNo", 1);
                            } else {
                                editText.setText("");
                                editText.setHint("Try again.");
                                Toast.makeText(context, "Wrong entry", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 4:
                            Log.d("time password ::: ", "from ET : " + sum + " from sys : " + ((hour * 100) + date));
                            if (sum == ((hour * 100) + date)) {
                                alertDialogdlgS.cancel();
                                tinyDB.putString("screenON","ok");
                                tinyDB.putInt("DolockNo", 1);
                            } else {
                                editText.setText("");
                                editText.setHint("Try again.");
                                Toast.makeText(context, "Wrong entry", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            Toast.makeText(context, "Wrong entry", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e1) {
                    Toast.makeText(context, "" + e1, Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialogBuilder
                .setCancelable(false);
        /*alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                tinyDB.putInt("DolockNo", 1);
                Intent intent = new Intent();
                intent.setAction("Home");
                context.sendBroadcast(intent);
            }
        });*/
        alertDialogdlgS = alertDialogBuilder.create();
        alertDialogdlgS.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialogdlgS.show();
    }

    private void dlgT() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.warn_diag);
        View i = View.inflate(context, R.layout.text_pass_lock_screen, null);
        alertDialogBuilder.setView(i);
        final EditText editText = (EditText) i.findViewById(R.id.text_pass_lock_screen_et);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
        ((Button) i.findViewById(R.id.text_pass_lock_screen_cancel_button))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tinyDB.getString("screenON").equals("ok")){
                            tinyDB.putInt("DolockNo", 1);
                            Intent intent = new Intent();
                            intent.setAction("Home");
                            context.sendBroadcast(intent);
                            alertDialogdlgT.cancel();
                        }
                    }
                });
        ((Button) i.findViewById(R.id.text_pass_lock_screen_ok_button))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText.getText().toString().equals(tinyDB.getString("TextPass"))) {
                            alertDialogdlgT.cancel();
                            tinyDB.putString("screenON","ok");
                            tinyDB.putInt("DolockNo", 1);
                        } else {
                            editText.setText("");
                            editText.setHint("Try again.");
                            Toast.makeText(context, "Wrong entry.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        alertDialogBuilder
                .setCancelable(false);
        /*alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                tinyDB.putInt("DolockNo", 1);
                Intent intent = new Intent();
                intent.setAction("Home");
                context.sendBroadcast(intent);
            }
        });*/
        alertDialogdlgT = alertDialogBuilder.create();
        alertDialogdlgT.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialogdlgT.show();
    }

    public void Ldiag() {
        if (tinyDB.getInt("DolockNo") == 1 || tinyDB.getInt("DolockNo") == 4) {
            Toast.makeText(context, "DolockNo Passed " + tinyDB.getInt("DolockNo"), Toast.LENGTH_SHORT).show();
            tinyDB.putInt("DolockNo", 2);
            final TinyDB tiny = new TinyDB(context);
            switch (tiny.getInt("Lock_type")) {
                case R.layout.textpass:
                    tinyDB.putInt("dialogtoclose",R.layout.textpass);
                    dlgT();
                    break;
                case R.layout.math_password:
                    tinyDB.putInt("dialogtoclose",R.layout.math_password);
                    dlgM();
                    break;
                case R.layout.time_password:
                    tinyDB.putInt("dialogtoclose",R.layout.time_password);
                    dlgMS();
                    break;
                default:
                    Setpass setpass = new Setpass(context);
                    setpass.diag();
            }
        } else {
            Toast.makeText(context, "DolockNo error " + tinyDB.getInt("DolockNo"), Toast.LENGTH_SHORT).show();
        }
    }
    public void closeDialog(){
        tinyDB.putInt("DolockNo", 1);
        switch (tinyDB.getInt("dialogtoclose")) {
            case R.layout.textpass:
                try {
                    alertDialogdlgT.cancel();
                }catch (Exception e){
                    Log.d("text pass.","failed :"+e);
                }
                break;
            case R.layout.math_password:
                try {
                    alertDialogdlgM.cancel();
                }catch (Exception e){
                    Log.d("math pass.","failed :"+e);
                }
                break;
            case R.layout.time_password:
                try {
                    alertDialogdlgS.cancel();
                }catch (Exception e){
                    Log.d("time pass.","failed :"+e);
                }
                break;
            default:
               /* Setpass setpass = new Setpass(context);
                setpass.diag();*/
        }
    }
}
