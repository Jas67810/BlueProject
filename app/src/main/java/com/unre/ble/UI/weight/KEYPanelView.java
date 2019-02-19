package com.unre.ble.UI.weight;
//
// Created by JasWorkSpace on 2019/2/18.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.unre.ble.R;
import com.unre.ble.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.qmuiteam.qmui.util.QMUIDirection.BOTTOM_TO_TOP;
import static com.qmuiteam.qmui.util.QMUIDirection.TOP_TO_BOTTOM;

public class KEYPanelView extends LinearLayout {
    @BindView(R2.id.keypanel_up)
    LinearLayout keypanelup;
    @BindView(R2.id.keypanel_down)
    LinearLayout keypaneldown;
    @BindView(R2.id.key_roundview)
    RoundMenuView roundMenuView;
    @OnClick({R2.id.key_power,R2.id.key_volumeup,R2.id.key_volumedown,
            R2.id.key_menu,R2.id.key_home,R2.id.key_back})
    public void onKeyClick(View view){
        int keycode = -100;
        switch (view.getId()){
            case R.id.key_power: keycode = KeyEvent.KEYCODE_POWER;break;
            case R.id.key_volumeup: keycode = KeyEvent.KEYCODE_VOLUME_UP;break;
            case R.id.key_volumedown: keycode = KeyEvent.KEYCODE_VOLUME_DOWN;break;
            case R.id.key_menu: keycode = KeyEvent.KEYCODE_MENU;break;
            case R.id.key_home: keycode = KeyEvent.KEYCODE_HOME;break;
            case R.id.key_back: keycode = KeyEvent.KEYCODE_BACK;break;
        }
        if(keycode != -100){
            notifyKeyPress(keycode);
        }
    }
    public KEYPanelView(Context context) {
        super(context);
        setupUI();
    }
    public KEYPanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }
    public KEYPanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }
    ///////////////////////////////////////////////////////////
    private void setupUI(){
        View view = View.inflate(getContext(), R.layout.layout_keypannel, this);
        ButterKnife.bind(this, view);
        initRoundView();
        setEnabled(false);
    }
    private RoundMenuView.RoundMenu createRoundMenu(int keycode, Bitmap icon, int color){
        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = color;
        roundMenu.strokeColor = color;
        roundMenu.icon= icon;
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyKeyPress(keycode);
            }
        };
        return roundMenu;
    }
    private void initRoundView(){
        int color = getContext().getColor(R.color.colorAccent);
        Bitmap arr = BitmapFactory.decodeResource(getResources(), R.mipmap.right_icon);
        roundMenuView.addRoundMenu(createRoundMenu(KeyEvent.KEYCODE_DPAD_RIGHT, arr, color));
        roundMenuView.addRoundMenu(createRoundMenu(KeyEvent.KEYCODE_DPAD_DOWN, arr, color));
        roundMenuView.addRoundMenu(createRoundMenu(KeyEvent.KEYCODE_DPAD_LEFT, arr, color));
        roundMenuView.addRoundMenu(createRoundMenu(KeyEvent.KEYCODE_DPAD_UP, arr, color));
        roundMenuView.setCoreMenu(color, color, color
                , 1, 0.43
                ,BitmapFactory.decodeResource(getResources(),R.mipmap.icon_ok)
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyKeyPress(KeyEvent.KEYCODE_DPAD_CENTER);
                    }
                }
        );
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        QMUIViewHelper.slideIn(keypanelup, 500, null, true, TOP_TO_BOTTOM);
        QMUIViewHelper.slideIn(keypaneldown, 500, null, true, BOTTOM_TO_TOP);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                QMUIViewHelper.fadeIn(roundMenuView, 1000,null, true);
            }
        }, 500);
    }
    ///////////////////////////////////////////////////////////
    private long lastpresstime;
    private KeyPannelListener mKeyPannelListener;
    public void setKeyPannelListener(KeyPannelListener listener){
        mKeyPannelListener = listener;
    }
    public interface KeyPannelListener{
        void onUnBleKeyDown(int keycode);
    }
    private synchronized void notifyKeyPress(int keycode){
        if(System.currentTimeMillis() - lastpresstime > 200){//200ms一次
            lastpresstime = System.currentTimeMillis();
            if(mKeyPannelListener != null){
                mKeyPannelListener.onUnBleKeyDown(keycode);
            }
        }
    }
    /////////////////////////////////////////////////////////////
    public void enableKeyPanel(boolean enable){
        if(isEnabled() != enable) {
            setEnabled(enable);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!isEnabled()) return false;
        return super.dispatchTouchEvent(ev);
    }
}
