package com.itis.itis_android_tasks

import android.content.Context
import kotlin.random.Random

object OptionGenerator {
    fun getOptions(ctx: Context) : List<String> {
        val array = ctx.resources.getStringArray(R.array.options)

        val optionList = mutableListOf<String>()

        val numberOfOptions = Random.nextInt(5, 11)

        repeat(numberOfOptions) {
            val newIndex = Random.nextInt(0, array.size)
            optionList.add(array[newIndex])
        }
        return optionList
    }
}
