package com.liudonghan.kit.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2019/11/29
 */
public class ADLocationUtils {

    private static final String TAG = "Mac_Liu";

    private static volatile ADLocationUtils instance = null;
    private AMapLocationClient aMapLocationClient;
    private LocationManager locationManager;

    private ADLocationUtils() {
    }

    public static ADLocationUtils getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (ADLocationUtils.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADLocationUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 配置定位
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
     * @param aMapLocationClientOption 定位配置参数
     * @return ADLocationUtils
     */
    public ADLocationUtils setConfig(AMapLocationClientOption aMapLocationClientOption) {
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
        init(context, "", new AMapLocationClientOption());
    }

    /**
     * 初始化定位
     *
     * @param context 上下文
     */
    public void init(Context context, String appKey) {
        init(context, appKey, new AMapLocationClientOption());
    }

    /**
     * 初始化定位
     *
     * @param context                  上下文
     * @param appKey                   应用Key
     * @param aMapLocationClientOption 定位参数配置
     */
    public void init(Context context, String appKey, AMapLocationClientOption aMapLocationClientOption) {
        try {
            AMapLocationClient.setApiKey(appKey);
            AMapLocationClient.updatePrivacyAgree(context, true);
            AMapLocationClient.updatePrivacyShow(context, true, true);
            aMapLocationClient = new AMapLocationClient(context);
            aMapLocationClient.setLocationOption(aMapLocationClientOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取高德天气情况
     *
     * @param context                 上下文
     * @param city                    城市
     * @param weatherType             实况天气 WeatherSearchQuery.WEATHER_TYPE_LIVE
     *                                预报天气 WeatherSearchQuery.WEATHER_TYPE_FORECAST
     * @param onWeatherSearchListener 天气实况回调
     */
    public void getCityWeather(Context context, String city, int weatherType, WeatherSearch.OnWeatherSearchListener onWeatherSearchListener) {
        WeatherSearchQuery weatherSearchQuery = new WeatherSearchQuery(city, weatherType);
        WeatherSearch weatherSearch;
        try {
            weatherSearch = new WeatherSearch(context);
            weatherSearch.setOnWeatherSearchListener(onWeatherSearchListener);
            weatherSearch.setQuery(weatherSearchQuery);
            weatherSearch.searchWeatherAsyn();
        } catch (AMapException e) {
            e.printStackTrace();
        }


    }

    /**
     * 注册定位监听
     *
     * @param onADLocationUtilsListener 监听回调
     */
    public ADLocationUtils registerLocationListener(OnADLocationUtilsListener onADLocationUtilsListener) {
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
     * 获取逆地理位置编码
     *
     * @param context                 上下文
     * @param latitude                经度
     * @param longitude               纬度
     * @param onGeocodeSearchListener 回调监听器
     */
    public void getGeocodeSearch(Context context, double latitude, double longitude, GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener) {
        GeocodeSearch geocodeSearch;
        try {
            geocodeSearch = new GeocodeSearch(context);
            geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(latitude, longitude), 200, GeocodeSearch.AMAP));
            geocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
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
        if (null == aMapLocationClient) {
            Log.i(TAG, "Initialization is not configured first LocationClient");
            return;
        }
        aMapLocationClient.stopLocation();
        aMapLocationClient.onDestroy();
        aMapLocationClient = null;
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


    public void originLocation(Context context) {
        //获取定位服务
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //检查定位是否被打开
        boolean gpsIsOpen = Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("Get Origin Location：", "定位是否打开：" + gpsIsOpen);
        // 为获取地理位置信息时设置查询条件 是按GPS定位还是network定位
        String bestProvider = getProvider();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        String mLongitude = String.valueOf(Objects.requireNonNull(location).getLongitude());
        String mLatitude = String.valueOf(location.getLatitude());
        Log.d("Get Origin Location：", "经纬度：" + mLongitude + " ====== " + mLatitude);
        Log.d("Get Origin Location：", "地址：" + getLocationAddress(location, context));
        double[] doubleArray = wgs2bd(location.getLatitude(), location.getLongitude());
        Log.d("Get Origin Location：", "百度经纬度：" + doubleArray[0] + "  ==== " + doubleArray[1]);
    }

    /**
     * 定位查询条件
     * 返回查询条件 ，获取目前设备状态下，最适合的定位方式
     */
    private String getProvider() {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        //Criteria.ACCURACY_FINE,当使用该值时，在建筑物当中，可能定位不了,建议在对定位要求并不是很高的时候用Criteria.ACCURACY_COARSE，避免定位失败
        // 查询精度：高
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(false);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 是否允许付费：是
        criteria.setCostAllowed(false);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
        return locationManager.getBestProvider(criteria, true);
    }

    /**
     * 将经纬度转换成中文地址
     *
     * @param location 定位信息
     * @return String
     */
    private String getLocationAddress(Location location, Context context) {
        String add;
        Geocoder geoCoder = new Geocoder(context, Locale.CHINESE);
        try {
            List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = addresses.get(0);
            Log.d("getLocationAddress：", address.toString());
            // Address[addressLines=[0:"中国",1:"北京市海淀区",2:"华奥饭店公司写字间中关村创业大街"]latitude=39.980973,hasLongitude=true,longitude=116.301712]
            int maxLine = address.getMaxAddressLineIndex();
            if (maxLine >= 2) {
                add = address.getAddressLine(1) + address.getAddressLine(2);
            } else {
                add = address.getAddressLine(1);
            }
        } catch (IOException e) {
            add = "";
            e.printStackTrace();
        }
        return add;
    }

    public static double[] wgs2bd(double lat, double lon) {
        double[] wgs2gcj = wgs2gcj(lat, lon);
        return gcj2bd(wgs2gcj[0], wgs2gcj[1]);
    }

    public static double[] gcj2bd(double lat, double lon) {
        double z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * x_pi);
        double theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_lat, bd_lon};
    }

    public static double[] bd2gcj(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[]{gg_lat, gg_lon};
    }

    public static double[] wgs2gcj(double lat, double lon) {
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    private static double transformLat(double lat, double lon) {
        double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
        ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lon / 12.0 * pi) + 320 * Math.sin(lon * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double lat, double lon) {
        double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat));
        ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0;
        return ret;

    }

    static double pi = 3.14159265358979324;
    static double a = 6378245.0;
    static double ee = 0.00669342162296594323;
    public final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    public interface OnADLocationUtilsListener {

        /**
         * 定位成功
         *
         * @param aMapLocation 定位结果
         */
        void onLocationSucceed(AMapLocation aMapLocation);

        /**
         * 定位失败
         *
         * @param errorCode 异常code码
         * @param errorInfo 异常信息
         */
        void onLocationError(int errorCode, String errorInfo);
    }

    private class ADLocationConfig {
    }
}
