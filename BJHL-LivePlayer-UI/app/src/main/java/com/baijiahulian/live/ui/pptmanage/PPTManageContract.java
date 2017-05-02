package com.baijiahulian.live.ui.pptmanage;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;

import java.util.List;

/**
 * Created by Shubo on 2017/4/26.
 */

interface PPTManageContract {

    interface View extends BaseView<Presenter> {
        void showPPTEmpty();

        void showPPTNotEmpty();
    }

    interface Presenter extends BasePresenter {
        int getCount();

        IDocumentModel getItem(int position);

        void uploadNewPics(List<String> picsPath);

        void selectItem(int position);

        void deselectItem(int position);

        void removeSelectedItems();
    }
}
