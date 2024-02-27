package com.architecture.demo.ui.keepalive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.anna.lib_keepalive.service.KeepAliveService;
import com.architecture.demo.R;

/**
 * 保活设置
 *
 */
public class KeepAliveSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_alive_setting);
        setTitle("App运行保活设置");

        findViewById(R.id.keep_alive_tips_battery_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeepAliveService.start(getBaseContext(), KeepAliveService.AliveStrategy.BATTERYOPTIMIZATION);
            }
        });

        findViewById(R.id.keep_alive_tips_background_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeepAliveService.start(getBaseContext(), KeepAliveService.AliveStrategy.RESTARTACTION);
            }
        });

    }
}