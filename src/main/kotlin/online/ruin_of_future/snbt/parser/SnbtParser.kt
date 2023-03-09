/*
* Main part of this parser is copied from
* https://github.com/FTBTeam/FTB-Library/blob/1.19/main/common/src/main/java/dev/ftb/mods/ftblibrary/snbt/SNBTParser.java
* */

package online.ruin_of_future.snbt.parser

import online.ruin_of_future.snbt.tag.Tag
import online.ruin_of_future.snbt.tag.TagId
import java.io.File
import java.io.FileNotFoundException

class SnbtParser(lines: Iterable<String>) {
    private var pos = 0
    private var buf = CharArray(1)

    init {
        val sb = StringBuilder()
        lines
            .map {
                it.trim()
            }.forEach {
                if (!it.startsWith("//") && !it.startsWith("#") && it.isNotBlank()) {
                    sb.append(it)
                }
                sb.append('\n')
            }
        buf = sb.toString().toCharArray()
        pos = 0
    }

    companion object {
        fun fromFile(path: String): SnbtParser {
            val file = File(path)
            if (!file.exists()) {
                throw FileNotFoundException(path)
            }
            return SnbtParser(file.readLines())
        }
    }

    /*
    * Read all contents as a CompoundTag
    * */
    fun read(): Tag {
        return readTag(nextNonSpace())
    }

    private fun next(): Char {
        if (pos >= buf.size) {
            throw ParseException("Wrongly get eod of file!")
        }
        return buf[pos++]
    }

    private fun nextNonSpace(): Char {
        while (true) {
            val c = next()
            if (c != ' ' && c != '\n' && c != '\t') {
                return c
            }
        }
    }

    private fun posString(p: Int): String {
        if (p >= buf.size) {
            return "EOF"
        }
        var col = 1
        var row = 1
        for (i in 0 until p) {
            if (buf[p] == '\n') {
                row++
                col = 0
            } else {
                col++
            }
        }
        return "Line $row, column $col"
    }

    private fun readQuotedString(stop: Char): String {
        val sb = StringBuilder()
        var escape = false
        while (true) {
            val c = next()

            if (escape) {
                escape = false
                sb.append("\\$c")
                continue
            }

            when (c) {
                '\n' -> throw ParseException("${posString(pos - 1)}: New line without closing string with $stop")
                '\\' -> escape = true
                stop -> return sb.toString()
                else -> sb.append(c)
            }
        }
    }

    private fun readUnQuotedString(first: Char): String {
        val sb = StringBuilder()
        sb.append(first)
        while (true) {
            val c = next()
            if (isSimpleChar(c)) {
                sb.append(c)
            } else {
                pos--
                return sb.toString()
            }
        }
    }

    private fun readTag(first: Char): Tag {
        when (first) {
            '{' -> return readCompound()
            '[' -> return readCollection()
            '"' -> return Tag.valueOf(readQuotedString('"'))
            '\'' -> return Tag.valueOf(readQuotedString('\''))
        }

        return when (val s = readUnQuotedString(first)) {
            "true" -> SpecialTags.TRUE
            "false" -> SpecialTags.FALSE
            "null", "end", "END" -> Tag.END_INSTANCE
            "Infinity", "Infinityd", "+Infinity", "+Infinityd", "∞", "∞d", "+∞", "+∞d" -> SpecialTags.POS_INFINITY_D
            "-Infinity", "-Infinityd", "-∞", "-∞d" -> SpecialTags.NEG_INFINITY_D
            "Infinityf", "+Infinityf", "∞f", "+∞f" -> SpecialTags.POS_INFINITY_F
            "-Infinityf", "-∞f" -> SpecialTags.NEG_INFINITY_F
            "NaN", "NaNd" -> SpecialTags.NAN_D;
            "NaNf" -> SpecialTags.NAN_F
            else -> when (getNumberType(s)) {
                TagId.BYTE -> Tag.valueOf(parseNumber<Byte>(s))
                TagId.SHORT -> Tag.valueOf(parseNumber<Short>(s))
                TagId.INT -> Tag.valueOf(parseNumber<Int>(s))
                TagId.LONG -> Tag.valueOf(parseNumber<Long>(s))
                TagId.FLOAT -> Tag.valueOf(parseNumber<Float>(s))
                TagId.DOUBLE -> Tag.valueOf(parseNumber<Double>(s))
                else -> Tag.valueOf(s)
            }
        }
    }

    private fun readArray(p: Int, type: Char): Tag {
        val supportedTypes = arrayOf('b', 'B', 'i', 'I', 'l', 'L')
        if (type !in supportedTypes) {
            throw ParseException("${posString(p)}: illegal type specifier when parsing numeric array")
        }

        val bytes = arrayListOf<Byte>()
        val ints = arrayListOf<Int>()
        val longs = arrayListOf<Long>()
        while (true) {
            val c = nextNonSpace()

            if (c == ']') {
                when (type) {
                    'b', 'B' -> return Tag.valueOf(bytes.toByteArray())
                    'i', 'I' -> return Tag.valueOf(ints.toIntArray())
                    'l', 'L' -> return Tag.valueOf(longs.toLongArray())
                }
            } else if (c == ',') {
                continue
            }

            val t = readTag(c)

            if (t.isNumeric()) {
                when (type) {
                    'b', 'B' -> bytes.add(t.value as Byte)
                    'i', 'I' -> ints.add(t.value as Int)
                    'l', 'L' -> longs.add(t.value as Long)
                }
            } else {
                throw ParseException("${posString(p)}: expected a number but got wrong contents")
            }
        }
    }

    private fun readList(): Tag {
        val list = mutableListOf<Tag>()
        while (true) {
            val c = nextNonSpace()

            if (c == ']') {
                return Tag.valueOf(list)
            } else if (c == ',') {
                continue
            }

            val t = readTag(c)
            list.add(t)
        }
    }

    private fun readCollection(): Tag {
        val prevPos = pos
        val next1 = nextNonSpace()
        val next2 = nextNonSpace()

        return if (next2 == ';' && (next1 in arrayOf('b', 'B', 'i', 'I', 'l', 'L'))) {
            readArray(prevPos, next1)
        } else {
            pos = prevPos
            readList()
        }
    }

    private fun readCompound(): Tag {
        val map = mutableMapOf<String, Tag>()
        while (true) {
            val c = nextNonSpace()

            if (c == '}') {
                return Tag.valueOf(map)
            } else if (c == ',' || c == '\n') {
                continue
            }

            val key = if (c == '"' || c == '\'') {
                readQuotedString(c)
            } else {
                readUnQuotedString(c)
            }

            val n = nextNonSpace()
            if (n == ':' || n == '=') {
                val t = readTag(nextNonSpace())
                map[key] = t
            } else {
                throw ParseException("${posString(pos)}: expected ':', got '$n'")
            }
        }
    }
}