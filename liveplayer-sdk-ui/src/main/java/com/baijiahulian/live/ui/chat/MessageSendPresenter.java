package com.baijiahulian.live.ui.chat;

import android.text.TextUtils;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.context.LPConstants;

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
        if (!TextUtils.isEmpty(message)) {
            if (message.startsWith("/dev")) {
                routerListener.showDebugBtn();
                return;
            }
        }
        routerListener.getLiveRoom().getChatVM().sendMessage(message);
        view.showMessageSuccess();
    }

    @Override
    public void sendEmoji(String emoji) {
        checkNotNull(routerListener);
        routerListener.getLiveRoom().getChatVM().sendEmojiMessage(emoji);
        view.showMessageSuccess();
    }

    @Override
    public void sendPicture(String path) {
        routerListener.sendImageMessage(path);
        view.onPictureSend();
    }

    @Override
    public void chooseEmoji() {
        view.showEmojiPanel();
    }

    @Override
    public boolean canSendPicture() {
        // 大班课只有老师和助教能发图片，一对一、小班课都能发
        return routerListener.getLiveRoom().getRoomType() != LPConstants.LPRoomType.Multi
                || routerListener.isTeacherOrAssistant();
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
