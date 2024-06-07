package com.architecture.demo.ui.keepalive


import android.os.Bundle
import android.view.View
import com.anylife.keepalive.R
import com.anylife.keepalive.h5.H5DozeAliveGuideFragment
import com.anylife.keepalive.utils.KeepCompactUtil
import com.architecture.baseUI.BaseActivity
import com.architecture.demo.databinding.ActivityH5DozealiveGuideBinding

/**
 * H5 引导
 *
 */
open class H5DozeAliveGuideActivity : BaseActivity(),H5DozeAliveGuideFragment.FragmentListener {
    private lateinit var binding: ActivityH5DozealiveGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityH5DozealiveGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActivityTitle("App 运行权限管理", binding.toolbar)


        val h5GuideFragment= H5DozeAliveGuideFragment.newInstance("")

        supportFragmentManager.beginTransaction()
            .add(R.id.container, h5GuideFragment)
            .commit()

        binding.goSetting.setOnClickListener {
            h5GuideFragment.goSetting()
        }


        val keepType=intent.getStringExtra(H5DozeAliveGuideFragment.KEEP_TYPE).toString()

        //华为特殊处理
        if(KeepCompactUtil.deviceEnum.toString()== KeepCompactUtil.PhoneBrandEnum.Huawei.toString()){
            if(keepType == H5DozeAliveGuideFragment.KeepTypeMenu.daemon.toString()){
                binding.goSetting.visibility= View.INVISIBLE
            }
        }

    }

    override fun process(str: String) {
        setActivityTitle(str, findViewById(R.id.toolbar))
    }
}