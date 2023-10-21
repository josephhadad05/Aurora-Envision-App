package com.example.auroraenvision.ui

import ai.picovoice.android.voiceprocessor.VoiceProcessor
import ai.picovoice.android.voiceprocessor.VoiceProcessorErrorListener
import ai.picovoice.android.voiceprocessor.VoiceProcessorException
import ai.picovoice.android.voiceprocessor.VoiceProcessorFrameListener
import ai.picovoice.cheetah.Cheetah
import ai.picovoice.cobra.Cobra
import ai.picovoice.eagle.Eagle
import ai.picovoice.eagle.EagleProfile
import ai.picovoice.eagle.EagleProfiler
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.auroraenvision.data.AppUiState
import com.example.auroraenvision.data.Speaker
import com.example.auroraenvision.data.SpeechCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@SuppressLint("StaticFieldLeak")
class AppViewModel (context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    val context: Context = context

    var eagleProfiler = EagleProfiler.Builder()
        .setAccessKey("Cp0vnF84HUFB/sGgbVSOfGAPZ93G3ONWU+7Z/b/h+Ob7gMvO04JyDw==")
        .build(context)
    var eagle: Eagle? = null
    var cheetah: Cheetah = Cheetah.Builder()
        .setAccessKey("Cp0vnF84HUFB/sGgbVSOfGAPZ93G3ONWU+7Z/b/h+Ob7gMvO04JyDw==")
        .setModelPath("cheetah_params.pv")
        .build(context)
    var cobra: Cobra = Cobra("Cp0vnF84HUFB/sGgbVSOfGAPZ93G3ONWU+7Z/b/h+Ob7gMvO04JyDw==")
    var voiceProcessor: VoiceProcessor = VoiceProcessor.getInstance();
    var frameListener: VoiceProcessorFrameListener? = null
    var errorListener: VoiceProcessorErrorListener? = null

    var transcript: String = ""
    var longFrame: ShortArray = ShortArray(0)



    fun getSpeakerData(): Array<out EagleProfile> {
        val speakerProfiles = ArrayList<EagleProfile>()
        for (speaker in _uiState.value.speakers) {
            if (speaker.data != null) {
                speakerProfiles.add(speaker.data!!)
            }
        }
        return speakerProfiles.toTypedArray()
    }

    fun getSpeakerNames(): ArrayList<String> {
        val speakerNames = ArrayList<String>()
        for (speaker in _uiState.value.speakers) {
            speakerNames.add(speaker.name)
        }

        return speakerNames
    }


    private fun processFrame(frame: ShortArray?) {
        if (frame != null) {
            Log.d("Progress: ", frame.size.toString())
        }

        var voiceProbability = cobra.process(frame)
        var partialResult = cheetah.process(frame)
        var eagleResult = eagle?.process(frame)

        val currentCard = _uiState.value.currentSpeechCard
        val editSpeaker = _uiState.value.editedSpeaker
        val silence = currentCard.silence


        var chosenSpeaker = currentCard.speaker

        if (_uiState.value.speaker) {
            eagleResult?.forEachIndexed { index, result ->
                if (result > 0.7) {
                    chosenSpeaker = _uiState.value.speakers[index]
                }
            }
        }

        if (_uiState.value.speech) {
            if ((partialResult!!.isEndpoint && voiceProbability < 0.5) && !silence) {
                val finalResult = cheetah.flush()
                transcript += partialResult.transcript
                transcript += finalResult.transcript
                updateCard(transcript, chosenSpeaker)
                startCard(true)
            }
            else if ((!partialResult!!.isEndpoint && voiceProbability > 0.7) && silence) {
                val finalResult = cheetah.flush()
                startCard(false)
                transcript += partialResult.transcript
                transcript += finalResult.transcript
                updateCard(transcript, chosenSpeaker)
            }
            else {
                transcript += partialResult.transcript
                updateCard(transcript, chosenSpeaker)
            }
        }

        if (editSpeaker.training) {
            if (longFrame.size < 20480) {
                if (frame != null) {
                    longFrame += frame
                }
            } else {
                if (editSpeaker.progress < 1.0F) {
                    val progress = eagleProfiler.enroll(longFrame.take(20480).toShortArray()).percentage / 100.0F
                    Log.d("Progress: ", progress.toString())
                    updateSpeakerProgress(progress = progress)
                    longFrame = ShortArray(0)
                } else {
                    val profile = eagleProfiler.export()
                    eagleProfiler.reset()
                    eagle?.reset()
                    updateSpeakerProfile(profile)
                    editSpeakerTraining(false)
                }
            }
        }
    }

    fun editSpeakerTraining(training: Boolean) {
        _uiState.update { currentState ->
            val newState = currentState.copy()
            val editedSpeaker = newState.editedSpeaker.copy()
            editedSpeaker.training = training
            newState.editedSpeaker = editedSpeaker
            newState
        }
    }

    fun saveSpeaker() {
        _uiState.update { currentState ->
            val newState = currentState.copy()
            if (newState.editedSpeaker.data != null) newState.speakers.add(newState.editedSpeaker)
            newState.editedSpeaker = Speaker()
            newState
        }
        eagle = Eagle.Builder()
            .setAccessKey("Cp0vnF84HUFB/sGgbVSOfGAPZ93G3ONWU+7Z/b/h+Ob7gMvO04JyDw==")
            .setSpeakerProfiles(getSpeakerData())
            .build(context)
    }

    fun deleteSpeaker(speaker: Speaker) {
        _uiState.update { currentState ->
            val newState = currentState.copy()
            newState.speakers
            newState
        }
        eagle = Eagle.Builder()
            .setAccessKey("Cp0vnF84HUFB/sGgbVSOfGAPZ93G3ONWU+7Z/b/h+Ob7gMvO04JyDw==")
            .setSpeakerProfiles(getSpeakerData())
            .build(context)
    }

    fun setSpeechCards(cards: ArrayList<SpeechCard>){
        _uiState.update { currentState ->
            currentState.copy(speechCards = cards)
        }
    }

    fun setSpeakerName(name: String) {
        _uiState.update { currentState ->
            val newState = currentState.copy()
            newState.editedSpeaker = newState.editedSpeaker.copy(name = name)
            newState
        }
    }

    fun setSpeech(speech: Boolean){
        _uiState.update { currentState ->
            currentState.copy(speech = speech)
        }
    }

    fun setEvent(event: Boolean){
        _uiState.update { currentState ->
            currentState.copy(event = event)
        }
    }

    fun setSpeaker(speaker: Boolean){
        _uiState.update { currentState ->
            currentState.copy(speaker = speaker)
        }
    }

    fun updateSpeakerProfile(profile: EagleProfile) {
        _uiState.update { currentState ->
            val newState = currentState.copy()
            newState.editedSpeaker = newState.editedSpeaker.copy(data = profile)
            newState
        }
    }

    fun updateSpeakerProgress(progress: Float) {
        _uiState.update { currentState ->
            val newState = currentState.copy()
            newState.editedSpeaker = newState.editedSpeaker.copy(progress = progress)
            newState
        }
    }

    fun startCard(silence: Boolean) {
        _uiState.update { currentState ->
            val newState = currentState.copy()
            newState.speechCards.add(newState.currentSpeechCard)
            newState.currentSpeechCard = SpeechCard(silence = silence, startTimeStamp = System.currentTimeMillis())
            newState
        }
        transcript = ""
    }

    fun updateCard(text: String, speaker: Speaker) {
        _uiState.update { currentState ->
            val newState = currentState.copy()
            val newSpeechCard = newState.currentSpeechCard.copy()
            newSpeechCard.speech = text
            newSpeechCard.speaker = speaker
            newSpeechCard.duration = System.currentTimeMillis() - newState.currentSpeechCard.startTimeStamp
            newState.currentSpeechCard = newSpeechCard
            newState
        }
    }

    init {
        frameListener =
            VoiceProcessorFrameListener { frame: ShortArray? ->
                try {
                    processFrame(frame)
                } catch (e: Exception) {
                    Log.e("YourTag", "An exception occurred", e)
                }
            }
        errorListener =
            VoiceProcessorErrorListener { e: VoiceProcessorException? ->
                Log.e("YourTag", "An exception occurred", e)
            }



        voiceProcessor.addFrameListener(frameListener)
        voiceProcessor.addErrorListener(errorListener)

        val frameLength = cheetah.frameLength
        val sampleRate = cheetah.sampleRate


        voiceProcessor.start(frameLength, sampleRate)
    }
}