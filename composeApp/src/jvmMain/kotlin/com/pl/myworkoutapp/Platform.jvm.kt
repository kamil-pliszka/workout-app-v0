package com.pl.myworkoutapp

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override fun isMobile(): Boolean = false
}

actual fun getPlatform(): Platform = JVMPlatform()