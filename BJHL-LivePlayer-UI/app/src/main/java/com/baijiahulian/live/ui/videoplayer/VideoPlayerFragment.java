package com.baijiahulian.live.ui.videoplayer;

import android.os.Bundle;
import android.view.View;

import com.baijiahulian.avsdk.liveplayer.GLTextureView;
import com.baijiahulian.avsdk.liveplayer.ViERenderer;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by Shubo on 2017/3/4.
 */

public class VideoPlayerFragment extends BaseFragment implements VideoPlayerContract.View {

    VideoPlayerContract.Presenter presenter;

    @Override
    public void setPresenter(VideoPlayerContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        if (view == null) {
            view = ViERenderer.CreateRenderer(getActivity(), true);
        }
        return view;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        presenter.getPlayer().setVideoView((GLTextureView)view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchWithMaximum();
            }
        });
    }
}
