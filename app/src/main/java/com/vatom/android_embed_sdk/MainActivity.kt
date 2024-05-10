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


    var AT = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Imw0Mjd4WnJxNjJlR0xhS0hhc0d0bkkyZ1JZVjF3c0VUUm0weDlDcEZiOWsifQ.eyJ1cm46dmF0b21pbmM6bG9nZ2VkLWluLXZpYSI6Im1hZ2ljLWNvZGUiLCJ1cm46dmF0b21pbmM6Z3Vlc3QiOmZhbHNlLCJ1cm46dmF0b21pbmM6cmVnaW9uIjoidXMtZWFzdDQuZ2NwIiwianRpIjoibHpYanhxUzcyYks3cC0tR3lKZkFpIiwic3ViIjoiMGUzN3Y5bCIsImlhdCI6MTcxNTM3NDAyMSwiZXhwIjoxNzE1Mzc3NjIxLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG9mZmxpbmVfYWNjZXNzIiwiaXNzIjoiaHR0cHM6Ly9pZC52YXRvbS5jb20iLCJhdWQiOiI5NEpIa2RqOGpGODNqZkZGMkxJOFE0In0.GQbC9mugbYcSy-ItX76lTj8ShO79IP8st_SlR0rPP7H5BRR7WgB2rM7kf31u-t2BA6letBgTzk7Lu8jtt-gTnwT8w3oCVFyRwQ98iNggGBHcgwlVjK3dPtSiyu8epYqef-bqn-tC-HNYh0FFlxbSXtgUl2I9LvOF2kzbD8NVJ_k6Zrt8aZNPQTYEAWT4KFVNOLLfZ_TX9EoR2l07MVVdbrmmdJbItNK07MgkekOpxGNTRWqgSt6F4ddl07UzXqv_GAs-VOSjL9236O2vu2qabYrIUmrmjjXhZhOjgWxY4FyLIql7HcYb37bMJNRSd_KH4CBYj773bOclIfPEzieBvQ"
    vatomWebWallet = VatomWebWalletView(this, this@MainActivity, AT, vatomConig)

    val webView = findViewById<WebView>(R.id.vatomWebView)

    val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    webView.layoutParams = layoutParams


    fun linkTo(){
      vatomWebWallet.linkTo("/map")
    }

    // Añadir la vista de vatomWebWallet al WebView
    webView.addView(vatomWebWallet!!.webView)
  }

}
