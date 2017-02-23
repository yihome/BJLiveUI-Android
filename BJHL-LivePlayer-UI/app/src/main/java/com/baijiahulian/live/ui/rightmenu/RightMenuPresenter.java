package com.baijiahulian.live.ui.rightmenu;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.models.imodels.IMediaControlModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/2/15.
 */

public class RightMenuPresenter implements RightMenuContract.Presenter {

    private LiveRoomRouterListener liveRoomRouterListener;
    private RightMenuContract.View view;
    private LPConstants.LPUserType currentUserType;
    private Subscription subscriptionOfMediaControl;

    public RightMenuPresenter(RightMenuContract.View view) {
        this.view = view;
    }

    @Override
    public void visitSpeakers() {
        liveRoomRouterListener.navigateToSpeakers();
    }

    @Override
    public void drawing() {
        liveRoomRouterListener.navigateToPPTDrawing();
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
        if (currentUserType == LPConstants.LPUserType.Teacher
                || currentUserType == LPConstants.LPUserType.Assistant)
            return;
        checkNotNull(liveRoomRouterListener);
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

        subscriptionOfMediaControl = liveRoomRouterListener.getLiveRoom().getSpeakQueueVM().getObservableOfMediaControl()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<IMediaControlModel>() {
                    @Override
                    public void call(IMediaControlModel iMediaControlModel) {
                        if(iMediaControlModel.isApplyAgreed()){
                            // 进入发言模式
                            liveRoomRouterListener.enableSpeakerMode();
                        }else{
                            // 结束发言模式
                            liveRoomRouterListener.disableSpeakerMode();
                        }
                    }
                });
    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfMediaControl);
    }

    @Override
    public void destroy() {
        liveRoomRouterListener = null;
        view = null;
    }
}
