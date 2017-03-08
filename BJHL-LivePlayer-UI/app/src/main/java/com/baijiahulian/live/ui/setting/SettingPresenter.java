package com.baijiahulian.live.ui.setting;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.wrapper.LPPlayer;
import com.baijiahulian.livecore.wrapper.LPRecorder;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/3/2.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View view;
    private LiveRoomRouterListener routerListener;
    private LPRecorder recorder;
    private LPPlayer player;

    public SettingPresenter(SettingContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.routerListener = liveRoomRouterListener;
        recorder = routerListener.getLiveRoom().getRecorder();
        player = routerListener.getLiveRoom().getPlayer();
    }

    @Override
    public void subscribe() {
        checkNotNull(routerListener);

        if (routerListener.getLiveRoom().getRecorder().getLinkType() == LPConstants.LPLinkType.TCP)
            view.showUpLinkTCP();
        else
            view.showUpLinkUDP();

        if (routerListener.getLiveRoom().getPlayer().getLinkType() == LPConstants.LPLinkType.TCP)
            view.showDownLinkTCP();
        else
            view.showDownLinkUDP();

        if (routerListener.getLiveRoom().getRecorder().isAudioAttached())
            view.showMicOpen();
        else
            view.showMicClosed();

        if (routerListener.getLiveRoom().getRecorder().isVideoAttached())
            view.showCameraOpen();
        else
            view.showCameraClosed();
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void destroy() {
        routerListener = null;
        recorder = null;
        view = null;
    }

    @Override
    public void changeMic() {
        switch (routerListener.getLiveRoom().getCurrentUser().getType()) {
            case Teacher:
            case Assistant:
                if (!recorder.isPublishing()) {
                    recorder.publish();
                }
                if (recorder.isAudioAttached()) {
                    recorder.detachAudio();
                    view.showMicClosed();
                } else {
                    recorder.attachAudio();
                    view.showMicOpen();
                }
                break;
            case Student:
                if (!recorder.isPublishing()) {
                    view.showStudentFail();
                }
                if (recorder.isAudioAttached()) {
                    recorder.detachAudio();
                    view.showMicClosed();
                } else {
                    recorder.attachAudio();
                    view.showMicOpen();
                }
                break;
            case Visitor:
                view.showVisitorFail();
                break;
        }

    }

    @Override
    public void changeCamera() {
        switch (routerListener.getLiveRoom().getCurrentUser().getType()) {
            case Teacher:
            case Assistant:
                if (!recorder.isPublishing()) {
                    recorder.publish();
                }
                if (recorder.isVideoAttached()) {
                    recorder.detachVideo();
                    view.showCameraClosed();
                } else {
                    recorder.attachVideo();
                    view.showCameraOpen();
                }
                break;
            case Student:
                if (!recorder.isPublishing()) {
                    view.showStudentFail();
                }
                if (recorder.isVideoAttached()) {
                    recorder.detachVideo();
                    view.showCameraClosed();
                } else {
                    recorder.attachVideo();
                    view.showCameraOpen();
                }
                break;
            case Visitor:
                view.showVisitorFail();
                break;
        }
    }

    @Override
    public void changeBeautyFilter() {
//        if (isBeautyFilterOn) {
//            recorder.closeBeautyFilter();
//            view.showBeautyFilterDisable();
//        } else {
//            recorder.openBeautyFilter();
//            view.showBeautyFilterEnable();
//        }
    }

    @Override
    public void setPPTFullScreen() {
//        LPConstants.LPPPTShowWay.
    }

    @Override
    public void setPPTOverspread() {

    }

    @Override
    public void setDefinitionLow() {

    }

    @Override
    public void setDefinitionHigh() {

    }

    @Override
    public void setUpLinkTCP() {
        recorder.setLinkType(LPConstants.LPLinkType.TCP);
    }

    @Override
    public void setUpLinkUDP() {
        recorder.setLinkType(LPConstants.LPLinkType.UDP);
    }

    @Override
    public void setDownLinkTCP() {
        player.setLinkType(LPConstants.LPLinkType.TCP);
    }

    @Override
    public void setDownLinkUDP() {
        player.setLinkType(LPConstants.LPLinkType.UDP);
    }


}
