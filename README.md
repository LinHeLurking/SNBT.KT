# SNBT.KT

![](https://github.com/LinHeLurking/SNBT.KT/actions/workflows/gradle.yml/badge.svg)

This is a SNBT file parser & converter for Kotlin language. Copied and modified from [FTB-Library](https://github.com/FTBTeam/FTB-Library). This is a standalone library which provides SNBT parse ability without dependencies from Minecraft itself.

## Setup

Gradle Kotlin DSL:

```Kotlin
implementation("io.github.linhelurking:kt-snbt:0.1.2")
```

Gradle Groovy DSL

```Groovy
implementation group: 'io.github.linhelurking', name: 'kt-snbt', version: '0.1.2'
```


## Basic Usage

>
See [SnbtTag](https://github.com/LinHeLurking/SNBT.KT/blob/main/src/main/kotlin/io/github/linhelurking/snbt/tag/SnbtTag.kt)
for more details.

```Kotlin
// Build parser from file path or collection of lines
val parser = SnbtParser("...")

// A compound tag is read
val tag = parser.read()

// Get compound tag field. It it's not a compound tag, an exception is thrown.
val value = tag["key"]

// Get byte value if it contains a byte, otherwise get null.
val b = tag.getByteOrNull()

// Get byte value if it contains a byte, otherwise a exception is thrown.
val b2 = tag.getByte()

// Other types also have their corresponding auxilary methods.

// Each tag has an `id` and `value` field
println(value.id)
println(value.value)
```
