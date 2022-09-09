package org.sopt.havit.domain.model

sealed class NetworkStatus {
    class Init : NetworkStatus()
    class Loading : NetworkStatus()
    class Success : NetworkStatus()
    class Error(val throwable: Throwable?) : NetworkStatus()
}