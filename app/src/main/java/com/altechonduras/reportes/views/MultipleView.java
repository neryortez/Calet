package com.altechonduras.reportes.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nery Ortez on 22-Dec-17.
 */

public class MultipleView extends LinearLayout {
    ArrayList<String> multis = new ArrayList<>();
    private final Map<String, SelectionView> viewsMap = new HashMap<>();

    private String key;

    public MultipleView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public MultipleView(Context context, AttributeSet attr){
        super(context, attr);
        setOrientation(VERTICAL);
    }

    public MultipleView(Context context, ArrayList<String> multis, String key) {
        super(context);
        setOrientation(VERTICAL);
        this.multis = multis;
        setKeys(context, multis);
        setKey(key);
    }

    public MultipleView setKeys(ArrayList<String> keys) {
       setKeys(getContext(), keys);
       return this;
    }

    public MultipleView setKeys(Context context, ArrayList<String> keys) {
        removeAllViews();
        for (String key : keys) {
            SelectionView view = new SelectionView(context, key);
            addView(view);
            viewsMap.put(key, view);
        }
        return this;
    }

    public MultipleView setKey(String key) {
        this.key = key;
        TextView child = new TextView(getContext());
        child.setTypeface(child.getTypeface(), Typeface.BOLD);
        child.setText(key);
        addView(child, 0);
        return this;
    }

    public HashMap<String, Boolean> getMapValues() {
        HashMap<String, Boolean> result = new HashMap<>();
        for (String key : viewsMap.keySet()) {
            result.put(key, viewsMap.get(key).isChecked());
        }
        return result;
    }

    public void setValues(HashMap<String, Boolean> values) {
        for (String key : viewsMap.keySet()) {
            if (values.get(key) != null)
            viewsMap.get(key).setChecked(values.get(key));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            for (SelectionView selecView : viewsMap.values()) {
                selecView.setEnabled(false);
            }
        }
    }
}
