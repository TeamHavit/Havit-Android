package org.sopt.havit.data.source.local

import org.sopt.havit.data.local.HavitLocalPreferences
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(private val havitLocalPreferences: HavitLocalPreferences) :
    AuthLocalDataSource {
    override fun saveAccessToken(token: String) {
        havitLocalPreferences.setXAuthToken(token)
    }

    override fun getAccessToken(): String {
        return havitLocalPreferences.getXAuthToken()
    }
}