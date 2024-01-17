import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.vatom.wallet_embedded_sdk.OnPageLoadedListener
import com.vatom.wallet_embedded_sdk.R
import com.vatom.wallet_embedded_sdk.VatomConfig
import com.vatom.wallet_embedded_sdk.VatomMessageHandler
import com.vatom.wallet_embedded_sdk.VatomWebViewClient
import com.vatom.wallet_embedded_sdk.onPageStartedListener

import pub.devrel.easypermissions.EasyPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.concurrent.CompletableFuture

class VatomWebWalletView @JvmOverloads constructor(
  context: Context,
  activity: AppCompatActivity,
  private val accessToken: String? = null,
  private val config: VatomConfig? = null,
  private val businessId: String? = null,
) : RelativeLayout(context) {
  lateinit var webView: WebView
  var vatomMessageHandler: VatomMessageHandler = VatomMessageHandler()
  var activity = activity
  var locationManager: LocationManager =
    activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
  var mGeoLocationRequestOrigin: String? = null
  var mGeoLocationCallback: GeolocationPermissions.Callback? = null
  val tabsRoutesAllowed = listOf("Home", "Wallet", "Map", "MapAr", "Connect")


  init {
    vatomMessageHandler.handle("vatomwallet:pre-init", ::initSDK)
    initView(context)
  }

  fun initSDK(data: Any? = null): Any? {
    try {
      vatomMessageHandler.sendMsg(
        "wallet-sdk-init",
        mapOf(
          "accessToken" to accessToken,
          "embeddedType" to "Android",
          "businessId" to businessId,
          "config" to config
        )
      )
    } catch (e: Exception) {
      Log.d("initSDK.error", e.toString())
    }
    return null
  }

  private fun initView(context: Context) {
  try {
    webView = WebView(context)
    val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    webView.layoutParams = layoutParams
    webView.addJavascriptInterface(vatomMessageHandler, "vatomMessageHandler")
    vatomMessageHandler.setWebView(webView)
    configureWebView()
  } catch (e:Exception ){
      Log.d("initView.error", e.toString())
    }
  }

  private fun configureWebView() {
    webView.settings.javaScriptEnabled = true
    webView.settings.mediaPlaybackRequiresUserGesture = false
    webView.settings.domStorageEnabled = true
    webView.settings.setSupportMultipleWindows(true)
    webView.settings.allowFileAccess = true
    webView.settings.allowFileAccessFromFileURLs = true
    webView.settings.allowUniversalAccessFromFileURLs = true

    //var url = "https://google.com"
    var url = "${config?.baseUrl ?: "https://wallet.vatom.com"}${if (businessId != null) "/b/${businessId}" else ""}"
    Log.d("VatomWebWallet.configureWebView", "configureWebView: ${url}")

    var vatomWebView = VatomWebViewClient(object : OnPageLoadedListener {
      override fun onPageLoaded() {
        initSDK()
      }
    }, object : onPageStartedListener {
      override fun onPageStarted() {
        initSDK()
      }
    })
    webView.webViewClient = vatomWebView

    webView.setWebChromeClient(object : WebChromeClient() {
      override fun onPermissionRequest(request: PermissionRequest) {
        var checkCameraPermission = context.checkSelfPermission(Manifest.permission.CAMERA)

        if (checkCameraPermission == PackageManager.PERMISSION_GRANTED){
          request.grant(request.resources)
        } else {

          try {
            var permissions =    arrayOf(Manifest.permission.CAMERA)
            activity.requestPermissions(permissions, 2)

            Handler(Looper.getMainLooper()).postDelayed({
              request?.grant(
                arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE)
              )
            }, 4000)

          } catch (e:Exception ){
            Log.d("onPermissionRequest.error", e.toString())
          }
        }
      }

      override fun onGeolocationPermissionsShowPrompt(
        origin: String?,
        callback: GeolocationPermissions.Callback?
      ) {
        try {
          mGeoLocationRequestOrigin = origin
          mGeoLocationCallback = callback
          if (callback != null) {
            if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
              ) != PackageManager.PERMISSION_GRANTED
            ) {
              EasyPermissions.requestPermissions(
                activity,
                "To have the best experience allow us access to your location",
                99,
                Manifest.permission.ACCESS_FINE_LOCATION
              )
            } else {
              if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                turnOnGps()
              }
              callback.invoke(origin, true, false)
            }
          };
        } catch (e: Exception) {
          Log.d("onGeolocationPermissionsShowPrompt", e.toString())
        }
      }


    })

    webView.setOnKeyListener(OnKeyListener { v, keyCode, event ->
      if (event.action == KeyEvent.ACTION_DOWN) {
        val webView = v as WebView
        when (keyCode) {
          KeyEvent.KEYCODE_BACK -> if (webView.canGoBack()) {
            webView.goBack()
            return@OnKeyListener true
          }
        }
      }
      false
    })

    webView.loadUrl(url)
  }

  private fun turnOnGps() {
    val locationRequest = LocationRequest.create().apply {
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
      interval = 1000
    }
    val settingsRequest =
      LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
    val client = LocationServices.getSettingsClient(activity)
    val task = client.checkLocationSettings(settingsRequest)
    task.addOnFailureListener { e ->
      if (e is ResolvableApiException) {
        try {
          e.startResolutionForResult(activity, 23)
        } catch (sendEx: IntentSender.SendIntentException) {
          sendEx.printStackTrace()
        }
      }
    }
  }

  fun performAction(tokenId: String, actionName: String, payload: Any?): Any? {
    val promise = CompletableFuture<Any?>()
    vatomMessageHandler.sendMsg(
      "walletsdk:performAction",
      mapOf(
        "tokenId" to tokenId,
        "actionName" to actionName,
        "actionPayload" to payload
      )
    ).thenAcceptAsync { res ->
      promise.complete(res)
    }.exceptionally { exception ->
      promise.completeExceptionally(exception)
      null
    }
    return promise
  }


  fun combineTokens(thisTokenId: String, otherTokenId: String): Any? {
     val promise = CompletableFuture<Any?>()
     val payload = mapOf("thisTokenId" to thisTokenId, "otherTokenId" to otherTokenId)
     vatomMessageHandler.sendMsg("walletsdk:combineToken",payload ).thenAcceptAsync{res ->
       promise.complete(res)
     }.exceptionally { exception ->
       promise.exceptionally { exception }
       null
     }
    return promise
   }

  fun trashToken(tokenId: String): Any? {
    val promise = CompletableFuture<Any?>()
    val payload = mapOf("tokenId" to tokenId)

    vatomMessageHandler.sendMsg("walletsdk:trashToken", payload).thenAcceptAsync{res ->
      promise.complete(res)
    }.exceptionally { exception ->
      promise.exceptionally { exception }
      null
    }
    return promise
  }

  fun getToken(tokenId: String): Any? {
    val promise = CompletableFuture<Any?>()
    val payload = mapOf("tokenId" to tokenId)

    vatomMessageHandler.sendMsg("walletsdk:getToken", payload).thenAcceptAsync{res ->
      promise.complete(res)
    }.exceptionally { exception ->
      promise.exceptionally { exception }
      null
    }
    return promise
  }

  fun getPublicToken(tokenId: String): Any? {
    val promise = CompletableFuture<Any?>()
    val payload = mapOf("tokenId" to tokenId)

    vatomMessageHandler.sendMsg("walletsdk:getToken", payload).thenAcceptAsync{res ->
      promise.complete(res)
    }.exceptionally { exception ->
      promise.exceptionally { exception }
      null
    }
    return promise
  }

  fun listTokens(): CompletableFuture<Any?> {
    val promise = CompletableFuture<Any?>()
    vatomMessageHandler.sendMsg("walletsdk:listTokens").thenAcceptAsync{res ->
      promise.complete(res)
    }.exceptionally { exception ->
      promise.exceptionally { exception }
      null
    }
    return promise
  }

  fun isLoggedIn(): Any? {
    val promise = CompletableFuture<Any?>()
    vatomMessageHandler.sendMsg("walletsdk:isLoggedIn").thenAcceptAsync{res ->
      promise.complete(res)
    }.exceptionally { exception ->
      promise.exceptionally { exception }
      null
    }
    return promise
  }

  fun getPublicProfile(userId: String): Any? {
    val promise = CompletableFuture<Any?>()
    vatomMessageHandler.sendMsg("walletsdk:getCurrentUser", mapOf("userId" to userId)).thenAcceptAsync{ res ->
      promise.complete(res)
    }.exceptionally { exception ->
      promise.exceptionally { exception }
      null
    }
    return promise
  }

  fun getCurrentUser(): CompletableFuture<Any?> {
    val promise = CompletableFuture<Any?>()
    vatomMessageHandler.sendMsg("walletsdk:getCurrentUser").thenAcceptAsync { user ->
      promise.complete(user)
    }.exceptionally { exception ->
      promise.completeExceptionally(exception)
      null
    }
    return promise
  }

  fun navigateToTab(tabRoute: String)  {
    if (!tabsRoutesAllowed.contains(tabRoute)) {
      throw Exception(
        "Route not allowed $tabRoute, allowed $tabsRoutesAllowed");
    }
    vatomMessageHandler.sendMsg("walletsdk:navigateToTab", mapOf(
      "route" to tabRoute,
      "params" to mapOf(
        "businessId" to businessId,
      )
    ))
  }

  fun getTabs(): CompletableFuture<Any?>  {
    val promise = CompletableFuture<Any?>()
    vatomMessageHandler.sendMsg("walletsdk:getBusinessTabs").thenAcceptAsync{res ->
      promise.complete(res)
    }.exceptionally { exception ->
      promise.exceptionally { exception }
      null
    }
    return promise
  }

  fun navigate(tabRoute: String, params: Map<String, Any?>?)  {
    vatomMessageHandler.sendMsg("walletsdk:navigate", mapOf(
      "route" to tabRoute,
      "params" to mapOf(
        "businessId" to businessId,
      )
    ))
  }

  fun openNFTDetail(tokenId: String)  {
    var route = "NFTDetail";
    if (businessId != null) {
      route = "NFTDetail_Business";
    }
    vatomMessageHandler.sendMsg("walletsdk:navigate",
      mapOf(
          "route" to route,
          "params" to mapOf("business" to businessId, "tokenId" to tokenId)
      ));
  }

  fun logOut()  {
    vatomMessageHandler.sendMsg("walletsdk:logOut")
  }

  fun openCommunity(communityId: String, roomId: String? = null) {
    vatomMessageHandler.sendMsg("walletsdk:openCommunity", mapOf(
      "bussinesId" to businessId,
      "communityId" to communityId,
      "roomId" to roomId,
    ));
  }


}
