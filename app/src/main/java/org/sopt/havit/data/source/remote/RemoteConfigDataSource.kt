package org.sopt.havit.data.source.remote

import java.lang.reflect.Type

interface RemoteConfigDataSource {
    suspend fun fetchRemoteConfig(configKey: String, valueType: Type): Any
}