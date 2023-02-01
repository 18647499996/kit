package com.liudonghan.kit.location.listener;

import com.amap.api.location.AMapLocation;

/**
 * Description：定位信息回调器
 *
 * @author Created by: Li_Min
 * Time:2/1/23
 */
public interface OnADLocationUtilsListener {
    /**
     * 定位成功
     *
     * @param aMapLocation 定位结果
     */
    void onLocationSucceed(AMapLocation aMapLocation);

    /**
     * 定位失败
     *
     * @param errorCode 异常code码
     * @param errorInfo 异常信息
     */
    void onLocationError(int errorCode, String errorInfo);
}
