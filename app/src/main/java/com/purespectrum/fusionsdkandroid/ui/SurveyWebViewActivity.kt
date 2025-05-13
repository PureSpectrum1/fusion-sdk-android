package com.purespectrum.fusionsdkandroid.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.purespectrum.fusionsdkandroid.R

class SurveyWebViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_webview)

        val url = intent.getStringExtra("url") ?: return

        val webView = findViewById<WebView>(R.id.surveyWebView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }
}
