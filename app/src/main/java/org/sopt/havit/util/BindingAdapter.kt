package org.sopt.havit.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.sopt.havit.R
import org.sopt.havit.util.BindingAdapter.defaultImageGrid
import org.sopt.havit.util.BindingAdapter.defaultImageLinearMax

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

    @BindingAdapter("imageDefaultLinearMin")
    @JvmStatic
    fun ImageView.defaultImageLinearMin(url: String) {
        Log.d("imageSet", "1")
        if(url==""){
            setImageResource(R.drawable.img_contents_dummy)
        }
        else{
            Glide.with(context)
                .load(url)
                .into(this)
        }
    }

    @BindingAdapter("imageDefaultGrid")
    @JvmStatic
    fun ImageView.defaultImageGrid(url: String) {
        Log.d("imageSet", "2")
        if(url==""){
            setImageResource(R.drawable.img_contents_dummy_2)
        }
        else{
            Glide.with(context)
                .load(url)
                .into(this)
        }
    }

    @BindingAdapter("imageDefaultLinearMax")
    @JvmStatic
    fun ImageView.defaultImageLinearMax(url: String) {
        Log.d("imageSet", "3")
        if(url==""){
            setImageResource(R.drawable.img_contents_dummy_3)
        }
        else{
            Glide.with(context)
                .load(url)
                .into(this)
        }
    }
}