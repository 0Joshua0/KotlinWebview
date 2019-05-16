package joshua.kotlinwebview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {
    internal var TAG = "webviewTest"
    private var mWebView: WebView? = null
    private var mExitTime = 0L
    companion object {
        // 定义WebView首页地址[伴生对象]
        //定义static final
        val WEB_URL = "https://github.com/0Joshua0"
        //val TAG=KotlinActivity::class.simpleName  //定义Log的TAG
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()  //隐藏ActionBar
        initAndSetupView();

        mWebView = findViewById<View>(R.id.kotlin_webview) as WebView
//        val i = intent.scheme
//        Log.d(TAG, "i=" + i!!)
    }

    // 初始化对象
    fun initAndSetupView() {
        val webViewContainer = findViewById(R.id.kotlin_webview) as WebView
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        mWebView = WebView(applicationContext)
        webViewContainer.addView(mWebView, params)
        var webSettings = mWebView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true// 设置允许访问文件数据
        webSettings.setSupportZoom(true)//支持缩放
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        mWebView!!.setOnKeyListener(OnKeyEvent)
        mWebView!!.setWebViewClient(webClient)
        mWebView!!.loadUrl(WEB_URL)
    }


    private val webClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d(TAG, url)
            return false
        }
    }

    private val OnKeyEvent = View.OnKeyListener { v, keyCode, event ->
        val action = event.action
        val webView = v as WebView
        if (KeyEvent.ACTION_DOWN == action && KeyEvent.KEYCODE_BACK == keyCode) {
            if (webView?.canGoBack()) {
                webView.goBack()
                return@OnKeyListener true
            }
        }
        false
    }

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            showToast("连按两下退出应用")
            //showToast("连按两下退出应用",Toast.LENGTH_SHORT) //此种方法调用也可以 有点可变参数的意思在里面
            mExitTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        mWebView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mWebView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebView?.clearCache(true)
        (mWebView?.parent as FrameLayout).removeView(mWebView)
        mWebView?.stopLoading()
        mWebView?.setWebViewClient(null)
        mWebView?.setWebChromeClient(null)
        mWebView?.removeAllViews()
        mWebView?.destroy()
        mWebView = null
    }

    //扩展函数
    fun MainActivity.showToast(message: String, length: Int = android.widget.Toast.LENGTH_SHORT) {
        android.widget.Toast.makeText(this, message, length).show()
    }
}
