package com.liudonghan.kit.push.huawei;

import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.liudonghan.kit.push.ADPushManager;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:4/4/23
 */
public class PushMessageReceiver extends HmsMessageService {

    private static final String TAG = "Mac_Liu";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived huawei listener：" + remoteMessage.getData());

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.i(TAG, "onDeletedMessages huawei listener");
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i(TAG, "onNewToken huawei listener：" + s);
        if (null != ADPushManager.getInstance().getOnADPushManagerListener()){
            ADPushManager.getInstance().getOnADPushManagerListener().onPushTokenSucceed(ADPushManager.BrandType.hms,s);
        }
    }

    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
        Log.i(TAG, "onTokenError huawei listener：" + e.getMessage());
        if (null != ADPushManager.getInstance().getOnADPushManagerListener()){
            ADPushManager.getInstance().getOnADPushManagerListener().onPushTokenError(ADPushManager.BrandType.hms,100025,e.getMessage());
        }
        e.printStackTrace();
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        Log.i(TAG, "onSendError huawei listener：" + e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.i(TAG, "onMessageSent huawei listener：" + s);
    }
}
