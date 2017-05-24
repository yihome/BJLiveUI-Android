package com.baijiahulian.live.ui.ppt;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;

/**
 * Created by Shubo on 2017/2/18.
 */

public class PPTPresenter implements PPTContract.Presenter {

    private PPTContract.View view;
    private LiveRoomRouterListener routerListener;
    private boolean isScreenCleared = false;

    public PPTPresenter(PPTContract.View view) {
        this.view = view;
    }

    @Override
    public void switchWithMaximum() {
        if(routerListener.switchable()) {
            routerListener.maximisePPTView();
            routerListener.setSwitching();
        }
    }

    @Override
    public void popUpPPTDialog() {
        routerListener.showPPTDialogFragment();
    }

    @Override
    public void clearScreen() {
        isScreenCleared = !isScreenCleared;
        if (isScreenCleared) routerListener.clearScreen();
        else routerListener.unClearScreen();
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
    }
}
