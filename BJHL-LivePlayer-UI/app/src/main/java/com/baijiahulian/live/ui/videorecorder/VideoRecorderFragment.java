package com.baijiahulian.live.ui.videorecorder;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.baijiahulian.avsdk.liveplayer.CameraGLSurfaceView;
import com.baijiahulian.avsdk.liveplayer.CameraGLTextureView;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by Shubo on 2017/2/18.
 */

public class VideoRecorderFragment extends BaseFragment implements VideoRecorderContract.View {

    private VideoRecorderContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        if (view == null) {
            view = new CameraGLSurfaceView(getActivity());
        }
        return view;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        presenter.getRecorder().setPreview((CameraGLTextureView) view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchWithMaximum();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        presenter.getRecorder().invalidVideo();
    }

    @Override
    public void setPresenter(VideoRecorderContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
