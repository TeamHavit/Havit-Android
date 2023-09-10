package org.sopt.havit

import android.content.ContentValues
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.FcmTokenParams
import javax.inject.Inject

class FirebaseTokenManager @Inject constructor(
    private val havitApi: HavitApi
) {

    fun sendRegistrationToServer(fcmToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                havitApi.refreshFcmToken(FcmTokenParams(fcmToken))
            }.onSuccess {
                Log.d(ContentValues.TAG, "onNewToken: success")
            }.onFailure {
                Log.d(ContentValues.TAG, "onNewToken: failure ${it.message}")
            }
        }

    }
}