package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.WorkoutEntity
import com.pl.myworkoutapp.data.database.WorkoutItemEntity
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.*

fun WorkoutId.BuiltIn.asString() = this.id.name

//fun String.toBuiltInWorkoutId(): WorkoutId.BuiltIn = BuiltInWorkoutId.entries.first { it.name == this }.asWorkoutId()
fun String.toBuiltInWorkoutId(): WorkoutId.BuiltIn = BuiltInWorkoutId.valueOf(this).asWorkoutId()

fun Difficulty.asString() = this.name

fun CustomWorkout.toEntity() = WorkoutEntity(
    id = id.toLong(),
    name = name,
    description = description,
    imageUri = imageUri,
    basedOn = basedOn?.asString(),
    difficulty = difficulty.asString(),
)

fun WorkoutEntity.toDomain() = CustomWorkout(
    id = id.asWorkoutId(),
    name = name,
    description = description,
    imageUri = imageUri,
    basedOn = basedOn?.toBuiltInWorkoutId(),
    difficulty = Difficulty.valueOf(difficulty),
    items = emptyList()
)

fun WorkoutExercise.toEntity() = WorkoutItemEntity(
    id = 0,
    workoutId = 0,
    parentId = null,
    position = 0,
    type = "EXERCISE",
    exerciseId = exerciseId.asString(),
    quantityValue = quantity.value,
    quantityType = quantity.type.name,
    phase = null,
    name = null,
    rounds = null,
    structureType = null,
    structureData = null,
)

fun Circuit.toEntity(): WorkoutItemEntity {
    val (structureType, structureData) = serializeStructure(structure)
    return WorkoutItemEntity(
        id = 0,
        workoutId = 0,
        parentId = null,
        position = 0,
        type = "CIRCUIT",
        exerciseId = null,
        quantityValue = null,
        quantityType = null,
        phase = phase.name,
        name = name,
        rounds = rounds,
        structureType = structureType,
        structureData = structureData,
    )
}

fun WorkoutItem.toEntity(): WorkoutItemEntity = when (this) {
    is Circuit -> this.toEntity()
    is WorkoutExercise -> this.toEntity()
}


fun serializeStructure(structure: CircuitStructure): Pair<String, String?> =
    when (structure) {
        CircuitStructure.Standard -> "STANDARD" to null
        is CircuitStructure.EMOM -> "EMOM" to structure.minutes.toString()
        is CircuitStructure.AMRAP -> "AMRAP" to structure.durationSec.toString()
        is CircuitStructure.Tabata -> "TABATA" to "${structure.workSec},${structure.restSec}"
    }

fun deserializeStructure(type: String, data: String?): CircuitStructure {
    return when (type) {
        "STANDARD" -> CircuitStructure.Standard
        "EMOM" -> {
            val minutes = data?.toIntOrNull() ?: error("Invalid EMOM data: $data")
            CircuitStructure.EMOM(minutes)
        }

        "AMRAP" -> {
            val duration = data?.toIntOrNull() ?: error("Invalid AMRAP data: $data")
            CircuitStructure.AMRAP(duration)
        }

        "TABATA" -> {
            val parts = data?.split(",") ?: error("Missing TABATA data")
            require(parts.size == 2) { "Invalid TABATA format: $data" }
            val work = parts[0].toIntOrNull() ?: error("Invalid TABATA workSec: ${parts[0]}")
            val rest = parts[1].toIntOrNull() ?: error("Invalid TABATA restSec: ${parts[1]}")
            CircuitStructure.Tabata(workSec = work, restSec = rest)
        }

        else -> error("Unknown structureType: $type")
    }
}

fun deserializeStructureNoCrash(type: String, data: String?): CircuitStructure? {
    return runCatching {
        when (type) {
            "STANDARD" -> CircuitStructure.Standard
            "EMOM" -> CircuitStructure.EMOM(data!!.toInt())
            "AMRAP" -> CircuitStructure.AMRAP(data!!.toInt())
            "TABATA" -> {
                val (w, r) = data!!.split(",")
                CircuitStructure.Tabata(w.toInt(), r.toInt())
            }

            else -> return null
        }
    }.getOrNull()
}

fun WorkoutItemEntity.toDomain(): WorkoutItem = when(type) {
    "EXERCISE" -> WorkoutExercise(
        exerciseId = this.exerciseId?.toExerciseIdOrNull()!!,
        quantity = Quantity(
            type = QuantityType.valueOf(quantityType!!),
            value = quantityValue!!
        )
    )

    "CIRCUIT" -> Circuit(
        phase = Phase.valueOf(this.phase!!),
        name = this.name,
        rounds = rounds!!,
        structure = deserializeStructure(structureType!!, structureData),
        items = emptyList()
    )

    else -> error("incorrect type: $type")
}

/*
fun toDomain(workoutEntity: WorkoutEntity, flatSortedItems : List<WorkoutItemEntity>) : CustomWorkout {

    val workoutDomain = workoutEntity.toDomain()

    val id2Children = mutableMapOf<Long, MutableList<Long>>()
    val id2Item = mutableMapOf<Long, WorkoutItem>()

    // 1. init
    flatSortedItems.forEach { entity ->
        id2Item[entity.id] = entity.toDomain()
        id2Children[entity.id] = mutableListOf()
    }

    // 2. relacje
    val rootsId = mutableListOf<Long>()
    flatSortedItems.forEach { entity ->
        if (entity.parentId != null) {
            //id2Children.getValue(entity.parentId).add(entity.id)
            val parent = id2Item.getValue(entity.parentId)
            require(parent is Circuit) {
                "Parent must be Circuit (parentId=${entity.parentId}, childId=${entity.id})"
            }
            id2Children.getValue(entity.parentId).add(entity.id)
        } else {
            rootsId.add(entity.id)
        }
    }

    // 3. build (REKURENCYJNIE)
    fun build(id: Long): WorkoutItem = when (val item = id2Item.getValue(id)) {
        is WorkoutExercise -> item
        is Circuit -> {
            val children = id2Children.getValue(id)
                .map { childId -> build(childId) }
            item.copy(items = children)
        }
    }

    val roots = rootsId.map { build(it) }

    val domainWorkout = workoutEntity.toDomain()
    val roots = WorkoutTreeBuilder().build(flatSortedItems)
    return domainWorkout.copy(items = roots)
}
*/


/*
fun flatten(items: List<WorkoutItem>, result: MutableList<FlatWorkoutItem>, parentIndex: Int?) {
    items.forEachIndexed { index, item ->
        val currentIndex = result.size
        result += FlatWorkoutItem(
            itemEntity = item.toEntity(),
            parentIndex = parentIndex,
            position = index // ✔ lokalna pozycja
        )
        if (item is Circuit) {
            flatten(
                item.items,
                result,
                currentIndex // ✔ poprawny parent
            )
        }
    }
}
*/