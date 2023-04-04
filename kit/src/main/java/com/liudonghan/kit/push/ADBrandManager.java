package com.liudonghan.kit.push;

import android.content.Context;
import android.os.Build;

import com.meizu.cloud.pushsdk.util.MzSystemUtils;

public class ADBrandManager {

    public static final int BRAND_HUAWEI = 2001;
    public static final int BRAND_XIAOMI = 2000;
    public static final int BRAND_OPPO = 2004;
    public static final int BRAND_VIVO = 2005;
    public static final int BRAND_MEIZU = 2003;
    public static final int BRAND_GOOLE_ELSE = 2002;

    public static boolean isBrandXiaomi() {
        return "xiaomi".equalsIgnoreCase(getBuildBrand())
                || "xiaomi".equalsIgnoreCase(getBuildManufacturer());
    }

    public static boolean isBrandHuawei() {
        return "huawei".equalsIgnoreCase(getBuildBrand())
                || "huawei".equalsIgnoreCase(getBuildManufacturer())
                || "honor".equalsIgnoreCase(getBuildBrand())
                || "honor".equalsIgnoreCase(getBuildManufacturer());
    }

    public static boolean isBrandMeizu(Context context) {
        return "meizu".equalsIgnoreCase(getBuildBrand())
                || "meizu".equalsIgnoreCase(getBuildManufacturer())
                || "22c4185e".equalsIgnoreCase(getBuildBrand())
                || MzSystemUtils.isBrandMeizu(context);
    }

    public static boolean isBrandOppo() {
        return "oppo".equalsIgnoreCase(getBuildBrand())
                || "realme".equalsIgnoreCase(getBuildBrand())
                || "oneplus".equalsIgnoreCase(getBuildBrand())
                || "oppo".equalsIgnoreCase(getBuildManufacturer())
                || "realme".equalsIgnoreCase(getBuildManufacturer())
                || "oneplus".equalsIgnoreCase(getBuildManufacturer());
    }

    public static boolean isBrandVivo() {
        return "vivo".equalsIgnoreCase(getBuildBrand())
                || "vivo".equalsIgnoreCase(getBuildManufacturer());
    }

//    public static boolean isGoogleServiceSupport() {
//        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(TUIOfflinePushConfig.getInstance().getContext());
//        return resultCode == ConnectionResult.SUCCESS;
//    }

    public static int getInstanceType(Context context) {
        int vendorId;
        if (isBrandXiaomi()) {
            vendorId = BRAND_XIAOMI;
        } else if (isBrandHuawei()) {
            vendorId = BRAND_HUAWEI;
        } else if (isBrandOppo()) {
            vendorId = BRAND_OPPO;
        } else if (isBrandVivo()) {
            vendorId = BRAND_VIVO;
        } else if (isBrandMeizu(context)) {
            vendorId = BRAND_MEIZU;
        } else {
            vendorId = BRAND_XIAOMI;
        }

        return vendorId;
    }

    public static String getBuildBrand() {
        String BRAND;
        synchronized (ADBrandManager.class) {
            BRAND = Build.BRAND;
        }

        return BRAND;
    }

    public static String getBuildManufacturer() {
        String MANUFACTURER;
        synchronized (ADBrandManager.class) {
            MANUFACTURER = Build.MANUFACTURER;
        }
        return MANUFACTURER;
    }
}
