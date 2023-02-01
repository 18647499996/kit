package com.liudonghan.main;

import android.app.Application;

import com.amap.api.location.AMapLocationClientOption;
import com.liudonghan.kit.location.ADLocationUtils;
import com.liudonghan.kit.oss.ADCosServiceFactory;

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
        ADCosServiceFactory.getInstance().setConfig("AKID5zd06vTlnX2GDtJTMyIM9esqWv2sSlnY", "VjgMltkKjsoTrzqPhjUyA6l9F4cttc4F", "shops-1307611133", "ap-beijing");
        ADLocationUtils.getInstance().init(this, "", new AMapLocationClientOption()
                .setGpsFirst(true)
                .setInterval(30000)
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                .setNeedAddress(true));

    }
}
