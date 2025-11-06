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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import net.ivanvega.archivosmultimediaconcompose.providers.MiFileProviderMedia
import net.ivanvega.archivosmultimediaconcompose.ui.theme.ArchivosMultimediaConComposeTheme
import kotlin.contracts.contract

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArchivosMultimediaConComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    pickphosAndImage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
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

    Column(modifier = modifier) {
        Button(onClick = {
            uri = MiFileProviderMedia.getImageUri(ctx)
            camara.launch(uri)


        }){
            Text(text = stringResource(id=R.string.pick_image_button))
        }
        AsyncImage(model = imageUri,
            contentDescription = null, modifier = modifier)

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