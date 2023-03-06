package com.liudonghan.kit.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItemV2;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResultV2;
import com.amap.api.services.poisearch.PoiSearchV2;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.liudonghan.kit.location.listener.OnADGeocodeSearchListener;
import com.liudonghan.kit.location.listener.OnADInputTipsQueryListener;
import com.liudonghan.kit.location.listener.OnADLocationUtilsListener;
import com.liudonghan.kit.location.listener.OnADPoiSearchListener;
import com.liudonghan.kit.location.listener.OnADWeatherSearchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：定位工具类
 * <p>
 * // todo 设置是否单次定位 mLocationOption.setOnceLocation();
 * 默认值：false
 * // todo 设置仅设备模式/高精度模式的系统定位自动回调最少间隔距离值 mLocationOption.setDeviceModeDistanceFilter();
 * 默认值：0米
 * 只有当定位模式为AMapLocationClientOption.AMapLocationMode.Device_Sensors（仅设备模式）或 AMapLocationClientOption.AMapLocationMode.Hight_Accuracy（高精度模式）有效，值小于0时无效
 * // todo 设置首次定位是否等待卫星定位结果 mLocationOption.setGpsFirst();
 * 默认值：false
 * 只有在单次定位高精度定位模式下有效
 * 设置为true时，会等待卫星定位结果返回，最多等待30秒，若30秒后仍无卫星定位结果返回，返回网络定位结果
 * 从4.5.0版本开始等待卫星定位结果返回的时间可以通过 AMapLocationClientOption.setGpsFirstTimeout(long)进行设置
 * // todo 设置发起定位请求的时间间隔 mLocationOption.setInterval();
 * 默认值：2000毫秒
 * // todo 设置联网超时时间 mLocationOption.setHttpTimeOut();
 * 默认值：30000毫秒
 * // todo 设置定位模式 mLocationOption.setLocationMode();
 * // todo 设置是否返回地址信息，默认返回地址信息 mLocationOption.setNeedAddress();
 * 默认值：true, 返回地址信息 2.9.0之前的版本定位类型为AMapLocation.LOCATION_TYPE_GPS时不会返回地址信息
 * 自2.9.0版本开始，当类型为AMapLocation.LOCATION_TYPE_GPS时也可以返回地址信息(需要网络通畅，第一次有可能没有地址信息返回）
 * // todo 设置退出时是否杀死进程 mLocationOption.setKillProcess();
 * 默认值:false, 不杀死
 * 注意：如果设置为true，并且配置的service不是remote的则会杀死当前页面进程，请慎重使用
 * // todo 设置是否使用缓存策略 mLocationOption.setLocationCacheEnable();
 * 默认为true 使用缓存策略
 * // todo 设置是否使用设备传感器 mLocationOption.setSensorEnable();
 * 默认值：false 不使用设备传感器
 * // todo 设置是否允许调用WIFI刷新 mLocationOption.setWifiScan();
 * 默认值为true，
 * 当设置为false时会停止主动调用WIFI刷新，将会极大程度影响定位精度，但可以有效的降低定位耗电
 * // todo 设置定位是否等待WIFI列表刷新 定位精度会更高，但是定位速度会变慢1-3秒 从3.7.0版本开始，支持连续定位（连续定位时首次会等待刷新） 3.7.0之前的版本，仅适用于单次定位，当设置为true时，连续定位会自动变为单次定位, mLocationOption.setOnceLocationLatest();
 * // todo 设置逆地理信息的语言,目前之中中文和英文 mLocationOption.setGeoLanguage();
 * 默认值：AMapLocationClientOption.GeoLanguage.DEFAULT
 *
 * @author Created by: Li_Min
 * Time:2019/11/29
 */
public class ADLocationManager {

    private static final String TAG = "Mac_Liu";

    private static volatile ADLocationManager instance = null;
    private AMapLocationClient aMapLocationClient;
    private LocationManager locationManager;
    private AMapLocationClientOption aMapLocationClientOption;

    private ADLocationManager() {
    }

    public static ADLocationManager getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (ADLocationManager.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADLocationManager();
                }
            }
        }
        return instance;
    }

    /**
     * 配置定位
     *
     * @param aMapLocationClientOption 定位配置参数
     * @return ADLocationUtils
     */
    public ADLocationManager setConfig(AMapLocationClientOption aMapLocationClientOption) {
        if (null == aMapLocationClient) {
            Log.i(TAG, "Initialization is not configured first LocationClient");
            return this;
        }
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        return this;
    }

    /**
     * 初始化定位
     *
     * @param context 上下文
     */
    public void init(Context context) {
        init(context, new AMapLocationClientOption());
    }

    /**
     * 初始化定位
     *
     * @param context                  上下文
     * @param aMapLocationClientOption 定位参数配置
     */
    public void init(Context context, AMapLocationClientOption aMapLocationClientOption) {
        try {
            AMapLocationClient.updatePrivacyAgree(context, true);
            AMapLocationClient.updatePrivacyShow(context, true, true);
            this.aMapLocationClientOption = aMapLocationClientOption;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ADLocationManager getLocation(Context context) {
        try {
            aMapLocationClient = new AMapLocationClient(context);
            aMapLocationClient.setLocationOption(aMapLocationClientOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 注册定位监听
     *
     * @param onADLocationUtilsListener 监听回调
     */
    public ADLocationManager registerLocationListener(OnADLocationUtilsListener onADLocationUtilsListener) {
        if (null == aMapLocationClient) {
            Log.i(TAG, "Initialization is not configured first LocationClient");
            return this;
        }
        aMapLocationClient.setLocationListener(aMapLocation -> {
            if (null != aMapLocation && 0 == aMapLocation.getErrorCode()) {
                onADLocationUtilsListener.onLocationSucceed(aMapLocation);
            } else {
                if (null != aMapLocation) {
                    onADLocationUtilsListener.onLocationError(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
                } else {
                    onADLocationUtilsListener.onLocationError(9999, "定位失败，请稍后重试");
                }
            }
            stopLocation();
        });
        return this;
    }

    /**
     * 获取高德天气情况
     *
     * @param context                   上下文
     * @param city                      城市
     * @param weatherType               实况天气 WeatherSearchQuery.WEATHER_TYPE_LIVE
     *                                  预报天气 WeatherSearchQuery.WEATHER_TYPE_FORECAST
     * @param onADWeatherSearchListener 天气实况回调
     */
    public void getCityWeather(Context context, String city, int weatherType, OnADWeatherSearchListener onADWeatherSearchListener) {
        WeatherSearch weatherSearch;
        try {
            weatherSearch = new WeatherSearch(context);
            weatherSearch.setQuery(new WeatherSearchQuery(city, weatherType));
            weatherSearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
                @Override
                public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                    if (1000 == i) {
                        if (null != localWeatherLiveResult && null != localWeatherLiveResult.getLiveResult()) {
                            onADWeatherSearchListener.onWeatherLiveSearched(localWeatherLiveResult.getLiveResult());
                        } else {
                            onADWeatherSearchListener.onFail(9999, "未查询到天气实况");
                        }
                    } else {
                        onADWeatherSearchListener.onFail(i, "实况天气查询失败");
                    }
                }

                @Override
                public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                    if (1000 == i) {
                        if (null != localWeatherForecastResult && null != localWeatherForecastResult.getForecastResult()) {
                            onADWeatherSearchListener.onWeatherForecastSearched(localWeatherForecastResult.getForecastResult());
                        } else {
                            onADWeatherSearchListener.onFail(9999, "未查询到天气预报");
                        }
                    } else {
                        onADWeatherSearchListener.onFail(i, "天气预报查询失败");
                    }
                }
            });
            weatherSearch.searchWeatherAsyn();
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 地址内容自动匹配器
     *
     * @param context                    上下文
     * @param inputTipsBuilder           匹配参数
     * @param onAdInputTipsQueryListener 回调监听
     */
    public void getInputTipsQuery(Context context, Builder inputTipsBuilder, OnADInputTipsQueryListener onAdInputTipsQueryListener) {
        InputtipsQuery inputTipsQuery = new InputtipsQuery(inputTipsBuilder.getTips(), inputTipsBuilder.getCity());
        inputTipsQuery.setCityLimit(inputTipsBuilder.isLimit());
        inputTipsQuery.setLocation(inputTipsBuilder.getLatLonPoint());
        Inputtips inputTips = new Inputtips(context, inputTipsQuery);
        inputTips.setInputtipsListener((list, i) -> {
            if (1000 != i || null == list || 0 == list.size()) {
                onAdInputTipsQueryListener.onFail(i, "未匹配到查询数据");
                return;
            }
            List<Tip> tipList = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                if (TextUtils.isEmpty(list.get(j).getPoiID()) || null == list.get(j).getPoint()) {
                    return;
                }
                tipList.add(list.get(j));
            }
            onAdInputTipsQueryListener.onGetInputTipsList(tipList);
        });
        inputTips.requestInputtipsAsyn();
    }

    /**
     * 关键字检索POI
     *
     * @param context               上下文
     * @param query                 查询参数
     * @param onADPoiSearchListener 检索回调接口
     */
    public void getPoiSearch(Context context, SearchBuilder query, OnADPoiSearchListener onADPoiSearchListener) {
        getPoiSearch(context, query, null, onADPoiSearchListener);

    }

    /**
     * 获取周边检索
     *
     * @param context               上下文
     * @param query                 检索条件参数
     * @param searchBound           周边检索参数
     * @param onADPoiSearchListener 检索回调接口
     */
    public void getPoiSearch(Context context, SearchBuilder query, PoiSearchV2.SearchBound searchBound, OnADPoiSearchListener onADPoiSearchListener) {
        try {
            PoiSearchV2 poiSearchV2 = new PoiSearchV2(context, query);
            poiSearchV2.setBound(searchBound);
            poiSearchV2.setOnPoiSearchListener(new PoiSearchV2.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResultV2 poiResultV2, int i) {
                    if (1000 == i) {
                        if (null != poiResultV2 && null != poiResultV2.getPois()) {
                            onADPoiSearchListener.onPoiSearched(poiResultV2.getPois());
                        } else {
                            onADPoiSearchListener.onFail(9999, "未检索到Poi数据");
                        }
                    } else {
                        onADPoiSearchListener.onFail(i, "未检索到Poi数据");
                    }
                }

                @Override
                public void onPoiItemSearched(PoiItemV2 poiItemV2, int i) {
                    onADPoiSearchListener.onPoiItemSearched(poiItemV2);
                }
            });
            poiSearchV2.searchPOIAsyn();
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取逆地理位置编码（ 经纬度坐标转地址信息 ）
     *
     * @param context                   上下文
     * @param latitude                  经度
     * @param longitude                 纬度
     * @param onADGeocodeSearchListener 回调监听器
     */
    public void getGeocodeSearch(Context context, double latitude, double longitude, OnADGeocodeSearchListener onADGeocodeSearchListener) {
        GeocodeSearch geocodeSearch;
        try {
            geocodeSearch = new GeocodeSearch(context);
            geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(latitude, longitude), 200, GeocodeSearch.AMAP));
            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (i == 1000) {
                        if (null != regeocodeResult && null != regeocodeResult.getRegeocodeAddress()) {
                            onADGeocodeSearchListener.onRegSearched(regeocodeResult.getRegeocodeAddress());
                        } else {
                            onADGeocodeSearchListener.onFail(9999, "未获取到地址信息");
                        }
                    } else {
                        onADGeocodeSearchListener.onFail(i, "获取定位地址信息异常");
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    if (i == 1000) {
                        if (null != geocodeResult && null != geocodeResult.getGeocodeAddressList()) {
                            onADGeocodeSearchListener.onGeocodeSearched(geocodeResult.getGeocodeAddressList());
                        } else {
                            onADGeocodeSearchListener.onFail(9999, "未获取到地址信息");
                        }
                    } else {
                        onADGeocodeSearchListener.onFail(i, "获取定位地址信息异常");
                    }
                }
            });
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启定位
     */
    public void startLocation() {
        if (null == aMapLocationClient) {
            Log.i(TAG, "Initialization is not configured first LocationClient");
            return;
        }
        aMapLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (null == aMapLocationClient) {
            Log.i(TAG, "Initialization is not configured first LocationClient");
            return;
        }
        aMapLocationClient.stopLocation();
    }

    /**
     * 销毁
     */
    public void destroyLocation() {
        if (aMapLocationClient != null) {
            aMapLocationClient.stopLocation();
            aMapLocationClient.onDestroy();
            aMapLocationClient = null;
        }
    }


    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 距离
     */
    public double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        double earthRadius = 6378.137;
        s = s * earthRadius;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }


    /**
     * 距离格式化
     *
     * @param distance 以千米为单位
     * @return Stirng
     */
    @SuppressLint("DefaultLocale")
    public String getDistanceKMFormat(double distance) {
        double value = distance;
        if (distance >= 1000) {
            value = value / 1000;
            return String.format("%.1f", value) + "km";
        } else {
            return String.format("%.0f", value) + "m";
        }


    }


    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 百度坐标转高德（传入经度、纬度）
     *
     * @param lng 百度经度
     * @param lat 百度纬度
     * @return LatLng
     */
    public LatLng bdDecrypt(Double lng, Double lat) {
        double X_PI = Math.PI * 3000.0 / 180.0;
        double x = lng - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double newLng = z * Math.cos(theta);
        double newLat = z * Math.sin(theta);
        return new LatLng(newLat, newLng);
    }

    /**
     * 高德坐标转百度（传入经度、纬度）
     *
     * @param lng 高德经度
     * @param lat 高德纬度
     * @return LatLng
     */
    public LatLng bdEncrypt(double lng, double lat) {
        double X_PI = Math.PI * 3000.0 / 180.0;
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * X_PI);
        double newlng = z * Math.cos(theta) + 0.0065;
        double newlat = z * Math.sin(theta) + 0.006;
        return new LatLng(newlat, newlng);
    }

    public static class Builder {
        private String tips;
        private String city;
        private LatLonPoint latLonPoint;
        private boolean isLimit;

        public Builder() {

        }

        public Builder(String tips, String city) {
            this.tips = tips;
            this.city = city;
        }

        public Builder(String tips, String city, LatLonPoint latLonPoint, boolean isLimit) {
            this.tips = tips;
            this.city = city;
            this.latLonPoint = latLonPoint;
            this.isLimit = isLimit;
        }

        public String getTips() {
            return tips;
        }

        public Builder setTips(String tips) {
            this.tips = tips;
            return this;
        }

        public String getCity() {
            return city;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public LatLonPoint getLatLonPoint() {
            return latLonPoint;
        }

        public Builder setLatLonPoint(LatLonPoint latLonPoint) {
            this.latLonPoint = latLonPoint;
            return this;
        }

        public boolean isLimit() {
            return isLimit;
        }

        public Builder setLimit(boolean limit) {
            isLimit = limit;
            return this;
        }
    }

    public static class SearchBuilder extends PoiSearchV2.Query {

        private boolean isLimit;

        public SearchBuilder() {
            super("", "", "");
        }

        public SearchBuilder(String keyWord, String poiCode) {
            super(keyWord, poiCode);
        }

        public SearchBuilder(String keyWord, String poiCode, String city) {
            super(keyWord, poiCode, city);
        }

        public SearchBuilder setPage(int pageNum) {
            setPageNum(pageNum);
            return this;
        }

        public SearchBuilder setLimit(int limit) {
            setPageSize(limit);
            return this;
        }

        public SearchBuilder setLimit(boolean isLimit) {
            setCityLimit(isLimit);
            return this;
        }

        public SearchBuilder setSort(boolean distanceSort) {
            setDistanceSort(distanceSort);
            return this;
        }
    }
}
