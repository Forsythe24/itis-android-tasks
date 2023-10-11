package com.itis.itis_android_tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel: ViewModel() {
    private val _messageData: MutableList<String?> = MutableList(3) {_ -> null}
    private val _messageLiveData = MutableLiveData<List<String?>>(_messageData)
    val messageData: LiveData<List<String?>>
        get() = _messageLiveData

    fun add(message: String) {
        _messageData.removeAt(0)
        _messageData.add(message)
        _messageLiveData.value = _messageData
    }
}
