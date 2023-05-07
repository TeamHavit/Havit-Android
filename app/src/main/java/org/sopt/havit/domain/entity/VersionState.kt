package org.sopt.havit.domain.entity

/** VersionState가 Unkown인 경우
 *  1. debug build로 실행할 경우 (Prod로 실행해주세요)
 *  2. Play Store지원을 하지 않는 Emulator를 사용하고 있는 경우
 * */
enum class VersionState {
    Latest, Update, Unknown

}