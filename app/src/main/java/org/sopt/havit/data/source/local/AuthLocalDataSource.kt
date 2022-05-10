package org.sopt.havit.data.source.local

interface AuthLocalDataSource {
    fun saveAccessToken(token: String)
    fun getAccessToken(): String
}