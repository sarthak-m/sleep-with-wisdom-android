package com.artofliving.volunteer.sleepwithwisdom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by sarthak-m on 18/09/18.
 */

class AlertDialogUtil {

    static void showPermissionDialog(final Activity activity, final int PERMISSIONS_REQUEST_CODE, String title, String message, final String[] permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.requestPermissions(permission, PERMISSIONS_REQUEST_CODE);
                    }
                })
                .setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
