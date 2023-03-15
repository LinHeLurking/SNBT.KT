package io.github.linhelurking.snbt.tag

import io.github.linhelurking.snbt.parser.SnbtParser
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class SnbtTagTest {

    @Test
    fun unwrap() {
        val stream = this.javaClass.getResourceAsStream("/test1.snbt")
        assert(stream != null)

        val lines =
            BufferedReader(InputStreamReader(stream!!, StandardCharsets.UTF_8)).lines().collect(Collectors.toList())
        val parser = SnbtParser(lines)
        val tag = parser.read()

        assert(tag.unwrap() is Map<*, *>)
        val raw = tag.unwrap() as Map<String, Any?>

        assert(raw["test_string"] == "value")
        assert(raw["test string key"] == "value \"2\"")
        assert(raw["testInt"] == 1234)
        assert(raw["testLong"] == 304993938434993L)

        assert(raw["intArray"] is IntArray)
        val intArray = raw["intArray"] as IntArray
        assert(intArray[0] == -94)
        assert(intArray[1] == 49)
        assert(intArray[2] == 29)

        assert(raw["testList"] is List<*>)
        val testList = raw["testList"] as List<*>
        testList.forEach {
            assert(it is String)
        }
        assert(testList[0] == "a@+0- \\\"string\\\" Lat's 1")
        assert(testList[1] == "b \$%*& \"string\" 2")
        assert(testList[2] == "c \$##@! 'string' 3")

        assert(raw["testCompound"] is Map<*, *>)
        val testCompound = raw["testCompound"] as Map<String, Any?>
        assert(testCompound["s1"] == 5)
        assert(testCompound["s2"] == "hello!")

        assert(raw["testListCompound"] is List<*>)
        val testListCompound = raw["testListCompound"] as List<Map<*, *>>
        testListCompound.forEach {
            assert(it is Map<*, *>)
        }
        (testListCompound[0] as Map<String, Any?>)["item"] == "name1"
        (testListCompound[1] as Map<String, Any?>)["item"] == "name2"
        (testListCompound[2] as Map<String, Any?>)["item"] == "name3"
        (testListCompound[0] as Map<String, Any?>)["count"] == 6
        (testListCompound[1] as Map<String, Any?>)["count"] == 3
        (testListCompound[2] as Map<String, Any?>)["count"] == 5
    }
}