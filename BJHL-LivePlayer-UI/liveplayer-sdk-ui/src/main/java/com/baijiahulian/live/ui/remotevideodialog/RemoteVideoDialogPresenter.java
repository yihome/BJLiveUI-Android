package com.baijiahulian.live.ui.remotevideodialog;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.models.imodels.IUserModel;

/**
 * Created by wangkangfei on 17/4/27.
 */

public class RemoteVideoDialogPresenter implements RemoteVideoDialogContract.Presenter {
    private RemoteVideoDialogContract.View view;
    private LiveRoomRouterListener routerListener;

    public RemoteVideoDialogPresenter(RemoteVideoDialogContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        IUserModel userModel = routerListener.getLiveRoom().getCurrentUser();
        if (userModel.getType() == LPConstants.LPUserType.Teacher
                || userModel.getType() == LPConstants.LPUserType.Assistant) {
            view.showCloseSpeaking();
        } else {
            view.hideCloseSpeaking();
        }
        view.showCurrentRemoteUserName(routerListener.getCurrentVideoUser().getUser().getName());
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
    }

    @Override
    public void switchFullscreen() {
        if (routerListener.switchable()) {
            routerListener.maximisePlayerView();
            routerListener.setSwitching();
        }
    }

    @Override
    public void closeVideo() {
        if (!routerListener.isCurrentUserTeacher())
            routerListener.setVideoManipulated(true);
        boolean isAudioOn = routerListener.getCurrentVideoUser().isAudioOn();
        String currentVideoUserId = routerListener.getCurrentVideoUser().getUser().getUserId();
        routerListener.playVideoClose(currentVideoUserId);
        if (isAudioOn) {
            routerListener.getLiveRoom().getPlayer().playAudio(currentVideoUserId);
        }
    }

    @Override
    public void closeSpeaking() {
        routerListener.playVideoClose(routerListener.getCurrentVideoUser().getUser().getUserId());
        routerListener.getLiveRoom().getSpeakQueueVM().closeOtherSpeak(routerListener.getCurrentVideoUser().getUser().getUserId());
    }
}
