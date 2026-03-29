package com.pl.myworkoutapp.domain

interface StorageSupport {
    suspend fun logStorage()
    suspend fun cleanupTemporary()
    suspend fun copyTmpToFinal(fromPath: String, toFilename: String): String
    //suspend fun saveProfileImage(input: InputStream): String
    suspend fun exists(path: String): Boolean
}