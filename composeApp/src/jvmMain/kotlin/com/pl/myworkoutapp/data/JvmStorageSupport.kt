package com.pl.myworkoutapp.data

import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.domain.StorageSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class JvmStorageSupport : StorageSupport {
    companion object {
        private val TAG = object {}.javaClass.enclosingClass?.simpleName
    }

    override suspend fun exists(path: String): Boolean = withContext(Dispatchers.IO) {
        File(path).exists()
    }

    override suspend fun logStorage(): Unit = withContext(Dispatchers.IO) {
        val appDir = getAppDataDir()

        appDir.listFiles()?.forEach { file ->
            Log.d(
                TAG,
                "File[appDir]: ${file.name}, size: ${file.length()}, exists: ${file.exists()}"
            )
        }
    }

    override suspend fun cleanupTemporary() = withContext(Dispatchers.IO) {
    }

    /*override suspend fun saveProfileImage(input: InputStream): String = withContext(Dispatchers.IO) {
        val file = File(context.cacheDir, "profile_tmp_gallery.jpg")
        input.use { it.copyTo(file.outputStream()) }
        file.absolutePath
    }*/

    override suspend fun copyTmpToFinal(fromPath: String, toFilename: String): String = withContext(Dispatchers.IO) {
        val appDataDir = getAppDataDir()
        if(!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val finalFile = File(appDataDir, toFilename)
        val tmpFile = File(fromPath)

        tmpFile.copyTo(finalFile, overwrite = true)

        Log.d(TAG, "Copied tmp file to: ${finalFile.absolutePath}")

        finalFile.absolutePath
    }

}