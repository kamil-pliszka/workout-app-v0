package com.pl.myworkoutapp.core

//@JvmInline
//value class ContentBytes(val bytes: ByteArray)

class ContentBytes(val bytes: ByteArray) {
    override fun toString(): String = "ContentBytes with size: " + bytes.size
}