package com.liudonghan.kit.pay;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/31/23
 */
public class ADAliPayUtils {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private OnPayResultListener onPayResultListener;


    private static volatile ADAliPayUtils instance = null;


    private ADAliPayUtils() {
    }

    public static ADAliPayUtils getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (ADAliPayUtils.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADAliPayUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 支付宝支付
     *
     * @param fragmentActivity activity引用
     * @param sign             支付签名信息
     */
    public void pay(FragmentActivity fragmentActivity, String sign) {
        Runnable payRunnable = () -> {
            PayTask aliPay = new PayTask(fragmentActivity);
            Map<String, String> result1 = aliPay.payV2(sign, true);
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result1;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 110:
                    onPayResultListener.onPaySucceed();
                    break;
                case 111:
                    onPayResultListener.onPayProgress();
                    break;
                case SDK_PAY_FLAG:
                    ADPayResult payResult = new ADPayResult((Map<String, String>) msg.obj);
                    Log.d("AliPayUtils：", "Pay CallBack Info ：" + payResult.toString());
                    switch (payResult.getResultStatus()) {
                        case "9000":
                            onPayResultListener.onPaySucceed();
                            break;
                        case "8000":
                            onPayResultListener.onPayProgress();
                            break;
                        case "6001":
                            onPayResultListener.onPayCancel();
                            break;
                        default:
                            onPayResultListener.onPayFail(payResult.getResultStatus(), "支付失败");
                            break;
                    }
                    break;
                case SDK_CHECK_FLAG:
                    onPayResultListener.onPayFail("-1", "支付失败");
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 设置监听
     *
     * @param onPayResultListener 支付回调
     */
    public void setOnPayResultListener(OnPayResultListener onPayResultListener) {
        this.onPayResultListener = onPayResultListener;
    }

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

}
