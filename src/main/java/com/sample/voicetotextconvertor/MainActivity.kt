package com.sample.voicetotextconvertor

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sample.voicetotextconvertor.ui.theme.VoiceToTextConvertorTheme
import java.util.Locale
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.material3.Shapes
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.android.gms.maps.model.Circle

class MainActivity : ComponentActivity() {
    lateinit var startForResult : ActivityResultLauncher<Intent>
    var speakText by mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK && it.data != null){
                val resultData = it.data
                val resultArray = resultData?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                speakText = resultArray?.get(0).toString()
            }
        }
        setContent {
            VoiceToTextConvertorTheme {
               Surface (
                   modifier = Modifier.fillMaxSize()
               ){
                   Image(painter = painterResource(id = R.drawable.rail), contentDescription = null,
                       contentScale =ContentScale.Crop)
                   VoiceToTextApp()
               }
            }
        }
    }
    @Composable
    fun VoiceToTextApp(){
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT," SPEAK NOW,\nI'm Listening...")
                    startForResult.launch(intent)
                },
                    modifier = Modifier
                        .size(150.dp) // Increase the size of the icon
                        .clip(CircleShape) // Clip the icon into a circle shape
                        .background(MaterialTheme.colorScheme.background) // Set background color
                        .border(2.dp, Color.White, CircleShape) )// Add a border around the icon)
                        {
                    Icon(Icons.Rounded.Mic, contentDescription = null,
                        modifier = Modifier
                            .size(100.dp) // Increase the size of the icon

                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                CopyableText(speakText)
            }


        }

    }
}

@Composable
fun CopyableText(speakText: String) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Text(
        text = speakText,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clickable {
                // Copy the text to the clipboard
                clipboardManager.setText(AnnotatedString(speakText))
                // Show a toast message to inform the user
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
    )
}

