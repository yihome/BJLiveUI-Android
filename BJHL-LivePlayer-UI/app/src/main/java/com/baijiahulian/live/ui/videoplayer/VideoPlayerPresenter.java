package com.baijiahulian.live.ui.videoplayer;

import android.text.TextUtils;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;
import com.baijiahulian.livecore.wrapper.LPPlayer;

import java.util.List;

import rx.Subscriber;
import rx.observables.ConnectableObservable;

/**
 * Created by Shubo on 2017/3/4.
 */

public class VideoPlayerPresenter implements VideoPlayerContract.Presenter {

    private VideoPlayerContract.View view;
    private LiveRoomRouterListener routerListener;
    private LPPlayer player;
    private String currentPlayingUserId;
    private Subscriber<List<IMediaModel>> subs;

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
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.routerListener = liveRoomRouterListener;
        this.player = routerListener.getLiveRoom().getPlayer();
    }

    @Override
    public void subscribe() {
        subs = new LPErrorPrintSubscriber<List<IMediaModel>>() {
            @Override
            public void call(List<IMediaModel> iMediaModels) {
                if (iMediaModels.size() > 0 && iMediaModels.get(0).isVideoOn()) {
                    currentPlayingUserId = iMediaModels.get(0).getUser().getUserId();
                    player.playVideo(currentPlayingUserId);
                }
            }
        };
        ConnectableObservable<List<IMediaModel>> obs = routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfActiveUsers();
        obs.subscribe(subs);
        obs.connect();

        routerListener.getLiveRoom().getSpeakQueueVM().requestActiveUsers();
    }

    @Override
    public void unSubscribe() {
        if (!TextUtils.isEmpty(currentPlayingUserId)) {
            player.playAVClose(currentPlayingUserId);
            currentPlayingUserId = null;
        }
        subs.unsubscribe();
    }

    @Override
    public void destroy() {
        player = null;
        routerListener = null;
        view = null;
    }
}
