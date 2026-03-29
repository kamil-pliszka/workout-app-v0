package com.pl.myworkoutapp.core

import kotlin.jvm.JvmInline

//@JvmInline
//value class ContentBytes(val bytes: ByteArray)

class ContentBytes(val bytes: ByteArray) {
    override fun toString(): String = "ContentBytes with size: " + bytes.size
}