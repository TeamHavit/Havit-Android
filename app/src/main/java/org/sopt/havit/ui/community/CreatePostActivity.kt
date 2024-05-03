package org.sopt.havit.ui.community

import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCreatePostBinding
import org.sopt.havit.ui.base.BaseActivity

class CreatePostActivity : BaseActivity<ActivityCreatePostBinding>(R.layout.activity_create_post) {
    private val createPostViewModel by viewModels<CreatePostViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupEditText()
        onEditTextChanged()
    }

    private fun setupEditText() {
        setupLinkEditText()
        setupTitleEditText()
        setUpDescriptionEditText()
    }

    private fun onEditTextChanged() {
        onUrlEditTextChanged()
        onTitleEditTextChanged()
        onDescriptionEditTextChanged()
    }

    private fun onTitleEditTextChanged() {
        createPostViewModel.title.observe(this) {
            createPostViewModel.fetchTitleInfoStatus()
            createPostViewModel.setIsTitleValid()
        }
    }

    private fun onDescriptionEditTextChanged() {
        createPostViewModel.description.observe(this) {
            createPostViewModel.fetchDescriptionInfoStatus()
            createPostViewModel.setIsDescriptionValid()
        }
    }

    private fun onUrlEditTextChanged() {
        createPostViewModel.url.observe(this) {
            createPostViewModel.fetchUrlInfoStatus()
            createPostViewModel.setIsUrlValid()
        }
    }


    private fun setupLinkEditText() {
        binding.etLink.apply {
            setLifecycleOwner(this@CreatePostActivity)
            bindEditTextData(createPostViewModel.urlEditTextData)
            setMaxLine(2)
            setMinLine(2)
            setGravity(Gravity.TOP)
        }
    }

    private fun setupTitleEditText() {
        binding.etTitle.apply {
            setLifecycleOwner(this@CreatePostActivity)
            bindEditTextData(createPostViewModel.titleEditTextData)
            disableNewLine()
            setMaxLine(2)
            setMinLine(2)
            setGravity(Gravity.TOP)
        }
    }

    private fun setUpDescriptionEditText() {
        binding.etDescription.apply {
            setLifecycleOwner(this@CreatePostActivity)
            bindEditTextData(createPostViewModel.descriptionEditTextData)
            setMaxLine(18)
            setMinLine(18)
            setGravity(Gravity.TOP)
        }
    }

}