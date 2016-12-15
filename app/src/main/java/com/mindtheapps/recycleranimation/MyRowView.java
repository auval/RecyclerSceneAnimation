package com.mindtheapps.recycleranimation;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mindtheapps.recycleranimation.databinding.LayoutRowAlertBinding;
import com.mindtheapps.recycleranimation.databinding.LayoutRowAlertLargerBinding;

/**
 * Created by amir on 12/6/16.
 */
public class MyRowView extends ConstraintLayout {
    LayoutRowAlertLargerBinding asLBind;
    LayoutRowAlertBinding asSBind;
    Transition mTransition;
    TransitionManager mTransitionManager;
    MyRowData myRowData;
    private Scene mSceneLarge;
    private Scene mSceneSmall;

    /**
     * I have only this constructor since I don't intend to add this class directly to a layout
     * in xml, but only via new {@link MyRowView}()
     *
     * @param vg
     */
    public MyRowView(ViewGroup vg) {
        super(vg.getContext());

        LayoutInflater layoutInflater = (LayoutInflater) vg.getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        asSBind = LayoutRowAlertBinding.inflate(layoutInflater, vg, false);
        asLBind = LayoutRowAlertLargerBinding.inflate(layoutInflater, vg,
                false);

        // a trick to force match_parent on this row
        asLBind.rowGv.getLayoutParams().width = vg.getWidth();
        asSBind.rowGv.getLayoutParams().width = vg.getWidth();

        mSceneLarge = new Scene(this, asLBind.getRoot());

        mSceneSmall = new Scene(this, asSBind.getRoot());

        mTransitionManager = new TransitionManager();
        mTransition = new ChangeBounds();

        /*
         * The MATCH_PARENT value in the xml doesn't work.
         * the trick to set width to match parent no longer work either:
         * http://stackoverflow.com/a/30692398/1180898
         */

    }


    public void bindThis(MyRowData data) {

        asLBind.setData(data);
        asSBind.setData(data);
        if (data.isDirty()) {
            gotoScene(data.isBig());
            data.setDirty(false);
        }
        myRowData = data;

    }

    private void gotoScene(boolean big) {

        Scene from = !big ? mSceneLarge : mSceneSmall;
        Scene to = big ? mSceneLarge : mSceneSmall;

        mTransitionManager.setTransition(from, to, mTransition);
        mTransitionManager.transitionTo(to);
    }



}
