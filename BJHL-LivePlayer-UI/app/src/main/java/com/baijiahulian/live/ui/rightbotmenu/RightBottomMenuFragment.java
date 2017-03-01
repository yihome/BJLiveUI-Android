package com.baijiahulian.live.ui.rightbotmenu;

import android.os.Bundle;
import android.view.View;

import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.utils.Query;

/**
 * Created by Shubo on 2017/2/16.
 */

public class RightBottomMenuFragment extends BaseFragment implements RightBottomMenuContract.View {

    private RightBottomMenuContract.Presenter presenter;
    private Query $;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_right_bottom_menu;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        $ = Query.with(view);

        $.id(R.id.fragment_right_bottom_video).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeVideo();
            }
        });

        $.id(R.id.fragment_right_bottom_audio).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeAudio();
            }
        });
    }

    @Override
    public void showVideoStatus(boolean isOn) {
        if(isOn)
            $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopvideo);
        else
            $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopvideo_on);
    }

    @Override
    public void showAudioStatus(boolean isOn) {
        if(isOn)
            $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio);
        else
            $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_audio_on);
    }

    @Override
    public void showAVButton() {
        $.id(R.id.fragment_right_bottom_video).visible();
        $.id(R.id.fragment_right_bottom_audio).visible();
    }

    @Override
    public void hideAVButton() {
        $.id(R.id.fragment_right_bottom_video).gone();
        $.id(R.id.fragment_right_bottom_audio).gone();
    }

    @Override
    public void clearScreen() {
        $.id(R.id.fragment_right_bottom_video).invisible();
        $.id(R.id.fragment_right_bottom_audio).invisible();
        $.id(R.id.fragment_right_bottom_zoom).invisible();
    }

    @Override
    public void unClearScreen() {
        $.id(R.id.fragment_right_bottom_video).visible();
        $.id(R.id.fragment_right_bottom_audio).visible();
        $.id(R.id.fragment_right_bottom_zoom).visible();
    }

    @Override
    public void setPresenter(RightBottomMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }
}
