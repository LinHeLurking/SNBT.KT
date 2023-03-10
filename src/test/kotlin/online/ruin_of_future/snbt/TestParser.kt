package online.ruin_of_future.snbt

import online.ruin_of_future.snbt.parser.SnbtParser
import online.ruin_of_future.snbt.tag.SnbtTag
import online.ruin_of_future.snbt.tag.TagId
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class TestParser {
    @Test
    fun parse() {
        val stream = this.javaClass.getResourceAsStream("/test1.snbt")
        assert(stream != null)

        val lines =
            BufferedReader(InputStreamReader(stream!!, StandardCharsets.UTF_8)).lines().collect(Collectors.toList())
        val parser = SnbtParser(lines)

        val tag = parser.read()
        // Id check
        assert(tag.id == TagId.COMPOUND)

        // Single value filed check
        assert(tag["test_string"]?.value == "value")
        assert(tag["test_string"]?.id == TagId.STRING)

        assert(tag["test string key"]?.value == "value \"2\"")
        assert(tag["test string key"]?.id == TagId.STRING)

        assert(tag["testInt"]?.value == 1234)
        assert(tag["testInt"]?.id == TagId.INT)

        assert(tag["testLong"]?.value == 304993938434993L)
        assert(tag["testLong"]?.id == TagId.LONG)

        assert(tag["testBool"]?.value == 1.toByte())
        assert(tag["testBool"]?.id == TagId.BYTE)

        assert(tag["testFloat"]?.value == 40402.4f)
        assert(tag["testFloat"]?.id == TagId.FLOAT)

        assert(tag["testDouble"]?.value == Double.NEGATIVE_INFINITY)
        assert(tag["testDouble"]?.id == TagId.DOUBLE)

        assert(tag["testShort"]?.value == 49.toShort())
        assert(tag["testShort"]?.id == TagId.SHORT)

        assert(tag["testByte"]?.value == (-124).toByte())
        assert(tag["testByte"]?.id == TagId.BYTE)

        assert(tag["another test string"]?.value == "he\\nll\\\\o")
        assert(tag["another test string"]?.id == TagId.STRING)

        // IntArray check
        assert(tag["intArray"]?.value is IntArray)
        assert(tag["intArray"]?.id == TagId.INT_ARRAY)
        val intArray = tag["intArray"]?.value as IntArray
        assert(intArray.size == 3)
        assert(intArray[0] == -94)
        assert(intArray[1] == 49)
        assert(intArray[2] == 29)

        // ByteArray check
        assert(tag["byteArray"]?.value is ByteArray)
        assert(tag["byteArray"]?.id == TagId.BYTE_ARRAY)
        val byteArray = tag["byteArray"]?.value as ByteArray
        assert(byteArray.size == 3)
        assert(byteArray[0] == (-94).toByte())
        assert(byteArray[1] == 49.toByte())
        assert(byteArray[2] == 29.toByte())

        // LongArray check
        assert(tag["longArray"]?.value is LongArray)
        assert(tag["longArray"]?.id == TagId.LONG_ARRAY)
        val longArray = tag["longArray"]?.value as LongArray
        assert(longArray.size == 2)
        assert(longArray[0] == 404049L)
        assert(longArray[1] == -34348L)

        // List check
        assert(tag["testList"]?.value is List<*>)
        assert(tag["testList"]?.id == TagId.LIST)
        val testList = tag["testList"]?.value as List<SnbtTag>
        assert(testList.size == 3)
        assert(testList[0].value == "a@+0- \\\"string\\\" Lat's 1")
        assert(testList[1].value == "b \$%*& \"string\" 2")
        assert(testList[2].value == "c \$##@! 'string' 3")

        // Compound check
        assert(tag["testCompound"]?.id == TagId.COMPOUND)
        val testCompound = tag["testCompound"]!!
        assert(testCompound["s1"]?.value == 5)
        assert(testCompound["s2"]?.value == "hello!")
        assert(testCompound["s3"]?.value == -4.435345e-2)


        // List of CompoundTag test
        assert(tag["testListCompound"]?.value is List<*>)
        assert(tag["testListCompound"]?.id == TagId.LIST)
        val testListCompound = tag["testListCompound"]?.value as List<SnbtTag>
        assert(testListCompound.size == 3)
        testListCompound.forEach {
            assert(it.id == TagId.COMPOUND)
            assert(it["item"]?.id == TagId.STRING)
            assert(it["count"]?.id == TagId.INT)
        }
        assert(testListCompound[0]["item"]?.value == "name1")
        assert(testListCompound[1]["item"]?.value == "name2")
        assert(testListCompound[2]["item"]?.value == "name3")

        assert(testListCompound[0]["count"]?.value == 6)
        assert(testListCompound[1]["count"]?.value == 3)
        assert(testListCompound[2]["count"]?.value == 5)
    }
}