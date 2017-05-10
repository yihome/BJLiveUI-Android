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
        routerListener.maximisePlayerView();
    }

    @Override
    public void closeVideo() {
        //TODO:关闭视频
        routerListener.playVideoClose(routerListener.getCurrentVideoUser().getUser().getUserId());
    }

    @Override
    public void closeSpeaking() {
        //TODO:结束发言
        routerListener.playVideoClose(routerListener.getCurrentVideoUser().getUser().getUserId());
        routerListener.getLiveRoom().getSpeakQueueVM().closeOtherSpeak(routerListener.getCurrentVideoUser().getUser().getUserId());
    }
}
