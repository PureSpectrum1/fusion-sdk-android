package com.purespectrum.fusionsdkandroid
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)

        val url = intent.getStringExtra("url") ?: return
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
    }
}