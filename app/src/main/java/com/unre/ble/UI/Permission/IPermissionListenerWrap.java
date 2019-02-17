package com.unre.ble.UI.Permission;

public interface IPermissionListenerWrap {

    public interface IEachPermissionListener {

        void onAccepted(Permission permission);

        void onException(Throwable throwable);
    }

    public interface IPermissionListener {

        void onAccepted(boolean isGranted);

        void onException(Throwable throwable);
    }
}
