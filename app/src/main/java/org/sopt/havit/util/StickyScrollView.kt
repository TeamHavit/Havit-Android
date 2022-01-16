package org.sopt.havit.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView

class StickyScrollView : ScrollView, ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        overScrollMode = OVER_SCROLL_NEVER      // NEVER => 역스크롤시 나오는 또잉~애니메이션 제거
        viewTreeObserver.addOnGlobalLayoutListener(this)    // 레이아웃 변경이 생길 때 일어날 함수
    }

    //    translationZ를 1로 설정 => 천장에 붙은 뷰를 다른 스크롤뷰 앞에 오도록
    var header: View? = null
        set(value) {
            field = value
            field?.let { it ->
                it.translationZ = 1f
//                it.setOnClickListener { _ ->
////                클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
//                    this.smoothScrollTo(scrollX, it.top)    // 지정한 스크롤의 위치로 부드럽게 스크롤
//                    callStickListener()
//                }
            }
        }

    //    두 리스너를 일급함수로 설정.
    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}

    private var mIsHeaderSticky = false     // 헤더가 천장에 달라 붙어 있는지 여부
    private var mHeaderInitPosition = 0f    // 헤더의 초기 위치 저장 변수

    //    헤더의 y포지션을 저장하는 부분
//    * 이 로직을 생성자나 onAttatchToWindow등에 넣어두면 올바르게 저장되지 않음!
    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
    }

    //    onScrollChanged를 오버라이드해서 어느 시점에 헤더를 어디에 붙일지 결정
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        if (t > mHeaderInitPosition) {
            stickHeader(t - mHeaderInitPosition)
        } else {
            freeHeader()
        }
    }

    private fun stickHeader(position: Float) {
        header?.translationY = position
        callStickListener()
    }

    private fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
        }
    }

    private fun freeHeader() {
        header?.translationY = 0f
        callFreeListener()
    }

    private fun callFreeListener() {
        if (mIsHeaderSticky) {
            freeListener(header ?: return)
            mIsHeaderSticky = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }
}