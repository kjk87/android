package com.pplus.prnumberuser.apps.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.SearchHistoryManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.main.ui.PadActivity
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivitySearchBinding
import com.pplus.utils.part.utils.StringUtils

class SearchDeliveryActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return "Main_number_search_detail"
    }

    private lateinit var binding: ActivitySearchBinding

    override fun getLayoutView(): View {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val searchData = intent.getStringExtra(Const.SCHEMA_SEARCH)

        binding.imageSearch.setOnClickListener {
            search()
        }

        binding.editSearchWord.setSingleLine()
        binding.editSearchWord.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.editSearchWord.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> search()
            }
            true
        }

        if (StringUtils.isNotEmpty(searchData)) {
            binding.layoutSearch.requestFocus()
            binding.editSearchWord.setText(searchData)
            search()
        } else {
            binding.layoutSearch.requestFocus()
//            search()
        }

        binding.imageSearchNumber.setOnClickListener {
            val intent = Intent(this, PadActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    fun search(word: String) {

        binding.editSearchWord.setText(word)
        search()
    }

    private fun search() {

        val word = binding.editSearchWord.text.toString().trim()
        PplusCommonUtil.hideKeyboard(this)
        SearchHistoryManager.getInstance().add(word)

        val fragment = supportFragmentManager.findFragmentById(R.id.search_container)

//        if (fragment is SearchResultFragment) {
//            fragment.reSearch(word)
//        } else {
//            openFragment(SearchResultFragment.newInstance(word))
//        }

        if (fragment is DeliverySearchResultFragment) {
            fragment.reSearch(word)
        } else {
            openFragment(DeliverySearchResultFragment.newInstance(word))
        }
    }

    fun openFragment(fragment: BaseFragment<*>) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.search_container, fragment, fragment.javaClass.simpleName)
        ft.addToBackStack(fragment.javaClass.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_search), ToolbarOption.ToolbarMenu.LEFT)
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_dialpad)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    } //                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    //                    val intent = Intent(this, PadActivity::class.java)
                    //                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    //                    startActivityForResult(intent, Const.REQ_SEARCH)
                    //                }
                }
            }
        }
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
