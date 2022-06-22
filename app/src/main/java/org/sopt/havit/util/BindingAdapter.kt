package org.sopt.havit.util

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.sopt.havit.R
import org.sopt.havit.util.DpToPxUtil.px

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
            .transform(CenterCrop(), RoundedCorners(px(4)))
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
    @BindingAdapter("app:ogImage")
    @JvmStatic
    fun ImageView.setOgImage(url: String?) {
        Glide.with(context)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(px(6)))
            .placeholder(R.drawable.img_contents_dummy_3)
            .into(this)
    }

    @BindingAdapter("description")
    @JvmStatic
    fun setDescription(textView: TextView, rate: Int?) {
        when (rate) {
            in 0..33 -> textView.text = textView.context.getString(R.string.mypage_description1)
            in 34..66 -> textView.text = textView.context.getString(R.string.mypage_description2)
            in 67..99 -> textView.text = textView.context.getString(R.string.mypage_description3)
            100 -> textView.text = textView.context.getString(R.string.mypage_description4)
        }
    }

    @BindingAdapter("descriptionImg")
    @JvmStatic
    fun setDescriptionImg(imageView: ImageView, rate: Int?) {
        when (rate) {
            in 0..33 -> Glide.with(imageView).load(R.drawable.ic_illust_stage_1).into(imageView)
            in 34..66 -> Glide.with(imageView).load(R.drawable.ic_illust_stage_2).into(imageView)
            in 67..99 -> Glide.with(imageView).load(R.drawable.ic_illust_stage_3).into(imageView)
            100 -> Glide.with(imageView).load(R.drawable.ic_illust_stage_4).into(imageView)
        }
    }

    @BindingAdapter("popupDescription")
    @JvmStatic
    fun setPopupDescription(textView: TextView, rate: Int) {
        when (rate) {
            in 0..33 -> textView.text = textView.context.getString(R.string.home_popup_description1)
            in 34..66 ->
                textView.text =
                    textView.context.getString(R.string.home_popup_description2)
            in 67..99 ->
                textView.text =
                    textView.context.getString(R.string.home_popup_description3)
            100 -> textView.text = textView.context.getString(R.string.home_popup_description4)
        }
    }

    @BindingAdapter("android:alarmText")
    @JvmStatic
    fun TextView.setAlarmText(string: String) {
        this.text = if (string == "알림 설정") "알림 설정" else setDateFormat(string)
    }

    private fun setDateFormat(originTime: String): String {
        Log.d("originTime", originTime) // 2022.01.25 00:04:54

        // 날짜 (2022.01.25)
        val date =
            "${originTime[2]}${originTime[3]}.${originTime[5]}${originTime[6]}.${originTime[8]}${originTime[9]}"
        // 시 (오후 11시 :: 12시간제 적용)
        val hour = "${originTime[11]}${originTime[12]}".toInt()
        val newHour = when (hour) {
            in 0..12 -> " 오전 ${hour}시 "
            else -> " 오후 ${hour - 12}시 "
        }
        // 분 (3분 :: 자릿수 재졍렬을 위한 이중 형변환 사용)
        val min = "${originTime[14]}${originTime[15]}".toInt().toString() + "분"

        return "$date$newHour$min 알림 예정"
    }

    @BindingAdapter("app:textVisibility")
    @JvmStatic
    fun TextView.textVisibility(isVisible: Boolean) {
        this.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:imageVisibility")
    @JvmStatic
    fun ImageView.imageVisibility(isSelected: Boolean) {
        this.visibility = if (isSelected) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:btnBackground")
    @JvmStatic
    fun AppCompatButton.setBtnBackground(isEnabled: Boolean) {
        this.setBackgroundResource(
            if (isEnabled) R.drawable.rectangle_havit_purple else R.drawable.rectangle_gray_2
        )
    }

    @BindingAdapter("app:btnEnabled")
    @JvmStatic
    fun AppCompatButton.setBtnEnable(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }
}
