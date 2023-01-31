package com.liudonghan.kit.pay;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/31/23
 */
public interface OnPayResultListener {

    /**
     * 支付成功
     */
    void onPaySucceed();

    /**
     * 支付中
     */
    void onPayProgress();

    /**
     * 支付取消
     */
    void onPayCancel();

    /**
     * 支付失败
     *
     * @param resultStatus 异常码
     * @param result       异常信息
     */
    void onPayFail(String resultStatus, String result);
}
