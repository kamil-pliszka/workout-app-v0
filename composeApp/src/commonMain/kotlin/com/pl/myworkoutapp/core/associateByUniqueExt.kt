package com.pl.myworkoutapp.core


//przekształca do mapy, rzuca wyjątkiem gdy jest zduplikowany klucz
inline fun <T, K> Iterable<T>.safeCollectToMap(crossinline keySelector: (T) -> K): Map<K, T> =
    this.groupingBy(keySelector).reduce { key, accumulator, element ->
        error("Duplicate key '$key' encountered for elements:\n$element\n$accumulator")
    }


inline fun <T, K> Iterable<T>.associateByUnique(
    keySelector: (T) -> K
): Map<K, T> {
    val result = LinkedHashMap<K, T>()

    for (element in this) {
        val key = keySelector(element)

        require(key !in result) {
            "Duplicate key '$key' encountered for element: $element and ${result[key]}"
        }

        result[key] = element
    }

    return result
}