package com.mindtheapps.recycleranimation;

import android.databinding.BaseObservable;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

/**
 * Created by amir on 12/6/16.
 */

public class MyRowData extends BaseObservable {

    private boolean big;
    private int color;
    private String colorName;
    private boolean dirty;
    private int textColor;

    public MyRowData() {
    }

    public String getColorName() {
        return colorName;
    }

    public MyRowData setColorName(String colorName) {
        this.colorName = colorName;
        return this;
    }

    public int getColor() {
        return color;
    }

    public MyRowData setColor(int color) {
        this.color = color;
        if (ColorUtils.calculateLuminance(color) < 0.5f) {
            textColor = Color.WHITE;
        } else {
            textColor = Color.BLACK;
        }
        return this;
    }

    public boolean isBig() {
        return big;
    }

    public MyRowData setBig(boolean big) {
        if (this.big != big) {
            dirty = true;
        }
        this.big = big;
        return this;
    }

    public void callNotify() {
        notifyChange();
    }

    public int getTextColor() {
        return textColor;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
