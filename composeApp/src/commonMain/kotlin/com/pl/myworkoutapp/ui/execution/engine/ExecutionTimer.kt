package com.pl.myworkoutapp.ui.execution.engine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

/**
 * ExecutionTimer
 * Źródło ticków czasowych.
 * Odpowiada za:
 * emit co sekundę
 * Nie powinien:
 * znać state workoutu
 * wykonywać transition
 * mieć logiki biznesowej
 */
class ExecutionTimer {
    fun ticker(): Flow<Unit> = flow {
        while (true) {
            delay(1000.milliseconds)
            emit(Unit)
        }
    }
}