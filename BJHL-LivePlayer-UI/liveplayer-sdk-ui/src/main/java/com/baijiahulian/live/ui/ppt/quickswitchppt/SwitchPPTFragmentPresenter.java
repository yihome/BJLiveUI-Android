package com.baijiahulian.live.ui.ppt.quickswitchppt;

import android.util.Log;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.viewmodels.impl.LPDocListViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by szw on 17/7/5.
 */

public class SwitchPPTFragmentPresenter implements SwitchPPTContract.Presenter {
    private SwitchPPTContract.View view;
    private LiveRoomRouterListener listener;
    private List<LPDocListViewModel.DocModel> mDocList = new ArrayList<>();

    public SwitchPPTFragmentPresenter(SwitchPPTContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.listener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void destroy() {
        listener = null;
        view = null;
    }


    @Override
    public List<LPDocListViewModel.DocModel> getDocList() {
        if (listener.getLiveRoom().getCurrentUser().getType() != LPConstants.LPUserType.Teacher
                && listener.getLiveRoom().getCurrentUser().getType() != LPConstants.LPUserType.Assistant) {//区分老师还是学生
            view.setType(true);
        }else{
            view.setType(false);
        }
        mDocList = listener.getLiveRoom().getDocListVM().getDocList();
        listener.getLiveRoom().getDocListVM().getObservableOfDocListChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LPDocListViewModel.DocModel>>() {
                    @Override
                    public void call(List<LPDocListViewModel.DocModel> docModels) {
                        view.docListChanged(docModels);
                    }
                });

        view.setIndex();
        return mDocList;
    }

    @Override
    public void setSwitchPosition(int position) {
        listener.notifyPageCurrent(position);
    }

    @Override
    public void notifyMaxIndexChange(int maxIndex) {
        view.setMaxIndex(maxIndex);
    }
}
