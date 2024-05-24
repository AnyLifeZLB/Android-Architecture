package com.anylife.keepalive.h5

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.anylife.keepalive.databinding.ActivityH5KeepAliveGuideBinding
import com.anylife.keepalive.utils.KeepCompactUtil.daemonSet
import com.anylife.keepalive.utils.KeepCompactUtil.noSleepSet

/**
 * 引导
 *
 */
class H5KeepAliveGuideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityH5KeepAliveGuideBinding
    private lateinit var url: String
    private lateinit var keepType: String

    companion object {
        const val URL = "url"
        const val KEEP_TYPE = "KeepTypeMenu"

        fun startWebView(context: Context, url: String, type: KeepTypeMenu) {
            val intent = Intent(context, H5KeepAliveGuideActivity::class.java)
            //通知路由 小米 Android 10无法打开爆了缺少该flags
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(URL, url)
            intent.putExtra(KEEP_TYPE, type.toString())
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityH5KeepAliveGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        binding.webView.webViewClient = WebViewClient()

        url = intent.getStringExtra(URL).toString()
        keepType = intent.getStringExtra(KEEP_TYPE).toString()

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

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                supportActionBar?.title = title
            }
        }

        binding.goSetting.setOnClickListener {
            if (keepType == KeepTypeMenu.battery.toString()) {
                noSleepSet(this@H5KeepAliveGuideActivity)
//                RestartSettingUtils.setReStartAction(this@H5KeepAliveGuideActivity)
            } else {
                daemonSet(this@H5KeepAliveGuideActivity)
            }
        }

    }


    override fun onBackPressed() {
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        else
            super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    enum class KeepTypeMenu {
        battery, daemon
    }

}