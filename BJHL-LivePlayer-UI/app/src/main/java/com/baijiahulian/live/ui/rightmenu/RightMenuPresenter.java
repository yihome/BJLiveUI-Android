package com.baijiahulian.live.ui.rightmenu;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.models.imodels.IForbidChatModel;
import com.baijiahulian.livecore.models.imodels.IMediaControlModel;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;
import com.baijiahulian.livecore.utils.LPRxUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
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
            subscriptionOfSpeakApply, subscriptionOfAvatarSwitcher, subscriptionOfSpeakApplyCounter,
            subscriptionOfSpeakApplyResponse;
    private int speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_NONE;
    private boolean isDrawing = false;
    private LiveRoom liveRoom;

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
        view.showDrawingStatus(isDrawing);
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
        if (speakApplyStatus == RightMenuContract.STUDENT_SPEAK_APPLY_NONE) {
            liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().requestSpeakApply();
            speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_APPLYING;
            subscriptionOfSpeakApplyCounter = Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                    .take(300)
                    .map(new Func1<Long, Long>() {
                        @Override
                        public Long call(Long aLong) {
                            return 30000L - aLong.intValue() * 100;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            view.showSpeakApplyCountDown(aLong.intValue());
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    }, new Action0() {
                        @Override
                        public void call() {
                            // 倒计时结束，取消发言请求
                            speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_NONE;
                            RxUtils.unSubscribe(subscriptionOfSpeakApplyCounter);
                            liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().cancelSpeakApply();
                            view.showSpeakApplyCanceled();
                        }
                    });
        } else if (speakApplyStatus == RightMenuContract.STUDENT_SPEAK_APPLY_APPLYING) {
            // 取消发言请求
            speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_NONE;
            RxUtils.unSubscribe(subscriptionOfSpeakApplyCounter);
            liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().cancelSpeakApply();
            view.showSpeakApplyCanceled();
        } else if (speakApplyStatus == RightMenuContract.STUDENT_SPEAK_APPLY_SPEAKING) {
            // 取消发言
            speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_NONE;
            liveRoomRouterListener.disableSpeakerMode();
            liveRoomRouterListener.getLiveRoom().getSpeakQueueVM()
                    .closeOtherSpeak(liveRoomRouterListener.getLiveRoom().getCurrentUser().getUserId());
            view.showSpeakApplyCanceled();
        }
    }

    @Override
    public void changePPTDrawBtnStatus(boolean shouldShow) {
        if (shouldShow) {
            view.showPPTDrawBtn();
        } else {
            view.hidePPTDrawBtn();
        }
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.liveRoomRouterListener = liveRoomRouterListener;
        this.liveRoom = liveRoomRouterListener.getLiveRoom();
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
                refreshSpeakQueueBtnStatus();
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
                        refreshSpeakQueueBtnStatus();
                    }
                });

        if (liveRoomRouterListener.isTeacherOrAssistant()) {
            // 老师
            subscriptionOfSpeakApply = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfSpeakApply()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                        @Override
                        public void call(IMediaModel iMediaModel) {
                            RxUtils.unSubscribe(subscriptionOfAvatarSwitcher);
                            view.showSpeakQueueImage(iMediaModel.getUser().getAvatar());
                            view.showSpeakQueueCount(liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getApplyList().size());
                        }
                    });

            subscriptionOfMediaControl = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaControl()
                    .mergeWith(liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfSpeakResponse())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaControlModel>() {
                        @Override
                        public void call(IMediaControlModel iMediaControlModel) {
                            refreshSpeakQueueBtnStatus();
                        }
                    });
        } else {
            // 学生
            subscriptionOfMediaControl = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM()
                    .getObservableOfMediaControl()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaControlModel>() {
                        @Override
                        public void call(IMediaControlModel iMediaControlModel) {
                            if (iMediaControlModel.isApplyAgreed()) {
                                // 邀请发言
                                speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_SPEAKING;
                                liveRoomRouterListener.enableSpeakerMode();
                            } else {
                                // 结束发言模式
                                RxUtils.unSubscribe(subscriptionOfSpeakApplyCounter);
                                speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_NONE;
                                liveRoomRouterListener.disableSpeakerMode();
                                if (isDrawing) {
                                    // 如果画笔打开 关闭画笔模式
                                    changeDrawing();
                                }
                                if (!iMediaControlModel.getSenderUserId().equals(liveRoomRouterListener.getLiveRoom().getCurrentUser().getUserId())) {
                                    // 不是自己结束发言的
                                    view.showSpeakClosedByTeacher();
                                }
                            }
                        }
                    });
            subscriptionOfSpeakApplyResponse = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfSpeakResponse()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaControlModel>() {
                        @Override
                        public void call(IMediaControlModel iMediaControlModel) {
                            if (!iMediaControlModel.getUser().getUserId()
                                    .equals(liveRoomRouterListener.getLiveRoom().getCurrentUser().getUserId()))
                                return;
                            // 请求发言的用户自己
                            if (iMediaControlModel.isApplyAgreed()) {
                                // 进入发言模式
                                RxUtils.unSubscribe(subscriptionOfSpeakApplyCounter);
                                speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_SPEAKING;
                                liveRoomRouterListener.enableSpeakerMode();
                                view.showSpeakApplyAgreed();
                                liveRoomRouterListener.getLiveRoom().getRecorder().publish();
                                liveRoomRouterListener.getLiveRoom().getRecorder().attachAudio();
                            } else {
                                RxUtils.unSubscribe(subscriptionOfSpeakApplyCounter);
                                speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_NONE;
                                if (!iMediaControlModel.getSenderUserId().equals(liveRoomRouterListener.getLiveRoom().getCurrentUser().getUserId())) {
                                    // 不是自己结束发言的
                                    view.showSpeakApplyDisagreed();
                                }
                            }
                        }
                    });
        }

        liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().requestActiveUsers();
        liveRoom.getObservableOfForbidAllChatStatus()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            view.showForbiddenHand();
                            if (speakApplyStatus == RightMenuContract.STUDENT_SPEAK_APPLY_APPLYING) {
                                //正在请求发言
                                speakApplyStatus = RightMenuContract.STUDENT_SPEAK_APPLY_NONE;
                                RxUtils.unSubscribe(subscriptionOfSpeakApplyCounter);
                                liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().cancelSpeakApply();
                            }
                        } else {
                            view.showNotForbiddenHand();
                        }
                    }
                });
//        if (liveRoom.getForbidStatus()) {
//            view.showForbiddenHand();
//        } else {
//            view.showNotForbiddenHand();
//        }
    }

    private void refreshSpeakQueueBtnStatus() {
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
                                view.showSpeakQueueImage(mediaModels.get(aLong.intValue() % mediaModels.size()).getUser().getAvatar());
                            }
                        });
        } else {
            LPRxUtils.unSubscribe(subscriptionOfAvatarSwitcher);
            view.showEmptyQueue();
        }
    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfMediaControl);
        RxUtils.unSubscribe(subscriptionOfActiveUser);
        RxUtils.unSubscribe(subscriptionOfMedia);
        RxUtils.unSubscribe(subscriptionOfSpeakApply);
        RxUtils.unSubscribe(subscriptionOfAvatarSwitcher);
        RxUtils.unSubscribe(subscriptionOfSpeakApplyCounter);
    }

    @Override
    public void destroy() {
        liveRoomRouterListener = null;
        view = null;
    }
}
