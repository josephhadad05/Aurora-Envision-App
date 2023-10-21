package com.example.auroraenvision.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.auroraenvision.AuroraScreens
import com.example.auroraenvision.R

class BottomBarScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AuroraAppBar(navController: NavController, modifier: Modifier = Modifier){
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.scrim,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Image(
                    painter = painterResource(id = R.drawable.winners_aurora_small),
                    contentDescription = "Home",
                    modifier = modifier
                        .clickable{ navController.navigate(AuroraScreens.Speech.name) }
                        .padding(8.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigate(AuroraScreens.Users.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "People"
                    )
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate(AuroraScreens.Settings.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        )
    }
}