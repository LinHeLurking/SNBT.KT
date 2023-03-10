package io.github.linhelurking.snbt.tag

enum class TagId {
    END, BYTE, SHORT, INT, LONG, FLOAT,
    DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY,
}

class SnbtTag(val id: TagId, var value: Any?) {

    companion object {
        inline fun <reified T> valueOf(v: T): SnbtTag {
            return when (v) {
                is Boolean -> {
                    val b = if (v) {
                        1
                    } else {
                        0
                    }
                    SnbtTag(TagId.BYTE, b.toByte())
                }

                is Byte -> SnbtTag(TagId.BYTE, v)
                is Short -> SnbtTag(TagId.SHORT, v)
                is Int -> SnbtTag(TagId.INT, v)
                is Long -> SnbtTag(TagId.LONG, v)
                is Float -> SnbtTag(TagId.FLOAT, v)
                is Double -> SnbtTag(TagId.DOUBLE, v)
                is ByteArray -> SnbtTag(TagId.BYTE_ARRAY, v)
                is String -> SnbtTag(TagId.STRING, v)
                is List<*> -> SnbtTag(TagId.LIST, v)
                is IntArray -> SnbtTag(TagId.INT_ARRAY, v)
                is LongArray -> SnbtTag(TagId.LONG_ARRAY, v)
                is Map<*, *> -> SnbtTag(TagId.COMPOUND, v)
                else -> throw Exception("Not support nbt type")
            }
        }

        val END_INSTANCE = SnbtTag(TagId.END, null)
    }

    fun isNumeric(): Boolean {
        return id in arrayOf(TagId.BYTE, TagId.INT, TagId.LONG)
    }

    override fun toString(): String {
        return "Tag(id = $id, value = $value)"
    }

    operator fun get(key: String): SnbtTag? {
        if (id != TagId.COMPOUND) {
            throw IllegalStateException("Only compound tag supports get by key!")
        }
        val map = value as Map<*, *>
        return (map[key] as SnbtTag?)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SnbtTag

        if (id != other.id) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }

    fun toByteOrNull(): Byte? {
        return if (id == TagId.BYTE) {
            value as Byte
        } else {
            null
        }
    }

    fun toByte(): Byte {
        return toByteOrNull() ?: throw TagConvertException(id, Byte::class)
    }

    fun toShortOrNull(): Short? {
        return if (id == TagId.SHORT) {
            value as Short
        } else {
            null
        }
    }

    fun toShort(): Short {
        return toShortOrNull() ?: throw TagConvertException(id, Short::class)
    }

    fun toIntOrNull(): Int? {
        return if (id == TagId.INT) {
            value as Int
        } else {
            null
        }
    }


    fun toInt(): Int {
        return toIntOrNull() ?: throw TagConvertException(id, Int::class)
    }

    fun toLongOrNull(): Long? {
        return if (id == TagId.LONG) {
            value as Long
        } else {
            null
        }
    }

    fun toLong(): Long {
        return toLongOrNull() ?: throw TagConvertException(id, Long::class)
    }

    fun toFloatOrNull(): Float? {
        return if (id == TagId.FLOAT) {
            value as Float
        } else {
            null
        }
    }

    fun toFloat(): Float {
        return toFloatOrNull() ?: throw TagConvertException(id, Float::class)
    }

    fun toDoubleOrNull(): Double? {
        return if (id == TagId.DOUBLE) {
            value as Double
        } else {
            null
        }
    }

    fun toDouble(): Double {
        return toDoubleOrNull() ?: throw TagConvertException(id, Double::class)
    }

    fun toByteArrayOrNull(): ByteArray? {
        return if (id == TagId.BYTE_ARRAY) {
            value as ByteArray
        } else {
            null
        }
    }

    fun toByteArray(): ByteArray {
        return toByteArrayOrNull() ?: throw TagConvertException(id, ByteArray::class)
    }

    fun toIntArrayOrNull(): IntArray? {
        return if (id == TagId.INT_ARRAY) {
            value as IntArray
        } else {
            null
        }
    }

    fun toIntArray(): IntArray {
        return toIntArrayOrNull() ?: throw TagConvertException(id, IntArray::class)
    }

    fun toLongArrayOrNull(): LongArray? {
        return if (id == TagId.LONG_ARRAY) {
            value as LongArray
        } else {
            null
        }
    }

    fun toLongArray(): LongArray {
        return toLongArrayOrNull() ?: throw TagConvertException(id, LongArray::class)
    }

    fun toListOrNull(): List<SnbtTag>? {
        return if (id == TagId.LIST) {
            value as List<SnbtTag>
        } else {
            null
        }
    }

    fun toList(): List<SnbtTag> {
        return toListOrNull() ?: throw TagConvertException(id, List::class)
    }

    fun toMapOrNull(): Map<String, SnbtTag>? {
        return if (id == TagId.COMPOUND) {
            value as Map<String, SnbtTag>
        } else {
            null
        }
    }

    fun toMap(): Map<String, SnbtTag> {
        return toMapOrNull() ?: throw TagConvertException(id, Map::class)
    }

    fun recursiveUnwrap(): Map<String, Any?> {
        val map = toMap().toMutableMap() as MutableMap<String, Any?>
        map.replaceAll { _, v ->
            v as SnbtTag
            return@replaceAll if (v.id == TagId.COMPOUND) {
                v.recursiveUnwrap()
            } else {
                v.value
            }
        }
        return map
    }
}

