package com.inhuman.reservasionline

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import kotlinx.android.synthetic.main.activity_main.*
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.net.ConnectivityManager
import android.widget.Toast
//import com.example.inhuman.firstdroid.R.id.swipe
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity ()  {

//    private val url = "http://192.168.0.47:4200"
//   private val url = "http://36.89.61.226:2222/e-reservasi"

    private val url = "http://103.228.236.74:2222/e-reservasi"
//    private val loadiindeterminateBarng: ProgressBar? = null //Menambahkan widget ProgressBar
//    var swipe: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        } else {
            onLoadWebView()
        }
//        swipe = (findViewById<View>(R.id.swipe) as SwipeRefreshLayout)
//        swipe?.setOnRefreshListener {  onLoadWebView() }
    }
    object DetectConnection {
        fun checkInternetConnection(context: Context): Boolean {

            val conmanager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return (!(conmanager.activeNetworkInfo == null || !conmanager.activeNetworkInfo.isAvailable || !conmanager.activeNetworkInfo.isConnected))
        }
    }

    private fun onLoadWebView(){
        // Get the web view settings instance
        val settings = webview.settings;

        // Enable java script in web view
        settings.javaScriptEnabled = true

        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)


        // Enable zooming in web view
//        settings.setSupportZoom(true)
//        settings.builtInZoomControls = true
//        settings.displayZoomControls = true


        // Zoom web view text
        //settings.textZoom = 125


        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.mediaPlaybackRequiresUserGesture = false
        }

        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.allowUniversalAccessFromFileURLs = true
        }

        settings.allowFileAccess = true

        // WebView settings
        webview.fitsSystemWindows = true

        /* if SDK version is greater of 19 then activate hardware acceleration
        otherwise activate software acceleration  */

        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        progress.setVisibility(View.VISIBLE);
        webview.loadUrl(url)

        // Set web view client
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // Page loading started
                // Do something
//                super.onPageStarted(view, url, favicon)
//                view?.visibility = View.INVISIBLE
//                indeterminateBar.visibility = View.VISIBLE


            }
            override  fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
//                handler.proceed()
//                Toast.makeText(MainActivity(), "Your Internet Connection May not be active Or " + error , Toast.LENGTH_LONG).show();

            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                try {
                    webview.stopLoading()
                } catch (e: Exception) {
                }


                if (webview.canGoBack()) {
                    webview.goBack()
                }

                webview.loadUrl("about:blank")
                val alertDialog = AlertDialog.Builder(this@MainActivity).create()
                alertDialog.setTitle("Error")
                alertDialog.setMessage("Check your internet connection and try again.")
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again",
                    DialogInterface.OnClickListener { dialog, which ->
                        finish()
                        startActivity(intent)
                    })

                alertDialog.show()
                super.onReceivedError(view, request, error)
            }
            override fun onPageFinished(view: WebView, url: String) {
                // Page loading finished
                // Enable disable back forward button
//                progress.setVisibility(View.GONE);
//                swipe?.isRefreshing = false
//                super.onPageFinished(view, url)
//                view?.visibility = View.VISIBLE
//                indeterminateBar.visibility = View.INVISIBLE

            }

        }
    }
    override fun onBackPressed() {
        if (webview.canGoBack()) {
            // If web view have back history, then go to the web view back history
            webview.goBack()
        }
    }

}



