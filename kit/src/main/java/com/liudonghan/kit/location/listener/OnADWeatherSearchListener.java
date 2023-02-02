package com.liudonghan.kit.location.listener;

import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherLive;

/**
 * Description：高德天气监听器
 *
 * @author Created by: Li_Min
 * Time:2/1/23
 */
public interface OnADWeatherSearchListener extends OnADListener {

    /**
     * 实况天气结果
     *
     * @param localWeatherLive 实况数据
     */
    void onWeatherLiveSearched(LocalWeatherLive localWeatherLive);

    /**
     * 天气预报结果
     *
     * @param localWeatherForecast 预报数据
     */
    void onWeatherForecastSearched(LocalWeatherForecast localWeatherForecast);
}
