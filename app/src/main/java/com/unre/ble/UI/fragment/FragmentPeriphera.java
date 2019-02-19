package com.unre.ble.UI.fragment;
//
// Created by JasWorkSpace on 2019/2/18.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.unre.ble.R;
import com.unre.ble.R2;
import com.unre.ble.UI.Base.BleBaseFragment;
import com.unre.ble.UI.weight.ConnectBleButton;
import com.unre.ble.UI.weight.KEYPanelView;
import com.unre.ble.UnBle.Client.BleCharacterService;
import com.unre.ble.UnBle.Client.BleCharacterState;
import com.unre.ble.UnBle.Client.BleClientService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
public class FragmentPeriphera extends BleBaseFragment implements KEYPanelView.KeyPannelListener, BleClientService.BleClientServiceListener {
    private final static String TAG = FragmentPeriphera.class.getSimpleName();
    private final static int REQUESTCODE_SEARCH = 100;
    @BindView(R2.id.topbar)
    QMUITopBarLayout qmuiTopBarLayout;
    @BindView(R2.id.keypanel)
    KEYPanelView keyPanelView;
    @BindView(R2.id.connectble)
    ConnectBleButton connectBleButton;
    @OnClick(R2.id.connectble)
    public void onConnectClick(){
        startFragmentForResult(new FragmentSearch(), REQUESTCODE_SEARCH);
    }
    private QMUIPopup mNormalPopup;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBleClentService();
    }
    @Override
    protected View onCreateView() {
        Log.d("Jas","onCreateView");
        View view = View.inflate(getContext(), R.layout.fragment_periphera, null);
        ButterKnife.bind(this, view);
        qmuiTopBarLayout.setTitle(R.string.periphera_key);
        qmuiTopBarLayout.addRightTextButton(R.string.connect_switch, 100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectClick();
            }
        });
        keyPanelView.setKeyPannelListener(this);
        return view;
    }
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        Log.d(TAG, "onFragmentResult requestCode:" + requestCode + ",resultCode:" + resultCode );
    }
    @Override
    public void onResume() {
        super.onResume();
        if(getBleClientService() != null) {
            getBleClientService().setBleClientServiceListener(this);
            showConnectBleButton(!getBleClientService().hasBleCharacterService());
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(getBleClientService() != null) {
            getBleClientService().setBleClientServiceListener(null);
        }
    }
    ///////////////////////////////////////////////////////////////////
    @Override
    public void onUnBleKeyDown(int keycode) {
        Log.d(TAG, "onUnBleKeyDown:" + KeyEvent.keyCodeToString(keycode));
        if(getBleClientService() != null){
            getBleClientService().sendBleKeyCode(keycode);
        }
    }
    //////////////////////////////////////////////////////////////////
    @Override
    public void onServiceConnectionChange(boolean bind) {
        super.onServiceConnectionChange(bind);
        if(getBleClientService() != null) {
            getBleClientService().setBleClientServiceListener(this);
            showConnectBleButton(!getBleClientService().hasBleCharacterService());
        }
    }
    private synchronized void showConnectBleButton(boolean show){
        Log.d(TAG, "showConnectBleButton:" + show);
        if(show){
            connectBleButton.setEnabled(true);
            keyPanelView.setEnabled(false);
            QMUIViewHelper.fadeIn(connectBleButton,200,null,true);
        } else {
            connectBleButton.setEnabled(false);
            keyPanelView.setEnabled(true);
            QMUIViewHelper.fadeOut(connectBleButton,200,null,true);
        }
    }
    ////////////////////////////////////////////////
    @Override
    public void onBleCharacterServiceChange(BleCharacterService bleCharacterService) {
        showConnectBleButton(bleCharacterService ==null);
    }

    @Override
    public void onBleCharacterConnectStateChange(String mac, boolean connected) {
        if(getBleClientService() != null){
            if(TextUtils.equals(mac, getBleClientService().getBleCharacterServiceMac())){
                qmuiTopBarLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!connected){
                            initNormalPopupIfNeed();
                            mNormalPopup.show(qmuiTopBarLayout);
                        } else {
                            if(mNormalPopup != null){
                                mNormalPopup.dismiss();
                            }
                        }
                    }
                });

            }
        }
    }

    @Override
    public void onBleCharacterStateChange(String mac, BleCharacterState oldState, BleCharacterState newState) {

    }
    ////////////////////////////////////////////////////////////
    private void initNormalPopupIfNeed() {
        if (mNormalPopup == null) {
            mNormalPopup = new QMUIPopup(getContext(), QMUIPopup.DIRECTION_NONE);
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(mNormalPopup.generateLayoutParam(MATCH_PARENT, WRAP_CONTENT));
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
            int padding = QMUIDisplayHelper.dp2px(getContext(), 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(R.string.connect_try);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color_description));
            mNormalPopup.setContentView(textView);
            mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_LEFT);
            mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
        }
    }
}
