package com.meedz.lifeos.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.meedz.lifeos.core.security.BiometricAuthManager
import com.meedz.lifeos.ui.navigation.LifeOSNavGraph
import com.meedz.lifeos.ui.theme.LifeOSTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject lateinit var biometricAuthManager: BiometricAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (biometricAuthManager.isBiometricAvailable()) {
            biometricAuthManager.promptBiometricAuth(
                activity = this,
                onSuccess = { loadApp() },
                onError = { /* Handle error or fallback to PIN */ }
            )
        } else {
            loadApp()
        }
    }

    private fun loadApp() {
        setContent {
            LifeOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LifeOSNavGraph()
                }
            }
        }
    }
}
