package com.liudonghan.kit.push.meizu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:4/3/23
 */
public class PushMessageReceiver extends MzPushMessageReceiver {

    private static final String TAG = "Mac_Liu";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "onReceive meizu Listener ：" + intent.toString());
    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        super.onMessage(context, s, s1);
        Log.i(TAG, "onMessage meizu Listener ：" + s1);
    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
        Log.i(TAG, "onPushStatus meizu Listener ：" + pushSwitchStatus.toString());
    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        Log.i(TAG, "onRegisterStatus meizu Listener ：" + registerStatus.toString());
    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {
        Log.i(TAG, "onUnRegisterStatus meizu Listener ：" + unRegisterStatus.toString());
    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {
        Log.i(TAG, "onSubTagsStatus meizu Listener ：" + subTagsStatus.toString());
    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {
        Log.i(TAG, "onSubAliasStatus meizu Listener ：" + subAliasStatus.toString());
    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationClicked(context, mzPushMessage);
        Log.i(TAG, "onNotificationClicked meizu Listener ：" + mzPushMessage.toString());
    }

    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationArrived(context, mzPushMessage);
        Log.i(TAG, "onNotificationArrived meizu Listener ：" + mzPushMessage.toString());
    }

    @Override
    public void onNotificationDeleted(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationDeleted(context, mzPushMessage);
        Log.i(TAG, "onNotificationDeleted meizu Listener ：" + mzPushMessage.toString());
    }

    public String toJson(Intent intent) {
        if (intent == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            Bundle extras = intent.getExtras();
            Set<String> keySet = extras.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                Object value = extras.get(next);
                if (value instanceof Boolean) {
                    jsonObject.put(next, (Boolean) value);
                } else if (value instanceof Integer) {
                    jsonObject.put(next, (Integer) value);
                } else if (value instanceof Long) {
                    jsonObject.put(next, (Long) value);
                } else if (value instanceof Double) {
                    jsonObject.put(next, (Double) value);
                } else if (value instanceof String) {
                    jsonObject.put(next, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}