package com.none.flow.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

typealias SnackbarMessage = Triple<String, String?, CompletableDeferred<Boolean>>

object SnackbarManager {

    private val _messages =
        Channel<SnackbarMessage>(capacity = Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    suspend fun showMessage(message: String, actionLabel: String? = null): Boolean {
        val deferredResult = CompletableDeferred<Boolean>()
        _messages.send(SnackbarMessage(message, actionLabel, deferredResult))
        return deferredResult.await()
    }
}