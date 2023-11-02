package com.itis.itis_android_tasks.utils

import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.model.Date
import com.itis.itis_android_tasks.model.Friend
import kotlin.random.Random

object FriendsRepository {
    private val itemsList = mutableListOf<Any>()
    private var list = mutableListOf<Friend>()


    init {
        list = mutableListOf(
            Friend(
                id = 1,
                name = "Chandler Bing",
                image = R.drawable.chandler_bing,
                info = "The funniest of friends"
            ),

            Friend(
                id = 2,
                name = "Ross Geller",
                image = R.drawable.ross_geller,
                info = "The divorce force"
            ),

            Friend(
                id = 3,
                name = "Monica Geller",
                image = R.drawable.monica_geller,
                info = "High maintenance, but it's okay"
            ),

            Friend(
                id = 4,
                name = "Joey Tribbiani",
                image = R.drawable.joey_tribbiani,
                info = "Doesn't stop the Q-tip when there is resistance"
            ),

            Friend(
                id = 5,
                name = "Phoebe Buffay",
                image = R.drawable.phoebe_buffay,
                info = "Flaky and bendy"
            ),

            Friend(
                id = 6,
                name = "Rachel Green",
                image = R.drawable.rachel_green,
                info = "Rambled on for 18 pages... FRONT AND BACK"
            ),
        )
    }

    fun getItemsList(number: Int): MutableList<Any> {
        if (itemsList.isEmpty()) {
            fillList(number)
        }
        return itemsList
    }

    fun fillList(number: Int) {

        val date = Date("28-10-2023")

        if (number == 0) {
            itemsList.add(date)
        } else {
            for (i in 0 until number) {
                if (i % 8 == 0) {
                    itemsList.add(date)
                }
                itemsList.add(getFriend())
            }
        }
    }

    fun getList(): MutableList<Friend> = list

    fun getFriend() : Friend{
        return list[Random.nextInt(0, 6)]
    }

    fun getCurrentItems(): MutableList<Any> = itemsList

    fun addToFavorites(friend: Friend) {
        val items = getCurrentItems()

        items.forEach{item ->
            if (item is Friend && item.id == friend.id) {
                item.isLiked = !item.isLiked
            }
        }
    }

}
