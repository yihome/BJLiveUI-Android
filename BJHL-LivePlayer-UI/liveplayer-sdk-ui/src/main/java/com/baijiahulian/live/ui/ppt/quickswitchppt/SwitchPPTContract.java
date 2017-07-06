package com.baijiahulian.live.ui.ppt.quickswitchppt;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.livecore.viewmodels.impl.LPDocListViewModel;

import java.util.List;

/**
 * Created by bjhl on 17/7/4.
 */

public class SwitchPPTContract {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        List<LPDocListViewModel.DocModel> getDocList();
        void setSwitchPosition(int position);
    }
}
