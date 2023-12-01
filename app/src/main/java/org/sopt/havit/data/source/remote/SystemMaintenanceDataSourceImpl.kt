package org.sopt.havit.data.source.remote

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SystemMaintenanceDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SystemMaintenanceDataSource {
    override fun isSystemMaintenance(): Boolean? {
        return true
    }

    override fun getSystemMaintenanceMessage(): String? {
        return "test"
    }
}