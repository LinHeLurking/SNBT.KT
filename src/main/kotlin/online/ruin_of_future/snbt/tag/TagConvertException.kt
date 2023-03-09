package online.ruin_of_future.snbt.tag

import kotlin.reflect.KClass

class TagConvertException(id: TagId, target: KClass<*>) :
    Exception("Cannot convert type $id to ${target.qualifiedName}")