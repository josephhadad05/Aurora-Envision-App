package com.example.auroraenvision.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.auroraenvision.data.SpeechCard

class SpeechScreen {

    @Composable
    fun SpeechScreenFun(viewModel: AppViewModel, modifier: Modifier = Modifier){
        val gameUiState by viewModel.uiState.collectAsState()

        LazyColumn {
            items(gameUiState.speechCards) {
                item : SpeechCard -> SpeechCardFun(item = item, modifier = modifier, viewModel = viewModel)
            }
            item {
                SpeechCardFun(item = gameUiState.currentSpeechCard, modifier = modifier, viewModel = viewModel)
            }
        }
    }

    @Composable
    fun SpeechCardFun(item: SpeechCard, viewModel: AppViewModel, modifier: Modifier = Modifier) {
        Card (
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column {
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
                            text = item.speaker.name,
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = modifier
                                .padding(4.dp)
                        )
                    }
                    Card (
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = modifier
                            .clip(CircleShape)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (item.silence) "Silence" else "Speech",
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = modifier
                                .padding(4.dp)
                        )
                    }
                    Spacer(modifier = modifier.weight(1f))
                    Card (
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        ),
                        modifier = modifier
                            .clip(CircleShape)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "${(item.duration/1000.0).toInt()} sec",
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = modifier
                                .padding(4.dp)
                        )
                    }
                }
                if (item.speech.isNotEmpty()) {
                    Text(
                        text = item.speech,
                        modifier = modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}