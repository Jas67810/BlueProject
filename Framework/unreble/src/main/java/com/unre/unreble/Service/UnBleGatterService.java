package com.unre.unreble.Service;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.util.Log;
import com.unre.unreble.Common.UnBleImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UnBleGatterService extends UnBleImpl {
    private final static String TAG = UnBleGatterService.class.getSimpleName();
    private BluetoothGattServer mBluetoothGattServer;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    /* Collection of notification subscribers */
    private Set<BluetoothDevice> mRegisteredDevices = new HashSet<>();
    private TimerReceiver mTimerReceiver;
    public UnBleGatterService(Context context) {
        super(context);
    }
    @Override
    public synchronized boolean setup() {
        Log.d(TAG, "--- setup ---");
        boolean setup = super.setup();
        if(setup){
            registerBleServiceReceiver();
            if(isBleEnable()){
                startBle();
            } else {
                enbaleBle();
            }
        }
        return setup;
    }
    @Override
    public synchronized boolean teardown() {
        Log.d(TAG, "--- teardown ---");
        unRegisterBleServiceReceiver();
        if(isBleEnable()){
            stopBle();
        }
        return super.teardown();
    }
    @Override
    public void onBluetoothAdapterStateChange(int state) {
        super.onBluetoothAdapterStateChange(state);
        if(state == BluetoothAdapter.STATE_ON){
            startBle();
        } else if(state == BluetoothAdapter.STATE_OFF){
            stopBle();
        }
    }
    /////////////////////////////////////
    // API
    private void startBle(){
        Log.d(TAG, "Bluetooth enabled...starting services");
        startAdvertising();
        startServer();
    }
    private void stopBle(){
        Log.d(TAG, "Bluetooth enabled...stop services");
        stopServer();
        stopAdvertising();
    }
    private void registerBleServiceReceiver(){
        if(mTimerReceiver == null){
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            mContext.registerReceiver(mTimerReceiver = new TimerReceiver(),filter);
        }
    }
    private void unRegisterBleServiceReceiver(){
        if(mTimerReceiver != null){
            mContext.unregisterReceiver(mTimerReceiver);
            mTimerReceiver = null;
        }
    }
    ////////////////////////////////////////////////////////////
    private void startServer() {
        mBluetoothGattServer = mBluetoothManager.openGattServer(mContext, mGattServerCallback);
        if (mBluetoothGattServer == null) {
            Log.w(TAG, "Unable to create GATT server");
            return;
        }
        mBluetoothGattServer.addService(UnBleProfile.createUnreBleService());
    }
    private void stopServer() {
        if (mBluetoothGattServer == null) return;
        mBluetoothGattServer.close();
    }
    ///////////////////////////////////////////////////////////////
    /**
     * Begin advertising over Bluetooth that this device is connectable
     * and supports the Current Time Service.
     */
    private void startAdvertising() {
        BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        if (mBluetoothLeAdvertiser == null) {
            Log.w(TAG, "Failed to create advertiser");
            return;
        }
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .build();
        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(false)
                .addServiceUuid(new ParcelUuid(UnBleProfile.UNBLE_SERVICE))
                .build();
        mBluetoothLeAdvertiser
                .startAdvertising(settings, data, mAdvertiseCallback);
    }
    private void stopAdvertising() {
        if (mBluetoothLeAdvertiser == null) return;
        mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
    }
    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.i(TAG, "######## LE Advertise Started ###########");
        }
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.w(TAG, "######### LE Advertise Failed: "+errorCode + " #########");
        }
    };
    ////////////////////////////////////////////////////////////////////////////
    private BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "BluetoothDevice CONNECTED: " + device);
                if(mRegisteredDevices.add(device)){
                    notifyRegisteredDevicesChange();
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "BluetoothDevice DISCONNECTED: " + device);
                //Remove device from any active subscriptions
                if(mRegisteredDevices.remove(device)) {
                    notifyRegisteredDevicesChange();
                }
            }
        }
        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            notifyCharacteristicWrite(offset, value);//通知外部接收
            //通知外围设备接收到了，如果需要告诉外围设备成功与否，修改返回的 value
            mBluetoothGattServer.sendResponse(device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    0,
                    null);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset,
                                                BluetoothGattCharacteristic characteristic) {
            long now = System.currentTimeMillis();
            if (UnBleProfile.CURRENT_TIME.equals(characteristic.getUuid())) {
                //外围设备读取中心设备时间
                Log.i(TAG, "Read CurrentTime");
                mBluetoothGattServer.sendResponse(device,
                        requestId,
                        BluetoothGatt.GATT_SUCCESS,
                        0,
                        UnBleProfile.getExactTime(now, UnBleProfile.ADJUST_NONE));
            } else {
                // Invalid characteristic
                Log.w(TAG, "Invalid Characteristic Read: " + characteristic.getUuid());
                mBluetoothGattServer.sendResponse(device,
                        requestId,
                        BluetoothGatt.GATT_FAILURE,
                        0,
                        null);
            }
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset,
                                            BluetoothGattDescriptor descriptor) {
            Log.d(TAG, "onDescriptorReadRequest");
            if (UnBleProfile.CLIENT_CONFIG.equals(descriptor.getUuid())) {
                Log.d(TAG, "Config descriptor read");
                byte[] returnValue;
                if (mRegisteredDevices.contains(device)) {
                    returnValue = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
                } else {
                    returnValue = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
                }
                mBluetoothGattServer.sendResponse(device,
                        requestId,
                        BluetoothGatt.GATT_FAILURE,
                        0,
                        returnValue);
            } else {
                Log.w(TAG, "Unknown descriptor read request");
                mBluetoothGattServer.sendResponse(device,
                        requestId,
                        BluetoothGatt.GATT_FAILURE,
                        0,
                        null);
            }
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId,
                                             BluetoothGattDescriptor descriptor,
                                             boolean preparedWrite, boolean responseNeeded,
                                             int offset, byte[] value) {
            Log.d(TAG, "onDescriptorWriteRequest:" + Arrays.toString(value));
            if (UnBleProfile.CLIENT_CONFIG.equals(descriptor.getUuid())) {
                if (Arrays.equals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, value)) {
                    Log.d(TAG, "Subscribe device to notifications: " + device);
                    if(mRegisteredDevices.add(device)) {
                        notifyRegisteredDevicesChange();
                    }
                } else if (Arrays.equals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE, value)) {
                    Log.d(TAG, "Unsubscribe device from notifications: " + device);
                    if(mRegisteredDevices.remove(device)) {
                        notifyRegisteredDevicesChange();
                    }
                }
                if (responseNeeded) {
                    mBluetoothGattServer.sendResponse(device,
                            requestId,
                            BluetoothGatt.GATT_SUCCESS,
                            0,
                            null);
                }
            } else {
                Log.w(TAG, "UnkonCharacteristicReadRequestnown descriptor write request");
                if (responseNeeded) {
                    mBluetoothGattServer.sendResponse(device,
                            requestId,
                            BluetoothGatt.GATT_FAILURE,
                            0,
                            null);
                }
            }
        }
    };
    /////////////////////////////////////////////////////////
    //心跳包
    private class TimerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            byte adjustReason;
            switch (intent.getAction()) {
                case Intent.ACTION_TIME_CHANGED:
                    adjustReason = UnBleProfile.ADJUST_MANUAL;
                    break;
                case Intent.ACTION_TIMEZONE_CHANGED:
                    adjustReason = UnBleProfile.ADJUST_TIMEZONE;
                    break;
                default:
                case Intent.ACTION_TIME_TICK:
                    adjustReason = UnBleProfile.ADJUST_NONE;
                    break;
            }
            long now = System.currentTimeMillis();
            notifyRegisteredDevices(now, adjustReason);
        }
    }
    private void notifyRegisteredDevices(long timestamp, byte adjustReason) {
        if (mRegisteredDevices.isEmpty()) {
            Log.i(TAG, "No subscribers registered");
            return;
        }
        byte[] exactTime = UnBleProfile.getExactTime(timestamp, adjustReason);
        Log.i(TAG, "Sending update to " + mRegisteredDevices.size() + " subscribers");
        for (BluetoothDevice device : mRegisteredDevices) {
            BluetoothGattCharacteristic timeCharacteristic = mBluetoothGattServer
                    .getService(UnBleProfile.UNBLE_SERVICE)
                    .getCharacteristic(UnBleProfile.CURRENT_TIME);
            timeCharacteristic.setValue(exactTime);
            mBluetoothGattServer.notifyCharacteristicChanged(device, timeCharacteristic, false);
        }
    }
    /////////////////////////////////////////////////////////
    private UnBleServiceListener unBleServiceListener;
    public void setUnBleServiceListener(UnBleServiceListener listener){
        unBleServiceListener = listener;
    }
    private synchronized void notifyCharacteristicWrite(int offset,byte[] value){
        if(unBleServiceListener!=null){
            unBleServiceListener.onCharacteristicWrite(offset, value);
        } else {
            Log.d(TAG, "notifyCharacteristicWrite but no listener");
        }
    }
    private synchronized void notifyRegisteredDevicesChange(){
        Log.d(TAG, "notifyRegisteredDevicesChange:" + debugRegisteredDevices());
        if(unBleServiceListener!=null){
            unBleServiceListener.onRegisteredDevicesChange();
        } else {
            Log.d(TAG, "notifyRegisteredDevicesChange but no listener");
        }
    }
    /////////////////////////////
    public String debugRegisteredDevices(){
        if(mRegisteredDevices != null) {
            return Arrays.toString(mRegisteredDevices.toArray());
        }
        return "null";
    }
    public Set<BluetoothDevice> getCurrentRegisteredDevices(){
        return mRegisteredDevices;
    }
}
