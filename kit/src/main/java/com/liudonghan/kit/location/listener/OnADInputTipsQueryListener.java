package com.liudonghan.kit.location.listener;

import com.amap.api.services.help.Tip;

import java.util.List;

/**
 * Description：自动匹配地址回调监听器
 *
 * @author Created by: Li_Min
 * Time:2/1/23
 */
public interface OnADInputTipsQueryListener {

    /**
     * 获取自动匹配地址信息
     *
     * @param tipList 匹配列表
     */
    void onGetInputTipsList(List<Tip> tipList);

    /**
     * 自动匹配异常
     *
     * @param errorMsg 异常描述
     */
    void onFail(String errorMsg);
}
