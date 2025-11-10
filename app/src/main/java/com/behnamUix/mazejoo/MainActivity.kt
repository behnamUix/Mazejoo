package com.behnamUix.mazejoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.behnamUix.mazejoo.ui.theme.MazejooTheme
import com.behnamUix.mazejoo.view.navigation.home.HomeSc
import com.behnamUix.mazejoo.view.splash.SplashSc1
import com.orhanobut.hawk.Hawk


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Hawk.init(this).build()

        setContent {
            MazejooTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)){
                        var firstRun=Hawk.get("firstRun",true)
                        if(firstRun){
                            Navigator(SplashSc1)
                        }else{
                            Navigator(HomeSc)

                       }


                   }
                }
            }
        }
    }
}

