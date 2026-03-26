package com.pl.myworkoutapp.data.database

import androidx.room.TypeConverter
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.user.Gender
import kotlinx.serialization.json.Json
import kotlin.time.Instant

object DbTypeConverters {

    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.decodeFromString(value)
    }
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromString2Ints(value: String): List<Int> {
        return Json.decodeFromString(value)
    }
    @TypeConverter
    fun fromListInts(list: List<Int>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }
    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun fromGender(unit: Gender?): String? =
        unit?.name
    @TypeConverter
    fun toGender(value: String?): Gender? = value?.let { Gender.valueOf(it) }

//    @TypeConverter
//    fun fromCategory(value: Category?): String? = value?.name
//    @TypeConverter
//    fun toCategory(value: String?): Category? = value?.let { Category.valueOf(it) }

    @TypeConverter
    fun fromDifficulty(value: Difficulty?): String? = value?.name
    @TypeConverter
    fun toDifficulty(value: String?): Difficulty? = value?.let { Difficulty.valueOf(it) }

}