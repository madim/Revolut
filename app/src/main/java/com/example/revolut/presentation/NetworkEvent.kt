package com.example.revolut.presentation

sealed class NetworkEvent {
    object OnAvailable : NetworkEvent()
    object OnLost : NetworkEvent()
}