package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSetNotificationBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.MySharedPreference
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SetNotificationFragment :
    BaseBindingFragment<FragmentSetNotificationBinding>(R.layout.fragment_set_notification) {

    val size = 5
    private lateinit var isSelected: Array<Boolean>
    private lateinit var btnList: Array<TextView>
    private lateinit var notificationTime : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        btnList = arrayOf(
            binding.btn1h,
            binding.btn2h,
            binding.btn3h,
            binding.btnTomorrow,
            binding.btn1min
        )

        initToolbarListener()
        initBtnClickListener()
        isSelected = Array(size+1) { false }

        return binding.root
    }

    private fun initBtnClickListener() {

        for (j in 0 until size ) {
            btnList[j].setOnClickListener {
                initBooleanList()
                isSelected[j] = !isSelected[j]
                setColor()
                notificationTime = getNotificationTime(j)
            }
        }
    }

    private fun setColor() {
        for (i in 0 until size ) {
            if (isSelected[i]) {
                btnList[i].setBackgroundResource(R.drawable.rectangle_purple_2_radius_6)
            } else {
                btnList[i].setBackgroundResource(R.drawable.rectangle_purple_category_radius_6)
            }
        }
    }

    private fun initBooleanList() {
        for (i in 0 until size) isSelected[i] = false
    }

    private fun initToolbarListener() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvComplete.setOnClickListener {

            MySharedPreference.setNotificationTime(requireContext(), notificationTime)
            findNavController().popBackStack()
        }
    }

    private fun getNotificationTime(idx : Int) : String {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val df : DateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        Log.d("Before change : ", df.format(cal.time))

        if (idx == 0) cal.add(Calendar.HOUR, 1)
        if (idx == 1) cal.add(Calendar.HOUR, 2)
        if (idx == 2) cal.add(Calendar.HOUR, 3)
        if (idx == 3) cal.add(Calendar.DATE, 1)
        if (idx == 4) cal.add(Calendar.MINUTE, 1)

        Log.d("After  change : ", df.format(cal.time))

        return df.format(cal.time)
    }


}