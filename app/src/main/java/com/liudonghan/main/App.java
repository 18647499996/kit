package com.liudonghan.main;

import android.app.Application;
import android.util.Log;

import com.amap.api.location.AMapLocationClientOption;
import com.liudonghan.kit.ijk.ADVideoPlayManager;
import com.liudonghan.kit.location.ADLocationManager;
import com.liudonghan.kit.push.ADPushManager;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/31/23
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // ijk播放器
        ADVideoPlayManager.getInstance().init(this);
        ADLocationManager.getInstance().init(this, new AMapLocationClientOption()
                .setGpsFirst(true)
                .setInterval(30000)
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                .setNeedAddress(true));
        ADPushManager.getInstance().init(this, new ADPushManager.OnADPushManagerListener() {
            @Override
            public void onPushTokenSucceed(ADPushManager.BrandType brandType, String pushToken) {

            }

            @Override
            public void onPushTokenError(ADPushManager.BrandType brandType, int errorCode, String errorMessage) {
                Log.i("Mac_Liu", errorMessage);
            }
        });

    }
}
