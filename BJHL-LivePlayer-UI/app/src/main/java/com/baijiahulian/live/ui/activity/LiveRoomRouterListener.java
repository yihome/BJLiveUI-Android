package com.baijiahulian.live.ui.activity;

import com.baijiahulian.livecore.context.LiveRoom;

/**
 * Created by Shubo on 2017/2/15.
 */

public interface LiveRoomRouterListener {

    LiveRoom getLiveRoom();

    void setLiveRoom(LiveRoom liveRoom);

    void navigateToMain();

    void clearScreen();

    void unClearScreen();

    void navigateToMessageInput();

    void navigateToPPTDrawing();

    void navigateToSpeakers();

    void navigateToPPTWareHouse();

    void disableSpeakerMode();

    void enableSpeakerMode();

    void maximiseRecorderView();

    void maximisePlayerView();

    void maximisePPTView();
}
