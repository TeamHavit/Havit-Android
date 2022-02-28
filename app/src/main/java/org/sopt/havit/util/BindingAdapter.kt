package org.sopt.havit.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.sopt.havit.R


object BindingAdapter {

    // item_category
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun ImageView.loadImage(url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.img_contents_dummy)
            .into(this)
    }

    @BindingAdapter("iconSrc")
    @JvmStatic
    fun ImageView.loadIcon(url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.img_contents_dummy)
            .circleCrop()
            .into(this)
    }

    @BindingAdapter("localIcon")
    @JvmStatic
    fun ImageView.loadIcon(url: Int?) {
        Glide.with(context)
            .load(url)
            .into(this)
    }

    @BindingAdapter("imgRes")
    @JvmStatic
    fun imgLoad(imageView: ImageView, resid: Drawable) {
        imageView.setImageDrawable(resid)
    }

    @BindingAdapter("btnColor")
    @JvmStatic
    fun setBtnColor(btn: Button, isNext: Boolean) {
        if (isNext) {
            btn.setBackgroundColor(btn.context.getColor(R.color.havit_purple))
        } else {
            btn.setBackgroundColor(btn.context.getColor(R.color.gray_2))
        }
    }

    @BindingAdapter("imageSearch")
    @JvmStatic
    fun ImageView.loadSearch(url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.img_contents_dummy)
            .into(this)
    }


    @BindingAdapter("imageDefaultLinearMin")
    @JvmStatic
    fun ImageView.defaultImageLinearMin(url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.img_contents_dummy)
            .into(this)
    }

    @BindingAdapter("imageDefaultGrid")
    @JvmStatic
    fun ImageView.defaultImageGrid(url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.img_contents_dummy_2)
            .into(this)
    }

    @BindingAdapter("imageDefaultLinearMax")
    @JvmStatic
    fun ImageView.defaultImageLinearMax(url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.img_contents_dummy_3)
            .into(this)
    }

    // item_category
    @BindingAdapter("ogImage")
    @JvmStatic
    fun ImageView.setOgImage(url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.img_contents_dummy_3)
            .into(this)
    }

    @BindingAdapter("description")
    @JvmStatic
    fun setDescription(textView: TextView, rate: Int) {
        when (rate) {
            in 0..33 -> textView.text = textView.context.getString(R.string.mypage_description1)
            in 34..66 -> textView.text = textView.context.getString(R.string.mypage_description2)
            in 67..99 -> textView.text = textView.context.getString(R.string.mypage_description3)
            100 -> textView.text = textView.context.getString(R.string.mypage_description4)
        }
    }

    @BindingAdapter("descriptionImg")
    @JvmStatic
    fun setDescriptionImg(imageView: ImageView,rate: Int){
        when (rate) {
            in 0..33 -> Glide.with(imageView).load(R.drawable.ic_illust_light_1).into(imageView)
            in 34..66 -> Glide.with(imageView).load(R.drawable.ic_illust_light_2).into(imageView)
            in 67..99 -> Glide.with(imageView).load(R.drawable.ic_illust_light_3).into(imageView)
            100 -> Glide.with(imageView).load(R.drawable.ic_illust_light_4).into(imageView)
        }
    }
}