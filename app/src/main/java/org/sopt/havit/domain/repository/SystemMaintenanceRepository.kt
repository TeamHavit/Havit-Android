package org.sopt.havit.domain.repository

interface SystemMaintenanceRepository {

    suspend fun isSystemMaintenance(): Boolean

    suspend fun getSystemMaintenanceMessage(): String
}