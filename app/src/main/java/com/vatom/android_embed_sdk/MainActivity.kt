package com.vatom.android_embed_sdk

import VatomWebWalletView
import android.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.vatom.wallet_embedded_sdk.ScannerFeatures
import com.vatom.wallet_embedded_sdk.VatomConfig

class MainActivity : AppCompatActivity() {
  var vatomWebWallet: VatomWebWalletView? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val vatomConig = VatomConfig(
      hideNavigation = false,
      scanner= ScannerFeatures(
         enabled = true
       ),
      hideTokenActions  = true,
      disableNewTokenToast = false,
      language = "en",
      )


    var AT = ""
    vatomWebWallet = VatomWebWalletView(this, this@MainActivity, AT, vatomConig)

    val webView = findViewById<WebView>(R.id.vatomWebView)

    val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    webView.layoutParams = layoutParams



    // Añadir la vista de vatomWebWallet al WebView
    webView.addView(vatomWebWallet!!.webView)
  }

}
