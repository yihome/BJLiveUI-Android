package com.baijiahulian.live.ui.videorecorder;

import android.os.Bundle;
import android.view.View;

import com.baijiahulian.avsdk.liveplayer.CameraGLTextureView;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by Shubo on 2017/2/18.
 */

public class RecorderFragment extends BaseFragment implements RecorderContract.View {

    private RecorderContract.Presenter presenter;

    private boolean isMaximised = false;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        if (view == null) {
            view = new CameraGLTextureView(getActivity());
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
                if (!isMaximised)
                    presenter.switchWithMaximum();
            }
        });
    }

    @Override
    public boolean isMaximised() {
        return isMaximised;
    }

    @Override
    public void setIsDisplayMaximised(boolean maximised) {
        this.isMaximised = maximised;
    }

    @Override
    public void setPresenter(RecorderContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
