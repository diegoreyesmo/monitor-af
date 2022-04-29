package com.rrfinformatica.monitoraf;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogUtil {

    public static void showSimpleDialog(Context context, String message) {
        if (message != null && !message.isEmpty()) {
            if (!((Activity) context).isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setMessage(message)
                        .setTitle(R.string.atencion).create();
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}
