package com.unre.ble.UI.adaptor;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.search.SearchResult;
import com.unre.ble.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BleDevicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R2.id.device_mac)
    TextView deviceMac;
    @BindView(R2.id.device_name)
    TextView deviceName;
    @BindView(R2.id.device_rssi)
    TextView deviceRssi;
    @BindView(R2.id.device_scanre)
    TextView deviceScanre;
    private BleDevicesRecyclerViewAdapter bleDevicesRecyclerViewAdapter;
    public BleDevicesViewHolder(View itemView, BleDevicesRecyclerViewAdapter adapter) {
        super(itemView);
        bleDevicesRecyclerViewAdapter = adapter;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }
    public void bindSearchResult(SearchResult result){
        deviceName.setText(result.getName());
        deviceMac.setText(result.getAddress());
        deviceRssi.setText("" + result.rssi);
        deviceScanre.setText(new Beacon(result.scanRecord).toString());
    }
    @Override
    public void onClick(View v) {
        bleDevicesRecyclerViewAdapter.onItemHolderClick(this);
    }
}
