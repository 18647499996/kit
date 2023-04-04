package com.liudonghan.kit.push.xiaomi;

import android.content.Context;
import android.util.Log;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:4/4/23
 */
public class PushMessageReceiver extends com.xiaomi.mipush.sdk.PushMessageReceiver {

    private static final String TAG = "Mac_Liu";

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
        Log.i(TAG, "onCommandResult xiaomi listener");
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
        Log.i(TAG, "onNotificationMessageArrived xiaomi listener");
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
        Log.i(TAG, "onNotificationMessageClicked xiaomi listener");
    }

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
        Log.i(TAG, "onReceivePassThroughMessage xiaomi listener");
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        Log.i(TAG, "onReceiveRegisterResult xiaomi listener");
    }

    @Override
    public void onRequirePermissions(Context context, String[] strings) {
        super.onRequirePermissions(context, strings);
        Log.i(TAG, "onRequirePermissions xiaomi listener");
    }

    @Override
    public void onReceiveMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceiveMessage(context, miPushMessage);
        Log.i(TAG, "onReceiveMessage xiaomi listener");
    }


}
