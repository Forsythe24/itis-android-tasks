package com.itis.itis_android_tasks

import android.content.Context
import kotlin.random.Random

object QuestionGenerator {
    fun getQuestion(ctx: Context) : String {
        val array = ctx.resources.getStringArray(R.array.questions)
        val index = Random.nextInt(0, array.size)
        return array[index]
    }
}
