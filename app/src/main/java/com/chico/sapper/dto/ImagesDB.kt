package com.chico.sapper.dto

import com.chico.sapper.R

data class ImagesDB (

    val forest: Map<String, Int> = mapOf(
        "one" to R.drawable.forest_one,
        "two" to R.drawable.forest_two,
        "three" to R.drawable.forest_three,
        "four" to R.drawable.forest_four,
        "five" to R.drawable.forest_five,
        "six" to R.drawable.forest_six,
        "seven" to R.drawable.forest_seven,
        "eight" to R.drawable.forest_eight,

        "empty" to R.drawable.forest_empty,
        "shirt" to R.drawable.forest_shirt,
        "mine" to R.drawable.forest_mine,
        "mineExploded" to R.drawable.forest_mineexploded,
        "mayBe" to R.drawable.forest_maybe,
        "mineIsHire" to R.drawable.forest_mineishire
    ),

    val classic: Map<String, Int> = mapOf(
        "one" to R.drawable.classic_one,
        "two" to R.drawable.classic_two,
        "three" to R.drawable.classic_three,
        "four" to R.drawable.classic_four,
        "five" to R.drawable.classic_five,
        "six" to R.drawable.classic_six,
        "seven" to R.drawable.classic_seven,
        "eight" to R.drawable.classic_eight,

        "empty" to R.drawable.classic_empty,
        "shirt" to R.drawable.classic_shirt,
        "mine" to R.drawable.classic_mine,
        "mineExploded" to R.drawable.classic_mineexploded,
        "mayBe" to R.drawable.classic_maybe,
        "mineIsHire" to R.drawable.classic_mineishire
    ),

    val vanilla: Map<String, Int> = mapOf(
        "one" to R.drawable.vanilla_one,
        "two" to R.drawable.vanilla_two,
        "three" to R.drawable.vanilla_three,
        "four" to R.drawable.vanilla_four,
        "five" to R.drawable.vanilla_five,
        "six" to R.drawable.vanilla_six,
        "seven" to R.drawable.vanilla_seven,
        "eight" to R.drawable.vanilla_eight,

        "empty" to R.drawable.vanilla_empty,
        "shirt" to R.drawable.vanilla_shirt,
        "mine" to R.drawable.vanilla_mine,
        "mineExploded" to R.drawable.vanilla_mineexploded,
        "mayBe" to R.drawable.vanilla_maybe,
        "mineIsHire" to R.drawable.vanilla_mineishire
    )
)