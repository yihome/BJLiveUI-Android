package com.baijiahulian.live.ui.speakerspanel;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

    private TextView tvName;
    private SurfaceView surfaceView;
    private String name;

    public VideoView(Context context) {
        super(context);
        init();
    }

    private void init() {
        ViewGroup.LayoutParams flLp = new ViewGroup.LayoutParams(DisplayUtils.dip2px(getContext(), 100), DisplayUtils.dip2px(getContext(), 76));
        this.setLayoutParams(flLp);
        //视频
        surfaceView = ViESurfaceViewRenderer.CreateRenderer(getContext(), true);
        surfaceView.setZOrderMediaOverlay(true);
        this.addView(surfaceView);
        //名字
        tvName = new TextView(getContext());
        FrameLayout.LayoutParams tvLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLp.gravity = Gravity.BOTTOM;
        tvName.setGravity(Gravity.CENTER);
        tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.live_white));
        tvName.setPadding(0, DisplayUtils.dip2px(getContext(), 2), 0, DisplayUtils.dip2px(getContext(), 2));
        tvName.setLines(1);
        tvName.setText(name);
        tvName.setTextSize(13);
        tvName.setBackgroundResource(R.drawable.shape_remote_name_bg);
        tvName.setLayoutParams(tvLp);
        this.addView(tvName);
    }

    public View getSurfaceView() {
        return surfaceView;
    }

    public void setNameText(String text) {
        name = text;
        if(tvName != null){
            tvName.setText(name);
        }
    }
}
