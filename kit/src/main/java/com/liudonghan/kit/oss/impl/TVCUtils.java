package com.liudonghan.kit.oss.impl;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.UUID;

public class TVCUtils {
    private static final String TAG             = "TVCUtils";
    private static String g_simulate_idfa = "";

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String string2Md5(String value) {
        String MD5 = "";

        if (null == value) return MD5;

        try {
            MessageDigest mD = MessageDigest.getInstance("MD5");
            MD5 = byteArrayToHexString(mD.digest(value.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (MD5 == null) MD5 = "";

        return MD5;
    }

    // SimulateIDFA
    public static String getSimulateIDFA(Context context) {
        if (!TextUtils.isEmpty(g_simulate_idfa)) {
            return g_simulate_idfa;
        }

        String idfa = null;
        String idfaInSP = null;
        String idfaInFile = null;

        File sdcardDir = context.getExternalFilesDir(null);
        if (sdcardDir == null) {
            return g_simulate_idfa;
        }
        //???SP
        SharedPreferences sp = context.getSharedPreferences("com.tencent.ugcpublish.dev_uuid", Context.MODE_PRIVATE);
        idfaInSP = sp.getString("key_user_id", "");

        try {
            //?????????
            String userIdFilePath = sdcardDir.getAbsolutePath() + "/txrtmp/spuid";
            File userIdFile = new File(userIdFilePath);
            if (userIdFile.exists()) {
                FileInputStream fin = new FileInputStream(userIdFile);
                int length = fin.available();
                if (length > 0) {
                    byte[] buffer = new byte[length];
                    fin.read(buffer);
                    idfaInFile = new String(buffer, "UTF-8");
                }
                fin.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "read UUID from file failed! reason: " + e.getMessage());
        }

        if (!TextUtils.isEmpty(idfaInSP)) {
            idfa = idfaInSP;
        } else if (!TextUtils.isEmpty(idfaInFile)) {
            idfa = idfaInFile;
        }

        if (TextUtils.isEmpty(idfa)) {
            //UUID???16???????????????(UTC????????????(6??????) + ???????????????????????????????????????????????????(4??????) + MD5(???????????? + ????????????UUID)(16??????)
            idfa = "";
            long utcTimeMS = System.currentTimeMillis();
            long tickTimeMS = SystemClock.elapsedRealtime();
            String packetName = getPackageName(context);
            for (int i = 5; i >= 0; --i) {
                idfa += String.format("%02x", (byte) ((utcTimeMS >> (i * 8)) & 0xff));
            }
            for (int i = 3; i >= 0; --i) {
                idfa += String.format("%02x", (byte) ((tickTimeMS >> (i * 8)) & 0xff));
            }
            idfa += string2Md5(packetName + UUID.randomUUID().toString());
        }

        g_simulate_idfa = idfa;
        Log.i(TAG, "UUID:" + g_simulate_idfa);
        if (idfaInFile == null || !idfaInFile.equals(idfa)) {
            try {
                //?????????
                String userIdDirPath = sdcardDir.getAbsolutePath() + "/txrtmp";
                File userIdDir = new File(userIdDirPath);
                if (!userIdDir.exists()) userIdDir.mkdir();
                String userIdFilePath = sdcardDir.getAbsolutePath() + "/txrtmp/spuid";
                File userIdFile = new File(userIdFilePath);
                if (!userIdFile.exists()) userIdFile.createNewFile();
                FileOutputStream fout = new FileOutputStream(userIdFile);
                byte[] bytes = idfa.getBytes();
                fout.write(bytes);
                fout.close();
            } catch (Exception e) {
                Log.e(TAG, "write UUID to file failed! reason: " + e.getMessage());
            }
        }

        if (idfaInSP == null || !idfaInSP.equals(idfa)) {
            //???SP
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("key_user_id", idfa);
            editor.commit();
        }
        return g_simulate_idfa;
    }

    public static String getDevUUID(Context context) {
        return getSimulateIDFA(context);
    }

    /*
     * ??????????????????
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // ????????????????????????
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // ??????????????????????????????
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ??????????????????
     *
     * @param context Context
     * @return true ??????????????????
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                return TVCConstants.NETTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobileInfo != null) {
                    switch (mobileInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:// ????????????
                            switch (mobileInfo.getSubtype()) {
                                case TelephonyManager.NETWORK_TYPE_UMTS:
                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                case TelephonyManager.NETWORK_TYPE_HSDPA:
                                case TelephonyManager.NETWORK_TYPE_HSUPA:
                                case TelephonyManager.NETWORK_TYPE_HSPA:
                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                case TelephonyManager.NETWORK_TYPE_EHRPD:
                                case TelephonyManager.NETWORK_TYPE_HSPAP:
                                    return TVCConstants.NETTYPE_3G;
                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                case TelephonyManager.NETWORK_TYPE_1xRTT:
                                case TelephonyManager.NETWORK_TYPE_IDEN:
                                    return TVCConstants.NETTYPE_2G;
                                case TelephonyManager.NETWORK_TYPE_LTE:
                                    return TVCConstants.NETTYPE_4G;
                                default:
                                    return TVCConstants.NETTYPE_NONE;
                            }
                    }
                }
            }
        }

        return TVCConstants.NETTYPE_NONE;
    }

    /**
     * ?????? ????????????
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        String packagename = "";
        if (context != null) {
            try {
                PackageInfo info;
                info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                // ?????????????????????
                packagename = info.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return packagename;
    }

    /**
     * ?????? ?????????
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appname = "";
        if (context != null) {
            try {
                PackageManager packageManager;
                ApplicationInfo info;
                packageManager = context.getPackageManager();
                info = packageManager.getApplicationInfo(context.getPackageName(), 0);

                // ?????????????????????
                appname = (String) packageManager.getApplicationLabel(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appname;
    }


    /**
     * ??????uri????????????????????????????????????
     *
     * @param context ?????????
     * @param uriStr  ??????uri
     * @return ????????????????????????????????????
     */
    public static String getFilePathByUri(Context context, String uriStr) {
        try {
            Uri uri = Uri.parse(uriStr);
            String path = null;
            // ??? file:// ?????????
            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
                path = uri.getPath();
                return path;
            }
            // ??? content:// ?????????????????? content://media/extenral/images/media/17766
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                Cursor cursor = context.getContentResolver()
                        .query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        if (columnIndex > -1) {
                            path = cursor.getString(columnIndex);
                        }
                    }
                    cursor.close();
                }
                return path;
            }
            // 4.4???????????? ?????? content:// ?????????????????? content://com.android.providers.media.documents/document/image%3A235700
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse(
                            "content://downloads/public_downloads"),
                            Long.parseLong(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                } else {
                    String ret = getDataColumn(context, uri, null, null);
                    if (ret == null) {
                        return Uri.fromFile(new File(uri.getPath())).getPath();
                    } else {
                        return ret;
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context,
                                        Uri uri,
                                        String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * ???????????????URI?????????????????????
     *
     * @param context      ?????????
     * @param pathOrURIStr ?????????????????????URI
     * @return ????????????????????????????????????
     */
    public static String getAbsolutePath(Context context, String pathOrURIStr) {
        String absPath;
        if (pathOrURIStr.startsWith("content://")) {
            // ??????URI ?????????,????????????????????????
            absPath = getFilePathByUri(context, pathOrURIStr);
        } else {
            // ?????????????????? ??????
            absPath = pathOrURIStr;
        }
        return absPath;
    }


    public static boolean isExistsForPathOrUri(Context context, String pathOrURIStr) {
        boolean bVideoFileExist = false;
        String absPath = getAbsolutePath(context, pathOrURIStr);
        try {
            File file = new File(absPath);
            bVideoFileExist = file.isFile() && file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bVideoFileExist;
    }
}
