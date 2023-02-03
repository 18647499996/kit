package com.liudonghan.kit.ijk;

import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory;
import xyz.doikki.videoplayer.ijk.IjkPlayerFactory;
import xyz.doikki.videoplayer.player.AndroidMediaPlayerFactory;
import xyz.doikki.videoplayer.player.ProgressManager;
import xyz.doikki.videoplayer.player.VideoViewConfig;
import xyz.doikki.videoplayer.player.VideoViewManager;

/**
 * Description：ijk播放器参数配置
 *
 * @author Created by: Li_Min
 * Time:2/3/23
 */
public class ADVideoViewManager {
    private static volatile ADVideoViewManager instance = null;

    private ADVideoViewManager() {
    }

    public static ADVideoViewManager getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (ADVideoViewManager.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADVideoViewManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                //使用使用IjkPlayer解码
                .setPlayerFactory(IjkPlayerFactory.create())
                //使用ExoPlayer解码
                .setPlayerFactory(ExoMediaPlayerFactory.create())
                //使用MediaPlayer解码
                .setPlayerFactory(AndroidMediaPlayerFactory.create())
                .build());
    }
}
