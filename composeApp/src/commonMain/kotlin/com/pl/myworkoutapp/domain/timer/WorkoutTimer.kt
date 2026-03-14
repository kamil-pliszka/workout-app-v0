package com.pl.myworkoutapp.domain.timer

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WorkoutTimer {

    fun start(seconds: Int): Flow<Int> = flow {

        for (i in seconds downTo 0) {

            emit(i)

            delay(1000)
        }
    }
}
