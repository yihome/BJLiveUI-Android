package com.baijiahulian.live.ui.more;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Shubo on 2017/4/17.
 */

public class MoreMenuPresenter implements MoreMenuContract.Presenter {

    private MoreMenuContract.View view;
    private LiveRoomRouterListener routerListener;
    private Subscription subscriptionOfCloudRecord;
    private boolean recordStatus;

    public MoreMenuPresenter(MoreMenuContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        subscriptionOfCloudRecord = routerListener.getLiveRoom().getObservableOfCloudRecordStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        recordStatus = aBoolean;
                        if (aBoolean)
                            view.showCloudRecordOn();
                        else
                            view.showCloudRecordOff();
                    }
                });
        recordStatus = routerListener.getLiveRoom().getCloudRecordStatus();
        if (recordStatus)
            view.showCloudRecordOn();
        else
            view.showCloudRecordOff();

    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfCloudRecord);
    }

    @Override
    public void destroy() {
        routerListener = null;
        view = null;
    }

    @Override
    public void navigateToAnnouncement() {
        routerListener.navigateToAnnouncement();
    }

    @Override
    public void switchCloudRecord() {
        //ui
        routerListener.navigateToCloudRecord(!recordStatus);
        //logic
        routerListener.getLiveRoom().requestCloudRecord(!recordStatus);
    }

    @Override
    public void navigateToHelp() {
        routerListener.navigateToHelp();
    }

    @Override
    public void navigateToSetting() {
        routerListener.navigateToSetting();
    }

    @Override
    public boolean isTeacherOrAssistant() {
        return routerListener.isTeacherOrAssistant();
    }
}
