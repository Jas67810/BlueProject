package com.unre.ble.UI.weight;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unre.ble.R;

public class LogView extends LinearLayout {
    private TextView mLogTextView;
    public LogView(Context context) {
        super(context);
        setupView();
    }
    public LogView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }
    public LogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }
    ///////////////////////
    private void setupView(){
        View view = View.inflate(getContext(), R.layout.layout_logview, this);
        mLogTextView = view.findViewById(R.id.log);
    }
    //////////////////////////////////////////////
    public void addLog(String log){
        mLogTextView.setText(mLogTextView.getText() + log);
    }
    public void cleanLog(){
        mLogTextView.setText("");
    }
}
