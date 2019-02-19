package com.unre.ble.UnBle;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.util.Log;
import android.view.KeyEvent;

import com.unre.ble.EventBus.EventID;
import com.unre.ble.EventBus.EventUtils;

import java.util.Arrays;

public class BleDataParser {
    private final static String TAG = BleDataParser.class.getSimpleName();
    //byte[] 需要小于 20 个字节。
    // 第一个字节为标示码，固定 0XFF
    // 第二个字节为操作码，0X01 代表按键值
    // 后续为操作参数
    ////////////////////////////////////////////
    /* 按键需要3个字节，第三个
    * */
    private final static byte BLE_HEAD = (byte)0xFF;
    private final static byte BLE_KEY_KEYCODE = (byte)0x01;
    private final static byte BLE_KEYCODE_LEFT = (byte)0x01;
    private final static byte BLE_KEYCODE_RIGHT = (byte)0x02;
    private final static byte BLE_KEYCODE_UP = (byte)0x03;
    private final static byte BLE_KEYCODE_DOWN = (byte)0x04;
    private final static byte BLE_KEYCODE_MENU = (byte)0x05;
    private final static byte BLE_KEYCODE_POWER = (byte)0x06;
    private final static byte BLE_KEYCODE_VOLUMEUP = (byte)0x07;
    private final static byte BLE_KEYCODE_VOLUMEDOWN = (byte)0x08;
    private final static byte BLE_KEYCODE_ENTER = (byte)0x09;
    private final static byte BLE_KEYCODE_HOME = (byte)0x0A;
    private final static byte BLE_KEYCODE_BACK = (byte)0x0B;

    public static boolean isUnBleData(byte[] data){
        return isUnBleData(0, data);
    }
    public static boolean isUnBleData(int offset, byte[] data){
        if(data != null && (data.length - offset) > 2) {
            return data[offset] == BLE_HEAD;
        }
        return false;
    }
    public static boolean parserUnBleData(byte[] data){
        return parserUnBleData(0, data);
    }
    public static boolean parserUnBleData(int offset, byte[] data){
        Log.d(TAG, "parserUnBleData offset:" + offset + ", data:" + Arrays.toString(data));
        try {
            if (isUnBleData(offset, data)) {
                switch (data[offset + 1]) {//操作码
                    case BLE_KEY_KEYCODE: {//按键需要3个字节，第三个字节为按键值
                        EventUtils.postEvent(true, EventID.EVENTID_BLE_RECEIVER_KEYCODE, parserKeyCode(data[offset+2]));
                    }
                    break;
                }
            }
        } catch (Throwable e){
            e.printStackTrace();
        }
        return false;
    }
    //////////////////////////////////////////////////////////////
    public static String tranformKEYCODE(int keycode){
        switch (keycode){
            case BLE_KEYCODE_DOWN: return "BLE_KEYCODE_DOWN";
            case BLE_KEYCODE_LEFT: return "BLE_KEYCODE_LEFT";
            case BLE_KEYCODE_MENU: return "BLE_KEYCODE_MENU";
            case BLE_KEYCODE_RIGHT:return "BLE_KEYCODE_RIGHT";
            case BLE_KEYCODE_UP: return "BLE_KEYCODE_UP";
            case BLE_KEYCODE_POWER: return "BLE_KEYCODE_POWER";
            case BLE_KEYCODE_VOLUMEUP: return "BLE_KEYCODE_VOLUMEUP";
            case BLE_KEYCODE_VOLUMEDOWN: return "BLE_KEYCODE_VOLUMEDOWN";
            case BLE_KEYCODE_ENTER: return "BLE_KEYCODE_ENTER";
            default:
                return  ("KEYCODE_" + String.valueOf(keycode));
        }
    }
    public static int parserKeyCode(byte keycode){
        Log.d(TAG, "parserKeyCode:" + keycode);
        switch (keycode){
            case BLE_KEYCODE_POWER: return KeyEvent.KEYCODE_POWER;
            case BLE_KEYCODE_VOLUMEUP: return KeyEvent.KEYCODE_VOLUME_UP;
            case BLE_KEYCODE_VOLUMEDOWN: return KeyEvent.KEYCODE_VOLUME_DOWN;
            case BLE_KEYCODE_MENU: return KeyEvent.KEYCODE_MENU;
            case BLE_KEYCODE_HOME: return KeyEvent.KEYCODE_HOME;
            case BLE_KEYCODE_BACK: return KeyEvent.KEYCODE_BACK;
            case BLE_KEYCODE_DOWN: return KeyEvent.KEYCODE_DPAD_DOWN;
            case BLE_KEYCODE_LEFT: return KeyEvent.KEYCODE_DPAD_LEFT;
            case BLE_KEYCODE_UP: return KeyEvent.KEYCODE_DPAD_UP;
            case BLE_KEYCODE_RIGHT: return KeyEvent.KEYCODE_DPAD_RIGHT;
            case BLE_KEYCODE_ENTER: return KeyEvent.KEYCODE_DPAD_CENTER;
        }
        return KeyEvent.KEYCODE_UNKNOWN;
    }
    public static byte[] getBleKeyCodeData(int keyCode){
        Log.d(TAG, "getBleKeyCodeData:" + keyCode);
        byte keydata = (byte) keyCode;
        switch (keyCode){
            case KeyEvent.KEYCODE_POWER: keydata = BLE_KEYCODE_POWER; break;
            case KeyEvent.KEYCODE_VOLUME_UP: keydata = BLE_KEYCODE_VOLUMEUP; break;
            case KeyEvent.KEYCODE_VOLUME_DOWN: keydata = BLE_KEYCODE_VOLUMEDOWN; break;
            case KeyEvent.KEYCODE_MENU: keydata = BLE_KEYCODE_MENU; break;
            case KeyEvent.KEYCODE_HOME: keydata = BLE_KEYCODE_HOME; break;
            case KeyEvent.KEYCODE_BACK: keydata = BLE_KEYCODE_BACK; break;

            case KeyEvent.KEYCODE_DPAD_DOWN: keydata = BLE_KEYCODE_DOWN; break;
            case KeyEvent.KEYCODE_DPAD_LEFT: keydata = BLE_KEYCODE_LEFT; break;
            case KeyEvent.KEYCODE_DPAD_UP: keydata = BLE_KEYCODE_UP; break;
            case KeyEvent.KEYCODE_DPAD_RIGHT: keydata = BLE_KEYCODE_RIGHT; break;
            case KeyEvent.KEYCODE_DPAD_CENTER: keydata = BLE_KEYCODE_ENTER; break;
            default: return new byte[]{};
        }
        return new byte[]{BLE_HEAD, BLE_KEY_KEYCODE, keydata};
    }
}
