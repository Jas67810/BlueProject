package com.unre.ble.UnBle.Client;

import com.inuker.bluetooth.library.BluetoothClient;
import com.unre.ble.BleApplication;

/**
 * Created by dingjikerbo on 2016/8/27.
 */
public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(BleApplication.getInstance());
                }
            }
        }
        return mClient;
    }
}
