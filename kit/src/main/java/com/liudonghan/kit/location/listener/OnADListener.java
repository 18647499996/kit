package com.liudonghan.kit.location.listener;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2/2/23
 */
public interface OnADListener {

    /**
     * 异常回调
     *
     * @param errorCode 异常编码
     * @param errorMsg  异常信息
     */
    void onFail(int errorCode, String errorMsg);
}
