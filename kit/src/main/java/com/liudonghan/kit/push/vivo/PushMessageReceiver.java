package com.liudonghan.kit.push.vivo;

import android.content.Context;
import android.util.Log;

import com.liudonghan.kit.push.ADPushManager;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

/**
 * Description：VIVO推送消息广播
 *
 * @author Created by: Li_Min
 * Time:4/3/23
 */
public class PushMessageReceiver extends OpenClientPushMessageReceiver {

    public static final String TAG = "Mac_Liu";

    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage msg) {
        Log.i(TAG, "onNotificationMessageClicked vivo listener : " + msg.toString());
    }

    @Override
    public void onReceiveRegId(Context context, String regId) {
        Log.i(TAG, "onReceiveRegId regId = " + regId);
        if (null != ADPushManager.getInstance().getOnADPushManagerListener()) {
            ADPushManager.getInstance().getOnADPushManagerListener().onPushTokenSucceed(ADPushManager.BrandType.vivo, regId);
        }
    }
}