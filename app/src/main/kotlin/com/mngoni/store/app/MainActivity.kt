package com.mngoni.store.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mngoni.store.app.ui.MngoniNavHost
import com.mngoni.store.app.ui.theme.MngoniTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as MngoniApp).container
        setContent {
            MngoniTheme {
                MngoniNavHost(container = container)
            }
        }
    }
}
