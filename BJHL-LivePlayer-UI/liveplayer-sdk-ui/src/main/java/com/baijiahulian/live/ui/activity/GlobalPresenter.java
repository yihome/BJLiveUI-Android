package com.baijiahulian.live.ui.activity;

import android.text.TextUtils;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.listener.OnRollCallListener;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.models.imodels.IUserInModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Shubo on 2017/5/11.
 */

public class GlobalPresenter implements BasePresenter {

    private LiveRoomRouterListener routerListener;

    private boolean teacherVideoOn, teacherAudioOn;

    private Subscription subscriptionOfClassStart, subscriptionOfClassEnd, subscriptionOfForbidAllStatus,
            subscriptionOfTeacherMedia, subscriptionOfUserIn, subscriptionOfUserOut, subscriptionOfRollCall;

    private boolean isVideoManipulated = false;

    private int counter = 0;

    private boolean isForbidChatChanged = false;

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        subscriptionOfClassStart = routerListener.getLiveRoom().getObservableOfClassStart()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        routerListener.showMessageClassStart();
                    }
                });
        subscriptionOfClassEnd = routerListener.getLiveRoom().getObservableOfClassEnd()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        routerListener.showMessageClassEnd();
                        teacherVideoOn = false;
                        teacherAudioOn = false;
                    }
                });
        subscriptionOfForbidAllStatus = routerListener.getLiveRoom().getObservableOfForbidAllChatStatus()
                .observeOn(AndroidSchedulers.mainThread())
//                .skip(1) // 排除进教室第一次回调
                .subscribe(new LPErrorPrintSubscriber<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (counter == 0) {
                            isForbidChatChanged = aBoolean;
                            counter++;
                            return;
                        }
                        if (isForbidChatChanged == aBoolean) return;
                        isForbidChatChanged = aBoolean;
                        routerListener.showMessageForbidAllChat(aBoolean);
                    }
                });

        if (!routerListener.isCurrentUserTeacher()) {
            // 学生监听老师音视频状态
            subscriptionOfTeacherMedia = routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaNew()
                    .mergeWith(routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaChange())
                    .mergeWith(routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaClose())
                    .filter(new Func1<IMediaModel, Boolean>() {
                        @Override
                        public Boolean call(IMediaModel iMediaModel) {
                            return !routerListener.isTeacherOrAssistant() && iMediaModel.getUser().getType() == LPConstants.LPUserType.Teacher;
                        }
                    })
//                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                        @Override
                        public void call(IMediaModel iMediaModel) {
                            if (!routerListener.getLiveRoom().isClassStarted()) {
                                return;
                            }
                            if (iMediaModel.isVideoOn() && iMediaModel.isAudioOn()) {
                                if (!teacherVideoOn || !teacherAudioOn) {
                                    routerListener.showMessageTeacherOpenAV();
                                    if (!isVideoManipulated && routerListener.getCurrentVideoUser() == null) {
                                        routerListener.playVideo(iMediaModel.getUser().getUserId());
                                        routerListener.setCurrentVideoUser(iMediaModel);
                                    }
                                }
                            } else if (iMediaModel.isVideoOn()) {
                                if (teacherAudioOn && teacherVideoOn) {
                                    routerListener.showMessageTeacherCloseAudio();
                                } else if (!teacherVideoOn) {
                                    routerListener.showMessageTeacherOpenVideo();
                                    if (!isVideoManipulated && routerListener.getCurrentVideoUser() == null) {
                                        routerListener.playVideo(iMediaModel.getUser().getUserId());
                                        routerListener.setCurrentVideoUser(iMediaModel);
                                    }
                                }
                            } else if (iMediaModel.isAudioOn()) {
                                if (teacherAudioOn && teacherVideoOn) {
                                    routerListener.showMessageTeacherCloseVideo();
                                } else if (!teacherAudioOn) {
                                    routerListener.showMessageTeacherOpenAudio();
                                }
                            } else {
                                routerListener.showMessageTeacherCloseAV();
                            }
                            setTeacherMedia(iMediaModel);
                        }
                    });

            subscriptionOfUserIn = routerListener.getLiveRoom().getObservableOfUserIn().observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IUserInModel>() {
                        @Override
                        public void call(IUserInModel iUserInModel) {
                            if (iUserInModel.getUser().getType() == LPConstants.LPUserType.Teacher) {
                                routerListener.showMessageTeacherEnterRoom();
                            }
                        }
                    });

            subscriptionOfUserOut = routerListener.getLiveRoom().getObservableOfUserOut().observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<String>() {
                        @Override
                        public void call(String s) {
                            if (TextUtils.isEmpty(s)) return;
                            if (routerListener.getLiveRoom().getTeacherUser() == null) return;
                            if (s.equals(routerListener.getLiveRoom().getTeacherUser().getUserId())) {
                                routerListener.showMessageTeacherExitRoom();
                            }
                        }
                    });
            //点名
            routerListener.getLiveRoom().setOnRollCallListener(new OnRollCallListener() {
                @Override
                public void onRollCall(int time, RollCall rollCallListener) {
                    routerListener.showRollCallDlg(time, rollCallListener);
                }

                @Override
                public void onRollCallTimeOut() {
                    routerListener.dismissRollCallDlg();
                }
            });
        }
    }

    void setTeacherMedia(IMediaModel media) {
        teacherVideoOn = media.isVideoOn();
        teacherAudioOn = media.isAudioOn();
    }

    public boolean isVideoManipulated() {
        return isVideoManipulated;
    }

    public void setVideoManipulated(boolean videoManipulated) {
        isVideoManipulated = videoManipulated;
    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfClassStart);
        RxUtils.unSubscribe(subscriptionOfClassEnd);
        RxUtils.unSubscribe(subscriptionOfForbidAllStatus);
        RxUtils.unSubscribe(subscriptionOfTeacherMedia);
        RxUtils.unSubscribe(subscriptionOfUserIn);
        RxUtils.unSubscribe(subscriptionOfUserOut);
    }

    @Override
    public void destroy() {
        unSubscribe();
        routerListener = null;
    }
}
