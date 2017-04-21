package com.baijiahulian.live.ui.rightbotmenu;

import android.os.Bundle;
import android.view.View;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Shubo on 2017/2/16.
 */

public class RightBottomMenuFragment extends BaseFragment implements RightBottomMenuContract.View {

    private RightBottomMenuContract.Presenter presenter;
    private Subscription subscriptionOfVideoClick, subscriptionOfAudioClick;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_right_bottom_menu;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        subscriptionOfVideoClick = $.id(R.id.fragment_right_bottom_video).clicked()
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        presenter.changeVideo();
                    }
                });

        subscriptionOfAudioClick = $.id(R.id.fragment_right_bottom_audio).clicked()
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        presenter.changeAudio();
                    }
                });

        $.id(R.id.fragment_right_bottom_more).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int location[] = new int[2];
                $.id(R.id.fragment_right_bottom_more).view().getLocationInWindow(location);
                presenter.more(location[0], location[1]);
            }
        });

        $.id(R.id.fragment_right_bottom_zoom).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeZoom();
            }
        });
    }

    @Override
    public void showVideoStatus(boolean isOn) {
        if (isOn)
            $.id(R.id.fragment_right_bottom_video).image(R.drawable.live_ic_stopvideo_on);
        else
            $.id(R.id.fragment_right_bottom_video).image(R.drawable.live_ic_stopvideo);
    }

    @Override
    public void showAudioStatus(boolean isOn) {
        if (isOn)
            $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio_1);
        else
            $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio);
    }

    @Override
    public void enableSpeakerMode() {
        $.id(R.id.fragment_right_bottom_video).visible();
        $.id(R.id.fragment_right_bottom_audio).visible();
    }

    @Override
    public void disableSpeakerMode() {
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
    public void showVolume(int level) {
        // level between [0,9]
        switch (level) {
            case 0:
                $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio_1);
                break;
            case 1:
            case 2:
                $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio_2);
                break;
            case 3:
            case 4:
                $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio_3);
                break;
            case 5:
            case 6:
                $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio_4);
                break;
            case 7:
            case 8:
                $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio_5);
                break;
            case 9:
                $.id(R.id.fragment_right_bottom_audio).image(R.drawable.live_ic_stopaudio_6);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(RightBottomMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxUtils.unSubscribe(subscriptionOfAudioClick);
        RxUtils.unSubscribe(subscriptionOfVideoClick);
        presenter = null;
    }
}
