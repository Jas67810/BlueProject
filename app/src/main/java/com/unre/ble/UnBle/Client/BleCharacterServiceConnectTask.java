package com.unre.ble.UnBle.Client;
//
// Created by JasWorkSpace on 2019/2/19.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.unre.ble.R;
import com.unre.unreface.Utils.ToastUtils;

import java.util.Arrays;

import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_LOADING;

public class BleCharacterServiceConnectTask extends AsyncTask<String,String,BleCharacterService> implements BleCharacterService.BleCharacterServiceListener {
    private final static String TAG = BleCharacterServiceConnectTask.class.getSimpleName();
    private QMUITipDialog qmuiTipDialog;
    private Context context;
    public BleCharacterServiceConnectTask(Context context){
        super();
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute");
        qmuiTipDialog = new QMUITipDialog.Builder(context)
                                        .setTipWord(context.getString(R.string.searching))
                                        .setIconType(ICON_TYPE_LOADING)
                                        .create();
        qmuiTipDialog.setCanceledOnTouchOutside(false);
        qmuiTipDialog.setCancelable(false);
        qmuiTipDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                Log.d(TAG, "onKey:" + KeyEvent.keyCodeToString(keyCode));
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    cancelByCustom();
                    return true;
                }
                return false;
            }
        });
        qmuiTipDialog.show();
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, "onProgressUpdate:" + Arrays.toString(values));
        parserCommond(values[0]);
    }
    @Override
    protected void onPostExecute(BleCharacterService bleCharacterService) {
        super.onPostExecute(bleCharacterService);
        Log.d(TAG, "onPostExecute:" + bleCharacterService.mac
                        + ", isConnected:" + bleCharacterService.isConnected()
                        + ", isReady:" + bleCharacterService.isReady());
        if(bleCharacterService != null){
            if(!bleCharacterService.isReady()){//make sure its closen
                bleCharacterService.tearDown();
            }
            notifyContectListener();
        }
        qmuiTipDialog.dismiss();
    }
    @Override
    protected BleCharacterService doInBackground(String... strings) {
        Log.d(TAG, "doInBackground:" + Arrays.toString(strings));
        publishProgress(HEAD_MAC+strings[0]);
        waitForConnected();
        return mBleCharacterService;
    }
    ////////////////////////////////////////////////
    private BleCharacterService mBleCharacterService;
    private boolean onCustomCancel = false;
    private Object object = new Object();
    private void waitForConnected(){
        synchronized (object){
            synchronized (object) {
                try {//wait connect success.
                    object.wait();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void notifyWait(){
        synchronized (object) {
            try {
                object.notify();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    public void cancelByCustom(){
        onCustomCancel = true;
        if(mBleCharacterService != null){
            mBleCharacterService.tearDown();
        }
        notifyWait();
    }
    @Override
    public void onBleCharacterStateChange(String mac, BleCharacterState oldState, BleCharacterState newState) {
        Log.d(TAG, "onBleCharacterStateChange:" + mac
                    + ", " + BleCharacterState.tranformStateToString(oldState)
                    + ", " + BleCharacterState.tranformStateToString(newState));
        if(newState == BleCharacterState.STATE_CONNECTED){
            //Test Write
            //mBleCharacterService.writeData(ByteUtils.stringToBytes("12"));
            notifyWait();
        } else if(newState == BleCharacterState.STATE_DISCONNECTED){
            if(oldState == BleCharacterState.STATE_CONNECTING){
                publishProgress(HEAD_NOTIFY+context.getString(R.string.connect_params_fail));
            }
        }
    }

    @Override
    public void onBleCharacterConnectStateChange(String mac, boolean connected) {
        Log.d(TAG, "onBleCharacterConnectStateChange:" + mac
                        + ", " + connected);
    }
    //////////////////////////////////////////////////////
    private final static String HEAD_MAC = "MAC-";
    private final static String HEAD_NOTIFY = "NOTIFY-";
    private synchronized void parserCommond(String msg){
        if(msg.startsWith(HEAD_MAC)){
            String mac = msg.substring(HEAD_MAC.length(), msg.length());
            mBleCharacterService = new BleCharacterService(mac);
            mBleCharacterService.setBleCharacterServiceListener(this);
            mBleCharacterService.startConnect();
        } else if(msg.startsWith(HEAD_NOTIFY)){
            String notify = msg.substring(HEAD_NOTIFY.length(), msg.length());
            ToastUtils.showShort(context, notify);
        }
    }
    //////////////////////////////////////////////////////
    private ContectListener contectListener;
    public void setContectListener(ContectListener listener){
        contectListener = listener;
    }
    public interface ContectListener{
        void onConnectComplete(BleCharacterService service);
    }
    private synchronized void notifyContectListener(){
        if(!onCustomCancel){
            if(contectListener != null){
                contectListener.onConnectComplete(mBleCharacterService);
            }
        }
    }
}
