package com.baijiahulian.live.ui.leftmenu;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;

/**
 * Created by Shubo on 2017/2/15.
 */

public class LeftMenuPresenter implements LeftMenuContract.Presenter {

    private LiveRoomRouterListener routerListener;
    private LeftMenuContract.View view;
    private boolean isScreenCleared = false;

    public LeftMenuPresenter(LeftMenuContract.View view) {
        this.view = view;
    }

    @Override
    public void clearScreen() {
        isScreenCleared = !isScreenCleared;
        view.notifyClearScreenChanged(isScreenCleared);
        if (isScreenCleared) routerListener.clearScreen();
        else routerListener.unClearScreen();
    }

    @Override
    public void showMessageInput() {
        routerListener.navigateToMessageInput();
    }

    @Override
    public boolean isScreenCleared() {
        return isScreenCleared;
    }

    @Override
    public boolean isForbidden() {
        return !routerListener.isTeacherOrAssistant() && routerListener.getLiveRoom().getForbidStatus();
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
        routerListener = null;
        view = null;
    }
}
