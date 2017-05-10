package com.baijiahulian.live.ui.loading;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.context.LPError;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.launch.LPLaunchListener;

/**
 * Created by Shubo on 2017/2/14.
 */

public class LoadingPresenter implements LoadingContract.Presenter {

    private LPLaunchListener launchListener;
    private LiveRoomRouterListener routerListener;
    private LoadingContract.View view;
    private String code, name;

    public LoadingPresenter(LoadingContract.View view, String code, String name) {
        this.view = view;
        this.code = code;
        this.name = name;
        launchListener = new LPLaunchListener() {
            @Override
            public void onLaunchSteps(int i, int i1) {
                LoadingPresenter.this.view.showLoadingSteps(i, i1);
            }

            @Override
            public void onLaunchError(LPError lpError) {
                LoadingPresenter.this.view.showLaunchError(lpError);
            }

            @Override
            public void onLaunchSuccess(LiveRoom liveRoom) {
                routerListener.setLiveRoom(liveRoom);
                routerListener.navigateToMain();
            }
        };
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
    }

    @Override
    public LPLaunchListener getLaunchListener() {
        return launchListener;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setLiveRoom(LiveRoom liveRoom) {
        routerListener.setLiveRoom(liveRoom);
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
