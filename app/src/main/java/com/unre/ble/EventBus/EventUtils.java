package com.unre.ble.EventBus;
//
// Created by JasWorkSpace on 2019/1/25.
// Copyright (c) 2019 Pinocchio_Android All rights reserved.


public class EventUtils {
    //订阅事件
    public static void register(boolean alive, Object object){
        //Log.d("EventUtils","register:" + alive + ", " + object);
        if(alive){
            EventBusHelper.register(object);
        } else {
            EventBusHelper.registerAlive(object);
        }
    }
    //取消订阅
    public static void unregister(boolean alive, Object object){
        //Log.d("EventUtils","unregister:" + alive + ", " + object);
        if(alive){
            EventBusHelper.unregister(object);
        } else {
            EventBusHelper.unregisterAlive(object);
        }
    }
    //发送事件
    public static void post(boolean alive, Object object){
        if(alive){
            EventBusHelper.post(object);
        } else {
            EventBusHelper.postAlive(object);
        }
    }
    //////////////////////////////////////////////////////////////
    /// 常用接口
    public static void postEvent(boolean alive, Event event){
        post(alive, event);
    }
    public static void postEvent(boolean alive, int eventID, int arg1){
        post(alive, new Event(eventID, arg1));
    }
    public static void postEvent(boolean alive, int eventID, Object object){
        post(alive, new Event(eventID, object));
    }
}
