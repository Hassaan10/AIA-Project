package com.example.aiaproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aiaproject.data.model.Message
import com.example.aiaproject.data.network.ApiResponse
import com.example.aiaproject.ui.theme.AIAProjectTheme
import com.example.aiaproject.ui.viewmodels.AIViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIAProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun ChatScreen(padding: PaddingValues, viewModel: AIViewModel = hiltViewModel() ) {

    val viewModelState by viewModel.homeUIState.collectAsStateWithLifecycle()

    val text = remember { mutableStateOf("Ask AI") }
    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val scrollState = rememberScrollState()

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (val uiState = viewModelState) {
            is ApiResponse.Success -> {
                uiState.data?.let { messages ->
                    ChatLayout(messages)
                }
            }

            is ApiResponse.Error -> {
                Text(uiState.error.message?: "Something went wrong")
            }

            is ApiResponse.Loading -> {
               CircularProgressIndicator()
            }
            is ApiResponse.Empty -> {
                Text("Tap on Ask AI to start")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    text.value = "listening"
                }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    text.value = "processing"
                }
                override fun onError(error: Int) {}
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        viewModel.askAI(matches[0])
                        text.value = "Ask AI"
                    }
                    else {
                        text.value = "No results found"
                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {
                    text.value = "heard partially"
                }
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
            speechRecognizer.startListening(recognizerIntent)
            } else {
                ActivityCompat.requestPermissions(context as ComponentActivity, arrayOf(android.Manifest.permission.RECORD_AUDIO), 1)
            }
        }) {
            Text(text.value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpeechToTextScreenPreview() {
    AIAProjectTheme {
        ChatScreen(PaddingValues(8.dp), hiltViewModel<AIViewModel>())
    }
}

@Preview(showBackground = true)
@Composable
fun ChatItemPreview() {
    ChatMessageItem(Message(
        content = "Hello, how can I help you?",
        role = "user",
    ))
}

@Composable
fun ChatMessageItem(message: Message) {
    val textToSpeech = rememberTextToSpeech()
    LaunchedEffect(message) {
        if (!message.isSentByUser()) {
            textToSpeech.value?.speak(message.content, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (message.isSentByUser()) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = if (message.isSentByUser()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                contentColor = if (message.isSentByUser()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
            ),
        ) {
            Text(
                text = message.content ?: "",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// Composable function to display the chat layout
@Composable
fun ChatLayout(messages: List<Message>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(messages) { message ->
            ChatMessageItem(message)
        }
    }
}



@Composable
fun rememberTextToSpeech(): MutableState<TextToSpeech?> {
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.US
            }
        }
        tts.value = textToSpeech

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
    return tts
}