package com.anylife.keepalive.h5

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anylife.keepalive.databinding.FragmentH5KeepAliveGuideBinding
import com.anylife.keepalive.utils.KeepCompactUtil


private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [H5DozeAliveGuideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class H5DozeAliveGuideFragment : Fragment() {
    private var param1: String? = null


    private lateinit var binding: FragmentH5KeepAliveGuideBinding
    private lateinit var url: String
    lateinit var keepType: String

    private lateinit var listener: FragmentListener


    interface FragmentListener {
        fun process(str: String)
    }


    companion object {
        const val URL = "url"
        const val KEEP_TYPE = "KeepTypeMenu"

        fun startWebView(context: Context, url: String, type: KeepTypeMenu,cls: Class<*>) {

//            val intent = Intent("com.common.dozealive.ACTION_H5_GUIDE")
//            intent.addCategory("com.common.dozealive.GUIDE")
//            intent.addCategory("android.intent.category.DEFAULT")

//            val intent=Intent(context,TestActivity::class.java)
            val intent=Intent(context,cls)


            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            intent.putExtra(URL, url)
            intent.putExtra(KEEP_TYPE, type.toString())
            context.startActivity(intent)
        }



        fun startWebView2(context: Context, url: String, type: KeepTypeMenu,clsName: String) {

            val cn = ComponentName(context.packageName, clsName)
            val intent = Intent()
            intent.setComponent(cn)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            intent.putExtra(URL, url)
            intent.putExtra(KEEP_TYPE, type.toString())
            context.startActivity(intent)
        }



        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment H5KeepAliveGuideFragment.
         */
        @JvmStatic
        fun newInstance(param1: String) =
            H5DozeAliveGuideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is FragmentListener) {
            listener = activity
        } else {
            throw IllegalArgumentException("activity must implements Fragment interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentH5KeepAliveGuideBinding.inflate(layoutInflater,container,false);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        binding.webView.webViewClient = WebViewClient()

        //requireActivity().intent
        url = requireActivity().intent.getStringExtra(URL).toString()
        keepType = requireActivity().intent.getStringExtra(KEEP_TYPE).toString()

        if (!TextUtils.isEmpty(url)) {
            binding.webView.loadUrl(url)
        }

        val settings: WebSettings = binding.webView.settings
        settings.javaScriptEnabled = true      // 启用javascript
        settings.domStorageEnabled = true      // 支持HTML5中的一些控件标签
        settings.builtInZoomControls = false   // 自选，非必要

        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(p0: WebView?, progress: Int) {
                super.onProgressChanged(p0, progress)
                binding.progressBar.progress = progress
                if (progress == 100) {
                    binding.progressBar.hide()
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String) {
                super.onReceivedTitle(view, title)
                activity?.setTitle(title)
                listener.process(title)
            }
        }


//        binding.goSetting.setOnClickListener {
//            goSetting()
//        }
//
//
//        //华为特殊处理
//        if(KeepCompactUtil.deviceEnum.toString()== KeepCompactUtil.PhoneBrandEnum.Huawei.toString()){
//            if(keepType == KeepTypeMenu.daemon .toString()){
//                binding.goSetting.visibility= View.INVISIBLE
//            }
//        }


        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * 跳转，根据H5 提示去设置
     *
     */
    public fun goSetting(){
        if (keepType == KeepTypeMenu.battery.toString()) {
            KeepCompactUtil.noSleepSet(requireActivity())
        } else {
            KeepCompactUtil.daemonSet(requireActivity())
        }
    }


    enum class KeepTypeMenu {
        battery, daemon
    }



}