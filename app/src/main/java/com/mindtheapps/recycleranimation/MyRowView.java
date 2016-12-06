package com.mindtheapps.recycleranimation;

import android.content.Context;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by amir on 12/6/16.
 */

public class MyRowView extends ConstraintLayout {
    TextView text;
    private int color;


    /**
     * I have only this constructor since I don't intend to add this class directly to a layout
     * in xml, but only via new {@link MyRowView}()
     *
     * @param context
     */
    public MyRowView(Context context) {
        super(context);
        init(this);
        /*
         * since this view is a ViewGroup, the system skips our onDraw as an optimization.
         * Now we're disabling this optimization.
         */
        setWillNotDraw(false);
    }

//    /**
//     * Measure the view to end up as a square, based on the minimum of the height and width.
//     */
//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int minDimension = Math.min(measuredWidth, measuredHeight);
//
//        super.onMeasure(MeasureSpec.makeMeasureSpec(minDimension+5, widthMode),
//                MeasureSpec.makeMeasureSpec(minDimension+5, heightMode));
//    }


    private void init(ViewGroup vg) {
        LayoutInflater layoutInflater = (LayoutInflater) vg.getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View vp = layoutInflater.inflate(R.layout.layout_row_alert
//                isFirst ?
//                        /*BIG*/R.layout.layout_row_alert
//                        :/*small*/R.layout.layout_row_alert_in_multi
                , this);

        text = (TextView) vp.findViewById(R.id.my_text);
    }

    public void setColor(int color) {
        this.color = color;
        text.setText("#" + Integer.toHexString(color));
//        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(color);
    }


}
