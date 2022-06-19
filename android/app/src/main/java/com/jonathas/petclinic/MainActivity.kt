package com.jonathas.petclinic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.jonathas.petclinic.ui.components.MainScreen
import com.jonathas.petclinic.ui.theme.PetClinicTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenCreated {
            var show = false
            installSplashScreen().apply {
                setKeepOnScreenCondition { show }
            }
            delay(5000)
            show = true
        }

        super.onCreate(savedInstanceState)

        setContent{
            PetClinicApp()
        }
    }
}
