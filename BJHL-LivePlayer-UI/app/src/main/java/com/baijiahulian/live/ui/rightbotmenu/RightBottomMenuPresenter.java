package com.baijiahulian.live.ui.rightbotmenu;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Shubo on 2017/2/16.
 */

public class RightBottomMenuPresenter implements RightBottomMenuContract.Presenter {

    private RightBottomMenuContract.View view;

    private LiveRoomRouterListener liveRoomRouterListener;

    private Subscription subscriptionOfCamera, subscriptionOfMic;

    public RightBottomMenuPresenter(RightBottomMenuContract.View view) {
        this.view = view;
    }

    @Override
    public void changeZoom() {

    }

    @Override
    public void changeAudio() {
        if(liveRoomRouterListener.getLiveRoom().getRecorder().isAudioAttached())
            liveRoomRouterListener.getLiveRoom().getRecorder().detachAudio();
        else
            liveRoomRouterListener.getLiveRoom().getRecorder().attachAudio();
    }

    @Override
    public void changeVideo() {
        if(liveRoomRouterListener.getLiveRoom().getRecorder().isVideoAttached())
            liveRoomRouterListener.getLiveRoom().getRecorder().detachVideo();
        else
            liveRoomRouterListener.getLiveRoom().getRecorder().attachVideo();
    }

    @Override
    public void more(int anchorX, int anchorY) {
        liveRoomRouterListener.showMorePanel(anchorX, anchorY);
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.liveRoomRouterListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        subscriptionOfCamera = liveRoomRouterListener.getLiveRoom().getRecorder().getObservableOfCameraOn()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        view.showVideoStatus(aBoolean);
                    }
                });
        subscriptionOfMic = liveRoomRouterListener.getLiveRoom().getRecorder().getObservableOfMicOn()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        view.showAudioStatus(aBoolean);
                    }
                });
    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfCamera);
        RxUtils.unSubscribe(subscriptionOfMic);
    }

    @Override
    public void destroy() {
        liveRoomRouterListener = null;
        view = null;
    }
}
