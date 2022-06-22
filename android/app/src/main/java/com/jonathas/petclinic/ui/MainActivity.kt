package com.jonathas.petclinic.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jonathas.petclinic.R
import com.jonathas.petclinic.ui.ui.main.MainViewModel
import com.jonathas.petclinic.ui.ui.main.domain.MainViewModelProvider
import com.jonathas.petclinic.utils.viewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val scope = CoroutineScope(
        Job() + Dispatchers.Main
    )

    private val viewModel: MainViewModel by viewModel(MainViewModelProvider())

    override fun onCreate(savedInstanceState: Bundle?) {
        scope.launch {
            var show = false
            installSplashScreen().apply {
                setKeepOnScreenCondition { show }
            }
            delay(5000)
            show = viewModel.loadSettings()
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}
