
package com.zenglb.framework.news;

import android.os.Bundle;

import com.zenglb.framework.goodlife.R;
import com.zlb.base.BaseWebViewActivity;

/**
 * WIP :work in Process
 * <p>
 * 验证 JSBridge
 */

public class NewsWebActivity extends BaseWebViewActivity {
    private final static int CAMERA_PERMISSION_CODE = 1000;    //请求CAMERA权限的Code
    private final static int REQUEST_CAPTURE_IMG = 1001;    //相册选取

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra(BaseWebViewActivity.URL);
        setURL(url);

    }


}

