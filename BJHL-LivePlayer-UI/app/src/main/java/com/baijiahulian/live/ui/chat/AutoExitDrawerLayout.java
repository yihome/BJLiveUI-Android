package com.baijiahulian.live.ui.chat;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by wangkangfei on 17/4/26.
 * 屏蔽drawer的处理键盘点击事件
 */

public class AutoExitDrawerLayout extends DrawerLayout {
    public AutoExitDrawerLayout(Context context) {
        super(context);
    }

    public AutoExitDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoExitDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }
}
