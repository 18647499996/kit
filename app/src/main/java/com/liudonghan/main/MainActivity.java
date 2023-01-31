package com.liudonghan.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.liudonghan.kit.oss.CosServiceFactory;
import com.liudonghan.kit.pay.AliPayUtils;
import com.liudonghan.kit.pay.OnPayResultListener;
import com.liudonghan.kit.pay.WxPayUtils;

public class MainActivity extends AppCompatActivity implements OnPayResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AliPayUtils.getInstance().setOnPayResultListener(this);
        findViewById(R.id.alipay).setOnClickListener(view -> AliPayUtils.getInstance().pay(MainActivity.this, ""));
        findViewById(R.id.wxpay).setOnClickListener(view -> WxPayUtils.getInstance().pay(MainActivity.this,"","","","","",""));
        CosServiceFactory.getInstance().uploadFile(this, "", "", new CosServiceFactory.OnUploadListener() {
            @Override
            public void onProgress(long current, long total) {

            }

            @Override
            public void onSucceed(String result) {

            }

            @Override
            public void onFail(String errorCode, String message) {

            }
        });
    }

    @Override
    public void onPaySucceed() {

    }

    @Override
    public void onPayProgress() {

    }

    @Override
    public void onPayCancel() {
        Log.d("tag", "sssss取消支付");
    }

    @Override
    public void onPayFail(String resultStatus, String result) {

    }
}