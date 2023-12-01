package org.sopt.havit.data.source.remote

interface SystemMaintenanceDataSource {

    fun isSystemMaintenance(): Boolean?

    fun getSystemMaintenanceMessage(): String?
}