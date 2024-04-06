package org.sopt.havit.data.repository

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

    companion object {
        private const val IS_SYSTEM_UNDER_MAINTENANCE = "isSystemUnderMaintenance"
        private const val SYSTEM_MAINTENANCE_MESSAGE = "systemMaintenanceMessage"
        private const val DEFAULT_MESSAGE = "현재 시스템 점검중입니다.\\n불편을 끼쳐드려 죄송합니다."
    }
}