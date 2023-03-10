package io.github.linhelurking.snbt.parser

import io.github.linhelurking.snbt.tag.TagId

fun isSimpleChar(c: Char): Boolean {
    return c.isLetterOrDigit() || c == '.' || c == '_' || c == '-' || c == '+' || c == '∞'
}

fun getNumberType(s: String): TagId {
    if (s.isEmpty()) {
        return TagId.STRING
    }

    val last = s.last().lowercaseChar()

    if (last.isDigit()) {
        if (s.toIntOrNull() != null) {
            return TagId.INT
        } else if (s.toFloatOrNull() != null) {
            return TagId.FLOAT
        }
    }

    val start = s.substring(0, s.length - 1)
    return if (last == 'b' && start.toByteOrNull() != null) {
        TagId.BYTE
    } else if (last == 's' && start.toShortOrNull() != null) {
        TagId.SHORT
    } else if (last == 'l' && start.toLongOrNull() != null) {
        TagId.LONG
    } else if (last == 'f' && start.toFloatOrNull() != null) {
        TagId.FLOAT
    } else if (last == 'd' && start.toDoubleOrNull() != null) {
        TagId.DOUBLE
    } else {
        TagId.STRING
    }
}

inline fun <reified T> parseNumber(s: String): T {
    val slice = if (s.last().isDigit()) {
        s
    } else {
        s.substring(0, s.length - 1)
    }
    return when (T::class) {
        Byte::class -> slice.toByte() as T
        Short::class -> slice.toShort() as T
        Int::class -> slice.toInt() as T
        Long::class -> slice.toLong() as T
        Float::class -> slice.toFloat() as T
        Double::class -> slice.toDouble() as T
        else -> throw ParseException("Cannot parse type ${T::class.qualifiedName} as number!")
    }
}