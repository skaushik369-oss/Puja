package com.puja.mantrajaap

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MantraViewModel(app: Application) : AndroidViewModel(app) {
    val player = ChantPlayer(app)

    private val _selected = MutableStateFlow(0)
    val selected: StateFlow<Int> = _selected.asStateFlow()

    fun select(index: Int) {
        if (index == _selected.value) return
        player.stop()
        _selected.value = index
    }

    override fun onCleared() {
        player.shutdown()
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: MantraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = PujaColors) {
                MantraScreen(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // The user may have just installed a Hindi voice in settings.
        viewModel.player.checkHindiVoice()
    }
}

private val Saffron = Color(0xFFE8711A)
private val DeepSaffron = Color(0xFFB5520C)
private val Maroon = Color(0xFF7A1F0E)
private val Cream = Color(0xFFFFF8F0)

private val PujaColors = lightColorScheme(
    primary = DeepSaffron,
    onPrimary = Color.White,
    secondary = Maroon,
    primaryContainer = Color(0xFFFFE0C2),
    onPrimaryContainer = Maroon,
    background = Cream,
    surface = Cream,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MantraScreen(vm: MantraViewModel) {
    val state by vm.player.state.collectAsStateWithLifecycle()
    val selectedIndex by vm.selected.collectAsStateWithLifecycle()
    val mantra = MANTRAS[selectedIndex]
    val playing = state.status == ChantPlayer.Status.Playing

    // Keep the screen on while chanting so the phone does not lock mid-jaap.
    val view = LocalView.current
    DisposableEffect(playing) {
        view.keepScreenOn = playing
        onDispose { view.keepScreenOn = false }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Mantra Jaap",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Maroon,
            )

            // Mantra picker
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = "${mantra.number}. ${mantra.title}",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select mantra") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    MANTRAS.forEachIndexed { index, m ->
                        DropdownMenuItem(
                            text = { Text("${m.number}. ${m.title}") },
                            onClick = {
                                vm.select(index)
                                expanded = false
                            },
                        )
                    }
                }
            }

            // Counter
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "${state.current} / ${mantra.repeatCount}",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Maroon,
                    )
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { state.current.toFloat() / mantra.repeatCount },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        when (state.status) {
                            ChantPlayer.Status.Idle ->
                                if (state.engineReady || state.problem != null) "Press Play to begin"
                                else "Preparing Hindi voice…"
                            ChantPlayer.Status.Playing -> "Chanting…"
                            ChantPlayer.Status.Paused -> "Paused"
                            ChantPlayer.Status.Finished -> "Complete 🙏"
                        },
                        color = Maroon,
                    )
                    mantra.note?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = Maroon.copy(alpha = 0.7f))
                    }
                }
            }

            // Controls
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { if (playing) vm.player.pause() else vm.player.play(mantra) },
                    enabled = state.engineReady || state.problem != null,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        when (state.status) {
                            ChantPlayer.Status.Playing -> "❚❚  Pause"
                            ChantPlayer.Status.Paused -> "▶  Resume"
                            ChantPlayer.Status.Finished -> "▶  Play again"
                            ChantPlayer.Status.Idle -> "▶  Play"
                        },
                        fontSize = 18.sp,
                    )
                }
                OutlinedButton(
                    onClick = { vm.player.stop() },
                    enabled = state.status != ChantPlayer.Status.Idle,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("■  Stop", fontSize = 18.sp)
                }
            }

            // Speed
            var rate by remember { mutableFloatStateOf(1.0f) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Speed ${"%.1f".format(rate)}×", modifier = Modifier.padding(end = 12.dp))
                Slider(
                    value = rate,
                    onValueChange = {
                        rate = it
                        vm.player.setSpeechRate(it)
                    },
                    valueRange = 0.5f..1.5f,
                    steps = 9,
                )
            }

            // Mantra text, scrollable when long (e.g. Hanuman Chalisa)
            val scroll = remember(selectedIndex) { ScrollState(0) }
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Text(
                    mantra.displayText,
                    fontSize = 20.sp,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Center,
                    color = Maroon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scroll)
                        .padding(20.dp),
                )
            }
        }
    }

    state.problem?.let { problem ->
        VoiceProblemDialog(problem, onDismiss = { vm.player.clearProblem() })
    }
}

@Composable
private fun VoiceProblemDialog(problem: ChantPlayer.VoiceProblem, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val (title, message) = when (problem) {
        ChantPlayer.VoiceProblem.NoEngine ->
            "Speech engine needed" to
                "This phone has no working text-to-speech engine. Install \"Speech Services by Google\" " +
                "from the Play Store, then come back and press Play."
        ChantPlayer.VoiceProblem.NoHindiVoice ->
            "Hindi voice not installed" to
                "To chant in Hindi, download the Hindi (India) voice. Tap \"Install voice\", choose Hindi, " +
                "then come back and press Play."
        ChantPlayer.VoiceProblem.SpeakError ->
            "Could not speak" to
                "The Hindi voice stopped with an error. It may not be fully downloaded, or it may need " +
                "internet. Install the Hindi voice for offline use, then press Resume."
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                if (problem == ChantPlayer.VoiceProblem.NoEngine) openPlayStore(context)
                else openVoiceInstall(context)
            }) {
                Text(if (problem == ChantPlayer.VoiceProblem.NoEngine) "Open Play Store" else "Install voice")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Not now") }
        },
    )
}

private fun openVoiceInstall(context: Context) {
    val attempts = listOf(
        Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA),
        Intent("com.android.settings.TTS_SETTINGS"),
        Intent(android.provider.Settings.ACTION_SETTINGS),
    )
    for (intent in attempts) {
        try {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            return
        } catch (_: ActivityNotFoundException) {
        } catch (_: SecurityException) {
        }
    }
}

private fun openPlayStore(context: Context) {
    val pkg = "com.google.android.tts"
    try {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    } catch (_: ActivityNotFoundException) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$pkg"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }
}
