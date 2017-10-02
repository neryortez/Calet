package com.altechonduras.calet;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */

public class Utilities {
    public static String getMGPdir(Context context) {
        return context.getString(R.string.mgp_location) + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static String getLPUdir(Context context) {
        return context.getString(R.string.lpu_location) + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid();

    }
}
