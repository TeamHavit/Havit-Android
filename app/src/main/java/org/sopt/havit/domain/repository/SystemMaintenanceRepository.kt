package org.sopt.havit.domain.repository

interface SystemMaintenanceRepository {

    fun isSystemMaintenance(): Boolean

    fun getSystemMaintenanceMessage(): String
}