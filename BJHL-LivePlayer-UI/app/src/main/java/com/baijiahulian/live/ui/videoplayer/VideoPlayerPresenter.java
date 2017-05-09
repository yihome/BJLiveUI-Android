package com.baijiahulian.live.ui.videoplayer;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.wrapper.LPPlayer;

/**
 * Created by Shubo on 2017/3/4.
 */

public class VideoPlayerPresenter implements VideoPlayerContract.Presenter {

    private VideoPlayerContract.View view;
    private LiveRoomRouterListener routerListener;
    private LPPlayer player;

    public VideoPlayerPresenter(VideoPlayerContract.View view) {
        this.view = view;
    }

    @Override
    public LPPlayer getPlayer() {
        return player;
    }

    @Override
    public void switchWithMaximum() {
        routerListener.maximisePlayerView();
    }

    @Override
    public void popUpRemoteVideoDialog() {
        routerListener.showRemoteVideoPlayer();
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.routerListener = liveRoomRouterListener;
        this.player = routerListener.getLiveRoom().getPlayer();
    }

    public String getCurrentPlayingUserId() {
        return player.getCurrentVideoUserId();
    }

    @Override
    public void subscribe() {
        IMediaModel mediaModel = routerListener.getCurrentVideoUser();
        if (mediaModel != null && mediaModel.getUser() != null) {
            view.showCurrentVideoUserName(mediaModel.getUser().getName());
        }
    }


    public void playVideo(String userId) {
        player.playVideo(userId);
    }

    public void playAVClose(String userId) {
        player.playAVClose(userId);
    }

    @Override
    public void unSubscribe() {
        player.playAVClose(player.getCurrentVideoUserId());
    }

    @Override
    public void destroy() {
        player = null;
        routerListener = null;
        view = null;
    }
}
