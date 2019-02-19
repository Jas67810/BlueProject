package com.unre.ble.UI.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.unre.ble.R;
import com.unre.ble.R2;
import com.unre.ble.UI.Base.BleBaseFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BleBaseFragmentActivity {
    @BindView(R2.id.qtopbar)
    QMUITopBarLayout mQMUITopBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mQMUITopBarLayout.setTitle(R.string.app_name);
    }
    @OnClick(R2.id.central)
    public void onClickCentral(){
        startActivity(new Intent(this, CentralActivity.class));
        finish();
    }
    @OnClick(R2.id.periphera)
    public void onCLickPeriphera(){
        //Toast.makeText(this, "外围设备模块功能待开发", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, PeripheraActivity.class));
    }
}
