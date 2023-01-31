package com.liudonghan.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.liudonghan.kit.oss.ADCosServiceFactory;
import com.liudonghan.kit.pay.ADAliPayUtils;
import com.liudonghan.kit.pay.ADWxPayUtils;

public class MainActivity extends AppCompatActivity implements ADAliPayUtils.OnPayResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ADAliPayUtils.getInstance().setOnPayResultListener(this);
        findViewById(R.id.alipay).setOnClickListener(view -> ADAliPayUtils.getInstance().pay(MainActivity.this, ""));
        findViewById(R.id.wxpay).setOnClickListener(view -> ADWxPayUtils.getInstance().pay(MainActivity.this,"","","","","",""));
        ADCosServiceFactory.getInstance().uploadFile(this, "", "", new ADCosServiceFactory.OnUploadListener() {
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