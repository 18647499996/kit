package com.liudonghan.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.help.Tip;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.liudonghan.kit.location.ADLocationUtils;
import com.liudonghan.kit.location.listener.OnADInputTipsQueryListener;
import com.liudonghan.kit.location.listener.OnADLocationUtilsListener;
import com.liudonghan.kit.location.listener.OnADWeatherSearchListener;
import com.liudonghan.kit.oss.ADCosServiceFactory;
import com.liudonghan.kit.pay.ADAliPayUtils;
import com.liudonghan.kit.pay.ADWxPayUtils;
import com.liudonghan.multi_image.permission.ADPermission;
import com.liudonghan.multi_image.permission.OnPermission;
import com.liudonghan.multi_image.permission.Permission;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ADAliPayUtils.OnPayResultListener, ADCosServiceFactory.OnUploadListener, OnADLocationUtilsListener, OnADWeatherSearchListener {

    private static final String TAG = "Mac_Liu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ADAliPayUtils.getInstance().setOnPayResultListener(this);
        findViewById(R.id.alipay).setOnClickListener(view -> ADAliPayUtils.getInstance().pay(MainActivity.this, ""));
        findViewById(R.id.wxpay).setOnClickListener(view -> ADWxPayUtils.getInstance().pay(MainActivity.this, "", "", "", "", "", ""));
        findViewById(R.id.oss).setOnClickListener(view -> ADCosServiceFactory.getInstance().uploadFile(MainActivity.this, "Android/Image/Portrait/" + System.currentTimeMillis(), "/storage/emulated/0/DCIM/Screenshots/Screenshot_2023-01-30-18-12-59-54_92b64b2a7aa6eb3771ed6e18d0029815.jpg", MainActivity.this));
        findViewById(R.id.location).setOnClickListener(view -> ADPermission.with(MainActivity.this)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            ADLocationUtils.getInstance()
                                    .getLocation(MainActivity.this)
                                    .registerLocationListener(MainActivity.this)
                                    .startLocation();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                }));
        findViewById(R.id.weather).setOnClickListener(view -> ADLocationUtils.getInstance().getCityWeather(MainActivity.this, "北京", WeatherSearchQuery.WEATHER_TYPE_LIVE, MainActivity.this));
        findViewById(R.id.tips).setOnClickListener(view -> ADLocationUtils.getInstance().getInputTipsQuery(MainActivity.this,
                new ADLocationUtils.InputTipsBuilder()
                        .setTips("潘家园")
                        .setCity("北京")
                        .setLimit(true), new OnADInputTipsQueryListener() {
                    @Override
                    public void onGetInputTipsList(List<Tip> tipList) {

                    }

                    @Override
                    public void onFail(String errorMsg) {

                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ADLocationUtils.getInstance().destroyLocation();
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

    @Override
    public void onProgress(long current, long total) {

    }

    @Override
    public void onSucceed(String result) {

    }

    @Override
    public void onFail(String errorCode, String message) {

    }

    @Override
    public void onLocationSucceed(AMapLocation aMapLocation) {

    }

    @Override
    public void onLocationError(int errorCode, String errorInfo) {
        Log.i(TAG, errorCode + " --------- " + errorInfo);
    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLive localWeatherLive) {
        Log.i(TAG, "天气实况： " + localWeatherLive.getWeather());
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecast localWeatherForecast) {
        Log.i(TAG, "天气预报：" + localWeatherForecast.getWeatherForecast().toString());
    }

    @Override
    public void onFail(int errorCode, String errorMsg) {

    }
}