package org.sopt.havit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.sopt.havit.data.repository.HomeReachData

class HomeViewModel : ViewModel() {

    private val _reachData = MutableLiveData<HomeReachData>().apply {
        value = HomeReachData(62.toString(), 145.toString(), 100.toString())
    }
    val reachData: LiveData<HomeReachData> = _reachData

    private val _userName = MutableLiveData<String>().apply {
        value = "000"
    }
    val userName: LiveData<String> = _userName
}