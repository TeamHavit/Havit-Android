package org.sopt.havit.data.source.remote

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class RemoteConfigDataSourceImpl @Inject constructor() : RemoteConfigDataSource {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 60 })
    }

    override suspend fun fetchRemoteConfig(configKey: String, valueType: Type): Any {
        return suspendCancellableCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val remoteConfigValue = when (valueType) {
                        String::class.java -> remoteConfig.getString(configKey)
                        Boolean::class.java -> remoteConfig.getBoolean(configKey)
                        Long::class.java -> remoteConfig.getLong(configKey)
                        else -> throw IllegalArgumentException("Not supported type. Please check valueType")
                    }
                    continuation.resumeWith(Result.success(remoteConfigValue))
                } else continuation.resumeWithException(
                    task.exception ?: IllegalStateException("Fetch RemoteConfig failed")
                )
            }
        }
    }

    override fun fetchRemoteConfigFlow(configKey: String, valueType: Type) = flow {
        val remoteConfigValue = when (valueType) {
            String::class.java -> remoteConfig.getString(configKey)
            Boolean::class.java -> remoteConfig.getBoolean(configKey)
            Long::class.java -> remoteConfig.getLong(configKey)
            else -> throw IllegalArgumentException("Not supported type. Please check valueType")
        }
        emit(remoteConfigValue)
    }
}