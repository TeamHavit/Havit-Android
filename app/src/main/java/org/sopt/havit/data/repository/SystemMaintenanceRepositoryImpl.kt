package org.sopt.havit.data.repository

import org.sopt.havit.data.source.remote.RemoteConfigDataSource
import org.sopt.havit.domain.repository.SystemMaintenanceRepository
import javax.inject.Inject

class SystemMaintenanceRepositoryImpl @Inject constructor(
    private val systemMaintenanceRemoteDataSource: RemoteConfigDataSource,
) : SystemMaintenanceRepository {
    override suspend fun isSystemMaintenance(): Boolean {
        val isSystemUnderMaintenance = systemMaintenanceRemoteDataSource.fetchRemoteConfig(
            "isSystemUnderMaintenance", Boolean::class.java
        ) as? Boolean
        return isSystemUnderMaintenance ?: false
    }

    override suspend fun getSystemMaintenanceMessage(): String {
        val systemMaintenanceMessage = systemMaintenanceRemoteDataSource.fetchRemoteConfig(
            "systemMaintenanceMessage", String::class.java
        ) as? String
        return systemMaintenanceMessage ?: ""
    }
}