package com.architecture.demo.ui.keepalive

import android.os.Bundle

import com.architecture.baseUI.BaseActivity
import com.architecture.demo.R


/**
 * 用Fragment 便于封装引用
 *
 */
class DozeAliveSettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keep_alive_setting)

        setActivityTitle("App 运行权限管理",findViewById(R.id.toolbar))


        intent.getStringExtra("CLSNAME")
    }



}