package com.baijiahulian.live.ui.recorderdialog;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.wrapper.LPRecorder;

/**
 * Created by wangkangfei on 17/4/27.
 */

public class RecorderDialogPresenter implements RecorderDialogContract.Presenter {
    private RecorderDialogContract.View view;
    private LiveRoomRouterListener routerListener;
    private LPRecorder recorder;

    public RecorderDialogPresenter(RecorderDialogContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.routerListener = liveRoomRouterListener;
        this.recorder = routerListener.getLiveRoom().getRecorder();
    }

    @Override
    public void subscribe() {
        if (recorder.isBeautyFilterOn()) {
            view.showSwitchPrettyFilterOff();
        } else {
            view.showSwitchPrettyFilterOn();
        }
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
        recorder = null;
    }

    @Override
    public void switchFullscreen() {
        routerListener.maximiseRecorderView();
    }

    @Override
    public void switchCamera() {
        recorder.switchCamera();
    }

    @Override
    public void switchPrettyFilter() {
        if (recorder.isBeautyFilterOn()) {
            recorder.closeBeautyFilter();
        } else {
            recorder.openBeautyFilter();
        }
    }

    @Override
    public void turnOffCamera() {
        if (recorder.isVideoAttached()) {
            recorder.detachVideo();
        }
    }
}
