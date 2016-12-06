package com.mindtheapps.recycleranimation;

/**
 * Created by amir on 12/6/16.
 */

public class MyRowData {
    private int color;

    public MyRowData(int color) {
        // notice that "this" means "of this object", while without this - it is the parameter
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
