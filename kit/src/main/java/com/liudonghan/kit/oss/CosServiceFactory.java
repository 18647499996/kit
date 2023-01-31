package com.liudonghan.kit.oss;

import android.content.Context;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Description：腾讯云OSS对象存储
 *
 * @author Created by: Li_Min
 * Time:
 */
public class CosServiceFactory {

    public String DEFAULT_REGION = "";

    public String BUCKET_NAME = "";

    public String SECRET_ID = "";

    public String SECRET_KEY = "";

    public static Map<String, CosXmlService> cosXmlServiceMap = new HashMap<>();


    private static volatile CosServiceFactory instance = null;

    private CosServiceFactory() {
    }

    public static CosServiceFactory getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (CosServiceFactory.class) {
                // double checkout
                if (null == instance) {
                    instance = new CosServiceFactory();
                }
            }
        }
        return instance;
    }

    /**
     * 添加配置
     *
     * @param secretId   应用ID
     * @param secretKey  应用Key
     * @param bucketName 存储桶名称
     * @param region     存储地区
     */
    public void setConfig(String secretId, String secretKey, String bucketName, String region) {
        this.SECRET_ID = secretId;
        this.SECRET_KEY = secretKey;
        this.BUCKET_NAME = bucketName;
        this.DEFAULT_REGION = region;
    }

    public CosXmlService getCosXmlService(Context context, String region, String secretId, String secretKey, boolean refresh) {
        if (refresh) {
            cosXmlServiceMap.remove(region);
        }
        CosXmlService cosXmlService = cosXmlServiceMap.get(region);

        if (cosXmlService == null) {
            CosXmlServiceConfig cosXmlServiceConfig = getCosXmlServiceConfig(region);
            QCloudCredentialProvider qCloudCredentialProvider = getCredentialProviderWithIdAndKey(secretId, secretKey);
            cosXmlService = new CosXmlService(context, cosXmlServiceConfig, qCloudCredentialProvider);
            cosXmlServiceMap.put(region, cosXmlService);
        }

        return cosXmlService;
    }

    public CosXmlService getCosXmlService(Context context, String secretId, String secretKey, boolean refresh) {
        return getCosXmlService(context, DEFAULT_REGION, secretId, secretKey, refresh);
    }

    /**
     * 获取配置类
     */
    private CosXmlServiceConfig getCosXmlServiceConfig(String region) {
        return new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .setDebuggable(true)
                .isHttps(true)
                .builder();
    }

    /**
     * 获取QCloudCredentialProvider对象，来给 SDK 提供临时密钥
     *
     * @param secretKey 永久密钥 secretKey
     * @param secretId  永久密钥 secretId
     */
    private static QCloudCredentialProvider getCredentialProviderWithIdAndKey(String secretId, String secretKey) {
        return new ShortTimeCredentialProvider(secretId, secretKey, 300);
    }

    /**
     * 腾讯OSS文件上传
     *
     * @param context          上下文
     * @param cosPath          文件标识
     * @param srcPath          文件绝对路径
     * @param onUploadListener 上传回调
     */
    public void uploadFile(Context context, String cosPath, String srcPath, OnUploadListener onUploadListener) {
        COSXMLUploadTask cosxmlUploadTask = new TransferManager(getCosXmlService(context, SECRET_ID, SECRET_KEY, false),
                new TransferConfig
                        .Builder()
                        .build()).upload(BUCKET_NAME, cosPath, srcPath, null);
        // 上传进度
        cosxmlUploadTask.setCosXmlProgressListener(onUploadListener::onProgress);
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
                COSXMLUploadTask.COSXMLUploadTaskResult result = (COSXMLUploadTask.COSXMLUploadTaskResult) cosXmlResult;
                onUploadListener.onSucceed(result.accessUrl);
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if (clientException != null) {
                    onUploadListener.onFail(String.valueOf(clientException.errorCode), clientException.getMessage());
                    clientException.printStackTrace();
                } else {
                    onUploadListener.onFail(Objects.requireNonNull(serviceException).getErrorCode(), serviceException.getMessage());
                    serviceException.printStackTrace();
                }
            }
        });
    }

    /**
     * 腾讯OSS视频上传功能（ 云点播视频审核 ）
     *
     * @param context               上下文
     * @param sign                  视频上传验签
     * @param videoPath             视频文件地址
     * @param onVideoUploadListener 回调结果
     */
    public void uploadVideoFile(Context context, String sign, String videoPath, OnVideoUploadListener onVideoUploadListener) {
        TXUGCPublish mVideoPublish = new TXUGCPublish(context, "independence_android");
        mVideoPublish.setListener(new TXUGCPublishTypeDef.ITXVideoPublishListener() {
            @Override
            public void onPublishProgress(long uploadBytes, long totalBytes) {
                onVideoUploadListener.onProgress(uploadBytes, totalBytes);
            }

            @Override
            public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
                onVideoUploadListener.onSucceed(result);
            }
        });

        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        param.signature = sign;
        param.videoPath = videoPath;
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode != 0) {
            onVideoUploadListener.onFail(publishCode, "视频发布失败，请稍后重试！");
        }
    }

    public interface OnUploadListener {

        /**
         * 上传进度
         *
         * @param current 当前上传
         * @param total   文件总大小
         */
        void onProgress(long current, long total);

        /**
         * 上传成功
         *
         * @param result 返回结果
         */
        void onSucceed(String result);

        /**
         * 请求失败
         *
         * @param errorCode 错误码
         * @param message   错误信息
         */
        void onFail(String errorCode, String message);
    }

    public interface OnVideoUploadListener {

        /**
         * 上传进度
         *
         * @param current 当前上传
         * @param total   文件总大小
         */
        void onProgress(long current, long total);

        /**
         * 上传成功
         *
         * @param result 返回结果
         */
        void onSucceed(TXUGCPublishTypeDef.TXPublishResult result);

        /**
         * 请求失败
         *
         * @param errorCode 错误码
         * @param message   错误信息
         */
        void onFail(int errorCode, String message);
    }
}
