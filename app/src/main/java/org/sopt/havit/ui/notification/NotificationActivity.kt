package org.sopt.havit.ui.notification

import android.os.Bundle
import android.util.Log
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityNotificationBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class NotificationActivity :
    BaseBindingActivity<ActivityNotificationBinding>(R.layout.activity_notification) {
    private val notificationViewModel: NotificationViewModel by lazy { NotificationViewModel(this) }
    private lateinit var notificationAdapter: NotificationRvAdapter
    var option = "before"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()
        clickBackBtn()
    }

    override fun onStart() {
        super.onStart()
        setData()
        dataObserve()
    }

    private fun initAdapter() {
        notificationAdapter = NotificationRvAdapter()
        binding.rvNotification.adapter = notificationAdapter
    }

    private fun clickBackBtn() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setData() {
        notificationViewModel.requestContentsTaken(option)
    }

    private fun dataObserve() {
        with(notificationViewModel) {
            binding.lifecycleOwner?.let { it ->
                contentsList.observe(it) { data ->
                    if (data.isEmpty()) {
                        if (option == "before")
                            Log.d("TAG", "dataObserve: 알림 예정 콘텐츠가 없습니다")
                        else
                            Log.d("TAG", "dataObserve: 지난 알림 콘텐츠가 없습니다")
                    } else {
                        notificationAdapter.contentsList.clear()
                        notificationAdapter.contentsList.addAll(data)
                        notificationAdapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }
}