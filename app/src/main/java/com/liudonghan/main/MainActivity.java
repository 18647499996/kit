package com.liudonghan.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItemV2;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiSearchV2;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.liudonghan.kit.ijk.ADTikTokController;
import com.liudonghan.kit.ijk.ADVideoPlayManager;
import com.liudonghan.kit.location.ADLocationManager;
import com.liudonghan.kit.location.listener.OnADGeocodeSearchListener;
import com.liudonghan.kit.location.listener.OnADInputTipsQueryListener;
import com.liudonghan.kit.location.listener.OnADLocationUtilsListener;
import com.liudonghan.kit.location.listener.OnADPoiSearchListener;
import com.liudonghan.kit.location.listener.OnADWeatherSearchListener;
import com.liudonghan.kit.oss.ADCosServiceManager;
import com.liudonghan.kit.pay.ADAliPayUtils;
import com.liudonghan.kit.pay.ADWxPayUtils;
import com.liudonghan.multi_image.permission.ADPermission;
import com.liudonghan.multi_image.permission.OnPermission;
import com.liudonghan.multi_image.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.player.VideoView;

public class MainActivity extends AppCompatActivity implements ADAliPayUtils.OnPayResultListener, ADCosServiceManager.OnUploadListener, OnADLocationUtilsListener, OnADWeatherSearchListener, OnADInputTipsQueryListener, OnADPoiSearchListener, OnADGeocodeSearchListener {

    private static final String TAG = "Mac_Liu";
    // https://v1.kwaicdn.com/upic/2024/02/21/17/BMjAyNDAyMjExNzE0MDhfMzM5MjUyOTEyNl8xMjU1NjgzMDczNDlfMF8z_hd15_B868895bcc4150326a071d4d1619a62fc.mp4?pkey=AAVO9mqPaX5JjraHc4Nk9f9l2d7YBvQ9JMGYPV544cRhc5WNvQe6W9iEPgcvDRoY_TSmKgx-Nfp0Y-WQs4wmnqh9EewvuXgoEePCYFoYuoHAWcB3auBchJ5naD2DQN5VCBE&tag=1-1708567193-unknown-1-nstvtcyr2p-6e4db18933fc2b98&clientCacheKey=3x9npqr7ib7xzva_hd15.mp4&di=6527d6d2&bp=14944&tt=hd15&ss=vp
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ADAliPayUtils.getInstance().setOnPayResultListener(this);
        findViewById(R.id.alipay).setOnClickListener(view -> ADAliPayUtils.getInstance().pay(MainActivity.this, ""));
        findViewById(R.id.wxpay).setOnClickListener(view -> ADWxPayUtils.getInstance().pay(MainActivity.this, "", "", "", "", "", ""));
        findViewById(R.id.oss).setOnClickListener(view -> ADCosServiceManager.getInstance().uploadFile(MainActivity.this, "Android/Image/Portrait/" + System.currentTimeMillis(), "/storage/emulated/0/DCIM/Screenshots/Screenshot_2023-01-30-18-12-59-54_92b64b2a7aa6eb3771ed6e18d0029815.jpg", MainActivity.this));
        findViewById(R.id.location).setOnClickListener(view -> ADPermission.with(MainActivity.this)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            ADLocationManager.getInstance()
                                    .getLocation(MainActivity.this)
                                    .registerLocationListener(MainActivity.this)
                                    .startLocation();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                }));
        findViewById(R.id.weather).setOnClickListener(view -> ADLocationManager.getInstance().getCityWeather(this, "北京", WeatherSearchQuery.WEATHER_TYPE_FORECAST, this));
        findViewById(R.id.tips).setOnClickListener(view -> ADLocationManager.getInstance().getInputTipsQuery(this,
                new ADLocationManager.Builder()
                        .setTips("荷塘月色")
                        .setCity("武汉")
                        .setLimit(true), this));
        findViewById(R.id.keyword).setOnClickListener(view -> ADLocationManager.getInstance().getPoiSearch(this, new ADLocationManager.SearchBuilder("一江赋", "", "武汉").setPage(1).setLimit(50), this));
        findViewById(R.id.bound).setOnClickListener(view -> ADLocationManager.getInstance().getPoiSearch(this, new ADLocationManager.SearchBuilder(), new PoiSearchV2.SearchBound(new LatLonPoint(39.961275, 116.406478), 200), this));
        ADLocationManager.getInstance().getGeocodeSearch(this, 39.961275, 116.406478, this);
        VideoView ijkVideoView = findViewById(R.id.ijk);
