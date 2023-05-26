package com.liudonghan.kit.push;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import com.heytap.mcssdk.callback.PushCallback;
import com.heytap.mcssdk.mode.SubscribeResult;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.meizu.cloud.pushsdk.PushManager;
import com.vivo.push.PushClient;
import com.vivo.push.util.VivoPushException;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:4/3/23
 */
public class ADPushManager {

    public OnADPushManagerListener onADPushManagerListener;

    private static final String TAG = "Mac_Liu";

    private static volatile ADPushManager instance = null;

    private ADPushManager() {
    }

    public static ADPushManager getInstance() {
        //single checkout
        if (null == instance) {
            synchronized (ADPushManager.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADPushManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化推送
     *
     * @param context 上下文
     */
    public void init(Context context, OnADPushManagerListener onADPushManagerListener) {
        this.onADPushManagerListener = onADPushManagerListener;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            switch (ADBrandManager.getInstanceType(context)) {
                case ADBrandManager.BRAND_OPPO:
                    Log.i(TAG, "ready init oppo push ");
                    initOppo(context, appInfo.metaData.getString("OPPO_APP_PUSH_KEY"), appInfo.metaData.getString("OPPO_APP_PUSH_SECRET"));
                    break;
                case ADBrandManager.BRAND_XIAOMI:
                    Log.i(TAG, "ready init xiaomi push ");
                    initXiaomi(context, appInfo.metaData.getString("MIPUSH_APPID"), appInfo.metaData.getString("MIPUSH_APPKEY"));
                    break;
                case ADBrandManager.BRAND_VIVO:
                    Log.i(TAG, "ready init vivo push ");
                    initVivo(context);
                    break;
                case ADBrandManager.BRAND_HUAWEI:
                    Log.i(TAG, "ready init hms push ");
                    initHuawei(context, appInfo.metaData.getString("HMS_APP_ID"));
                    break;
                case ADBrandManager.BRAND_MEIZU:
                    Log.i(TAG, "ready init fiyme push ");
                    initMeizu(context, appInfo.metaData.getString("MEIZU_PUSH_APP_ID"), appInfo.metaData.getString("MEIZU_PUSH_APP_KEY"));
                    break;
            }
        } catch (PackageManager.NameNotFoundException | VivoPushException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册华为推送
     *
     * @param context 上下文
     * @param appId   华为应用ID
     */
    private void initHuawei(Context context, String appId) {
        HandlerThread thread = new HandlerThread("workHandler");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(() -> {
            try {
                String token = HmsInstanceId.getInstance(context).getToken(appId, "HCM");
                if (!TextUtils.isEmpty(token)) {
                    onADPushManagerListener.onPushTokenSucceed(BrandType.hms, token);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 注册OPPO推送
     *
     * @param context   上下文
     * @param appKey    OPPO应用Key
     * @param appSecret OPPO应用签名
     */
    private void initOppo(Context context, String appKey, String appSecret) {
        com.heytap.mcssdk.PushManager.getInstance().register(context, appKey, appSecret, new PushCallback() {
            @Override
            public void onRegister(int i, String s) {
                Log.i(TAG, " onRegister oppo listener：" + s);
                if (null != onADPushManagerListener) {
                    if (!TextUtils.isEmpty(s)) {
                        onADPushManagerListener.onPushTokenSucceed(BrandType.oppo, s);
                    } else {
                        onADPushManagerListener.onPushTokenError(BrandType.oppo, 100025, "");
                    }
                }
            }

            @Override
            public void onUnRegister(int i) {
                Log.i(TAG, " onUnRegister oppo listener：" + i);
            }

            @Override
            public void onSetPushTime(int i, String s) {
                Log.i(TAG, " onSetPushTime oppo listener：" + s);
            }

            @Override
            public void onGetPushStatus(int i, int i1) {
                Log.i(TAG, " onGetPushStatus oppo listener：" + i1);
            }

            @Override
            public void onGetNotificationStatus(int i, int i1) {
                Log.i(TAG, " onGetNotificationStatus oppo listener：" + i1);
            }

            @Override
            public void onGetAliases(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onGetAliases oppo listener：" + list.toString());
            }

            @Override
            public void onSetAliases(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onSetAliases oppo listener：" + list.toString());
            }

            @Override
            public void onUnsetAliases(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onUnsetAliases oppo listener：" + list.toString());
            }

            @Override
            public void onSetUserAccounts(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onSetUserAccounts oppo listener：" + list.toString());
            }

            @Override
            public void onUnsetUserAccounts(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onUnsetUserAccounts oppo listener：" + list.toString());
            }

            @Override
            public void onGetUserAccounts(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onGetUserAccounts oppo listener：" + list.toString());
            }

            @Override
            public void onSetTags(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onSetTags oppo listener：" + list.toString());
            }

            @Override
            public void onUnsetTags(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onUnsetTags oppo listener：" + list.toString());
            }

            @Override
            public void onGetTags(int i, List<SubscribeResult> list) {
                Log.i(TAG, " onGetTags oppo listener：" + list.toString());
            }
        });
    }

    /**
     * 注册vivo推送
     *
     * @param context 上下文
     * @throws VivoPushException
     */
    private void initVivo(Context context) throws VivoPushException {
        PushClient.getInstance(context).initialize();
        PushClient.getInstance(context).turnOnPush(i -> {
            Log.i(TAG, " onStateChanged vivo turnOnPush listener：" + i);
            if (null != onADPushManagerListener) {
                if (!TextUtils.isEmpty(PushClient.getInstance(context).getRegId())) {
                    onADPushManagerListener.onPushTokenSucceed(BrandType.vivo, PushClient.getInstance(context).getRegId());
                } else {
                    onADPushManagerListener.onPushTokenError(BrandType.vivo, 100024, "get vivo push token error");
                }
            }
        });
    }

    /**
     * 注册小米推送
     *
     * @param context 上下文
     * @param appId   小米应用ID
     * @param appKey  小米应用key
     */
    private void initXiaomi(Context context, String appId, String appKey) {
        MiPushClient.registerPush(context, appId, appKey);
    }

    /**
     * 注册魅族推送
     *
     * @param context 上下文
     * @param appId   魅族应用ID
     * @param appKey  魅族应用Key
     */
    private void initMeizu(Context context, String appId, String appKey) {
        PushManager.register(context, appId, appKey);
        PushManager.switchPush(context, appId, appKey, PushManager.getPushId(context), 1, true);
        if (null != onADPushManagerListener) {
            if (!TextUtils.isEmpty(PushManager.getPushId(context))) {
                onADPushManagerListener.onPushTokenSucceed(BrandType.fiyme, PushManager.getPushId(context));
            } else {
                onADPushManagerListener.onPushTokenError(BrandType.fiyme, 100023, "get fiyme push token error");
            }

        }
    }

    public OnADPushManagerListener getOnADPushManagerListener() {
        return onADPushManagerListener;
    }

    public interface OnADPushManagerListener {

        /**
         * 获取推送token
         *
         * @param brandType xiaomi、oppo、hms、vivo、 fiyme
         * @param pushToken 推送token
         */
        void onPushTokenSucceed(BrandType brandType, String pushToken);

        /**
         * 推送token获取异常
         *
         * @param brandType    xiaomi、oppo、hms、vivo、 fiyme
         * @param errorCode    异常编码
         * @param errorMessage 异常信息
         */
        void onPushTokenError(BrandType brandType, int errorCode, String errorMessage);
    }

    public enum BrandType {
        xiaomi,
        oppo,
        hms,
        vivo,
        fiyme
    }


}
