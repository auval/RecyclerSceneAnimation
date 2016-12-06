package com.mindtheapps.recycleranimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import android.support.transition.TransitionInflater;

/**
 * Created by amir on 12/6/16.
 */

public class MyRowView extends ConstraintLayout {
    Transition mTransition;
    TransitionManager mTransitionManager;
    //    TransitionSet mTransitionSet;
    //    boolean selected = false;
    TextView text;
    private boolean big;
    private int color;
    private Runnable enterAction = new Runnable() {
        @Override
        public void run() {
            text = (TextView) findViewById(R.id.my_text);
            text.setText("#" + Integer.toHexString(color));
        }
    };
    private Runnable exitAction = new Runnable() {
        @Override
        public void run() {

        }
    };
    private Scene mSceneLarge;
    private Scene mSceneSmall;

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
                , vg);

        /*
         * a trick to set width to match parent. The value in the xml doesn't work.
         * http://stackoverflow.com/a/30692398/1180898
         */
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp.setLayoutParams(lp);


//        TransitionInflater transitionInflater = TransitionInflater.from(getContext());
//        android.transition.TransitionManager mTransitionManager = transitionInflater.inflateTransitionManager(R.transition.transition_manager, container);

        mSceneLarge = Scene.getSceneForLayout(this, R.layout.layout_row_alert_larger, getContext());
        mSceneSmall = Scene.getSceneForLayout(this, R.layout.layout_row_alert, getContext());

        mTransitionManager = new TransitionManager();
        mTransition = new ChangeBounds();

//        mTransitionSet = new TransitionSet();
//        mTransitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
//        mTransitionSet.addTransition(new ChangeBounds());

        mSceneLarge.setEnterAction(enterAction);
        mSceneLarge.setExitAction(exitAction);
        mSceneSmall.setEnterAction(enterAction);
        mSceneSmall.setExitAction(exitAction);

        mSceneSmall.enter();

    }

    public void setColor(int color) {
        this.color = color;
        text.setText("#" + Integer.toHexString(color));
        if (ColorUtils.calculateLuminance(color) < 0.5f) {
            text.setTextColor(Color.WHITE);
        } else {
            text.setTextColor(Color.BLACK);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(color);
    }

    private void doTransition() {
        if (big) {
            mTransitionManager.setTransition(mSceneSmall, mSceneLarge, mTransition);
            mTransitionManager.transitionTo(mSceneLarge);
        } else {
            mTransitionManager.setTransition(mSceneLarge, mSceneSmall, mTransition);
            mTransitionManager.transitionTo(mSceneSmall);
        }

        // not good: instant, and doesn't keep color
//        if (big) {
//            mSceneSmall.enter();
//        } else {
//            mSceneLarge.enter();
//        }

        // not good: fade in/out only
//        if (big) {
//            TransitionManager.go(mSceneSmall);
//        } else {
//            TransitionManager.go(mSceneLarge);
//        }
    }

    public boolean isBig() {
        return big;
    }

    public void setBig(boolean big) {
        if (this.big != big) {
            this.big = big;
            doTransition();
        }
    }
}
