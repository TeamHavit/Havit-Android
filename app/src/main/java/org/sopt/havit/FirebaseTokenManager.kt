package org.sopt.havit

import android.content.ContentValues
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.FcmTokenParams
import org.sopt.havit.domain.repository.AuthRepository
import javax.inject.Inject

class FirebaseTokenManager @Inject constructor(
    authRepository: AuthRepository,
) {
    private val accessToken = authRepository.getAccessToken()

    fun sendRegistrationToServer(fcmToken: String) {
        GlobalScope.launch {
            kotlin.runCatching {
                RetrofitObject.provideHavitApi(accessToken)
                    .refreshFcmToken(FcmTokenParams(fcmToken))
            }.onSuccess {
                Log.d(ContentValues.TAG, "onNewToken: success")
            }.onFailure {
                Log.d(ContentValues.TAG, "onNewToken: failure ${it.message}")
            }
        }

    }
}