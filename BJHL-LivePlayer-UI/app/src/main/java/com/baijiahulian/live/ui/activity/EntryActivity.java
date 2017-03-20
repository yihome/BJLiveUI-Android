package com.baijiahulian.live.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baijiahulian.live.ui.LiveSDKWithUI;
import com.baijiahulian.live.ui.R;

/**
 * Created by Shubo on 2017/3/20.
 */

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveSDKWithUI.enterRoom(EntryActivity.this, "l", "l", null);
            }
        });
    }
}
