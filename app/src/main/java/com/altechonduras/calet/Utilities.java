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
    private static final String GRUPO = "grupo";

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

    public static String getReportData(String mReporteID, Context c) {
        //TODO: traer reportes unicamente desde la (ref) perteneciente al grupo... Algo asi como:
        return Utilities.getGroup(c)  + "/data/" + Utilities.getUserUid() + "/" + mReporteID;

    }

    public static String getFormatoReporte(String mReporteID, Context c) {
        return Utilities.getGroup(c) + "/reportes/" + mReporteID;
    }

    public static String getOrdenReporte(String mReporteID, Context c) {
        return Utilities.getGroup(c) + "/reportes/" + mReporteID + "/order";
    }

    public static void saveGrupo(String grupo, Context context) {
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().putString(GRUPO, grupo).apply();
    }

    public static String getGrupo(Context context) {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(GRUPO, null);
    }

    public static String getGroup(Context context) {
        return getGrupo(context);
    }
}
