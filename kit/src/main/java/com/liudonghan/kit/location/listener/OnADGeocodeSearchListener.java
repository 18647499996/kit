package com.liudonghan.kit.location.listener;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;

import java.util.List;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2/2/23
 */
public interface OnADGeocodeSearchListener extends OnADListener {


    /**
     * 地址信息
     * @param regeocodeAddress
     */
    void onRegSearched(RegeocodeAddress regeocodeAddress);

    /**
     * 地址信息列表
     * @param geocodeAddressList 地址列表
     */
    void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList);
}
