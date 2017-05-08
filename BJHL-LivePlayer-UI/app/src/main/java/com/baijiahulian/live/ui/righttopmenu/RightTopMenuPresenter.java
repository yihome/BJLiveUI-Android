package com.baijiahulian.live.ui.righttopmenu;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wangkangfei on 17/5/8.
 */

public class RightTopMenuPresenter implements RightTopMenuContract.Presenter {
    private LiveRoomRouterListener liveRoomRouterListener;
    private Subscription subscriptionOfCloudRecord;
    private LPConstants.LPUserType currentUserType;

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.liveRoomRouterListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        currentUserType = liveRoomRouterListener.getLiveRoom().getCurrentUser().getType();
        subscriptionOfCloudRecord = liveRoomRouterListener.getLiveRoom().getObservableOfCloudRecordStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            if (currentUserType == LPConstants.LPUserType.Teacher || currentUserType == LPConstants.LPUserType.Assistant) {
                                liveRoomRouterListener.navigateToCloudRecord(true);
                            } else {
                                liveRoomRouterListener.navigateToCloudRecord(false);
                            }
                        } else {
                            liveRoomRouterListener.navigateToCloudRecord(false);
                        }
                    }
                });

    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfCloudRecord);
    }

    @Override
    public void destroy() {
        liveRoomRouterListener = null;
    }
}
