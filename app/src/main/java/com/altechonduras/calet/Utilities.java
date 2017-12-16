package com.altechonduras.calet;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */

public class Utilities {
    private static final String PREFERENCES = "preferences";
    private static final String DEVICE = "device";
    public static String getMGPdir(Context context) {
        return context.getString(R.string.mgp_location) + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static String getLPUdir(Context context) {
        return context.getString(R.string.lpu_location) + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    public static String getGastodir(Context context) {
        return context.getString(R.string.gasto_location) + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    public static String getUserUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public static String getDevice(Context context) {
        String device = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(DEVICE, null);
        if (device == null) {
            device = UUID.randomUUID().toString();
            setDevice(device, context);
        }
        return device;
    }

    public static void setDevice(String device, Context context) {
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().putString(DEVICE, device).apply();
    }

    public static String getReporteID(String mReporteID) {
        //TODO: traer reportes unicamente desde la (ref) perteneciente al grupo... Algo asi como:
        return /*Utilities.getGroup() +*/ "tests" + "/data/" + Utilities.getUserUid() + "/" + mReporteID;

    }

    public static String getFormatoReporte(String mReporteID) {
        return /*Utilities.getGroup() +*/ "tests" + "/reportes/" + mReporteID + "/format";
    }
}
