package com.baijiahulian.live.ui.chat;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/3/4.
 */

public class MessageSendPresenter implements MessageSendContract.Presenter {

    private MessageSendContract.View view;
    private LiveRoomRouterListener routerListener;

    public MessageSendPresenter(MessageSendContract.View view) {
        this.view = view;
    }

    @Override
    public void sendMessage(String message) {
        checkNotNull(routerListener);
        routerListener.getLiveRoom().getChatVM().sendMessage(message);
        view.showMessageSuccess();
    }

    @Override
    public void choosePhoto() {

    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.routerListener = liveRoomRouterListener;
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