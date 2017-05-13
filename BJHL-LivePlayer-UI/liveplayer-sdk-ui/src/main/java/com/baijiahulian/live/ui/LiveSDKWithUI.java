package com.baijiahulian.live.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.baijiahulian.live.ui.activity.LiveRoomActivity;
import com.baijiahulian.live.ui.utils.LPShareModel;

import java.util.ArrayList;

/**
 * 入口类
 * Created by Shubo on 2017/2/13.
 */

public class LiveSDKWithUI {

    public static void enterRoom(@NonNull Context context, @NonNull String code, @NonNull String name, @NonNull LiveSDKEnterRoomListener listener) {
        if (TextUtils.isEmpty(name)) {
            listener.onError("name is empty");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            listener.onError("code is empty");
            return;
        }

        Intent intent = new Intent(context, LiveRoomActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

    public interface LiveSDKEnterRoomListener {
        void onError(String msg);
    }

    public interface LPShareListener {
        void onShareClicked(Context context, int type);

        ArrayList<? extends LPShareModel> setShareList();

        void getShareData(Context context, long roomId);
    }

    public static void setShareListener(LPShareListener listener) {
        LiveRoomActivity.setShareListener(listener);
    }
}
