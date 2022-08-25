package org.sopt.havit.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun useBitmapImg(context: Context, imgUrl: String?, useBitmap: (Bitmap) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(imgUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                useBitmap(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
}
