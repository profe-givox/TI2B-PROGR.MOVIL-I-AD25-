package net.ivanvega.archivosmultimediaconcompose

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import net.ivanvega.archivosmultimediaconcompose.receivers.AlarmaReceiver
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AlarmasScreen( alarmScheduler: AlarmScheduler){

    val recordAudioPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    var secondText by remember {
        mutableStateOf("")
    }
    var messageText by remember {
        mutableStateOf("")
    }
    var alarmItem : AlarmItem? = null
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = secondText, onValueChange = {
            secondText = it
        },
            label = {
                Text(text = "Delay Second")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = messageText, onValueChange = {
            messageText = it
        },
            label = {
                Text(text = "Message")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                /*alarmItem =
                   AlarmItem(
                       alarmTime = LocalDateTime.now().plusSeconds(
                           secondText.toLong()
                       ),
                       message = messageText
                   )*/
                alarmItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AlarmItem(
                        LocalDateTime.now().plusSeconds(secondText.toLong()),
                        "El mensaje"
                    )
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                alarmItem?.let(alarmScheduler::schedule)
                secondText = ""
                messageText = ""
            }) {
                Text(text = "Schedule")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                alarmItem?.let(alarmScheduler::cancel)
            }) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

data class AlarmItem(
    val alarmTime : LocalDateTime,
    val message : String
)

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}

class ProgramarAlarma (
    val ctx : Context
) : AlarmScheduler {
    private val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(alarmItem: AlarmItem) {

        val alarmIntent = Intent(ctx, AlarmaReceiver::class.java).let { intent ->
            intent.putExtra("EXTRA_MESSAGE", alarmItem.message)
            PendingIntent.getBroadcast(ctx, 1001, intent,  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

            alarmManager?.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 5 * 1000,
            alarmIntent
        )
        Log.d("PMII", "Quedo progrmada")
    }

    override fun cancel(alarmItem: AlarmItem) {
        TODO("Not yet implemented")
    }
}