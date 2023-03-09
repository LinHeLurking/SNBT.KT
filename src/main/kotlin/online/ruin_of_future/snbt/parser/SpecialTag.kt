package online.ruin_of_future.snbt.parser

import online.ruin_of_future.snbt.tag.Tag

object SpecialTags {
    val TRUE = Tag.valueOf(true)
    val FALSE = Tag.valueOf(false)
    val NAN_D = Tag.valueOf(Double.NaN)
    val NAN_F = Tag.valueOf(Float.NaN)
    val POS_INFINITY_D = Tag.valueOf(Double.POSITIVE_INFINITY)
    val POS_INFINITY_F = Tag.valueOf(Float.POSITIVE_INFINITY)
    val NEG_INFINITY_D = Tag.valueOf(Double.NEGATIVE_INFINITY)
    val NEG_INFINITY_F = Tag.valueOf(Float.NEGATIVE_INFINITY)
}