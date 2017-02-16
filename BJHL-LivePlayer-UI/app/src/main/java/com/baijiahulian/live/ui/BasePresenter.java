package com.baijiahulian.live.ui;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;

/**
 * Created by Shubo on 2017/2/13.
 */

public interface BasePresenter {

    void setRouter(LiveRoomRouterListener liveRoomRouterListener);

    void subscribe();

    void unSubscribe();

    void destroy();
}
