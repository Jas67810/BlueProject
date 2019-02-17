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

import com.qmuiteam.qmui.widget.QMUIAppBarLayout;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.unre.ble.EventBus.Event;
import com.unre.ble.EventBus.EventID;
import com.unre.ble.EventBus.EventUtils;
import com.unre.ble.R;
import com.unre.ble.R2;
import com.unre.ble.UI.Base.BleBaseFragment;
import com.unre.ble.UI.weight.LogView;
import com.unre.ble.UnBle.BleDataParser;
import com.unre.ble.UnBle.BleGatterServiceHelper;
import com.unre.unreface.Base.BaseState;

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBleGatterServiceHelper = new BleGatterServiceHelper(getContext());
        mBleGatterServiceHelper.bindBleGatterService();
    }
    @Override
    protected View onCreateView() {
        View view = View.inflate(getContext(), R.layout.fragment_central, null);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qmuiTopBarLayout.setTitle(R.string.central);
    }
    @Override
    public void onResume() {
        super.onResume();
        mBleGatterServiceHelper.bindBleGatterService();//make sure its bind.
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mBleGatterServiceHelper.unbindBleGatterService();
    }
    //////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event){
        Log.d(TAG,"onEvent:" + event);
        switch (event.id){
            case EventID.EVENTID_BLE_RECEIVER_KEYCODE:{
                if(getmBaseState() == BaseState.BASE_STATE_ONPAUSE){
                    Toast.makeText(getContext(), BleDataParser.tranformKEYCODE(event.arg1), Toast.LENGTH_SHORT).show();
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
        }
    }
}
