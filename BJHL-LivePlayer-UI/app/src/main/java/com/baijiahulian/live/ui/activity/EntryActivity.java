package com.baijiahulian.live.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.baijiahulian.live.ui.LiveSDKWithUI;
import com.baijiahulian.live.ui.R;
import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.context.LPConstants;

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
                String code = ((EditText)findViewById(R.id.activity_entry_join_code)).getText().toString();
                String name = ((EditText)findViewById(R.id.activity_entry_name)).getText().toString();
                LiveSDK.init(LPConstants.LPDeployType.Test);
                LiveSDKWithUI.enterRoom(EntryActivity.this, code, name, null);
            }
        });
    }
}
