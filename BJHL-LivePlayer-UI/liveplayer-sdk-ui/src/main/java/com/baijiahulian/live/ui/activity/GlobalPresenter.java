package com.baijiahulian.live.ui.activity;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import java.util.concurrent.TimeUnit;

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
            subscriptionOfTeacherMedia;

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
                        routerListener.showMessage("上课了");
                    }
                });
        subscriptionOfClassEnd = routerListener.getLiveRoom().getObservableOfClassEnd()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        routerListener.showMessage("下课了");
                    }
                });
        subscriptionOfForbidAllStatus = routerListener.getLiveRoom().getObservableOfForbidAllChatStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .skip(1) // 排除进教室第一次回调
                .subscribe(new LPErrorPrintSubscriber<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (routerListener.isTeacherOrAssistant()) {
                            routerListener.showMessage((aBoolean ? "打开" : "关闭") + "全体禁言成功");
                        } else {
                            routerListener.showMessage("老师" + (aBoolean ? "打开了" : "关闭了") + "全体禁言");
                        }
                    }
                });

        if (!routerListener.isTeacherOrAssistant()) {
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
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                        @Override
                        public void call(IMediaModel iMediaModel) {
                            if (iMediaModel.isVideoOn() && iMediaModel.isAudioOn()) {
                                if (!teacherVideoOn || !teacherAudioOn) {
                                    routerListener.showMessage("老师打开了音视频");
                                }
                            } else if (iMediaModel.isVideoOn()) {
                                if (teacherAudioOn && teacherVideoOn) {
                                    routerListener.showMessage("老师关闭了音频");
                                } else if (!teacherVideoOn) {
                                    routerListener.showMessage("老师打开了音频");
                                }
                            } else if (iMediaModel.isAudioOn()) {
                                if (teacherAudioOn && teacherVideoOn) {
                                    routerListener.showMessage("老师关闭了视频");
                                } else if (!teacherAudioOn) {
                                    routerListener.showMessage("老师打开了视频");
                                }
                            } else {
                                routerListener.showMessage("老师关闭了音视频");
                            }
                            setTeacherMedia(iMediaModel);
                        }
                    });
        }
    }

    void setTeacherMedia(IMediaModel media) {
        teacherVideoOn = media.isVideoOn();
        teacherAudioOn = media.isAudioOn();
    }


    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfClassStart);
        RxUtils.unSubscribe(subscriptionOfClassEnd);
        RxUtils.unSubscribe(subscriptionOfForbidAllStatus);
        RxUtils.unSubscribe(subscriptionOfTeacherMedia);
    }

    @Override
    public void destroy() {
        unSubscribe();
        routerListener = null;
    }
}
