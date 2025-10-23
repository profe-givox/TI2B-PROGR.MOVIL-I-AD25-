package net.ivanvega.archivosmultimediaconcompose.providers

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import net.ivanvega.archivosmultimediaconcompose.R
import java.io.File

class MiFileProviderMedia : FileProvider(
    R.xml.file_paths
) {
    companion object{
        fun getImageUri(context : Context ): Uri {

            val dirIma = File(context.cacheDir, "images")
            dirIma.mkdirs()

            val fileImage = File.createTempFile(
                "img_",
                ".jpg",
                dirIma
            )

            val authority = context.packageName + ".fileprovider"

            return getUriForFile(context, authority  ,fileImage)
        }

    }

}