package com.example.auroraenvision.data

import ai.picovoice.eagle.EagleProfile

data class SpeechCard (
    val startTimeStamp: Long = System.currentTimeMillis(),
    var duration: Long = 0,
    var speech: String = "",//"Good Afternoon Ladies and Gentlemen. Greatness is for the bold, the brilliant, and the brave...",
    var silence: Boolean = false,
    var speaker: Speaker = Speaker()
)

data class Speaker (
    var data: EagleProfile? = null,
    var name: String = "Unknown",
    var progress: Float = 0.0f,
    var training: Boolean = false
)

data class AppUiState(
    var speechCards: ArrayList<SpeechCard> = ArrayList<SpeechCard>(),
    var currentSpeechCard: SpeechCard = SpeechCard(),
    var speakers: ArrayList<Speaker> = ArrayList<Speaker>(),
    var editedSpeaker: Speaker = Speaker(),
    var speech: Boolean = false,
    var speaker: Boolean = false,
    var event: Boolean = false
) {}
