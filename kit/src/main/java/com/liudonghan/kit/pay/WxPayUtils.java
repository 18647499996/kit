package com.liudonghan.kit.pay;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/31/23
 */
public class WxPayUtils {
    private static volatile WxPayUtils instance = null;

    private WxPayUtils() {
    }

    public static WxPayUtils getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (WxPayUtils.class) {
                // double checkout
                if (null == instance) {
                    instance = new WxPayUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 微信支付
     * todo 微信支付回调自行根据包名配置回调activity
     *
     * @param context   上下文
     * @param appId     应用唯一ID
     * @param partnerId 商户ID
     * @param prepayId  支付ID
     * @param nonceStr  流水号
     * @param timesTamp 支付时间戳
     * @param sign      订单签名信息
     */
    public void pay(Context context, String appId, String partnerId, String prepayId, String nonceStr, String timesTamp, String sign) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, appId, false);
        iwxapi.registerApp(appId);
        PayReq req = new PayReq();
        // appId
        req.appId = appId;
        // 商户号
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.nonceStr = nonceStr;
        req.timeStamp = timesTamp;
        req.packageValue = "Sign=WXPay";
        req.sign = sign;
        iwxapi.sendReq(req);
    }
}
