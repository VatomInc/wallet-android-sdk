package com.vatom.wallet_embedded_sdk

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


interface OnPageLoadedListener {
  fun onPageLoaded()
}

interface onPageStartedListener  {
  fun onPageStarted()
}


class VatomWebViewClient (private val onPageLoadedListener: OnPageLoadedListener,
                          private val onPageStartedListener: onPageStartedListener
) : WebViewClient() {

  override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
    super.onPageStarted(view, url, favicon)
    onPageStartedListener.onPageStarted()
  }

  override fun onPageFinished(view: WebView?, url: String?) {
    super.onPageFinished(view, url)
    onPageLoadedListener.onPageLoaded()
  }

  override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
    if (request?.isForMainFrame == true &&
      !request.url.toString().contains("vatom") &&
      !request.url.toString().contains("ngrok") &&
      !request.url.toString().contains("https://www.google.com/recaptcha")
    ) {
      view?.loadUrl(request.url.toString())
      return true
    }
    return super.shouldOverrideUrlLoading(view, request)
  }

}
