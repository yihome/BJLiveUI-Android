package com.baijiahulian.live.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.baijiahulian.live.ui.LiveSDKWithUI;
import com.baijiahulian.live.ui.R;

/**
 * Created by Shubo on 2017/3/20.
 */

public class EntryActivity extends AppCompatActivity {

    private String code;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        sp = getSharedPreferences("live_temp", Context.MODE_PRIVATE);
        code = sp.getString("code", "nl8t1h");
        ((EditText) findViewById(R.id.activity_entry_join_code)).setText(code);

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = ((EditText) findViewById(R.id.activity_entry_join_code)).getText().toString();
                String name = ((EditText) findViewById(R.id.activity_entry_name)).getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("code", code);
                editor.apply();

                LiveSDKWithUI.enterRoom(EntryActivity.this, code, name, null);
            }
        });
    }
}
