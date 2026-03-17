package com.pl.myworkoutapp.domain.model.user

data class Settings (
    //kod języka(null - default)
    val langTag: String? = null,
    //czas odstepu miedzy ćwiczeniami (w sekundach)
    val exerciseRest: Int = 30,
    //czas odstepu miedzy setami/obwodami (w sekundach)
    val circuitRest: Int = 60,
)