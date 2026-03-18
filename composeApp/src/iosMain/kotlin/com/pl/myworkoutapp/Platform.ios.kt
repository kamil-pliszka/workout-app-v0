package com.pl.myworkoutapp

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override fun isMobile(): Boolean = true
}

actual fun getPlatform(): Platform = IOSPlatform()