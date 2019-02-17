package com.unre.unreble.Common;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

public enum  UnBleState {
    STATE_UNUSE(-1),
    STATE_UNKNOW(0),
    STATE_IDLE(1),
    STATE_INITING(2),
    STATE_INITED(3),
    STATE_CLOSING(4),
    STATE_CLOSED(5);
    ///////////////////
    private final int state;
    UnBleState(int state){
        this.state = state;
    }
    public int getState(){
        return state;
    }
    public static UnBleState parser(int state){
        switch (state){
            case -1:return STATE_UNUSE;
            case 0:return STATE_UNKNOW;
            case 1:return STATE_IDLE;
            case 2:return STATE_INITING;
            case 3:return STATE_INITED;
            case 4:return STATE_CLOSING;
            case 5:return STATE_CLOSED;
            default:
                throw new RuntimeException("why get un-support UnBleState");
        }
    }
}
