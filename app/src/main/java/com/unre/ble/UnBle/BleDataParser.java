package com.unre.ble.UnBle;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import com.unre.ble.EventBus.EventID;
import com.unre.ble.EventBus.EventUtils;

public class BleDataParser {
    //byte[] 需要小于 20 个字节。
    // 第一个字节为标示码，固定 0XFF
    // 第二个字节为操作码，0X01 代表按键值
    // 后续为操作参数
    ////////////////////////////////////////////
    /* 按键需要3个字节，第三个
    * */
    public final static byte HEAD = (byte)0xFF;
    public final static byte KEY_KEYCODE = (byte)0x01;
    public final static byte KEYCODE_LEFT = (byte)0x01;
    public final static byte KEYCODE_RIGHT = (byte)0x02;
    public final static byte KEYCODE_UP = (byte)0x03;
    public final static byte KEYCODE_DOWN = (byte)0x04;
    public final static byte KEYCODE_MENU = (byte)0x05;

    public static boolean isUnBleData(byte[] data){
        return isUnBleData(0, data);
    }
    public static boolean isUnBleData(int offset, byte[] data){
        if(data != null && (data.length - offset) > 2) {
            return data[offset] == HEAD;
        }
        return false;
    }
    public static boolean parserUnBleData(byte[] data){
        return parserUnBleData(0, data);
    }
    public static boolean parserUnBleData(int offset, byte[] data){
        try {
            if (isUnBleData(offset, data)) {
                switch (data[offset + 1]) {//操作码
                    case KEY_KEYCODE: {
                        EventUtils.postEvent(true, EventID.EVENTID_BLE_RECEIVER_KEYCODE, (int) data[offset + 2]);
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
            case KEYCODE_DOWN: return "KEYCODE_DOWN";
            case KEYCODE_LEFT: return "KEYCODE_LEFT";
            case KEYCODE_MENU: return "KEYCODE_MENU";
            case KEYCODE_RIGHT:return "KEYCODE_RIGHT";
            case KEYCODE_UP: return "KEYCODE_UP";
            default:
                return  ("KEYCODE_" + String.valueOf(keycode));
        }
    }

}
