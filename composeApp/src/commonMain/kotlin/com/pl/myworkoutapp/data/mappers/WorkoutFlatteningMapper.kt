package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.FlatWorkoutItem
import com.pl.myworkoutapp.domain.model.workout.Circuit
import com.pl.myworkoutapp.domain.model.workout.WorkoutItem

class WorkoutFlatteningMapper {

    fun flatten(items: List<WorkoutItem>): List<FlatWorkoutItem> {
        val result = mutableListOf<FlatWorkoutItem>()
        flatten(items, result, parentIndex = null)
        return result
    }

    private fun flatten(
        items: List<WorkoutItem>,
        result: MutableList<FlatWorkoutItem>,
        parentIndex: Int?
    ) {
        items.forEachIndexed { index, item ->

            val currentIndex = result.size

            result += FlatWorkoutItem(
                itemEntity = item.toEntity(),
                parentIndex = parentIndex,
                position = index
            )

            if (item is Circuit) {
                flatten(
                    item.items,
                    result,
                    currentIndex
                )
            }
        }
    }
}