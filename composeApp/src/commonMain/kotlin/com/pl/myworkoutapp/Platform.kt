package com.pl.myworkoutapp

interface Platform {
    val name: String
    fun isMobile(): Boolean
}

expect fun getPlatform(): Platform