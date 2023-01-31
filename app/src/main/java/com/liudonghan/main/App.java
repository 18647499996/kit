package com.liudonghan.main;

import android.app.Application;

import com.liudonghan.kit.oss.CosServiceFactory;

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
        CosServiceFactory.getInstance().setConfig("AKID5zd06vTlnX2GDtJTMyIM9esqWv2sSlnY","VjgMltkKjsoTrzqPhjUyA6l9F4cttc4F","shops-1307611133","ap-beijing");
    }
}
