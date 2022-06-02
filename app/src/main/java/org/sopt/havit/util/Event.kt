package org.sopt.havit.util

open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) { // 이벤트가 이미 처리
            null
        } else { // 아직 이벤트 처리 x
            hasBeenHandled = true // 이벤트 처리되었다고 표시
            return content // 값 반환
        }
    }

    // 이벤트 처리 여부에 상관 없이 값 반환
    fun peekContent(): T = content
}