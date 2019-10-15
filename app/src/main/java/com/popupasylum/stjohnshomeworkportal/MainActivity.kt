package com.popupasylum.stjohnshomeworkportal

import android.content.res.Configuration
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.webkit.CookieSyncManager
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.os.Handler
import android.util.Log.e
import android.view.*


class MainActivity : AppCompatActivity() {

    private var myWebView: WebView? = null
    private var isMathletics = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myWebView = WebView(applicationContext)
        this.myWebView = myWebView

        setupCookies(myWebView)
        setupErrorHandling(myWebView)

        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.allowContentAccess = true
        myWebView.settings.loadWithOverviewMode = true
        myWebView.settings.useWideViewPort = false
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.savePassword = true
        myWebView.settings.builtInZoomControls = true
        myWebView.settings.displayZoomControls = false

        myWebView.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            Handler().postDelayed({hideSystemUI()}, 1400)
            return@OnTouchListener false;
        })

        setContentView(myWebView)

        if (savedInstanceState == null) {
            myWebView?.loadUrl("https://login.mathletics.com/")
            isMathletics = true
        }

        hideSystemUI()
    }

    private fun setupErrorHandling(myWebView: WebView) {
        myWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                hideSystemUI()
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        myWebView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
                Log.d("MyApplication", "$message -- From line $lineNumber of $sourceID")
            }
        }
    }

    private fun setupCookies(myWebView: WebView) {
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(myWebView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
    }

    override fun onBackPressed() {
        hideSystemUI()

        if (isMathletics) {
            myWebView?.loadUrl("https://www.activelearnprimary.co.uk/")
        }else {
            myWebView?.loadUrl("https://login.mathletics.com/")
        }
        isMathletics = !isMathletics
        //super.onBackPressed()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
    }

    private fun hideSystemUI(): Boolean {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        return false
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        myWebView?.saveState(outState);
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        myWebView?.restoreState(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }
}
