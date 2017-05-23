package com.baijiahulian.live.ui.pptdialog;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.models.imodels.IUserModel;

/**
 * Created by wangkangfei on 17/4/27.
 */

public class PPTDialogPresenter implements PPTDialogContract.Presenter {
    private LiveRoomRouterListener routerListener;
    private PPTDialogContract.View view;
    private IUserModel currentUser;

    public PPTDialogPresenter(PPTDialogContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.routerListener = liveRoomRouterListener;
        this.currentUser = routerListener.getLiveRoom().getCurrentUser();
    }

    @Override
    public void subscribe() {
        if (routerListener.isTeacherOrAssistant()) {
            view.showManagePPT();
        } else {
            view.hideManagePPT();
        }
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
        currentUser = null;
    }

    @Override
    public void switchFullscreen() {
        if(routerListener.switchable()) {
            routerListener.maximisePPTView();
            routerListener.setSwitching();
        }
    }

    @Override
    public void managePPT() {
        routerListener.navigateToPPTWareHouse();
    }
}
