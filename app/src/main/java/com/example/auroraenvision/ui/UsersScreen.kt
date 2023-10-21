package com.example.auroraenvision.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.auroraenvision.data.Speaker

class UsersScreen {

    @Composable
    fun UsersScreenFun(viewModel: AppViewModel, modifier: Modifier = Modifier){
        val gameUiState by viewModel.uiState.collectAsState()

        LazyColumn {
            items(gameUiState.speakers) {
                    item : Speaker ->
                SpeakerCard(item = item, modifier = modifier, viewModel = viewModel)
            }
            item {
                EditableSpeakerCard(item = gameUiState.editedSpeaker, modifier = modifier, viewModel = viewModel)
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditableSpeakerCard(item: Speaker, viewModel: AppViewModel, modifier: Modifier = Modifier) {
        val gameUiState by viewModel.uiState.collectAsState()

        Card (
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
        ){
            Column {
                OutlinedTextField(value = item.name, onValueChange = { viewModel.setSpeakerName(it) },
                    modifier
                        .padding(8.dp)
                        .fillMaxWidth())
                LinearProgressIndicator(
                    progress = gameUiState.editedSpeaker.progress,
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                Row (
                    modifier = modifier.padding(8.dp)) {
                    Button(
                        onClick = { viewModel.editSpeakerTraining(true) },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) { Text(text = if (gameUiState.editedSpeaker.training) "Training" else "Train", color = MaterialTheme.colorScheme.onTertiary) }
                    Spacer(modifier = modifier.weight(1f))
                    Button(
                        onClick = { viewModel.saveSpeaker() },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) { Text(text = "Save", color = MaterialTheme.colorScheme.onTertiary) }
                }
            }
        }
    }

    @Composable
    fun SpeakerCard(item: Speaker, viewModel: AppViewModel, modifier: Modifier = Modifier) {
        Card (
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row {
                Card (
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = modifier
                        .clip(CircleShape)
                        .padding(8.dp)
                ) {
                    Text(
                        text = item.name,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = modifier
                            .padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { viewModel.deleteSpeaker(item) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Speaker"
                    )
                }
            }
        }
    }
}