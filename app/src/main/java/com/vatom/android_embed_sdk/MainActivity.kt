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
      hideNavigation = true,
      scanner= ScannerFeatures(
         enabled = true
       ),
      hideTokenActions  = true,
      disableNewTokenToast = false,
      language = "en",
      )


    var AT = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Imw0Mjd4WnJxNjJlR0xhS0hhc0d0bkkyZ1JZVjF3c0VUUm0weDlDcEZiOWsifQ.eyJ1cm46dmF0b21pbmM6Z3Vlc3QiOmZhbHNlLCJ1cm46dmF0b21pbmM6cmVnaW9uIjoidXMtZWFzdDQuZ2NwIiwianRpIjoici1zM0VvbFVPcEhldW9qQUdDOWg3Iiwic3ViIjoiMGUzN3Y5bCIsImlhdCI6MTcwNTUxNzA1MywiZXhwIjoxNzA1NTIwNjUzLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG9mZmxpbmVfYWNjZXNzIiwiaXNzIjoiaHR0cHM6Ly9pZC52YXRvbS5jb20iLCJhdWQiOiI5NEpIa2RqOGpGODNqZkZGMkxJOFE0In0.PU8e5mLWMjTRqBkS32fwfA_4WKkcHcJCD7zNFTtIk8Blqgw1FG8eNi2N5oz-oBoVAdx3pBbI886AIQYSMhcz0snnNVl6oSe-Nqs_DrgePghpZHua8elU-ekYMKbxLzCHioPc7iMa8O4zVY3an4RXMRPqreK6GCDLq9zZFCeCvO8w2rxhriCwi9clKb_cYqSoLu28nKpeANTvqk5mAGUUSNnOqyLU1qSFrwonDX_Kej45NGq5wslIV0Yyi0ZZzjCchkPBPH4HWhgl8lxCF162qetLrewhUYx9DmtyIGQpSoIkTHGoH3KBGTvbK1LkHxCjfmlCmZV5GRFJNJzI3Evnfg"
    vatomWebWallet = VatomWebWalletView(this, this@MainActivity, AT, vatomConig)

    val webView = findViewById<WebView>(R.id.vatomWebView)

    val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    webView.layoutParams = layoutParams



    // Añadir la vista de vatomWebWallet al WebView
    webView.addView(vatomWebWallet!!.webView)
  }

}
