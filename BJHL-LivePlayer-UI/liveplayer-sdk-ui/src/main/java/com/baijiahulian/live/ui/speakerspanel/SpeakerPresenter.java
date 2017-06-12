package com.baijiahulian.live.ui.speakerspanel;

import android.view.View;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.models.imodels.IMediaControlModel;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.models.imodels.IUserModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;
import com.baijiahulian.livecore.wrapper.LPPlayer;
import com.baijiahulian.livecore.wrapper.LPRecorder;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;

import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.PPT_TAG;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.RECORD_TAG;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_APPLY;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_PPT;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_RECORD;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_SPEAKER;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_VIDEO_PLAY;

/**
 * currentFullScreenTag 为当前全屏的tag VideoView为UserId
 * Created by Shubo on 2017/6/5.
 */

public class SpeakerPresenter implements SpeakersContract.Presenter {

    private LiveRoomRouterListener routerListener;
    private SpeakersContract.View view;
    private String currentFullScreenTag = PPT_TAG;
    private List<String> videoPlayingUserIdList;

    private List<String> displayList;

    private int _displayPPTSection = -1;
    private int _displayRecordSection = -1;
    private int _displayVideoSection = -1;
    private int _displaySpeakerSection = -1;
    private int _displayApplySection = -1;

    private Subscription subscriptionOfMediaNew, subscriptionOfMediaChange, subscriptionOfMediaClose,
            subscriptionSpeakApply, subscriptionSpeakResponse, subscriptionOfActiveUser;

    public SpeakerPresenter(SpeakersContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
        videoPlayingUserIdList = new ArrayList<>();
    }

    private void initView() {
        displayList = new ArrayList<>();
        _displayPPTSection = displayList.size();
        if (!currentFullScreenTag.equals(PPT_TAG)) {
            displayList.add(PPT_TAG);
        }
        _displayRecordSection = displayList.size();
        if (routerListener.getLiveRoom().getRecorder().isVideoAttached()) {
            displayList.add(RECORD_TAG);
        }
        _displayVideoSection = displayList.size();
        _displaySpeakerSection = displayList.size();
        for (IMediaModel model : routerListener.getLiveRoom().getSpeakQueueVM().getSpeakQueueList()) {
            displayList.add(model.getUser().getUserId());
        }
        _displayApplySection = displayList.size();
        for (IUserModel model : routerListener.getLiveRoom().getSpeakQueueVM().getApplyList()) {
            displayList.add(model.getUserId());
        }
        // init view
        for (int i = 0; i < displayList.size(); i++) {
            view.notifyItemInserted(i);
        }
    }