//        String proxyUrl = ADVideoPlayManager.getInstance().diskCacheStorage("https://recordcdn.quklive.com/upload/vod/user1462960877450854/1702396014865166/8/video.m3u8");
        ijkVideoView.setUrl("https://recordcdn.quklive.com/upload/vod/user1462960877450854/1708396984566219/8/video.m3u8");
//        ijkVideoView.setUrl(proxyUrl);
        ADTikTokController tikTokController = new ADTikTokController(this);
        ijkVideoView.setVideoController(tikTokController);
        tikTokController.addControlComponent(new CompleteView(this), new ErrorView(this), new PrepareView(this), new VodControlView(this));
//        standardVideoController.addDefaultControlComponent("标题", false);
//        standardVideoController.setPlayerState(ijkVideoView.getCurrentPlayerState());
//        standardVideoController.setPlayState(ijkVideoView.getCurrentPlayState());
        ijkVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ADLocationManager.getInstance().destroyLocation();
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
        Log.e(TAG, "OSS地址：" + result);
    }

    @Override
    public void onFail(String errorCode, String message) {

    }

    @Override
    public void onLocationSucceed(AMapLocation aMapLocation) {
        Log.d(TAG, "定位信息：" + "\n" +
                aMapLocation.getProvince() + "\n" +
                aMapLocation.getCity() + "\n" +
                aMapLocation.getCityCode() + "\n" +
                aMapLocation.getAddress() + "\n" +
                aMapLocation.getAdCode() + "\n" +
                aMapLocation.getLatitude() + "\n" +
                aMapLocation.getLongitude() + "\n" +
                aMapLocation.getDistrict() + "\n" +
                aMapLocation.getStreet() + aMapLocation.getStreetNum() + "\n" +
                aMapLocation.getTime() + "\n" +
                aMapLocation.getLocationType() + "\n" +
                aMapLocation.getPoiName() + "\n" +
                aMapLocation.getAoiName() + "\n" +
                aMapLocation.getLocationDetail());
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
    public void onPoiSearched(ArrayList<PoiItemV2> poi) {
        for (int i = 0; i < poi.size(); i++) {
            Log.i(TAG, "poi检索：" + poi.get(i).getTitle() + " " + poi.get(i).getProvinceName() + " " + poi.get(i).getCityName() + " - " + poi.get(i).getAdName() + " " + poi.get(i).getSnippet() + " " + poi.get(i).getLatLonPoint().getLongitude() + "," + poi.get(i).getLatLonPoint().getLatitude());
        }
    }

    @Override
    public void onGetInputTipsList(List<Tip> tipList) {
        for (int i = 0; i < tipList.size(); i++) {
            Log.i(TAG, "poi检索：" + tipList.get(i).getAddress() + " " + tipList.get(i).getName() + " " + tipList.get(i).getPoint().getLongitude() + "," + tipList.get(i).getPoint().getLatitude());
        }
    }

    @Override
    public void onFail(int errorCode, String errorMsg) {

    }

    @Override
    public void onPoiItemSearched(PoiItemV2 poiItem) {
        Log.i(TAG, "poi检索条目：" + poiItem.toString());
    }

    @Override
    public void onRegSearched(RegeocodeAddress regeocodeAddress) {
        Log.i(TAG, "逆地理位置编码：" + regeocodeAddress.getFormatAddress());
    }

    @Override
    public void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList) {

    }
}