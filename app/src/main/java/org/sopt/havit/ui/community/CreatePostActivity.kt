package org.sopt.havit.ui.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.HavitFirebaseMessagingService.Companion.TAG
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCreatePostBinding
import org.sopt.havit.domain.model.NetworkStatus
import org.sopt.havit.ui.base.BaseActivity
import org.sopt.havit.ui.home.community.CommunityFragment.Companion.CREATE_COMMUNITY
import org.sopt.havit.ui.model.CommunityCategoryRO
import org.sopt.havit.util.CommunityCategoryChipGroup
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.ERROR_OCCUR_TYPE
import org.sopt.havit.util.ToastUtil
import org.sopt.havit.util.setOnSingleClickListener

@AndroidEntryPoint
class CreatePostActivity : BaseActivity<ActivityCreatePostBinding>(R.layout.activity_create_post) {
    private val createPostViewModel by viewModels<CreatePostViewModel>()
    private lateinit var chipGroup: CommunityCategoryChipGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindViewModel()
        setupEditText()
        onEditTextChanged()
        setCategory()
        observeUrlInfoStatus()
        onBackPressedDispatched()
        onCloseButtonClicked()
        observeUrlValid()
        handleCreatePostState()
        onClickCompleteButton()
    }

    private fun observeUrlValid() {
        lifecycleScope.launchWhenStarted {
            createPostViewModel.isUrlValid.collect { isValid ->
                if (isValid)
                    createPostViewModel.loadOgData()
            }
        }
    }

    private fun bindViewModel() {
        binding.viewModel = createPostViewModel
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

    private fun setCategory() {
        createPostViewModel.communityCategoryList.observe(this) {
            if (it.isEmpty()) return@observe
            onCategoryLoaded(it)
        }
    }

    private fun onCategoryLoaded(categoryList: List<CommunityCategoryRO>) {
        initChipGroup(categoryList)
        syncSelectedCategory()
        onCategoryChanged()
    }

    private fun initChipGroup(categoryList: List<CommunityCategoryRO>) {
        chipGroup = CommunityCategoryChipGroup(
            binding.cgCategory, categoryList
        )
    }

    private fun syncSelectedCategory() {
        createPostViewModel.setCommunityCategoryROList(chipGroup.selectedCategory)
    }

    private fun onCategoryChanged() {
        createPostViewModel.selectedCategory.observe(this) {
            createPostViewModel.setIsCategoryValid()
        }
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
        }
    }

    private fun observeUrlInfoStatus() {
        val urlInfoStatus = createPostViewModel.getUrlInfoStatus()
        urlInfoStatus.observe(this) {
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

    private fun onBackPressedDispatched() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleCloseState()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun onCloseButtonClicked() {
        binding.ibClose.setOnSingleClickListener {
            handleCloseState()
        }
    }

    private fun handleCloseState() {
        if (isUnderPosting()) {
            showCancelConfirmDialog()
        } else {
            finish()
        }
    }

    private fun isUnderPosting(): Boolean {
        return createPostViewModel.isWriting.value
    }

    private fun showCancelConfirmDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_POST_COMMUNITY, ::finish)
        dialog.show(supportFragmentManager, this.javaClass.name)
    }

    private fun handleCreatePostState() {
        createPostViewModel.createPostState.observe(this) {
            Log.d(TAG, "handleCreatePostState:  $it")
            when (it) {
                is NetworkStatus.Success -> {
                    val intent = Intent()
                    setResult(CREATE_COMMUNITY, intent)
                    finish()
                }

                is NetworkStatus.Error -> ToastUtil(this).makeToast(ERROR_OCCUR_TYPE)
                else -> {}/* no-op */
            }
        }
    }


    private fun onClickCompleteButton() {
        binding.btnComplete.setOnSingleClickListener {
            createPostViewModel.writePost()
        }
    }
}