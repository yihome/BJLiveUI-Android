package com.baijiahulian.live.ui.videorecorder;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.baijiahulian.avsdk.liveplayer.CameraGLSurfaceView;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by Shubo on 2017/2/18.
 */

public class VideoRecorderFragment extends BaseFragment implements VideoRecorderContract.View {

    private VideoRecorderContract.Presenter presenter;
    private GestureDetector gestureDetector;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        if (view == null) {
            view = new CameraGLSurfaceView(getActivity());
            ((SurfaceView)view).setZOrderMediaOverlay(true);
        }
        return view;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        presenter.getRecorder().setPreview((CameraGLSurfaceView) view);

        gestureDetector = new GestureDetector(getContext(), new MyGestureListener());

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            presenter.switchWithMaximum();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            presenter.popUpRecorderDialog();
            return super.onSingleTapConfirmed(e);
        }
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
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
