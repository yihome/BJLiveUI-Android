package com.baijiahulian.live.ui.speakerspanel;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baijiahulian.avsdk.liveplayer.ViESurfaceViewRenderer;
import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.utils.DisplayUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by Shubo on 2017/6/10.
 */

public class VideoView extends FrameLayout {

    private TextView tvName;
    private SurfaceView surfaceView;
    private ImageView imageView;

    private String name;
    private String waterMarkUrl;
    private int waterMarkPosition = 1;

    @ColorInt
    int color = -1;

    public VideoView(Context context, String name, String waterMarkUrl, int waterMarkPosition) {
        super(context);
        this.name = name;
        this.waterMarkPosition = waterMarkPosition;
        this.waterMarkUrl = waterMarkUrl;
        init();
    }

    public VideoView(Context context, String name) {
        super(context);
        this.name = name;
        init();
    }

    private void init() {
        ViewGroup.LayoutParams flLp = new ViewGroup.LayoutParams(DisplayUtils.dip2px(getContext(), 100), DisplayUtils.dip2px(getContext(), 76));
        this.setLayoutParams(flLp);
        if (color == -1) color = ContextCompat.getColor(getContext(), R.color.live_white);
        //视频
        surfaceView = ViESurfaceViewRenderer.CreateRenderer(getContext(), true);
        surfaceView.setZOrderMediaOverlay(true);
        this.addView(surfaceView);
        //名字
        tvName = new TextView(getContext());
        FrameLayout.LayoutParams tvLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLp.gravity = Gravity.BOTTOM;
        tvName.setGravity(Gravity.CENTER);
        tvName.setTextColor(color);
        tvName.setPadding(0, DisplayUtils.dip2px(getContext(), 2), 0, DisplayUtils.dip2px(getContext(), 2));
        tvName.setLines(1);
        tvName.setText(name);
        tvName.setTextSize(13);
        tvName.setBackgroundResource(R.drawable.shape_remote_name_bg);
        tvName.setLayoutParams(tvLp);
        this.addView(tvName);

        if (TextUtils.isEmpty(waterMarkUrl)) {
            return;
        }
        ImageView ivWaterMark = new ImageView(getContext());
        FrameLayout.LayoutParams ivLp = new FrameLayout.LayoutParams(DisplayUtils.dip2px(getContext(), 12), DisplayUtils.dip2px(getContext(), 8));
        switch (waterMarkPosition) {
            case 1:
                ivLp.gravity = GravityCompat.START | Gravity.TOP;
                break;
            case 2:
                ivLp.gravity = GravityCompat.END | Gravity.TOP;
                break;
            case 3:
                ivLp.gravity = GravityCompat.END | Gravity.BOTTOM;
                break;
            case 4:
                ivLp.gravity = GravityCompat.START | Gravity.BOTTOM;
                break;
        }
        this.addView(ivWaterMark);
        Picasso.with(getContext()).load(waterMarkUrl).into(ivWaterMark);
    }

    public View getSurfaceView() {
        return surfaceView;
    }

    public void setNameColor(@ColorInt int color) {
        this.color = color;
        if (tvName != null) {
            tvName.setTextColor(color);
        }
    }
}
