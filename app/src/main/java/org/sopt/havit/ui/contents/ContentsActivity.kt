package org.sopt.havit.ui.contents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.ActivityContentsBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.category.CategoryContentModifyActivity
import org.sopt.havit.ui.category.CategoryContentModifyActivity.Companion.RESULT_DELETE_CATEGORY
import org.sopt.havit.ui.category.CategoryContentModifyActivity.Companion.RESULT_MODIFY_CATEGORY
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_ID
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_IMAGE_ID
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_ITEM_LIST
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_NAME
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_POSITION
import org.sopt.havit.ui.category.CategoryViewModel
import org.sopt.havit.ui.contents.DialogContentsFilterFragment.Companion.CONTENTS_FILTER
import org.sopt.havit.ui.contents.more.ContentsMoreFragment
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.ui.search.SearchActivity
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.*
import java.io.Serializable

@AndroidEntryPoint
class ContentsActivity : BaseBindingActivity<ActivityContentsBinding>(R.layout.activity_contents) {
    private lateinit var contentsAdapter: ContentsAdapter
    private val contentsViewModel: ContentsViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(this) }
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private var categoryId = 0
    private var categoryName = "error"
    private var categoryIconId = 0
    private var categoryPosition = 0
    private var contentsOption = "all" // chip??? ?????? (??????/????????????/?????????/??????)
    private var contentsFilter = "created_at" // ?????? ?????? (?????????/?????????/???????????????)
    private lateinit var currentHavitView: ImageView
    private var currentHavitPosition = -1
    private var deleteContentsPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vmContents = contentsViewModel
        binding.vmCategory = categoryViewModel

        setContentView(binding.root)

        setCategoryInfo()
        initValue()
        initAdapter()
        dataObserve()
        changeLayout()
        clickBack()
        initSearchSticky()
        clickAddContents()
        setOrderDialog()
        moveSearch()
        clickItemView()
        setChipOrder()
        setCategoryListDialog()
        clickModify()
        getModifyData()
        clickItemHavit()
        clickItemMore()
        refreshContentsData()
        setHavitAction()
        observeDeleteState()
    }

    override fun onResume() {
        super.onResume()
        requestContentsData()
    }

    private fun requestContentsData() {
        if (categoryId <= -1) {
            contentsViewModel.getAllContents(contentsOption, contentsFilter)
            contentsViewModel.setCategoryName(categoryName)
        } else {
            contentsViewModel.getContentsByCategory(categoryId, contentsOption, contentsFilter)
        }
    }

    private fun initAdapter() {
        contentsAdapter = ContentsAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun setCategoryInfo() {
        // ???????????? ?????? ????????? ????????? ?????? ??????
        categoryViewModel.requestCategoryTaken()

        // ???????????? ????????? ???????????? ???????????? ContentsActivity??? ????????? ??????
        categoryId =
            intent.getIntExtra(CATEGORY_ID, 0).also { binding.categoryId = it }
        intent.getStringExtra(CATEGORY_NAME)?.let {
            categoryName = it
        }
        categoryIconId = intent.getIntExtra(CATEGORY_IMAGE_ID, 0)
        categoryPosition = intent.getIntExtra(CATEGORY_POSITION, 0)

        // ???????????? ?????? ?????????
        setCategoryName(categoryName)
    }

    private fun refreshContentsData() {
        binding.layoutNetworkError.ivRefresh.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotation_refresh))
            requestContentsData()
        }
    }

    private fun initValue() {
        // ???????????? ?????????
        layout = LINEAR_MIN_LAYOUT
        // ?????? ??? ?????? ?????????, ???????????????????????? -2?????? ????????? ???????????? ??????
        contentsOption = if (categoryId == -2) "true" else "all"
        contentsFilter = "created_at"
    }

    private fun dataObserve() {
        with(contentsViewModel) {
            loadState.observe(this@ContentsActivity) {
                // ?????? ???????????? ???????????? ???????????? ?????? ??? shimmer ????????? ?????????
                if (it == NetworkState.LOADING) {
                    binding.sflContents.startShimmer()
                    binding.sflCount.startShimmer()
                } else {
                    binding.sflContents.stopShimmer()
                    binding.sflCount.stopShimmer()
                }
            }
            contentsList.observe(this@ContentsActivity) {
                // ????????? ????????? ????????????
                contentsAdapter.submitList(it.toList())
            }
            categoryName.observe(this@ContentsActivity) {
                binding.tvCategory.text = it
            }
        }
    }

    private fun changeLayout() {
        binding.ivLayout.setOnClickListener {
            // ?????? viewholder??? binding?????? ?????? ?????? ?????? ??????
            binding.rvContents.removeAllViews()

            when (layout) {
                LINEAR_MIN_LAYOUT -> {
                    layout = GRID_LAYOUT
                    binding.rvContents.layoutManager = GridLayoutManager(this@ContentsActivity, 2)
                    binding.ivLayout.setImageResource(R.drawable.ic_layout_4)
                }
                GRID_LAYOUT -> {
                    layout = LINEAR_MAX_LAYOUT
                    binding.rvContents.layoutManager = LinearLayoutManager(this@ContentsActivity)
                    binding.ivLayout.setImageResource(R.drawable.ic_layout_2)
                }
                LINEAR_MAX_LAYOUT -> {
                    layout = LINEAR_MIN_LAYOUT
                    binding.rvContents.layoutManager = LinearLayoutManager(this@ContentsActivity)
                    binding.ivLayout.setImageResource(R.drawable.ic_layout_3)
                }
            }
        }
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun initSearchSticky() {
        binding.svMain.run {
            header = binding.clOrderOption
            stickListener = { _ ->
                Log.d("LOGGER_TAG", "stickListener")
            }
            freeListener = { _ ->
                Log.d("LOGGER_TAG", "freeListener")
            }
        }
    }

    private fun clickAddContents() {
        binding.tvAddContents.setOnClickListener {
            SaveFragment(categoryName).show(supportFragmentManager, "?????? ?????????")
        }
    }

    // ?????????, ?????????, ?????? ????????? ?????????????????? ?????? ??????
    private fun setOrderDialog() {
        binding.clOrder.setOnClickListener {
            val dialog = DialogContentsFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(CONTENTS_FILTER, contentsFilter)
                }
            }
            dialog.show(supportFragmentManager, "contentsOrder")

            // ?????? ?????? ??? ????????? ??????
            dialog.setFilterClickListener(
                object : DialogContentsFilterFragment.OnFilterClickListener {
                    override fun onClick(filter: String) {
                        contentsFilter = filter
                        binding.tvOrder.text = when (filter) {
                            "created_at" -> "?????????"
                            "reverse" -> "?????????"
                            else -> "?????? ?????????"
                        }
                        // ?????? ??????
                        requestContentsData()
                        dialog.dismiss()
                    }
                })
        }
    }

    private fun setCategoryListDialog() {
        binding.clCategory.setOnClickListener {
            DialogContentsCategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_ITEM_LIST, categoryViewModel.categoryList.value)
                    putInt(CATEGORY_ID, categoryId)
                }
            }.show(
                supportFragmentManager,
                "categoryList"
            )
        }
    }

    private fun moveSearch() {
        binding.clSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra(
                CATEGORY_NAME,
                "${contentsViewModel.categoryName.value}"
            )
            startActivity(intent)
        }
    }

    private fun clickItemView() {
        contentsAdapter.setItemClickListener(object : ContentsAdapter.OnItemClickListener {
            override fun onWebClick(v: View, position: Int) {
                val intent = Intent(v.context, WebActivity::class.java)
                contentsViewModel.contentsList.value?.get(position)
                    ?.let {
                        intent.putExtra("url", it.url)
                        intent.putExtra("contentsId", it.id)
                        intent.putExtra("isSeen", it.isSeen)
                    }
                startActivity(intent)
            }
        })
    }

    // ????????? ????????? ?????? ??? ?????????
    private fun clickItemMore() {
        contentsAdapter.setItemSetClickListner(object : ContentsAdapter.OnItemSetClickListener {
            override fun onSetClick(v: View, position: Int) {
                deleteContentsPosition = position

                val dataMore = contentsViewModel.contentsList.value?.get(position)?.let {
                    ContentsMoreData(
                        it.id,
                        it.image,
                        it.title,
                        it.createdAt,
                        it.url,
                        it.isNotified,
                        it.notificationTime
                    )
                }

                val showDeleteDialog: () -> Unit = {
                    val dialog =
                        DialogUtil(DialogUtil.REMOVE_CONTENTS) {
                            contentsViewModel.deleteContents(
                                requireNotNull(dataMore?.id)
                            )
                        }
                    dialog.show(supportFragmentManager, this.javaClass.name)
                }
                val bundle = setBundle(dataMore, showDeleteDialog, position)
                val dialog = ContentsMoreFragment()
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "setting")
            }
        })
    }

    // ContentsMoreFragment??? ?????? bundle ??????
    private fun setBundle(
        dataMore: ContentsMoreData?,
        showDeleteDialog: () -> Unit,
        position: Int
    ): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(ContentsMoreFragment.CONTENTS_MORE_DATA, dataMore)
        bundle.putSerializable(
            ContentsMoreFragment.SHOW_DELETE_DIALOG,
            showDeleteDialog as Serializable
        )
        bundle.putInt(ContentsMoreFragment.POSITION, position)
        return bundle
    }

    private fun observeDeleteState() {
        contentsViewModel.requestDeleteState.observe(this) {
            when (it) {
                NetworkState.FAIL -> {
                    ToastUtil(this).makeToast(
                        ERROR_OCCUR_TYPE
                    )
                }
                NetworkState.SUCCESS -> {
                    val list =
                        contentsAdapter.currentList.toMutableList() // mutable??? ???????????? ??????(??????) ??????
                    list.removeAt(deleteContentsPosition)
                    // ???????????? ????????? ????????? ????????? ???????????? -> observer??? ?????? adapter??? list??? ???????????? ??????
                    contentsViewModel.updateContentsList(list)
                    contentsViewModel.decreaseContentsCount(1) // ????????? ?????? 1 ??????

                    ToastUtil(this).makeToast(
                        CONTENT_DELETE_TYPE
                    )
                }
                else -> {}
            }
        }
    }

    private fun setChipOrder() {
        with(binding) {
            chAll.setOnClickListener {
                contentsOption = "all"
                requestContentsData()
            }
            chSeen.setOnClickListener {
                contentsOption = "true"
                requestContentsData()
            }
            chUnseen.setOnClickListener {
                contentsOption = "false"
                requestContentsData()
            }
            chAlarm.setOnClickListener {
                contentsOption = "notified"
                requestContentsData()
            }
        }
    }

    // ??????????????? ???????????? ???
    private fun clickModify() {
        binding.tvModify.setOnClickListener {
            // ???????????? ?????? list
            val categoryTitleList = ArrayList<String>()
            for (item in categoryViewModel.categoryList.value!!)
                categoryTitleList.add(item.title)

            // ???????????? ?????? ?????? ?????? intent
            val intent = Intent(this, CategoryContentModifyActivity::class.java).apply {
                putExtra(CATEGORY_ID, categoryId)
                putExtra(CATEGORY_NAME, categoryName)
                putExtra(CATEGORY_IMAGE_ID, categoryIconId)
                putStringArrayListExtra("categoryNameList", categoryTitleList)
                putExtra("preActivity", "ContentsActivity")
            }

            getResult.launch(intent)
        }
    }

    // ?????? ????????? ??? ??????/?????? ????????? ?????????
    private fun getModifyData() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_DELETE_CATEGORY -> { // ??????
                    finish()
                }
                RESULT_MODIFY_CATEGORY -> { // ???????????? ?????? & ????????? ??????
                    // ????????? ??????????????? ????????? ?????????
                    categoryName = it.data?.getStringExtra(CATEGORY_NAME) ?: "null"
                    categoryIconId =
                        it.data?.getIntExtra(CATEGORY_IMAGE_ID, 0) ?: 0

                    setCategoryName(categoryName)
                    modifyCategoryItemData(categoryPosition, categoryName, categoryIconId)
                }
            }
        }
    }

    private fun setCategoryName(name: String) {
        contentsViewModel.setCategoryName(name)
    }

    private fun modifyCategoryItemData(position: Int, name: String, imageId: Int) {
        categoryViewModel.setCategoryListItemName(position, name)
        categoryViewModel.setCategoryListItemIconId(position, imageId)
    }

    // ?????? ?????? ??? ????????? ?????? ??????
    private fun clickItemHavit() {
        contentsAdapter.setHavitClickListener(object : ContentsAdapter.OnItemHavitClickListener {
            override fun onHavitClick(v: ImageView, position: Int) {
                // ?????? ??????
                contentsViewModel.setIsSeen(contentsAdapter.currentList[position].id)
                currentHavitView = v
                currentHavitPosition = position
            }
        })
    }

    private fun setHavitAction() {
        contentsViewModel.requestSeenState.observe(this@ContentsActivity) {
            when (it) {
                NetworkState.SUCCESS -> setSuccessHaivtAction()
                NetworkState.FAIL -> setFailAction()
                else -> {}
            }
        }
    }

    private fun setSuccessHaivtAction() {
        with(contentsAdapter) {
            // ?????? ????????? ???????????? ?????? ???????????? ????????? ????????? ?????????
            if (!currentList[currentHavitPosition].isSeen) {
                ToastUtil(this@ContentsActivity).makeToast(CONTENT_CHECK_COMPLETE_TYPE)
            }

            currentList[currentHavitPosition].isSeen = !currentList[currentHavitPosition].isSeen

            // ?????? ?????????
            val isSeen = (currentHavitView.tag == "seen")
            currentHavitView.tag = if (isSeen) "unseen" else "seen"
            currentHavitView.setImageResource(if (isSeen) R.drawable.ic_contents_unread else R.drawable.ic_contents_read_2)

            // ??? ????????? ???????????? ?????? ?????? ??? ??????
            if ((contentsOption == "true" || contentsFilter == "seen_at") && currentHavitView.tag == "unseen") {
                removeContentsItem(currentHavitPosition)
            }
            // ??? ??? ????????? ???????????? ?????? ?????? ??? ??????
            else if (contentsOption == "false" && currentHavitView.tag == "seen") {
                removeContentsItem(currentHavitPosition)
            }
        }

        contentsViewModel.initRequestState()
    }

    private fun removeContentsItem(index: Int) {
        val list =
            contentsAdapter.currentList.toMutableList() // mutable??? ???????????? ??????(??????) ??????
        list.removeAt(index)
        // ???????????? ????????? ????????? ????????? ???????????? -> observer??? ?????? adapter??? list??? ???????????? ??????
        contentsViewModel.updateContentsList(list)
        contentsViewModel.decreaseContentsCount(1) // ????????? ?????? 1 ??????
    }

    private fun setFailAction() {
        ToastUtil(this).makeToast(ERROR_OCCUR_TYPE)
        contentsViewModel.initRequestState()
    }

    companion object {
        const val LINEAR_MIN_LAYOUT = 1
        const val GRID_LAYOUT = 2
        const val LINEAR_MAX_LAYOUT = 3
        var layout = 1
    }
}
