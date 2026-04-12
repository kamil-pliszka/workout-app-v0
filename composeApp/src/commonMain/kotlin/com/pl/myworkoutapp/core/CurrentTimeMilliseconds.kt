package com.pl.myworkoutapp.core

import kotlin.time.Clock

fun currentTimeMilliseconds()  = Clock.System.now().toEpochMilliseconds()