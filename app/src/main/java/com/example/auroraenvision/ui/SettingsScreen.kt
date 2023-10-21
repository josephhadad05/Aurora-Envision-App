package com.example.auroraenvision.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SettingsScreen {

    @Composable
    fun SettingsScreenFun(viewModel: AppViewModel, modifier: Modifier = Modifier) {
        val gameUiState by viewModel.uiState.collectAsState()

        Column (
            modifier = modifier.fillMaxWidth()
        ){
            Row (
                modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Speech Recognition", modifier.padding(4.dp), color = MaterialTheme.colorScheme.tertiary)
                Spacer(Modifier.weight(1f))
                Switch(checked = gameUiState.speech, onCheckedChange = { viewModel.setSpeech(!gameUiState.speech) }, modifier.padding(4.dp))
            }
            Row (
                modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Event Recognition", modifier.padding(4.dp), color = MaterialTheme.colorScheme.tertiary)
                Spacer(Modifier.weight(1f))
                Switch(checked = gameUiState.event, onCheckedChange = { viewModel.setEvent(!gameUiState.event) }, modifier.padding(4.dp))
            }
            Row (
                modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Speaker Recognition", modifier.padding(4.dp), color = MaterialTheme.colorScheme.tertiary)
                Spacer(Modifier.weight(1f))
                Switch(checked = gameUiState.speaker, onCheckedChange = { viewModel.setSpeaker(!gameUiState.speaker) }, modifier.padding(4.dp))
            }
        }
    }
}