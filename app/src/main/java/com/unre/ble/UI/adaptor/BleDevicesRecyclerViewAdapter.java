package com.unre.ble.UI.adaptor;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.inuker.bluetooth.library.search.SearchResult;
import com.unre.ble.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BleDevicesRecyclerViewAdapter extends RecyclerView.Adapter<BleDevicesViewHolder> implements Comparator<SearchResult> {
    private Context mContext;
    private List<SearchResult> mDataList;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    public BleDevicesRecyclerViewAdapter(Context context){
        mContext = context;
        mDataList = new ArrayList<SearchResult>();
    }
    public void setDataList(List<SearchResult> datas) {
        mDataList.clear();
        mDataList.addAll(datas);
        Collections.sort(mDataList, this);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public BleDevicesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View root = inflater.inflate(R.layout.layout_item_devices, viewGroup, false);
        return new BleDevicesViewHolder(root,this);
    }
    @Override
    public void onBindViewHolder(@NonNull BleDevicesViewHolder bleDevicesViewHolder, int i) {
        bleDevicesViewHolder.bindSearchResult(mDataList.get(i));
    }
    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public SearchResult getItemObject(int position){
        return mDataList.get(position);
    }
    @Override
    public int compare(SearchResult lhs, SearchResult rhs) {
        return rhs.rssi - lhs.rssi;
    }
    //////////////////////////////////////////////////////////////
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    protected void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }
}
