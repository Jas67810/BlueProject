package com.unre.ble.UnBle.Client;
//
// Created by JasWorkSpace on 2019/2/18.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.inuker.bluetooth.library.BluetoothClient;
import com.unre.ble.UnBle.BleDataParser;
import com.unre.ble.UnBle.BleService;

public class BleClientService extends BleService implements BleCharacterService.BleCharacterServiceListener {
    private final static String TAG = BleClentServiceBinder.class.getSimpleName();
    public class BleClentServiceBinder extends Binder{
        BleClientService getBleClentService(){
            return BleClientService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BleClentServiceBinder();
    }
    /////////////////////////////////////////////////////////////
    // API
    public void sendBleKeyCode(int keycode){
        Message m = createMessgae(BLE_MSG_SENDKEYCODE);
        m.arg1 = keycode;
        m.sendToTarget();
    }
    public BluetoothClient getBluetoothClient(){
        return ClientManager.getClient();
    }
    public void connectBleCharacterService(String mac){
        Message m = createMessgae(BLE_MSG_NEWCONNECT);
        m.obj = mac;
        m.sendToTarget();
    }
    public synchronized void setConnectedBleCharacterService(BleCharacterService bleCharacterService){
        releaseBleCharacterService();
        this.bleCharacterService = bleCharacterService;
    }
    public void disconnectBleCharacterService(){
        createMessgae(BLE_MSG_DISCONNECT).sendToTarget();
    }
    public synchronized String getBleCharacterServiceMac(){
        if(bleCharacterService != null){
            return bleCharacterService.mac;
        }
        return null;
    }
    public synchronized boolean hasBleCharacterService(){
        return (bleCharacterService != null);
    }
    ////////////////////////////////////////////////////////////
    private final static int BLE_MSG_NEWCONNECT = 101;
    private final static int BLE_MSG_TRYCONNECT = 102;
    private final static int BLE_MSG_DISCONNECT = 103;
    private final static int BLE_MSG_SENDKEYCODE = 104;
    @Override
    protected void handleBleMessage(Message msg) {
        super.handleBleMessage(msg);
        switch (msg.what){
            case BLE_MSG_NEWCONNECT:{
                if(msg.obj != null){
                    connectNewBleBleCharacterService((String) msg.obj);
                }
            }break;
            case BLE_MSG_TRYCONNECT:{
                connectBleCharacterServiceIfNeed();
            }break;
            case BLE_MSG_DISCONNECT:{
                releaseBleCharacterService();
            }break;
            case BLE_MSG_SENDKEYCODE:{
                byte[] data = BleDataParser.getBleKeyCodeData(msg.arg1);
                if(data.length > 0 && bleCharacterService != null){
                    bleCharacterService.writeData(data);
                }
            }break;
        }
    }
    //////////////////////////////////////////////
    private volatile BleCharacterService bleCharacterService;
    @Override
    public void onBleCharacterStateChange(String mac, BleCharacterState oldState, BleCharacterState newState) {
        notifyBleCharacterStateChange(mac, oldState, newState);
    }
    @Override
    public void onBleCharacterConnectStateChange(String mac, boolean connected) {
        if(bleCharacterService != null && TextUtils.equals(mac, bleCharacterService.mac)){
            createMessgae(BLE_MSG_TRYCONNECT).sendToTarget();
        }
        notifyBleCharacterConnectStateChange(mac, connected);
    }
    private synchronized void releaseBleCharacterService(){
        if(bleCharacterService != null){
            bleCharacterService.tearDown();
            bleCharacterService.setBleCharacterServiceListener(null);
            bleCharacterService = null;
            notifyBleCharacterServiceChange();
        }
    }
    private synchronized void setBleCharacterService(BleCharacterService service){
        assert (service != null);
        assert (bleCharacterService == null);
        bleCharacterService = service;
        bleCharacterService.setBleCharacterServiceListener(this);
        notifyBleCharacterServiceChange();
    }
    private synchronized void connectBleCharacterServiceIfNeed(){
        if(bleCharacterService != null){
            bleCharacterService.startConnect();
        }
    }
    private synchronized boolean connectNewBleBleCharacterService(String mac){
        releaseBleCharacterService();
        bleCharacterService = new BleCharacterService(mac);
        setBleCharacterService(bleCharacterService);
        bleCharacterService.startConnect();
        return true;
    }
    ////////////////////////////////////////////////////////////
    private BleClientServiceListener mBleClientServiceListener;
    public interface BleClientServiceListener{
        void onBleCharacterServiceChange(BleCharacterService bleCharacterService);
        void onBleCharacterConnectStateChange(String mac, boolean connected);
        void onBleCharacterStateChange(String mac, BleCharacterState oldState, BleCharacterState newState);
    }
    public void setBleClientServiceListener(BleClientServiceListener listener){
        mBleClientServiceListener = listener;
    }
    private synchronized void notifyBleCharacterServiceChange(){
        if(mBleClientServiceListener != null){
            mBleClientServiceListener.onBleCharacterServiceChange(bleCharacterService);
        }
    }
    private synchronized void notifyBleCharacterConnectStateChange(String mac, boolean connected){
        if(mBleClientServiceListener != null){
            mBleClientServiceListener.onBleCharacterConnectStateChange(mac, connected);
        }
    }
    private synchronized void notifyBleCharacterStateChange(String mac, BleCharacterState oldState, BleCharacterState newState){
        if(mBleClientServiceListener != null){
            mBleClientServiceListener.onBleCharacterStateChange(mac, oldState, newState);
        }
    }
}
