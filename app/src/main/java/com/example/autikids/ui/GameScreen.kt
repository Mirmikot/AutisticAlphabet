package com.example.autikids.ui

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autikids.R
import com.example.autikids.data.ItemsRepository
import java.util.Locale
import kotlin.random.Random

@Composable
fun GameScreen() {
	val context = LocalContext.current
	val items = remember { ItemsRepository.load(context) }
	var currentIndex by remember { mutableStateOf(0) }
	var isCorrect by remember { mutableStateOf(false) }

	val currentItem = items[currentIndex % items.size]
	val targetWord = currentItem.word.uppercase()

	val letters = remember(targetWord) { targetWord.toCharArray().toMutableList() }
	val shuffled = remember(targetWord) { letters.shuffled(Random(System.currentTimeMillis())) }
	val usedIndices = remember(targetWord) { mutableStateListOf<Int>() }
	var assembled by remember(targetWord) { mutableStateOf("") }

	// TextToSpeech setup
	val tts = remember {
		TextToSpeech(context) {}
	}
	LaunchedEffect(Unit) {
		tts.language = Locale("ru")
	}
	DisposableEffect(Unit) {
		onDispose { tts.shutdown() }
	}

	LaunchedEffect(targetWord) {
		isCorrect = false
		assembled = ""
		usedIndices.clear()
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 16.dp, vertical = 16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f)
				.clip(RoundedCornerShape(16.dp))
		) {
			Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				Image(
					painter = painterResource(id = currentItem.imageRes),
					contentDescription = currentItem.word,
					modifier = Modifier.fillMaxSize(),
					contentScale = ContentScale.Crop
				)
			}
		}

		Spacer(Modifier.height(12.dp))

		// Assembled word area
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight()
				.clip(RoundedCornerShape(12.dp))
				.background(
					if (isCorrect) Color(0xFF2E7D32) else MaterialTheme.colorScheme.surfaceVariant
				)
				.padding(12.dp)
		) {
			Text(
				text = if (assembled.isEmpty()) "" else assembled,
				modifier = Modifier.fillMaxWidth(),
				fontSize = 32.sp,
				fontWeight = FontWeight.Bold,
				textAlign = TextAlign.Center,
				color = if (isCorrect) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
			)
		}

		AnimatedVisibility(visible = isCorrect) {
			Text(
				text = stringResource(id = R.string.congrats),
				modifier = Modifier.padding(top = 8.dp),
				fontSize = 24.sp,
				fontWeight = FontWeight.SemiBold,
				color = Color(0xFF2E7D32)
			)
		}

		Spacer(Modifier.height(8.dp))

		// Letters row(s)
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			shuffled.forEachIndexed { index, ch ->
				val disabled = usedIndices.contains(index)
				LetterButton(
					text = ch.toString(),
					enabled = !disabled && !isCorrect,
					onClick = {
						usedIndices.add(index)
						assembled += ch
						if (assembled.equals(targetWord, ignoreCase = true)) {
							isCorrect = true
						}
					}
				)
			}
		}

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 12.dp),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			Button(
				modifier = Modifier
					.weight(1f)
					.height(64.dp),
				onClick = {
					assembled = ""
					usedIndices.clear()
					isCorrect = false
				},
				colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5))
			) {
				Text(stringResource(id = R.string.clear), fontSize = 20.sp)
			}

			Button(
				modifier = Modifier
					.weight(1f)
					.height(64.dp),
				enabled = true,
				onClick = {
					tts.speak(currentItem.word, TextToSpeech.QUEUE_FLUSH, null, "speak")
				},
				colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
			) {
				Text(stringResource(id = R.string.speak), fontSize = 20.sp)
			}

			Button(
				modifier = Modifier
					.weight(1f)
					.height(64.dp),
				enabled = isCorrect,
				onClick = { currentIndex += 1 },
				colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2), disabledContainerColor = Color(0xFF90CAF9))
			) {
				Text(stringResource(id = R.string.next), fontSize = 20.sp)
			}
		}
	}
}

@Composable
private fun LetterButton(text: String, enabled: Boolean, onClick: () -> Unit) {
	Button(
		modifier = Modifier
			.size(width = 64.dp, height = 64.dp),
		enabled = enabled,
		onClick = onClick,
		colors = ButtonDefaults.buttonColors(
			containerColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
			contentColor = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
		)
	) {
		Text(text = text, fontSize = 24.sp, fontWeight = FontWeight.Bold)
	}
}
