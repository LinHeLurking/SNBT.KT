package online.ruin_of_future.snbt.parser

import online.ruin_of_future.snbt.tag.SnbtTag

object SpecialTags {
    val TRUE = SnbtTag.valueOf(true)
    val FALSE = SnbtTag.valueOf(false)
    val NAN_D = SnbtTag.valueOf(Double.NaN)
    val NAN_F = SnbtTag.valueOf(Float.NaN)
    val POS_INFINITY_D = SnbtTag.valueOf(Double.POSITIVE_INFINITY)
    val POS_INFINITY_F = SnbtTag.valueOf(Float.POSITIVE_INFINITY)
    val NEG_INFINITY_D = SnbtTag.valueOf(Double.NEGATIVE_INFINITY)
    val NEG_INFINITY_F = SnbtTag.valueOf(Float.NEGATIVE_INFINITY)
}