package com.liudonghan.kit.push.xiaomi;

import android.content.Context;
import android.util.Log;

import com.liudonghan.kit.push.ADPushManager;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

import java.util.List;

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
        Log.i(TAG, "onCommandResult xiaomi listener：" + miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                if (null != ADPushManager.getInstance().getOnADPushManagerListener()) {
                    ADPushManager.getInstance().getOnADPushManagerListener().onPushTokenSucceed(ADPushManager.BrandType.xiaomi, cmdArg1);
                }
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                // 设置别名 cmdArg1
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                // 注销别名 cmdArg1
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                // 设置标签 cmdArg1

            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                // 注销标签 cmdArg1
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                // 接收时间 cmdArg1( 开始时间 ） cmdArg2( 结束时间 ）
            }
        }
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
        Log.i(TAG, "onReceiveRegisterResult xiaomi listener：" + miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                if (null != ADPushManager.getInstance().getOnADPushManagerListener()) {
                    ADPushManager.getInstance().getOnADPushManagerListener().onPushTokenSucceed(ADPushManager.BrandType.xiaomi, cmdArg1);
                }
            }
        }
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
