package online.ruin_of_future.snbt.tag

enum class TagId {
    END, BYTE, SHORT, INT, LONG, FLOAT,
    DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY,
}

class Tag(val id: TagId, var value: Any?) {

    companion object {
        inline fun <reified T> valueOf(v: T): Tag {
            return when (v) {
                is Boolean -> {
                    val b = if (v) {
                        1
                    } else {
                        0
                    }
                    Tag(TagId.BYTE, b.toByte())
                }

                is Byte -> Tag(TagId.BYTE, v)
                is Short -> Tag(TagId.SHORT, v)
                is Int -> Tag(TagId.INT, v)
                is Long -> Tag(TagId.LONG, v)
                is Float -> Tag(TagId.FLOAT, v)
                is Double -> Tag(TagId.DOUBLE, v)
                is ByteArray -> Tag(TagId.BYTE_ARRAY, v)
                is String -> Tag(TagId.STRING, v)
                is List<*> -> Tag(TagId.LIST, v)
                is Tag -> Tag(TagId.COMPOUND, v)
                is IntArray -> Tag(TagId.INT_ARRAY, v)
                is LongArray -> Tag(TagId.LONG_ARRAY, v)
                is Map<*, *> -> Tag(TagId.COMPOUND, v)
                else -> throw Exception("Not support nbt type")
            }
        }

        val END_INSTANCE = Tag(TagId.END, null)
    }

    fun isNumeric(): Boolean {
        return id in arrayOf(TagId.BYTE, TagId.INT, TagId.LONG)
    }

    override fun toString(): String {
        return "Tag(id = $id, value = $value)"
    }

    operator fun get(key: String): Tag? {
        if (id != TagId.COMPOUND) {
            throw IllegalStateException("Only compound tag supports get by key!")
        }
        val map = value as Map<*, *>
        return (map[key] as Tag?)
    }
}

