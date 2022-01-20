package org.sopt.havit.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {

    // item_category
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun ImageView.loadImage(url: String) {
        Glide.with(context)
            .load(url)
            .into(this)
    }

    @BindingAdapter("iconSrc")
    @JvmStatic
    fun ImageView.loadIcon(url: String) {
        Glide.with(context)
            .load(url)
            .circleCrop()
            .into(this)
    }

    @BindingAdapter("localIcon")
    @JvmStatic
    fun ImageView.loadIcon(url: Int) {
        Glide.with(context)
            .load(url)
            .circleCrop()
            .into(this)
    }

    @BindingAdapter("imgRes")
    @JvmStatic
    fun imgLoad(imageView:ImageView, resid:Drawable) {
        imageView.setImageDrawable(resid)
    }

    @BindingAdapter("btnColor")
    @JvmStatic
    fun setBtnColor(btn:Button,isNext:Boolean){
        if(isNext){
            btn.setBackgroundColor(Color.parseColor("#8578ff"))
        }else{
            btn.setBackgroundColor(Color.parseColor("#afafb7"))
        }
    }



}