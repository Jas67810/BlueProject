package com.unre.ble.UI.weight;
//
// Created by JasWorkSpace on 2019/2/18.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

public class ConnectBleButton extends QMUIRoundButton {
    public ConnectBleButton(Context context) {
        super(context);
    }
    public ConnectBleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ConnectBleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.d("Jas","onVisibilityChanged");
        checkAndStartAnimation();
    }
    ///////////////////////////////////////
    private Animator mAnimator;
    private AlphaAnimation animation(){
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation1.setDuration(1500);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.REVERSE);
        setAnimation(alphaAnimation1);
        alphaAnimation1.start();
        return alphaAnimation1;
    }
    public synchronized void checkAndStartAnimation(){
        Log.d("Jas","checkAndStartAnimation");
        if(isAttachedToWindow()){
            if(getVisibility() == VISIBLE){
                //if(mAnimator == null){
                    animation();
                    return;
                //}
            }
        }
        if(getAnimation() != null) {
            getAnimation().cancel();
        }
        if(mAnimator != null){
            QMUIViewHelper.clearValueAnimator(mAnimator);
            mAnimator = null;
        }
    }

}
