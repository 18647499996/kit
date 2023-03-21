package com.liudonghan.main;

import android.app.Application;

import com.amap.api.location.AMapLocationClientOption;
import com.liudonghan.kit.ijk.ADVideoViewManager;
import com.liudonghan.kit.location.ADLocationManager;

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
        ADVideoViewManager.getInstance().init();
        ADLocationManager.getInstance().init(this, new AMapLocationClientOption()
                .setGpsFirst(true)
                .setInterval(30000)
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                .setNeedAddress(true));

    }
}
