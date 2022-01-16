package org.sopt.havit.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.sopt.havit.R
import org.sopt.havit.ShareActivity
import org.sopt.havit.databinding.FragmentMyPageBinding
import org.sopt.havit.ui.contents.ContentsViewModel
import org.sopt.havit.ui.search.SearchActivity

class MyPageFragment : Fragment() {

    //private val myPageViewModel: MyPageViewModel by viewModel()
     val contentsViewModel:ContentsViewModel by viewModels()
    private var _binding: FragmentMyPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyPageBinding.inflate(inflater, container, false)


        binding.textHome.setOnClickListener {
            startActivity(Intent(context,SearchActivity::class.java))
        }




        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}