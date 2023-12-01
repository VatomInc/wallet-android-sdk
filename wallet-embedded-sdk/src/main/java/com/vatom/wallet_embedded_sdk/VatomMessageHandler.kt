package com.vatom.wallet_embedded_sdk

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.google.gson.Gson
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.reflect.KFunction1


data class PendingRequest(
  val resolve: KFunction1<Any?, Boolean>,
  val reject: (Exception) -> Unit,
  val timer: CompletableFuture<Unit>
)

data class VatomMessage(
  val id: String,
  val name: String?,
  val request: Boolean,
  val payload: Any?,
  val error: String?
)

class VatomMessageHandler {
  private lateinit var webView: WebView
  private val pending = HashMap<String, PendingRequest>()
  private val handlers = HashMap<String, (Any?) -> Any?>()
  private val gson = Gson()


  @JavascriptInterface
  fun postMessage(message: String) {
    val decodedVatomMessage: VatomMessage = decodeMessage(message) ?: return
    if (decodedVatomMessage.request){
      val handler = handlers[decodedVatomMessage.name]
        ?: throw Exception("postMessage: No handler registered for ${decodedVatomMessage.name}")

      val result = if (decodedVatomMessage.payload != null && decodedVatomMessage.payload != {}) {
        handler(decodedVatomMessage.payload)
      } else {
        handler(null)
      }
      sendPayload(mapOf("id" to decodedVatomMessage.id, "payload" to result, "request" to false))

    } else {
      if (!pending.containsKey(decodedVatomMessage.id)) {
        return
      }

      pending[decodedVatomMessage.id]?.let {
        if (decodedVatomMessage.error != null) {
          it.reject(Exception(decodedVatomMessage.error))
        } else {
          it.resolve(decodedVatomMessage.payload)
        }
      }

      pending[decodedVatomMessage.id]?.timer?.cancel(false)
      pending.remove(decodedVatomMessage.id)
    }


  }

  private fun decodeMessage(message: String): VatomMessage? {
    return try {
      gson.fromJson(message, VatomMessage::class.java)
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  fun setWebView(webview: WebView){
    webView = webview
  }


  private fun sendPayload(payload: Map<String, Any?>) {
    val jsonString = Gson().toJson(payload)
    try {
      webView.post {
        webView.evaluateJavascript("window.postMessage($jsonString);", null)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun handle(name: String, callback: (Any?) -> Any?) {
    handlers[name] = callback
  }

  fun sendMsg(name: String, payload: Any? = null): CompletableFuture<Any?> {
    val id = UUID.randomUUID().toString()
    sendPayload(mapOf("id" to id, "name" to name, "payload" to payload, "request" to true))

    val completer = CompletableFuture<Any?>()
    val scheduledExecutorService = Executors.newScheduledThreadPool(1)
    val timer = scheduledExecutorService.schedule({
      pending.remove(id)?.reject?.let { it(TimeoutException("Timed out waiting for response.")) }
    }, 15, TimeUnit.SECONDS)

    pending[id] = PendingRequest(
      resolve = completer::complete,
      reject = completer::completeExceptionally,
      timer = CompletableFuture.completedFuture(Unit).apply {
        whenComplete { _, _ -> timer.cancel(false) }
      }
    )

    return completer
  }

}
