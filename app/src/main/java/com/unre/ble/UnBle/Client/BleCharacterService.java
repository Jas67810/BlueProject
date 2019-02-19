package com.unre.ble.UnBle.Client;
//
// Created by JasWorkSpace on 2019/2/19.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.bluetooth.BluetoothDevice;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.unre.unreble.Service.UnBleProfile;

import java.util.List;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

public class BleCharacterService extends BleConnectStatusListener implements BleWriteResponse {
    private final static String TAG = BleCharacterService.class.getSimpleName();
    public final String mac;
    private UUID serviceUUID;
    private UUID characterUUID;
    private BleCharacterState mBleCharacterState;
    private boolean connected = false;
    public BleCharacterService(String mac){
        assert (!TextUtils.isEmpty(mac));
        this.mac = mac;
        mBleCharacterState = BleCharacterState.STATE_IDLE;
        ClientManager.getClient().registerConnectStatusListener(mac, this);
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        ClientManager.getClient().unregisterConnectStatusListener(mac, this);
    }
    @Override
    public void onConnectStatusChanged(String mac, int status) {
        if(TextUtils.equals(this.mac, mac)){
            setConnectedState(status == STATUS_CONNECTED);
        }
    }
    private synchronized boolean setConnectedState(boolean connected){
        if(this.connected != connected){
            this.connected = connected;
            notifyBleCharacterConnectStateChange(this.connected);
        }
        return this.connected;
    }
    private synchronized void notifyBleCharacterConnectStateChange(boolean connected){
        if(mBleCharacterServiceListener != null){
            mBleCharacterServiceListener.onBleCharacterConnectStateChange(this.mac, connected);
        }
    }
    //////////////////
    private synchronized void setDisConnect(){
        setBleCharacterState(BleCharacterState.STATE_DISCONNECTED);
    }
    private synchronized BleCharacterState setBleCharacterState(BleCharacterState state){
        Log.d(TAG, "setBleCharacterState("
                + BleCharacterState.tranformStateToString(state)
                + ", currentState:" + BleCharacterState.tranformStateToString(mBleCharacterState)
                + ")"
        );
        if(isRelease()) return BleCharacterState.STATE_RELEASE;
        if(state != mBleCharacterState){
            BleCharacterState oldState = mBleCharacterState;
            mBleCharacterState = state;
            notifyBleCharacterStateChange(oldState, mBleCharacterState);
        }
        return mBleCharacterState;
    }
    private synchronized void notifyBleCharacterStateChange(BleCharacterState old, BleCharacterState state){
        if(mBleCharacterServiceListener!=null){
            mBleCharacterServiceListener.onBleCharacterStateChange(this.mac, old, state);
        }
    }
    public boolean isRelease(){
        return mBleCharacterState == BleCharacterState.STATE_RELEASE;
    }
    ////////////////////////////////////
    public synchronized boolean startConnect(){
        if(mBleCharacterState == BleCharacterState.STATE_IDLE){
            connectDevice(BluetoothUtils.getRemoteDevice(this.mac));
        }
        return false;
    }
    public synchronized boolean tearDown(){
        setBleCharacterState(BleCharacterState.STATE_RELEASE);
        ClientManager.getClient().disconnect(mac);
        return true;
    }
    public boolean isConnected() {
        return connected;
    }
    public boolean isReady(){
        return mBleCharacterState == BleCharacterState.STATE_CONNECTED;
    }
    public BleCharacterState getCurrentBleCharacterState(){
        return mBleCharacterState;
    }
    ///////////////////////////////////////////////////
    public boolean writeData(byte[] data){
        if(connected && mBleCharacterState == BleCharacterState.STATE_CONNECTED) {
            ClientManager.getClient().write(mac, serviceUUID, characterUUID,
                    data, this);
            return true;
        }
        return false;
    }
    @Override
    public void onResponse(int code) {
        Log.d(TAG,"writeData onResponse:" + code);
        if (code == REQUEST_SUCCESS) {
            //CommonUtils.toast("wirte success");
        } else {
            //CommonUtils.toast("wirte failed");
        }
    }
    ////////////////////////////////////////////////////////////
    private boolean isNeedScan(){
        if(isRelease()) return false;
        if(mBleCharacterState == BleCharacterState.STATE_CONNECTING) return false;
        return !connected;
    }
    private boolean connectDevice(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "connectDevice:" + bluetoothDevice.getName() + "," + bluetoothDevice.getAddress());
        if(isNeedScan()) {
            BleConnectOptions options = new BleConnectOptions.Builder()
                    .setConnectRetry(3)
                    .setConnectTimeout(20000)
                    .setServiceDiscoverRetry(3)
                    .setServiceDiscoverTimeout(10000)
                    .build();
            ClientManager.getClient().connect(bluetoothDevice.getAddress(), options, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile profile) {
                    if (code == REQUEST_SUCCESS) {
                        if (connectBleGattProfile(profile)) {
                            return;
                        }
                    }
                    setDisConnect();
                }
            });
            setBleCharacterState(BleCharacterState.STATE_CONNECTING);
        }
        return true;
    }
    private boolean connectBleGattProfile(BleGattProfile profile){
        List<BleGattService> services = profile.getServices();
        for (BleGattService service : services) {
            UUID serviceUUID = service.getUUID();
            Log.d(TAG,"connectBleGattProfile serviceUUID:" + serviceUUID);
            if(TextUtils.equals(serviceUUID.toString(), UnBleProfile.UNBLE_SERVICE.toString())){
                List<BleGattCharacter> characters = service.getCharacters();
                byte[] data = new byte[]{0x02};
                for (BleGattCharacter character : characters) {
                    UUID characterUUID = character.getUuid();
                    Log.d(TAG,"connectBleGattProfile characterUUID:" + characterUUID);
                    if(TextUtils.equals(characterUUID.toString(), UnBleProfile.CURRENT_TIME.toString())){
//                        int permissions = character.getPermissions();
//                        if(((permissions & BluetoothGattCharacteristic.PERMISSION_WRITE) != 0)
//                                &&((permissions & BluetoothGattCharacteristic.PERMISSION_READ) != 0)) {
//                            return startListener(serviceUUID, characterUUID);
//                        } else {
//                            Log.d(TAG, "why no READ|WRITE permission !!!!!!!!");
//                        }
                        return startListener(serviceUUID, characterUUID);
                    }
                }
            }
        }
        return false;
    }
    private boolean startListener(UUID serviceUUID, UUID characterUUID){
        Log.d(TAG, "startListener...");
        if(mBleCharacterState == BleCharacterState.STATE_CONNECTING) {
            this.serviceUUID = serviceUUID;
            this.characterUUID = characterUUID;
            setBleCharacterState(BleCharacterState.STATE_CONNECTED);
            return true;
        }
        return false;
    }
    /////////////////////////////////////////
    private BleCharacterServiceListener mBleCharacterServiceListener;
    public void setBleCharacterServiceListener(BleCharacterServiceListener listener){
        mBleCharacterServiceListener = listener;
    }
    public interface BleCharacterServiceListener{
        void onBleCharacterStateChange(String mac,BleCharacterState oldState, BleCharacterState newState);
        void onBleCharacterConnectStateChange(String mac, boolean connected);
    }
}
