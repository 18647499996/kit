package com.liudonghan.kit.ijk;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import xyz.doikki.videoplayer.controller.BaseVideoController;
import xyz.doikki.videoplayer.player.AbstractPlayer;
import xyz.doikki.videoplayer.player.BaseVideoView;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:12/8/23
 */
public class ADVideoPlayView extends BaseVideoView<AbstractPlayer> {

    public ADVideoPlayView(@NonNull Context context) {
        super(context, null);
    }

    public ADVideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ADVideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
