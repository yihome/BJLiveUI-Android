package com.baijiahulian.live.ui.topbar;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/2/13.
 */

public class TopBarPresenter implements TopBarContract.Presenter {

    private LiveRoomRouterListener routerListener;

    private TopBarContract.View view;

    private Subscription subscriptionOfUserCountChange;

    public TopBarPresenter(TopBarContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        checkNotNull(routerListener);
        subscriptionOfUserCountChange = routerListener.getLiveRoom().getObservableOfUserNumberChange().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        view.showOnlineUserCount(integer);
                    }
                });
        // TODO: 2017/2/13 增加教室信息接口（名称）
    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfUserCountChange);
    }

    @Override
    public void destroy() {
        routerListener = null;
        view = null;
    }

    @Override
    public void navigateToShare() {

    }

    @Override
    public void navigateToUserList() {

    }
}
