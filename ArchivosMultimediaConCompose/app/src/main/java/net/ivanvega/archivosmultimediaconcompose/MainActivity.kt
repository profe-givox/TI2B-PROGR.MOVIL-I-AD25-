package net.ivanvega.archivosmultimediaconcompose

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import net.ivanvega.archivosmultimediaconcompose.providers.MiFileProviderMedia
import net.ivanvega.archivosmultimediaconcompose.ui.theme.ArchivosMultimediaConComposeTheme
import java.io.File
import kotlin.contracts.contract

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArchivosMultimediaConComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //pickphosAndImage(modifier = Modifier.padding(innerPadding))
                    // pickphosAndImage(modifier = Modifier.padding(innerPadding))
                    /*GrabarAudioScreen (
                        {
                            File(cacheDir, "audio.mp3").also {
                                recorder.start(it)
                                audioFile = it
                            }

                        } ,
                        {
                            recorder.stop()
                        },
                        {
                            audioFile?.let { player.start(it) }
                        },{
                            audioFile?.let { player.stop() }
                        })
                }*/

                    AlarmasScreen(ProgramarAlarma(applicationContext))
                }
            }
        }
    }
}
@Composable
fun VideoPlayer(videoUri: Uri, modifier: Modifier = Modifier.fillMaxWidth()) {
    val context = LocalContext.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
        }
    }
    val playbackState = exoPlayer
    val isPlaying = playbackState?.isPlaying ?: false

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = modifier
    )

    IconButton(
        onClick = {
            if (isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
        },
        modifier = Modifier
            //.align(Alignment.BottomEnd)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Refresh else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun pickphosAndImage (modifier: Modifier = Modifier){

    // 1
    var hasImage by remember {
        mutableStateOf(false)
    }
    var hasVideo by remember {
        mutableStateOf(false)
    }
    // 2
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }


    var uri : Uri? = null
    val ctx = LocalContext.current

    val camara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Log.d("FOTOX", "Imagen capturada con exito")
            Log.d("IMG", hasImage.toString())
            Log.d("URI", imageUri.toString())
            if(success) imageUri = uri
            hasImage = success
        }
    }

    val camaraVideo = rememberLauncherForActivityResult(
        ActivityResultContracts.CaptureVideo()) { success ->
        if (success) {
            Log.d("VIDEOX", "Video capturado con exito")
            Log.d("VIDEO", hasVideo.toString())
            Log.d("URI", imageUri.toString())
            if(success) imageUri = uri
            hasVideo = success
        }
    }

    Column(modifier = modifier) {
        Button(onClick = {
            uri = MiFileProviderMedia.getImageUri(ctx)
            camara.launch(uri!!)
        }){
            Text(text = stringResource(id=R.string.pick_image_button))
        }
        Button(onClick = {
            uri = MiFileProviderMedia.getImageUri(ctx)
            camaraVideo.launch(uri)
        }) {
            Text(text = stringResource(R.string.capturar_video))
        }

        AsyncImage(model = imageUri,
            contentDescription = null, modifier = modifier)

            if (hasVideo) {
                VideoPlayer(videoUri = imageUri!!)
            }

    }

}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArchivosMultimediaConComposeTheme {
        Greeting("Android")
    }
}