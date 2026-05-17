package com.pl.myworkoutapp.ui.execution

//klasa chwilowo zachowana ze względu na komentarze
//jak zostanie to uwzględnione , wtedy zostanie usunięta
/*
sealed interface WorkoutExecutionState {
    object Loading : WorkoutExecutionState
    data class RestartContinue(
        //wyświetlany na początku w przypadku gdy user wcześniej wykonał jakieś ćwiczenia,
        // jeśli to "czyste" rozpoczęcie workoutu wtedy od razu stan Ready
        //akcje: Restart / Continue
        val progress: Float
    ) : WorkoutExecutionState

    data class Ready(
        //stan przed pierwszym ćwiczeniem, domyślnie 10 sek na zebranie się w sobie i wyświetlenie info o nast. ćwiczeniu
        //akcje: skip(go to exercise) / pause
        val remainingSeconds: Int = 10,
        val nextExercise: WorkoutExercise
    ) : WorkoutExecutionState

    data class RunningExercise(
        //wykonywanie ćwiczenia
        // rózne akcje w zależności od typu ćwiczenia
        // akcje:
        //  -prev?/next
        //  -finish(REP, REP_PER_SIDE,DISTANCE)
        //  -show exe info
        //  -pause(DURATION)
        val currentExercise: WorkoutExercise,
        val remainingSeconds: Int//TODO - to zależy od rodzaju ćwiczenia
    ) : WorkoutExecutionState

    data class RunningCircuit(
        //TODO - tu na razie nie mam pomysłu
        val currentCircuit: Any,
    )

    data class Rest(
        //przerwa przed kolejnym ćwiczeniem
        //akcje:
        // +20sek przerwy
        // skip(rest)
        val remainingSeconds: Int,
        val nextExercise: Any,
    ) : WorkoutExecutionState

    data class Paused(
        //zatrzymane wykonywanie ćwiczenia, wyśweitlamy małą ikonę ćwiczenia
        //akcje: resume, restart(exercise)
        val currentExercise: Any,
    ) : WorkoutExecutionState

    data class Finished(
        //po zakończeniu workouta
        //akcje : NavToHistory
        val totalTime: Int, //z przerwami
        val execTime: Int, //czas ćwiczeń
        val estiatedkcal: Int, //jakieś szacunki
    ) : WorkoutExecutionState
}
*/