package com.pl.myworkoutapp.ui.execution.logic

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun countdown(seconds: Int): Flow<Int> = flow {
    for (i in seconds downTo 0) {
        emit(i)
        delay(1000)
    }
}
