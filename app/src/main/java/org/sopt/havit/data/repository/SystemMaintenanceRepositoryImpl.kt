package org.sopt.havit.data.repository

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.sopt.havit.data.source.remote.RemoteConfigDataSource
import org.sopt.havit.domain.repository.SystemMaintenanceRepository
import javax.inject.Inject

class SystemMaintenanceRepositoryImpl @Inject constructor(
    private val systemMaintenanceRemoteDataSource: RemoteConfigDataSource,
) : SystemMaintenanceRepository {
    override suspend fun isSystemMaintenance(): Boolean {
        val isSystemUnderMaintenance = systemMaintenanceRemoteDataSource.fetchRemoteConfig(
            IS_SYSTEM_UNDER_MAINTENANCE,
            Boolean::class.java
        ) as? Boolean
        return isSystemUnderMaintenance ?: false
    }

    override suspend fun getSystemMaintenanceMessage(): String {
        val message = systemMaintenanceRemoteDataSource.fetchRemoteConfig(
            SYSTEM_MAINTENANCE_MESSAGE,
            String::class.java
        ).toString()
        return message.ifEmpty { DEFAULT_MESSAGE }
    }

    override fun getForcedUpdateVer(): Flow<Int> = flow {
        systemMaintenanceRemoteDataSource.fetchRemoteConfigFlow(
            ANDROID_FORCED_UPDATE_VER,
            Long::class.java
        ).catch {
            Firebase.crashlytics.recordException(it)
            emit(0)
        }.collect {
            emit((it as? Long)?.toInt() ?: 0)
        }
    }


    companion object {
        private const val IS_SYSTEM_UNDER_MAINTENANCE = "isSystemUnderMaintenance"
        private const val SYSTEM_MAINTENANCE_MESSAGE = "systemMaintenanceMessage"
        private const val ANDROID_FORCED_UPDATE_VER = "androidForcedUpdateVer"
        private const val DEFAULT_MESSAGE = "현재 시스템 점검중입니다.\\n불편을 끼쳐드려 죄송합니다."
    }
}