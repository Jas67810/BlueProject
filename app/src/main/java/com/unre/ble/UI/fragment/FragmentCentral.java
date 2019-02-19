package com.unre.ble.UI.fragment;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.unre.ble.EventBus.Event;
import com.unre.ble.EventBus.EventID;
import com.unre.ble.R;
import com.unre.ble.R2;
import com.unre.ble.UI.Base.BleBaseFragment;
import com.unre.ble.UI.weight.LogView;
import com.unre.ble.UnBle.BleDataParser;
import com.unre.ble.UnBle.BleGatterServiceHelper;
import com.unre.unreface.Base.BaseState;
import com.unre.unreface.Utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.Arrays;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentCentral extends BleBaseFragment {
    private final static String TAG = FragmentCentral.class.getSimpleName();
    @BindView(R2.id.qtopbar)
    QMUITopBarLayout qmuiTopBarLayout;
    @BindView(R2.id.logview)
    LogView logView;
    @OnClick(R2.id.clearbutton)
    public void clearLog(){
        logView.cleanLog();
    }
    private QMUITipDialog notifyQMUITipDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBleGatterService();
    }
    @Override
    protected View onCreateView() {
        return View.inflate(getContext(), R.layout.fragment_central, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        qmuiTopBarLayout.setTitle(R.string.central);
    }
    //////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event){
        Log.d(TAG,"onEvent:" + event);
        switch (event.id){
            case EventID.EVENTID_BLE_RECEIVER_KEYCODE:{
                if(getmBaseState() == BaseState.BASE_STATE_ONRESUME){
                    ToastUtils.showShort(getContext(), BleDataParser.tranformKEYCODE(event.arg1));
                }
            }break;
            case EventID.EVENTID_BLE_RECEIVER_DATA:{
                try {
                    logView.addLog("receiver --> " + Arrays.toString((byte[])event.object) + "\n");
                } catch (Throwable e){
                    e.printStackTrace();
                }
            }break;
            case EventID.EVENTID_BLE_RECEIVER_DEVICESCHANGE:{
                try{//maybe null
                    Set<BluetoothDevice> devices = mBleGatterServiceHelper.getBleGatterService()
                            .getCurrentRegisteredDevices();
                    logView.addLog("currentRegisterDevice -->"
                            + "count:" + devices.size()
                            + ", " + Arrays.toString(devices.toArray())
                            + "\n");
                } catch (Throwable e){
                    e.printStackTrace();
                }
            }break;
            case EventID.EVENTID_BLE_SERVICE_ADVERTISE:{
                //if(getmBaseState() == BaseState.BASE_STATE_ONPAUSE){
                notifyAdvertiseStateChange(event.arg1);
                //}
            }break;
        }
    }

    private void notifyAdvertiseStateChange(int state){
        if(getContext() != null) {
            if(state == 1){
                notifyQMUITipDialog = new QMUITipDialog.Builder(getContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(getString(R.string.advertise_success))
                        .create();
            } else if(state == 0) {
                notifyQMUITipDialog = new QMUITipDialog.Builder(getContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(getString(R.string.advertise_fail))
                        .create();
            }
            if(notifyQMUITipDialog != null){
                notifyQMUITipDialog.show();
                getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyQMUITipDialog.dismiss();
                    }
                }, 1500);
            }
        }
    }
}
