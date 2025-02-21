package com.codewithdipesh.mangareader.data.observer

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.codewithdipesh.mangareader.domain.observer.connectivityObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityObserverImpl @Inject constructor(
    @ApplicationContext private val context : Context
): connectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<connectivityObserver.Status> {
         return callbackFlow {
             val callback = object : ConnectivityManager.NetworkCallback(){
                 override fun onAvailable(network: Network) {
                     super.onAvailable(network)
                     launch { send(connectivityObserver.Status.Available) }
                 }
                 override fun onLost(network: Network) {
                     super.onLost(network)
                     launch { send(connectivityObserver.Status.Lost) }
                 }

                 override fun onUnavailable() {
                     super.onUnavailable()
                     launch { send(connectivityObserver.Status.UnAvailable) }
                 }
             }

             connectivityManager.registerDefaultNetworkCallback(callback)
             awaitClose {//when viewmodelscope ended it will lcosed
                 //clean up what it need to be
                 connectivityManager.unregisterNetworkCallback(callback)
             }
         }.distinctUntilChanged()
    }
}