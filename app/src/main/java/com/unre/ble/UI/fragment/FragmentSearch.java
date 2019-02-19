package com.unre.ble.UI.fragment;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.unre.ble.R;
import com.unre.ble.R2;
import com.unre.ble.UI.Base.BleBaseFragment;
import com.unre.ble.UI.adaptor.BleDevicesRecyclerViewAdapter;
import com.unre.ble.UnBle.Client.BleCharacterService;
import com.unre.ble.UnBle.Client.BleCharacterServiceConnectTask;
import com.unre.ble.UnBle.Client.BleCharacterState;
import com.unre.ble.UnBle.Client.BleClientService;
import com.unre.ble.UnBle.Client.ClientManager;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE;

public class FragmentSearch extends BleBaseFragment implements SearchResponse, AdapterView.OnItemClickListener, BleClientService.BleClientServiceListener, BleCharacterServiceConnectTask.ContectListener {
    private final static String TAG = FragmentSearch.class.getSimpleName();
    private BluetoothClient mBluetoothClient;
    private SearchRequest mSearchRequest;
    private BleDevicesRecyclerViewAdapter mAdapter;
    private volatile ArrayList<SearchResult> mSearchDevices;
    @BindView(R2.id.topbar)
    QMUITopBarLayout qmuiTopBarLayout;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBleClentService();//用于维持服务状态
        mBluetoothClient = ClientManager.getClient();
        mSearchDevices = new ArrayList<>();
        mAdapter = new BleDevicesRecyclerViewAdapter(this.getContext());
    }
    @Override
    protected View onCreateView() {
        return View.inflate(getContext(), R.layout.fragment_search, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        qmuiTopBarLayout.setTitle(R.string.search);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.pregress_color_1,R.color.pregress_color_2,
                R.color.pregress_color_3,R.color.pregress_color_4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                checkAndStartScan();
            }
        });
        initRecyclerView();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(getBleClientService() != null) {
            getBleClientService().setBleClientServiceListener(this);
        }
        checkAndStartScan();
    }
    @Override
    public void onPause() {
        super.onPause();
        if(getBleClientService() != null) {
            getBleClientService().setBleClientServiceListener(null);
        }
        stopScan();
    }
    //////////////////////////////////
    private void initRecyclerView(){
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClientManager.getClient().stopSearch();//make sure its stop.
        SearchResult searchResult = mAdapter.getItemObject(position);
        connectBle(searchResult);
    }
    ///////////////////////////////////////////////////
    private synchronized void stopScan(){
        mBluetoothClient.stopSearch();
        mSearchRequest = null;
    }
    private synchronized void checkAndStartScan(){
        if(mSearchRequest == null) {
            mSearchDevices.clear();
            mSearchRequest = new SearchRequest.Builder()
                    .searchBluetoothLeDevice(5000, 2).build();
            mBluetoothClient.search(mSearchRequest, this);
        }
    }
    /////////////////////////////////////////////////////////////////
    // Scan
    @Override
    public void onSearchStarted() {
        Log.d(TAG,"onSearchStarted");
        swipeRefreshLayout.setRefreshing(true);
    }
    @Override
    public void onDeviceFounded(SearchResult device) {
        Log.d(TAG,"onDeviceFounded:" + device);
        if(device.device.getType() == DEVICE_TYPE_LE
            && !mSearchDevices.contains(device)){
            mSearchDevices.add(device);
            mAdapter.setDataList(mSearchDevices);
        }
    }
    @Override
    public void onSearchStopped() {
        Log.d(TAG,"onSearchStopped");
        mSearchRequest = null;
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void onSearchCanceled() {
        Log.d(TAG,"onSearchCanceled");
    }

    @Override
    public void onBleCharacterServiceChange(BleCharacterService bleCharacterService) {

    }

    @Override
    public void onBleCharacterConnectStateChange(String mac, boolean connected) {

    }

    @Override
    public void onBleCharacterStateChange(String mac, BleCharacterState oldState, BleCharacterState newState) {

    }
    //////////////////////////////////////////////////////
    //private BleCharacterServiceConnectTask bleCharacterServiceConnectTask;
    private synchronized void connectBle(SearchResult searchResult){
        Log.d(TAG,"----- connectBle -------");
        BleCharacterServiceConnectTask task = new BleCharacterServiceConnectTask(getContext());
        task.setContectListener(this);
        task.execute(searchResult.getAddress());
    }

    @Override
    public void onConnectComplete(BleCharacterService service) {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getBleClientService() != null){
                    getBleClientService().setConnectedBleCharacterService(service);
                    FragmentSearch.this.setFragmentResult(1, null);
                }
                popBackStack();
            }
        }, 500);
    }
}
