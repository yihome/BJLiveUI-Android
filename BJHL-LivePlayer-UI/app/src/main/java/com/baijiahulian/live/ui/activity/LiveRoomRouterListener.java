package com.baijiahulian.live.ui.activity;

import com.baijiahulian.livecore.context.LPConstants;
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

    LPConstants.LPPPTShowWay getPPTShowType();

    void setPPTShowType(LPConstants.LPPPTShowWay type);

    void navigateToSpeakers();

    void navigateToUserList();

    void navigateToPPTWareHouse();

    void disableSpeakerMode();

    void enableSpeakerMode();

    void maximiseRecorderView();

    void maximisePlayerView();

    void maximisePPTView();

    void showMorePanel(int anchorX, int anchorY);

    void navigateToShare();

    void navigateToAnnouncement();

    void navigateToCloudRecord();

    boolean getCloudRecordStatus();

    void navigateToHelp();

    void navigateToSetting();

    boolean isTeacherOrAssistant();

    String getCurrentVideoPlayingUserId();

    void playVideo(String userId);

    void playVideoClose(String userId);

    void attachVideo();

    void detachVideo();

    void showRecorderDialogFragment();
}
