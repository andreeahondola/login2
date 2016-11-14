package com.example.andreea.login.util;

/**
 * Created by Andreea on 11.11.2016.
 */

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class DialogUtils {
    public static void showError(Context context, Exception e) {
        new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(e.getMessage())
                .setCancelable(true)
                .create()
                .show();
    }
}
