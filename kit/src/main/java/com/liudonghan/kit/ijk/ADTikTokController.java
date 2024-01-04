package com.liudonghan.kit.ijk;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.liudonghan.kit.R;

import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.controller.GestureVideoController;
import xyz.doikki.videoplayer.controller.IControlComponent;
import xyz.doikki.videoplayer.player.VideoView;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:12/8/23
 */
public class ADTikTokController extends GestureVideoController {

    private LinearProgressIndicator linearProgressIndicator;
    /**
     * 是否缓冲
     */
    private boolean isBuffering;

    public ADTikTokController(@NonNull Context context) {
        this(context, null);
    }

    public ADTikTokController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ADTikTokController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ad_tik_tok_controller;
    }

    @Override
    protected void initView() {
        super.initView();
        linearProgressIndicator = findViewById(R.id.ad_tik_tok_controller_linear_progress);
        addControlComponent(
                new CompleteView(mActivity),
                new ErrorView(mActivity),
                new PrepareView(mActivity),
                new VodControlView(mActivity));
    }

    @Override
    protected void onPlayerStateChanged(int playerState) {
        super.onPlayerStateChanged(playerState);
    }

    @Override
    protected void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            //调用release方法会回到此状态
            case VideoView.STATE_IDLE:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                linearProgressIndicator.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
            case VideoView.STATE_PAUSED:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_BUFFERED:
                if (playState == VideoView.STATE_BUFFERED) {
                    isBuffering = false;
                }
                if (!isBuffering) {
                    linearProgressIndicator.setVisibility(GONE);
                }
                break;
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_BUFFERING:
                linearProgressIndicator.setVisibility(VISIBLE);
                if (playState == VideoView.STATE_BUFFERING) {
                    isBuffering = true;
                }
                break;
        }
    }

    @Override
    public void addControlComponent(IControlComponent... component) {
        super.addControlComponent(component);
    }


}
