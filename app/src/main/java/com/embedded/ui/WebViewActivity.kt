package com.embedded.ui

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.embedded.R
import com.embedded.utils.Util
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.content_web_view.*


class WebViewActivity : BaseActivity(){
    companion object{
        val TAG=WebViewActivity::class.java.simpleName
        private const val FRAGMENT_DIALOG = "dialog"
        private const val REQUEST_CAMERA_PERMISSION = 1
        private const val REQUEST_AUDIO_PERMISSION = 1
         const val MEETING_URL = "MEETING_URL"
    }
    private var meetingUrl:String?= null
    private var mPermissionRequest: PermissionRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        setSupportActionBar(toolbar)
        initData()
        //initWebView()
        //initViewWebViewClient()
        setupPermissions()
    }
private fun initData(){
     meetingUrl= intent.getStringExtra(MEETING_URL)

   meetingUrl="$meetingUrl"
    //meetingUrl="https://vps819243.ovh.net/calendar/store?skipMediaPermissionPrompt=true&room=c87637e5-e95b-4a02-bc2d-b813dc7cd707&roomKey=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtZWV0aW5nSWQiOiIyNjkzODg5OCIsInJvb21SZWZlcmVuY2UiOnsicm9vbU5hbWUiOiIvYzg3NjM3ZTUtZTk1Yi00YTAyLWJjMmQtYjgxM2RjN2NkNzA3Iiwib3JnYW5pemF0aW9uSWQiOiI2NjgyMCJ9LCJpc3MiOiJodHRwczovL2FjY291bnRzLnNydi53aGVyZWJ5LmNvbSIsImlhdCI6MTYxMzY2MjY4Mywicm9vbUtleVR5cGUiOiJtZWV0aW5nSG9zdCJ9.2a04-x4ni27_a24boUHtXrsSx54ZFO7LXJZJ2WC6ZjQ&forced"
   // meetingUrl="https://whereby.com/testdemo?skipMediaPermissionPrompt"
    println("URL>>>>>>>> $meetingUrl")
}

    private fun initViewWebViewClient(){
        try{
            toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.ic_action_back)
            toolbar.setNavigationOnClickListener { onBackPressed() }
            toolbar.title = getString(R.string.meeting)

            web_view.settings.javaScriptEnabled = true;
            web_view.settings.loadWithOverviewMode = true;
            web_view.settings.useWideViewPort = true;
            progress_bar.visibility=View.VISIBLE
            web_view.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    progress_bar.visibility=View.VISIBLE
                    view!!.loadUrl(request?.url.toString())
                    return true
                }
                override fun onPageFinished(view: WebView, url: String) {
                    progress_bar.visibility=View.GONE
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    progress_bar.visibility=View.GONE
                    showToast("${error?.description}")
                }

            }
            web_view.loadUrl(meetingUrl!!);
        }catch (e:Exception){
            e.printStackTrace()
            Log.e(TAG,e.message!!)
            progress_bar.visibility=View.GONE
        }
    }
    private fun initWebView() {
        toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.ic_action_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = getString(R.string.meeting)
        progress_bar.visibility= View.GONE
        //web_view.loadUrl("https://whereby.com/omdemo")
        configureWebSettings(web_view.settings);
        println("load chrome>>>>>>>>>>>>>>")
        web_view.loadUrl(meetingUrl!!)
        //web_view.webChromeClient=webChromeClient

        web_view.setWebChromeClient(object : WebChromeClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionRequest(request: PermissionRequest) {
                println("onPermissionRequest>>>>>>>>>>>>>>")
                request.grant(request.resources)
            }
        })

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun configureWebSettings(settings: WebSettings) {
        settings.javaScriptEnabled = true
        settings.useWideViewPort=true
        settings.loadWithOverviewMode=true
        settings.builtInZoomControls=true
        settings.domStorageEnabled=true
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            settings.displayZoomControls=false
            web_view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }
        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
       // settings.mediaPlaybackRequiresUserGesture=true
        //setMediaPlaybackRequiresUserGesture(false);
        // AppRTC requires third party cookies to work
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.allowContentAccess = true
      //  settings.allowFileAccessFromFileURLs = true
        //settings.allowUniversalAccessFromFileURLs = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false
        web_view.clearCache(true)
        web_view.clearHistory()
        web_view.webViewClient = WebViewClient()
        // AppRTC requires third party cookies to work
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptThirdPartyCookies(web_view, true)
    }

    private val webChromeClient =object :WebChromeClient(){
        override fun onPermissionRequestCanceled(request: PermissionRequest?) {
            super.onPermissionRequestCanceled(request)
            println("request canceled>>>> ${request!!.resources}")
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onPermissionRequest(request: PermissionRequest?) {
           // super.onPermissionRequest(request)
            val requestedResources = request!!.resources
            println("inside request>>>>>>>>>>>>>>")
            runOnUiThread {
                request.grant(requestedResources)
//                if(request.origin.toString() =="https://appnova.whereby.com/"){
//                    request.grant(requestedResources)
//                }else{
//                    videoPermission(requestedResources)
//                }
               // request.grant(requestedResources)

            }
//            mPermissionRequest =request
//            println("onPermissionRequest>>>>>>>>")
//            val requestedResources = request!!.resources
//            videoPermission(requestedResources)
//            requestedResources.forEachIndexed { index, r ->
//                println("s>>>>>>>> $r")
//                if (r == PermissionRequest.RESOURCE_VIDEO_CAPTURE) {
//                    // In this sample, we only accept video capture request.
//                   videoPermission(requestedResources)
//                }else if(r == PermissionRequest.RESOURCE_AUDIO_CAPTURE){
//                  audioPermission(requestedResources)
//                }
//            }
        }




    }

    private fun videoPermission(resources: Array<String?>?){
    //  var str:String= "${getString(R.string.confirmation, TextUtils.join("\n", resources!!))}"
        var str = "${getString(R.string.confirmation,TextUtils.join("\n",resources!!))}"
        MaterialDialog(this).show {
            message(null,str)
            positiveButton(R.string.allow)
            negativeButton(R.string.deny)
            positiveButton {
                mPermissionRequest?.grant(resources)
                Log.d(TAG,
                    "Permission granted."
                )
            }
            negativeButton {
                mPermissionRequest?.deny()
                Log.d(TAG,
                    "Permission request denied."
                )
            }
        }
    }

    private fun audioPermission(resources: Array<String?>?){
        MaterialDialog(this).show {
            message(R.string.confirmation)
            positiveButton(R.string.allow)
            negativeButton(R.string.deny)
            negativeButton {
                mPermissionRequest?.deny()
                Log.d(TAG,
                    "Permission request denied."
                )
            }
            positiveButton {
                mPermissionRequest?.grant(resources)
                Log.d(TAG,
                    "Permission granted."
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //setupPermissions()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access the Camera is required for this app to record audio.")
                    .setTitle("Permission required")
                builder.setPositiveButton(
                    "OK"
                ) { dialog, id ->
                    Log.i(TAG, "Clicked")
                    makeRequest()
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        }else{
            initWebView()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO),
            REQUEST_CAMERA_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                    Util.alertDialog("Permission has been denied by user", this)
                }else if (grantResults.isEmpty() || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                    Util.alertDialog("Permission has been denied by user", this)
                }
                else {
                    showToast("Permission has been granted by user")
                    Log.i(TAG, "Permission has been granted by user")
                    initWebView()
                }
            }
        }
    }

}