package com.baijiahulian.live.ui.rightmenu;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.models.imodels.IMediaControlModel;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;
import com.baijiahulian.livecore.utils.LPRxUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/2/15.
 */

public class RightMenuPresenter implements RightMenuContract.Presenter {

    private LiveRoomRouterListener liveRoomRouterListener;
    private RightMenuContract.View view;
    private LPConstants.LPUserType currentUserType;
    private Subscription subscriptionOfMediaControl, subscriptionOfActiveUser, subscriptionOfMedia,
            subscriptionOfSpeakApply, subscriptionOfAvatarSwitcher;

    private boolean isDrawing = false;

    public RightMenuPresenter(RightMenuContract.View view) {
        this.view = view;
    }

    @Override
    public void visitSpeakers() {
        liveRoomRouterListener.navigateToSpeakers();
    }

    @Override
    public void changeDrawing() {
        liveRoomRouterListener.navigateToPPTDrawing();
        isDrawing = !isDrawing;
    }

    @Override
    public void managePPT() {
        if (currentUserType == LPConstants.LPUserType.Teacher
                || currentUserType == LPConstants.LPUserType.Assistant) {
            liveRoomRouterListener.navigateToPPTWareHouse();
        }
    }

    @Override
    public void speakApply() {
        checkNotNull(liveRoomRouterListener);
        if (liveRoomRouterListener.isTeacherOrAssistant()) return;
        liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().requestSpeakApply();
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.liveRoomRouterListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        checkNotNull(liveRoomRouterListener);
        currentUserType = liveRoomRouterListener.getLiveRoom().getCurrentUser().getType();
        if (currentUserType == LPConstants.LPUserType.Teacher
                || currentUserType == LPConstants.LPUserType.Assistant) {
            view.showTeacherRightMenu();
        } else {
            view.showStudentRightMenu();
        }

        LPErrorPrintSubscriber<List<IMediaModel>> activeUserSubscriber = new LPErrorPrintSubscriber<List<IMediaModel>>() {
            @Override
            public void call(List<IMediaModel> iMediaModels) {
                refreshBtnStatus();
            }
        };
        final ConnectableObservable<List<IMediaModel>> observable = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfActiveUsers();
        subscriptionOfActiveUser = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(activeUserSubscriber);
        observable.connect();

        subscriptionOfMedia = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaNew()
                .mergeWith(liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaClose())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                    @Override
                    public void call(IMediaModel iMediaModel) {
                        refreshBtnStatus();
                    }
                });

        if (liveRoomRouterListener.isTeacherOrAssistant()) {

            subscriptionOfSpeakApply = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfSpeakApply()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                        @Override
                        public void call(IMediaModel iMediaModel) {
                            RxUtils.unSubscribe(subscriptionOfAvatarSwitcher);
                            view.showSpeakApplyImage(iMediaModel.getUser().getAvatar());
                            view.showSpeakApplyCount(liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getApplyList().size());
                        }
                    });

            subscriptionOfMediaControl = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaControl()
                    .mergeWith(liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfSpeakResponse())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaControlModel>() {
                        @Override
                        public void call(IMediaControlModel iMediaControlModel) {
                            refreshBtnStatus();
                        }
                    });
        } else {
            subscriptionOfMediaControl = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaControl()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaControlModel>() {
                        @Override
                        public void call(IMediaControlModel iMediaControlModel) {
                            if (iMediaControlModel.getUser().getUserId()
                                    .equals(liveRoomRouterListener.getLiveRoom().getCurrentUser().getUserId())) {
                                // 请求发言的用户自己
                                if (iMediaControlModel.isApplyAgreed()) {
                                    // 进入发言模式
                                    liveRoomRouterListener.enableSpeakerMode();
                                    view.showSpeakApplyAgreed();
                                } else {
                                    // 结束发言模式
                                    liveRoomRouterListener.disableSpeakerMode();
                                    if (isDrawing) {
                                        // 如果画笔打开 关闭画笔模式
                                        changeDrawing();
                                    }
                                    if (!iMediaControlModel.getSenderUserId().equals(liveRoomRouterListener.getLiveRoom().getCurrentUser().getUserId())) {
                                        // 不是自己结束发言的
                                        view.showSpeakApplyDisagreed();
                                    }
                                }
                            }
                        }
                    });
        }

        liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().requestActiveUsers();
    }

    private void refreshBtnStatus() {
        if (liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getSpeakQueueList().size() > 0) {
            if (liveRoomRouterListener.isTeacherOrAssistant() &&
                    liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getApplyList().size() > 0) {
                return;
            }
            if (subscriptionOfAvatarSwitcher == null || subscriptionOfAvatarSwitcher.isUnsubscribed())
                subscriptionOfAvatarSwitcher = Observable.interval(0, 5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new LPErrorPrintSubscriber<Long>() {
                            @Override
                            public void call(Long aLong) {
                                List<IMediaModel> mediaModels = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getSpeakQueueList();
                                view.showSpeakApplyImage(mediaModels.get(aLong.intValue() % mediaModels.size()).getUser().getAvatar());
                            }
                        });
        } else {
            LPRxUtils.unSubscribe(subscriptionOfAvatarSwitcher);
            view.showEmptySpeakers();
        }
    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfMediaControl);
        RxUtils.unSubscribe(subscriptionOfActiveUser);
        RxUtils.unSubscribe(subscriptionOfMedia);
        RxUtils.unSubscribe(subscriptionOfSpeakApply);
        RxUtils.unSubscribe(subscriptionOfAvatarSwitcher);
    }

    @Override
    public void destroy() {
        liveRoomRouterListener = null;
        view = null;
    }
}
