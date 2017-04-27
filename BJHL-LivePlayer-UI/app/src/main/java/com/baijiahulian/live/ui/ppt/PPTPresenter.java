package com.baijiahulian.live.ui.ppt;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;

/**
 * Created by Shubo on 2017/2/18.
 */

public class PPTPresenter implements PPTContract.Presenter {

    private PPTContract.View view;
    private LiveRoomRouterListener routerListener;

    public PPTPresenter(PPTContract.View view) {
        this.view = view;
    }

    @Override
    public void switchWithMaximum() {
        routerListener.showPPTDialogFragment();
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
