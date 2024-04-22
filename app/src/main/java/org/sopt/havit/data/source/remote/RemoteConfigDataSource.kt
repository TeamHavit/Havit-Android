package org.sopt.havit.data.source.remote

import kotlinx.coroutines.flow.Flow
import java.lang.reflect.Type

interface RemoteConfigDataSource {
    suspend fun fetchRemoteConfig(configKey: String, valueType: Type): Any
    fun fetchRemoteConfigFlow(configKey: String, valueType: Type): Flow<Any>
}