package com.liudonghan.kit.ijk;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

import xyz.doikki.videoplayer.ijk.IjkPlayerFactory;
import xyz.doikki.videoplayer.player.AndroidMediaPlayerFactory;
import xyz.doikki.videoplayer.player.VideoViewConfig;
import xyz.doikki.videoplayer.player.VideoViewManager;

/**
 * Description：ijk播放器参数配置
 *
 * @author Created by: Li_Min
 * Time:2/3/23
 */
public class ADVideoPlayManager {
    private HttpProxyCacheServer httpProxyCacheServer;

    private static volatile ADVideoPlayManager instance = null;

    private ADVideoPlayManager() {
    }

    public static ADVideoPlayManager getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (ADVideoPlayManager.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADVideoPlayManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化ijk音频
     */
    public void init(Context context) {
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                // 使用MediaPlayer解码
                .setPlayerFactory(AndroidMediaPlayerFactory.create())
                // 使用ijkPlayer解码
                .setPlayerFactory(IjkPlayerFactory.create())
                .build());
        httpProxyCacheServer = new HttpProxyCacheServer(context);
    }

    /**
     * 获取适配缓存配置
     * @return HttpProxyCacheServer
     */
    public HttpProxyCacheServer getHttpProxyCacheServer() {
        return httpProxyCacheServer;
    }
}
