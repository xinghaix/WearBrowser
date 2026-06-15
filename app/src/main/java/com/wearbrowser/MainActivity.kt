package com.wearbrowser

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wearbrowser.browser.BrowserScreen
import com.wearbrowser.browser.BrowserStore
import com.wearbrowser.design.WearBrowserTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val store = BrowserStore(this)
        setContent { WearBrowserTheme { BrowserScreen(store = store) } }
    }
}