    @Override
    public void subscribe() {

        LPErrorPrintSubscriber<List<IMediaModel>> activeUserSubscriber = new LPErrorPrintSubscriber<List<IMediaModel>>() {
            @Override
            public void call(List<IMediaModel> iMediaModels) {
                initView();
            }
        };
        final ConnectableObservable<List<IMediaModel>> observable = routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfActiveUsers();
        subscriptionOfActiveUser = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(activeUserSubscriber);
        observable.connect();
        routerListener.getLiveRoom().getSpeakQueueVM().requestActiveUsers();

        subscriptionOfMediaNew = routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaNew()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                    @Override
                    public void call(IMediaModel iMediaModel) {
                        displayList.add(_displayApplySection, iMediaModel.getUser().getUserId());
                        _displayApplySection++;
                        view.notifyItemInserted(_displayApplySection - 1);
                    }
                });

        subscriptionOfMediaChange = routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaChange()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                    @Override
                    public void call(IMediaModel iMediaModel) {
                        int position = displayList.indexOf(iMediaModel.getUser().getUserId());
                        if (position < _displayVideoSection) {
                            throw new RuntimeException("position < _displayVideoSection");
                        } else if (position < _displaySpeakerSection) { // 视频打开用户
                            if (!iMediaModel.isVideoOn()) { // 通知视频关闭
                                view.notifyItemDeleted(position);
                                routerListener.getLiveRoom().getPlayer().playAVClose(getItem(position));
                                routerListener.getLiveRoom().getPlayer().playAudio(getItem(position));
                                String item = displayList.remove(position);
                                displayList.add(_displayApplySection - 1, item);
                                _displaySpeakerSection--;
                                view.notifyItemInserted(_displayApplySection - 1);
                            }
                        } else if (position < _displayApplySection) { // 视频未打开用户
                            view.notifyItemChanged(position);
                        } else {
                            throw new RuntimeException("position > _displayApplySection");
                        }
                    }
                });

        subscriptionOfMediaClose = routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaClose()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                    @Override
                    public void call(IMediaModel iMediaModel) {
                        int position = displayList.indexOf(iMediaModel.getUser().getUserId());
                        if (position < _displayVideoSection) {
                            throw new RuntimeException("position < _displayVideoSection");
                        } else if (position < _displaySpeakerSection) { // 视频打开用户
                            view.notifyItemDeleted(position);
                            displayList.remove(position);
                            _displaySpeakerSection--;
                            _displayApplySection--;
                        } else if (position < _displayApplySection) { // 视频未打开用户
                            view.notifyItemDeleted(position);
                            _displayApplySection--;
                        } else {
                            throw new RuntimeException("position > _displayApplySection");
                        }
                    }
                });

        if (routerListener.isTeacherOrAssistant()) {

            subscriptionSpeakApply = routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfSpeakApply()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaModel>() {
                        @Override
                        public void call(IMediaModel iMediaModel) {
                            displayList.add(iMediaModel.getUser().getUserId());
                            view.notifyItemInserted(displayList.size() - 1);
                            _displayApplySection++;
                        }
                    });

            subscriptionSpeakResponse = routerListener.getLiveRoom().getSpeakQueueVM().getObservableOfSpeakResponse()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LPErrorPrintSubscriber<IMediaControlModel>() {
                        @Override
                        public void call(IMediaControlModel iMediaControlModel) {
                            int position = displayList.indexOf(iMediaControlModel.getUser().getUserId());
                            if (position < _displayApplySection) {
                                throw new RuntimeException("position < _displayApplySection");
                            } else if (position < displayList.size()) {
                                view.notifyItemDeleted(position);
                                displayList.remove(position);
                            } else {
                                throw new RuntimeException("position > displayList.size()");
                            }
                        }
                    });
        }
    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfMediaNew);
        RxUtils.unSubscribe(subscriptionOfMediaChange);
        RxUtils.unSubscribe(subscriptionOfMediaClose);
        RxUtils.unSubscribe(subscriptionSpeakApply);
        RxUtils.unSubscribe(subscriptionSpeakResponse);
        RxUtils.unSubscribe(subscriptionOfActiveUser);
    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 0) {
            throw new RuntimeException("position < 0 in getItemViewType");
        } else if (position < _displayRecordSection)
            return VIEW_TYPE_PPT;
        else if (position < _displayVideoSection)
            return VIEW_TYPE_RECORD;
        else if (position < _displaySpeakerSection)
            return VIEW_TYPE_VIDEO_PLAY;
        else if (position < _displayApplySection)
            return VIEW_TYPE_SPEAKER;
        else if (position < displayList.size())
            return VIEW_TYPE_APPLY;
        else
            throw new RuntimeException("position > displayList.size() in getItemViewType");
    }

    @Override
    public int getItemViewType(String userId) {
        return getItemViewType(displayList.indexOf(userId));
    }

    @Override
    public LPRecorder getRecorder() {
        return routerListener.getLiveRoom().getRecorder();
    }

    @Override
    public LPPlayer getPlayer() {
        return routerListener.getLiveRoom().getPlayer();
    }

    @Override
    public IUserModel getApplyModel(int position) {
        String userId = displayList.get(position);
        for (IUserModel model : routerListener.getLiveRoom().getSpeakQueueVM().getApplyList()) {
            if (model.getUserId().equals(userId)) {
                return model;
            }
        }
        return null;
    }

    @Override
    public IMediaModel getSpeakModel(int position) {
        String userId = displayList.get(position);
        for (IMediaModel model : routerListener.getLiveRoom().getSpeakQueueVM().getSpeakQueueList()) {
            if (model.getUser().getUserId().equals(userId)) {
                return model;
            }
        }
        return null;
    }

    @Override
    public void playVideo(String userId) {
        int position = displayList.indexOf(userId);

        // 在dialog操作过程中 数据发生了变化
        if (position == -1) return;
        if (getSpeakModel(position) == null || !getSpeakModel(position).isVideoOn()) return;

        view.notifyItemDeleted(position);
        displayList.remove(position);
        displayList.add(_displaySpeakerSection, userId);
        _displaySpeakerSection++;
        view.notifyItemInserted(_displaySpeakerSection - 1);
    }

    @Override
    public void closeVideo(String userId) {
        int position = displayList.indexOf(userId);

        // 在dialog操作过程中 数据发生了变化
        if (position == -1) return;
        if (getSpeakModel(position) == null) return;

        routerListener.getLiveRoom().getPlayer().playAVClose(userId);
        routerListener.getLiveRoom().getPlayer().playAudio(userId);
        view.notifyItemDeleted(position);
        displayList.remove(position);
        displayList.add(_displayApplySection - 1, userId);
        _displaySpeakerSection--;
        view.notifyItemInserted(_displayApplySection - 1);
    }

    @Override
    public void closeSpeaking(String userId) {
        int position = displayList.indexOf(userId);
        if (position == -1) return;
//        if (speakList.get(position - applyList.size()).getUser().getUserId().equals(routerListener.getCurrentVideoPlayingUserId())) {
//            routerListener.playVideoClose(speakList.get(position - applyList.size()).getUser().getUserId());
//            if (!routerListener.isCurrentUserTeacher())
//                routerListener.setVideoManipulated(true);
//        }
        routerListener.getLiveRoom().getSpeakQueueVM().closeOtherSpeak(userId);
        view.notifyItemDeleted(position);
    }

    @Override
    public boolean isTeacherOrAssistant() {
        return routerListener.isTeacherOrAssistant();
    }

    @Override
    public String getItem(int position) {
        if (position < displayList.size())
            return displayList.get(position);
        else
            throw new RuntimeException("position > displayList.size() in getItem");
    }

    @Override
    public int getCount() {
        // ppt + record video + speaking list(contains playing video) + speak apply list - full screen item
        return displayList.size();
    }

    @Override
    public void agreeSpeakApply(String userId) {
        int position = displayList.indexOf(userId);
        if (position == -1) {
            throw new RuntimeException("invalid userId:" + userId + " in agreeSpeakApply");
        } else {
            routerListener.getLiveRoom().getSpeakQueueVM().agreeSpeakApply(displayList.get(position));
//            view.notifyItemDeleted(position);
        }
    }

    @Override
    public void disagreeSpeakApply(String userId) {
        int position = displayList.indexOf(userId);
        if (position == -1) {
            throw new RuntimeException("invalid userId:" + userId + " in disagreeSpeakApply");
        } else {
            routerListener.getLiveRoom().getSpeakQueueVM().disagreeSpeakApply(displayList.get(position));
//            view.notifyItemDeleted(position);
        }
//        if (position < _displayApplySection) {
//            throw new RuntimeException("position < _displayApplySection in disagreeSpeakApply");
//        } else if (position < displayList.size())
//            routerListener.getLiveRoom().getSpeakQueueVM().disagreeSpeakApply(displayList.get(position));
//        else
//            throw new RuntimeException("position > displayList.size() in disagreeSpeakApply");
    }

    public void attachVideo() {
        if (_displayRecordSection == _displayVideoSection) {
            displayList.add(_displayRecordSection, RECORD_TAG);
            _displayVideoSection ++;
            _displaySpeakerSection ++;
            _displayApplySection ++;
            view.notifyItemInserted(_displayRecordSection);
        }
    }

    public void detachVideo() {
        if(_displayRecordSection == _displayVideoSection -1){
            view.notifyItemDeleted(_displayRecordSection);
            displayList.remove(_displayRecordSection);
            _displayVideoSection --;
            _displaySpeakerSection --;
            _displayApplySection --;
        }
    }

    @Override
    public View getPPTView() {
        return null;
    }
}
