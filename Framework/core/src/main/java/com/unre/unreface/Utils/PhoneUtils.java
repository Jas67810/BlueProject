package com.unre.unreface.Utils;

import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import static android.Manifest.permission.CALL_PHONE;

/**
 * @author by TOME .
 * @data on      2018/6/25 14:21
 * @describe ${手机相关}
 */

public class PhoneUtils {

    /**
     * 判断设备是否是手机
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 跳至拨号界面
     *
     * @param phoneNumber The phone number.
     */
    public static void dial(Context context, final String phoneNumber) {
        context.startActivity(IntentUtils.getDialIntent(phoneNumber, true));
    }

    /**
     * 拨打 phoneNumber
     * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}</p>
     *
     * @param phoneNumber The phone number.
     */
    @RequiresPermission(CALL_PHONE)
    public static void call(Context context, final String phoneNumber) {
        context.startActivity(IntentUtils.getCallIntent(phoneNumber, true));
    }


}
