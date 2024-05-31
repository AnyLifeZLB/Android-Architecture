package com.anylife.keepalive.guide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anylife.keepalive.R
import com.anylife.keepalive.databinding.FragmentKeepAliveSettingBinding
import com.anylife.keepalive.h5.H5DozeAliveGuideFragment
import com.anylife.keepalive.utils.BatteryOptimization
import com.anylife.keepalive.utils.KeepCompactUtil

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [DozeAliveSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DozeAliveSettingFragment : Fragment() {
    private var param1: String? = null


    private lateinit var  ignoringBatteryOptiTextView: TextView
    private var isDestroy = false
    private var isIgnoringBatteryOptimizations = false

    private lateinit var deviceName: String
    private lateinit var binding: FragmentKeepAliveSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }


    override fun onResume() {
        super.onResume()
        isIgnoringBatteryOptimizations = BatteryOptimization.isIgnoringBatteryOptimizations(context)
        if (isIgnoringBatteryOptimizations) {
            ignoringBatteryOptiTextView.text = "已优化"
            ignoringBatteryOptiTextView.setTextColor(resources.getColor(R.color.height_grey))
        } else {
            ignoringBatteryOptiTextView.text = "去设置"
            ignoringBatteryOptiTextView.setTextColor(resources.getColor(R.color.black))
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentKeepAliveSettingBinding.inflate(layoutInflater,container,false);

        //判断出是什么品牌的手机
        deviceName = KeepCompactUtil.deviceEnum.toString()

        ignoringBatteryOptiTextView =binding.keepAliveBatterySet
        ignoringBatteryOptiTextView.setOnClickListener(View.OnClickListener { // 已经设置的不需要再去设置
            if (!isIgnoringBatteryOptimizations) {
                BatteryOptimization.requestIgnoreBatteryOptimizations(activity)
            }
        })


        binding.keepAliveBackgroundSet.setOnClickListener {
            H5DozeAliveGuideFragment.Companion.startWebView(requireContext(),getURLPath(
                H5DozeAliveGuideFragment.KeepTypeMenu.daemon,deviceName),
                H5DozeAliveGuideFragment.KeepTypeMenu.daemon)
        }

        binding.batterySetBackgroundSet.setOnClickListener {
            H5DozeAliveGuideFragment.Companion.startWebView(requireContext(),getURLPath(
                H5DozeAliveGuideFragment.KeepTypeMenu.battery,deviceName),
                H5DozeAliveGuideFragment.KeepTypeMenu.battery)
        }



        // Inflate the layout for this fragment
        return binding.root
    }




    /**
     * 不用判断一堆的手机型号，按照一定的规则来处理拼接处理URL
     *
     * @param keepType    保活类型，省电优化还是后台启动
     * @param deviceName  设备厂商类型
     */
    private fun getURLPath(keepType: H5DozeAliveGuideFragment.KeepTypeMenu, deviceName: String): String {
        return "file:///android_asset/dozealive_guide/html/$keepType/$deviceName/$deviceName.html"
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment KeepAliveSettingFragment.
         */
        @JvmStatic
        fun newInstance(param1: String) =
            DozeAliveSettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}