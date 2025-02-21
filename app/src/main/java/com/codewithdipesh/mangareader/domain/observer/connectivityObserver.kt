package com.codewithdipesh.mangareader.domain.observer

import kotlinx.coroutines.flow.Flow

interface connectivityObserver {
    fun observe() : Flow<Status>

    enum class Status{
        UnAvailable,
        Available,
        Lost
    }
}