package com.example.android_tv_frontend

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity

/**
 * PUBLIC_INTERFACE
 * Main Activity for Android TV that renders the generated Home Page design using a WebView.
 *
 * The HTML/CSS/JS and images live in the repository's root-level 'assets/' folder.
 * Those files are packaged into the APK via Gradle sourceSets (see app/build.gradle.kts),
 * so they remain in place without moving or renaming.
 *
 * On launch, this activity loads 'file:///android_asset/home-page-1-2.html' which
 * references local CSS/JS and images relatively (e.g., './common.css', './figmaimages/...').
 */
class MainActivity : FragmentActivity() {

    private lateinit var webView: WebView

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.web_view)
        configureWebView(webView)

        // Black background for TV experience
        webView.setBackgroundColor(Color.BLACK)

        // Load the Home Page from packaged assets
        webView.loadUrl("file:///android_asset/home-page-1-2.html")
    }

    /**
     * Configure WebView to support local file access and JavaScript for the static UI.
     * This ensures CSS/JS/images referenced relatively in the HTML are resolved correctly.
     */
    private fun configureWebView(wv: WebView) {
        val s: WebSettings = wv.settings
        s.javaScriptEnabled = true
        s.domStorageEnabled = true
        s.allowFileAccess = true
        s.allowContentAccess = true
        s.loadsImagesAutomatically = true
        s.useWideViewPort = true
        s.loadWithOverviewMode = true
        // Enable file URL access for local resource resolution inside the HTML
        s.allowFileAccessFromFileURLs = true
        s.allowUniversalAccessFromFileURLs = true

        wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                // Keep navigation inside this WebView
                return false
            }
        }
        wv.webChromeClient = WebChromeClient()
        wv.isFocusable = true
        wv.isFocusableInTouchMode = true
    }

    // PUBLIC_INTERFACE
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_ENTER -> {
                // SELECT/OK - currently no special bindings required for static page
                true
            }
            KeyEvent.KEYCODE_BACK -> {
                if (this::webView.isInitialized && webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}
