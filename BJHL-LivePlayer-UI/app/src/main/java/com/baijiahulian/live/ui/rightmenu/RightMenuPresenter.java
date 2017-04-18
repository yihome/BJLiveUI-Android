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
    private LPErrorPrintSubscriber<Long> avatarSwitcherSubscriber;
    private Observable<Long> avatarSwitcherObservable;

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

        avatarSwitcherObservable = Observable.interval(0, 5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread());

        avatarSwitcherSubscriber = new LPErrorPrintSubscriber<Long>() {
            @Override
            public void call(Long aLong) {
                List<IMediaModel> mediaModels = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getSpeakQueueList();
                view.showSpeakApplyImage(mediaModels.get(mediaModels.size() % aLong.intValue()).getUser().getAvatar());
            }
        };

        final LPErrorPrintSubscriber<List<IMediaModel>> activeUserSubscriber = new LPErrorPrintSubscriber<List<IMediaModel>>() {
            @Override
            public void call(List<IMediaModel> iMediaModels) {
                if (iMediaModels == null || iMediaModels.size() == 0) {
                    view.showEmptySpeakers();
                } else {
                    view.showSpeakApplyImage(iMediaModels.get(iMediaModels.size() - 1).getUser().getAvatar());
                }
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
                        if (liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getSpeakQueueList().size() > 0) {
                            if (liveRoomRouterListener.isTeacherOrAssistant() &&
                                    liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getApplyList().size() > 0) {
                                return;
                            }
                            if (subscriptionOfAvatarSwitcher.isUnsubscribed())
                                subscriptionOfAvatarSwitcher = avatarSwitcherObservable.subscribe(avatarSwitcherSubscriber);
                        } else {
                            LPRxUtils.unSubscribe(subscriptionOfAvatarSwitcher);
                            view.showEmptySpeakers();
                        }
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
        }

        subscriptionOfMediaControl = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaControl()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<IMediaControlModel>() {
                    @Override
                    public void call(IMediaControlModel iMediaControlModel) {
                        if (liveRoomRouterListener.isTeacherOrAssistant()) {
                            int applyCount = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getApplyList().size();
                            // 如果还有用户请求发言的显示最后一个用户的头像，没有了则开始转正在发言的用户头像
                            if (applyCount > 0) {
                                view.showSpeakApplyImage(liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getApplyList().get(applyCount - 1).getAvatar());
                                view.showSpeakApplyCount(applyCount);
                            } else {
                                subscriptionOfAvatarSwitcher = avatarSwitcherObservable.subscribe(avatarSwitcherSubscriber);
                            }
                        } else {
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
                    }
                });
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
