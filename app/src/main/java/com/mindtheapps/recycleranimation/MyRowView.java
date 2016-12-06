package com.mindtheapps.recycleranimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by amir on 12/6/16.
 */

public class MyRowView extends ConstraintLayout implements View.OnClickListener {
    boolean selected = false;
    TextView text;
    private int color;


    /**
     * I have only this constructor since I don't intend to add this class directly to a layout
     * in xml, but only via new {@link MyRowView}()
     *
     * @param p
     */
    public MyRowView(ViewGroup p) {
        super(p.getContext());
        init(this);
        /*
         * since this view is a ViewGroup, the system skips our onDraw as an optimization.
         * Now we're disabling this optimization.
         */
        setWillNotDraw(false);
    }

    private void init(ViewGroup vg) {
        LayoutInflater layoutInflater = (LayoutInflater) vg.getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View vp = layoutInflater.inflate(R.layout.layout_row_alert
//                isFirst ?
//                        /*BIG*/R.layout.layout_row_alert
//                        :/*small*/R.layout.layout_row_alert_in_multi
                , vg);

        /*
         * a trick to set width to match parent. The value in the xml doesn't work.
         * http://stackoverflow.com/a/30692398/1180898
         */
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp.setLayoutParams(lp);

        text = (TextView) vp.findViewById(R.id.my_text);

        vp.setOnClickListener(this);
    }

    public void setColor(int color) {
        this.color = color;
        text.setText("#" + Integer.toHexString(color));
        if (ColorUtils.calculateLuminance(color) < 0.5f) {
            text.setTextColor(Color.WHITE);
        } else {
            text.setTextColor(Color.BLACK);
        }
       // requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(color);
    }

    @Override
    public void onClick(View view) {
        selected = !selected;
//        Scene scene = new Scene(this,)
    }
}
