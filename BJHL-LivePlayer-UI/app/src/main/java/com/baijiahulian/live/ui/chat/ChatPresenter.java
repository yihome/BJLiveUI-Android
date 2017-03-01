package com.baijiahulian.live.ui.chat;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.models.imodels.IMessageModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;
import com.baijiahulian.livecore.utils.LPRxUtils;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/2/23.
 */

public class ChatPresenter implements ChatContract.Presenter {

    private LiveRoomRouterListener routerListener;
    private ChatContract.View view;
    private Subscription subscriptionOfDataChange;

    public ChatPresenter(ChatContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        checkNotNull(routerListener);
        subscriptionOfDataChange = routerListener.getLiveRoom().getChatVM().getObservableOfNotifyDataChange()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        view.notifyDataChanged();
                    }
                });
    }

    @Override
    public void unSubscribe() {
        LPRxUtils.unSubscribe(subscriptionOfDataChange);
    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
    }

    @Override
    public int getCount() {
        checkNotNull(routerListener);
        return routerListener.getLiveRoom().getChatVM().getMessageCount();
    }

    @Override
    public IMessageModel getMessage(int position) {
        checkNotNull(routerListener);
        return routerListener.getLiveRoom().getChatVM().getMessage(position);
    }
}
