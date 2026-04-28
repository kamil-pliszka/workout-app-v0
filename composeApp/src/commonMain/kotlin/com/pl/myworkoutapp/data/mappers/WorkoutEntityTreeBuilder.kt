package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.CustomWorkoutItemEntity
import com.pl.myworkoutapp.domain.model.workout.*

class WorkoutEntityTreeBuilder {

    fun build(flatItems: List<CustomWorkoutItemEntity>): List<WorkoutItem> {
        val id2Children = mutableMapOf<Long, MutableList<Long>>()
        val id2Item = mutableMapOf<Long, WorkoutItem>()

        flatItems.forEach { entity ->
            id2Item[entity.id] = entity.toDomain()
            id2Children[entity.id] = mutableListOf()
        }

        val roots = mutableListOf<Long>()

        flatItems.forEach { entity ->
            if (entity.parentId != null) {
                id2Children.getValue(entity.parentId).add(entity.id)
            } else {
                roots.add(entity.id)
            }
        }

        fun buildNode(id: Long): WorkoutItem {
            val item = id2Item.getValue(id)
            val children = id2Children.getValue(id)

            return when (item) {
                is WorkoutExercise -> {
                    require(children.isEmpty())
                    item
                }
                is Circuit -> {
                    item.copy(
                        items = children.map { buildNode(it) }
                    )
                }
            }
        }

        return roots.map { buildNode(it) }
    }
}