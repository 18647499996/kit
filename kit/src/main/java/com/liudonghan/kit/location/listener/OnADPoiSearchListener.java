package com.liudonghan.kit.location.listener;

import com.amap.api.services.core.PoiItemV2;

import java.util.ArrayList;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2/2/23
 */
public interface OnADPoiSearchListener extends OnADListener {

    /**
     * poi检索数据结果
     *
     * @param poi poi数据
     */
    void onPoiSearched(ArrayList<PoiItemV2> poi);

    /**
     * poi条目信息
     *
     * @param poiItem 条目数据
     */
    void onPoiItemSearched(PoiItemV2 poiItem);
}
