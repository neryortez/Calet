package com.altechonduras.reportes.views;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * TODO: document your custom view class.
 */
public class SelectionView extends AppCompatCheckBox {
    private String mKey = "M";
    private boolean mValue = false;


    public SelectionView(Context context) {
        super(context);
    }

    public SelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SelectionView(Context context, String key, boolean value) {
        super(context);
        setKey(key);
        setValue(value);
        invalidateTextPaintAndMeasurements();
    }

    public SelectionView(Context context, String key) {
        this(context, key, false);
    }

    private void invalidateTextPaintAndMeasurements() {
        this.setText(mKey);
        this.setChecked(mValue);

//        setChecked(mValue);
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getKey() {
        return mKey;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setKey(String exampleString) {
        mKey = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public boolean getValue() {
        return isChecked();
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setValue(boolean exampleColor) {
        mValue = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

}
