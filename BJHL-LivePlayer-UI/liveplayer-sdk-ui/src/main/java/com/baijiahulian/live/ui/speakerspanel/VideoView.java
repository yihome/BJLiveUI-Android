package com.baijiahulian.live.ui.speakerspanel;

import android.content.Context;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baijiahulian.avsdk.liveplayer.ViESurfaceViewRenderer;
import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.utils.DisplayUtils;

/**
 * Created by Shubo on 2017/6/10.
 */

public class VideoView extends FrameLayout {

    private FrameLayout flContainer;
    private TextView tvName;
    private SurfaceView surfaceView;

    public VideoView(Context context) {
        super(context);
        init();
    }

    private void init() {
        flContainer = new FrameLayout(getContext());
        ViewGroup.LayoutParams flLp = new ViewGroup.LayoutParams(DisplayUtils.dip2px(getContext(), 100), DisplayUtils.dip2px(getContext(), 76));
        flContainer.setLayoutParams(flLp);
        //视频
        surfaceView = ViESurfaceViewRenderer.CreateRenderer(getContext(), true);
        surfaceView.setZOrderMediaOverlay(true);
        flContainer.addView(surfaceView);
        //名字
        tvName = new TextView(getContext());
        FrameLayout.LayoutParams tvLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLp.gravity = Gravity.BOTTOM;
        tvName.setGravity(Gravity.CENTER);
        tvName.setTextColor(getResources().getColor(R.color.live_white));
        tvName.setPadding(0, 5, 0, 5);
        tvName.setLines(1);
        tvName.setBackgroundResource(R.drawable.shape_remote_name_bg);
        tvName.setLayoutParams(tvLp);
        flContainer.addView(tvName);

        this.addView(flContainer);
    }

    public View getSurfaceView() {
        return surfaceView;
    }
}
