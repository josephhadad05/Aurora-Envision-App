package com.example.auroraenvision

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auroraenvision.ui.AppViewModel
import com.example.auroraenvision.ui.BottomBarScreen
import com.example.auroraenvision.ui.SettingsScreen
import com.example.auroraenvision.ui.SpeechScreen
import com.example.auroraenvision.ui.UsersScreen

enum class AuroraScreens() {
    Speech,
    Settings,
    Users
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUi(viewModel: AppViewModel){
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold (
            bottomBar = {
                BottomBarScreen().AuroraAppBar(navController = navController)
            }
        ) {paddingValues ->
            NavHost(
                navController = navController,
                startDestination = AuroraScreens.Speech.name,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(AuroraScreens.Speech.name){
                    SpeechScreen().SpeechScreenFun(viewModel)
                }

                composable(AuroraScreens.Settings.name){
                    SettingsScreen().SettingsScreenFun(viewModel)
                }

                composable(AuroraScreens.Users.name){
                    UsersScreen().UsersScreenFun(viewModel)
                }

            }
        }

    }
}