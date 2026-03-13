package com.pl.myworkoutapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform