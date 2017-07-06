package com.baijiahulian.live.ui.ppt.quickswitchppt;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.viewmodels.impl.LPDocListViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by szw on 17/7/5.
 */

public class SwitchPPTFragmentPresenter implements SwitchPPTContract.Presenter{
    private SwitchPPTContract.View view;
    private LiveRoomRouterListener listener;
    private List<LPDocListViewModel.DocModel> mDocList = new ArrayList<>();
    private int position;



    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        this.listener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        mDocList = listener.getLiveRoom().getDocListVM().getDocList();
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void destroy() {

    }


    @Override
    public List<LPDocListViewModel.DocModel> getDocList() {
        return mDocList;
    }

    @Override
    public void setSwitchPosition(int position) {
        listener.notifyPageCurrent(position);
    }
}
