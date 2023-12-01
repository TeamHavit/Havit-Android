package org.sopt.havit.data.repository

import org.sopt.havit.data.source.remote.SystemMaintenanceDataSource
import org.sopt.havit.domain.repository.SystemMaintenanceRepository
import javax.inject.Inject

class SystemMaintenanceRepositoryImpl @Inject constructor(
    private val systemMaintenanceRemoteDataSource: SystemMaintenanceDataSource,
) : SystemMaintenanceRepository {
    override fun isSystemMaintenance(): Boolean {
        return systemMaintenanceRemoteDataSource.isSystemMaintenance() ?: false
    }

    override fun getSystemMaintenanceMessage(): String {
        return systemMaintenanceRemoteDataSource.getSystemMaintenanceMessage() ?: "unknown error"
    }
}