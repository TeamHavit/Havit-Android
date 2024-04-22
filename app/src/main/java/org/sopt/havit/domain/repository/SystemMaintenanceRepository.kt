package org.sopt.havit.domain.repository

import kotlinx.coroutines.flow.Flow

interface SystemMaintenanceRepository {

    suspend fun isSystemMaintenance(): Boolean

    suspend fun getSystemMaintenanceMessage(): String

    fun getForcedUpdateVer(): Flow<Int>
}