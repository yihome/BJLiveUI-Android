package com.baijiahulian.live.ui.videorecorder;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.wrapper.LPRecorder;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/2/18.
 */

public class RecorderPresenter implements RecorderContract.Presenter {

    private RecorderContract.View view;
    private LiveRoomRouterListener routerListener;

    public RecorderPresenter(RecorderContract.View view) {
        this.view = view;
    }

    @Override
    public void switchWithMaximum() {
        routerListener.maximiseRecorderView();
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        if(!routerListener.getLiveRoom().getRecorder().isPublishing()) {
            routerListener.getLiveRoom().getRecorder().publish();
        }
        if(!routerListener.getLiveRoom().getRecorder().isVideoAttached()) {
            routerListener.getLiveRoom().getRecorder().attachVideo();
        }
    }

    @Override
    public void unSubscribe() {
        if(routerListener.getLiveRoom().getRecorder().isVideoAttached()) {
            routerListener.getLiveRoom().getRecorder().detachVideo();
        }
    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
    }

    @Override
    public LPRecorder getRecorder() {
        checkNotNull(routerListener);
        return routerListener.getLiveRoom().getRecorder();
    }
}
