# SNBT.KT

![](https://github.com/LinHeLurking/SNBT.KT/actions/workflows/gradle.yml/badge.svg)

This is a SNBT file parser & converter for Kotlin language.

## Basic Usage

> See [Tests](https://github.com/LinHeLurking/SNBT.KT/blob/main/src/test/kotlin/online/ruin_of_future/snbt/TestParser.kt) for more details.


```Kotlin
# Build parser from file path or collection of lines
val parser = SnbtParser(...)

# A compound tag is read
val tag = parser.read()

# Get compound tag field
val value = tag["key"]

# Each tag has an `id` and `value` field
println(value.id)
println(value.value)
```
