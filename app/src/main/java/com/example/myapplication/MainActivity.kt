package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.di.appModule
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.views.screens.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin{
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }
        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}