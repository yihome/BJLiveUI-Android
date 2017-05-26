package com.baijiahulian.live.ui.loading;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.context.LPError;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.launch.LPLaunchListener;
import com.baijiahulian.livecore.models.imodels.IUserModel;
import com.baijiahulian.livecore.utils.LPSDKTaskQueue;

/**
 * Created by Shubo on 2017/2/14.
 */

public class LoadingPresenter implements LoadingContract.Presenter {

    private LPLaunchListener launchListener;
    private LiveRoomRouterListener routerListener;
    private LoadingContract.View view;
    private String code, name, sign;
    private long roomId;
    private boolean isReconnecting;
    private IUserModel userModel;
    private boolean isJoinCode;

    public LoadingPresenter(LoadingContract.View view, String code, String name, final boolean isReconnecting) {
        this.view = view;
        this.code = code;
        this.name = name;
        this.isReconnecting = isReconnecting;
        isJoinCode = true;
        initListener();
    }

    public LoadingPresenter(LoadingContract.View view, long roomId, String sign, IUserModel model, final boolean isReconnecting) {
        this.view = view;
        this.isReconnecting = isReconnecting;
        this.roomId = roomId;
        this.sign = sign;
        this.userModel = model;
        isJoinCode = false;
        initListener();
    }

    private void initListener() {
        launchListener = new LPLaunchListener() {
            @Override
            public void onLaunchSteps(int i, int i1) {
                if (LoadingPresenter.this.view != null)
                    LoadingPresenter.this.view.showLoadingSteps(i, i1);
            }

            @Override
            public void onLaunchError(LPError lpError) {
                // 如果出错显示ErrorFragment，LoadingFragment被移除了 routerListener为空了
                if (routerListener != null)
                    routerListener.showError(lpError);
            }

            @Override
            public void onLaunchSuccess(LiveRoom liveRoom) {
                if (isReconnecting) {
                    routerListener.showReconnectSuccess();
                } else {
                    routerListener.setLiveRoom(liveRoom);
                    routerListener.navigateToMain();
                }
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
    public String getSign() {
        return sign;
    }

    @Override
    public IUserModel getUser() {
        return userModel;
    }

    @Override
    public long getRoomId() {
        return roomId;
    }

    @Override
    public void setLiveRoom(LiveRoom liveRoom) {
        routerListener.setLiveRoom(liveRoom);
    }

    @Override
    public boolean isReconnect() {
        return isReconnecting;
    }

    @Override
    public boolean isJoinCode() {
        return isJoinCode;
    }

    @Override
    public void subscribe() {
        if (isReconnecting) {
            LPSDKTaskQueue queue = routerListener.getLiveRoom().createReconnectTaskQueue(launchListener);
            queue.start();
        }
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
