package com.unre.ble.EventBus;
//
// Created by JasWorkSpace on 2019/1/25.
// Copyright (c) 2019 Pinocchio_Android All rights reserved.

public class Event {
    public int id;
    public int arg1;
    public int arg2;
    public Object object;

    public Event(int id){
        this.id = id;
    }

    public Event(int id, int arg1){
        this.id = id;
        this.arg1 = arg1;
    }

    public Event(int id, Object object){
        this.id = id;
        this.object = object;
    }
    @Override
    public String toString() {
        return "{id:" + id
                + ", arg1:" + arg1
                + ", arg2:" + arg2
                + ", object:" + object
                +"}";
    }
}
