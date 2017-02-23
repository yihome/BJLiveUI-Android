package com.baijiahulian.live.ui.rightbotmenu;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;

/**
 * Created by Shubo on 2017/2/16.
 */

public class RightBottomMenuPresenter implements RightBottomMenuContract.Presenter {

    private RightBottomMenuContract.View view;

    private LiveRoomRouterListener liveRoomRouterListener;

    public RightBottomMenuPresenter(RightBottomMenuContract.View view) {
        this.view = view;
    }

    @Override
    public void changeZoom() {

    }

    @Override
    public void changeAudio() {

    }

    @Override
    public void changeVideo() {

    }

    @Override
    public void more(int anchorX, int anchorY) {

    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.liveRoomRouterListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void destroy() {

    }
}
