package com.unre.ble.EventBus;
//
// Created by JasWorkSpace on 2019/1/25.
// Copyright (c) 2019 Pinocchio_Android All rights reserved.
import org.greenrobot.eventbus.EventBus;

public class EventBusHelper {
    private static volatile EventBus normalEventBus = new EventBus();
    private static volatile EventBus aliveEventBus = new EventBus();
    protected static void register(Object object){
        register(normalEventBus, object);
    }
    protected static void registerAlive(Object object){
        register(aliveEventBus, object);
    }
    private static void register(EventBus bus, Object object){
        if(!bus.isRegistered(object)){
            bus.register(object);
        }
    }
    ////////////////////////////////////////////////////////
    protected static void unregister(Object object){
        unregister(normalEventBus, object);
    }
    protected static void unregisterAlive(Object object){
        unregister(aliveEventBus, object);
    }
    private static void unregister(EventBus bus, Object object){
        bus.unregister(object);
    }
    /////////////////////////////////////////////////////////
    protected static boolean isRegistered(Object object){
        return isRegistered(normalEventBus, object);
    }
    protected static boolean isRegisteredAlive(Object object){
        return isRegistered(aliveEventBus, object);
    }
    private static boolean isRegistered(EventBus bus, Object object){
        return bus.isRegistered(object);
    }
    ///////////////////////////////////////////////////////////
    protected static void post(Object object){
        post(normalEventBus, object);
    }
    protected static void postAlive(Object object){
        post(aliveEventBus, object);
    }
    private static void post(EventBus bus, Object object){
        bus.post(object);
    }
}
