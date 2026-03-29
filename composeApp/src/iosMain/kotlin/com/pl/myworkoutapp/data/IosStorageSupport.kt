@file:OptIn(ExperimentalForeignApi::class)

package com.pl.myworkoutapp.data

import com.pl.myworkoutapp.domain.StorageSupport
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import kotlin.collections.emptyList


class IosStorageSupport : StorageSupport {

    companion object {
        private val TAG = "IosStorageSupport"
    }

    private fun getDocumentsDir(): String {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )
        return paths.first() as String
    }

    private fun getCachesDir(): String {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true
        )
        return paths.first() as String
    }

    override suspend fun exists(path: String): Boolean = withContext(Dispatchers.IO) {
        NSFileManager.defaultManager.fileExistsAtPath(path)
    }

    override suspend fun logStorage() = withContext(Dispatchers.IO) {
        val fm = NSFileManager.defaultManager

        fun logDir(dir: String, name: String) {
            val contents = fm.contentsOfDirectoryAtPath(dir, null) ?: emptyList<Any>()

            contents.forEach { fileName ->
                val fullPath = "$dir/$fileName"
                val attributes = fm.attributesOfItemAtPath(fullPath, null)

                val size = (attributes?.get(NSFileSize) as? NSNumber)?.longValue ?: 0L
                val exists = fm.fileExistsAtPath(fullPath)

                println("File[$name]: $fileName, size: $size, exists: $exists")
            }
        }

        logDir(getDocumentsDir(), "Documents")
        logDir(getCachesDir(), "Caches")
    }

    override suspend fun cleanupTemporary() = withContext(Dispatchers.IO) {
        val cacheDir = getCachesDir()
        val fm = NSFileManager.defaultManager

        val contents = fm.contentsOfDirectoryAtPath(cacheDir, null) ?: return@withContext

        contents.forEach { fileName ->
            val fullPath = "$cacheDir/$fileName"
            fm.removeItemAtPath(fullPath, null)
        }
    }

    override suspend fun copyTmpToFinal(fromPath: String, toFilename: String): String =
        withContext(Dispatchers.IO) {

            val documentsDir = getDocumentsDir()
            val finalPath = "$documentsDir/$toFilename"

            val fm = NSFileManager.defaultManager

            val success = fm.copyItemAtPath(fromPath, finalPath, null)

            if (!success) {
                throw IllegalStateException("Failed to copy file from $fromPath to $finalPath")
            }

            println("Copied tmp file to: $finalPath")

            finalPath
        }
}