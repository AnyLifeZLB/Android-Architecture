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
import androidx.core.widget.ContentLoadingProgressBar
import com.anylife.keepalive.R

/**
 * 引导
 *
 */
class H5KeepAliveGuideActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var url: String
    private lateinit var progressBar: ContentLoadingProgressBar

    companion object {
        const val URL = "url" //网页url

        fun startWebView(context: Context, url: String) {
            val intent = Intent(context, H5KeepAliveGuideActivity::class.java)
            //通知路由 小米 Android 10无法打开爆了缺少该flags
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(H5KeepAliveGuideActivity.URL, url)
            context.startActivity(intent)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_h5_keep_alive_guide)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        webView = findViewById(R.id.webView)
        progressBar= findViewById(R.id.progress)
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()


        url = intent.getStringExtra(H5KeepAliveGuideActivity.URL).toString()


        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url)
        }


        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true // 启用javascript
        settings.domStorageEnabled = true // 支持HTML5中的一些控件标签
        settings.builtInZoomControls = false // 自选，非必要

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)


        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(p0: WebView?, progress: Int) {
                super.onProgressChanged(p0, progress)
                progressBar.progress = progress
                if (progress == 100) {
                    progressBar.hide()
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                supportActionBar?.title=title
            }
        }

    }


    // if you press Back button this code will work
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (webView.canGoBack())
            webView.goBack()
        // if your webview cannot go back
        // it will exit the application
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

}