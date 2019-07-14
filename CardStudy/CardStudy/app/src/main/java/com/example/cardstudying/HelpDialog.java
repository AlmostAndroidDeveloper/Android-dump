package com.example.cardstudying;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

class HelpDialog {
    static void show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Справка")
                .setMessage(context.getResources().getString(R.string.help))
                .setCancelable(false)
                .setNegativeButton("Понятно",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}