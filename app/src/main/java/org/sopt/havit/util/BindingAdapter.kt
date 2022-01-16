package org.sopt.havit.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {

    /** follower_list 의 팔로워 프로필 */
    @BindingAdapter("imageUrl")       // Binding Adapter 생성
    @JvmStatic                                   // Static 함수로 설정해주기 위한 Annotation
    fun ImageView.loadImage(url : String){
        Glide.with(context)
            .load(url)
            .into(this)
    }

}