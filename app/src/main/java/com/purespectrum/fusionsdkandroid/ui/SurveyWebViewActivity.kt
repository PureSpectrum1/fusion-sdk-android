package com.purespectrum.fusionsdkandroid.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.purespectrum.fusionsdkandroid.R
import androidx.core.view.WindowCompat

class SurveyWebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private var surveyUrl: String? = null

    companion object {
        const val EXTRA_URL = "url"
        const val EXTRA_TOOLBAR_COLOR = "com.purespectrum.fusionsdkandroid.ui.EXTRA_TOOLBAR_COLOR"
        const val EXTRA_TOOLBAR_TITLE_COLOR = "com.purespectrum.fusionsdkandroid.ui.EXTRA_TOOLBAR_TITLE_COLOR"
        const val EXTRA_TOOLBAR_TITLE = "com.purespectrum.fusionsdkandroid.ui.EXTRA_TOOLBAR_TITLE"

        fun launch(context: Context, url: String, toolbarColor: Int?, toolbarTitleColor: Int?, toolbarTitle: String?) {
            val intent = Intent(context, SurveyWebViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                toolbarColor?.let { putExtra(EXTRA_TOOLBAR_COLOR, it) }
                toolbarTitleColor?.let { putExtra(EXTRA_TOOLBAR_TITLE_COLOR, it) }
                toolbarTitle?.let { putExtra(EXTRA_TOOLBAR_TITLE, it) }
            }
            if (context !is android.app.Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        surveyUrl = intent.getStringExtra(EXTRA_URL)
        if (surveyUrl == null) {
            finish()
            return
        }

        val rootLayout = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        EdgeToEdgeHelper.setup(this, rootLayout)
        setContentView(rootLayout)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val customTitle = intent.getStringExtra(EXTRA_TOOLBAR_TITLE)
        supportActionBar?.title = customTitle ?: getString(R.string.fusion_sdk_survey_title_default)

        intent.getIntExtra(EXTRA_TOOLBAR_COLOR, -1).takeIf { it != -1 }?.let {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
        }
        intent.getIntExtra(EXTRA_TOOLBAR_TITLE_COLOR, -1).takeIf { it != -1 }?.let {
        }

        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            isIndeterminate = true
            visibility = View.VISIBLE
        }

        webView = WebView(this).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }

                @Deprecated("Deprecated in Java", ReplaceWith("true"))
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let { view?.loadUrl(it) }
                    return true
                }
            }
        }

        rootLayout.addView(webView)
        rootLayout.addView(progressBar)

        surveyUrl?.let { webView.loadUrl(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Use onBackPressedDispatcher.onBackPressed() in onOptionsItemSelected or register a callback")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        (webView.parent as? ViewGroup)?.removeView(webView)
        webView.destroy()
        super.onDestroy()
    }
}

