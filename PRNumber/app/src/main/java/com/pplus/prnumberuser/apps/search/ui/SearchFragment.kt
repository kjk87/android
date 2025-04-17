package com.pplus.prnumberuser.apps.search.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.SearchHistoryManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentSearchBinding

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : BaseFragment<SearchActivity>() {

    override fun getPID(): String {
        return ""
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {
        binding.editSearchWord.setSingleLine()
        binding.editSearchWord.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.editSearchWord.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> search()
            }
            true
        }

        binding.layoutSearch.requestFocus()

        binding.imageSearch.setOnClickListener {
            search()
        }

        search()

    }

    fun search(word: String) {

        binding.editSearchWord.setText(word)
        search()
    }

    private fun search() {

        val word = binding.editSearchWord.text.toString().trim()
        PplusCommonUtil.hideKeyboard(activity)
        SearchHistoryManager.getInstance().add(word)

        val fragment = childFragmentManager.findFragmentById(R.id.search_container)

        if (fragment is SearchResultFragment) {
            fragment.reSearch(word)
        } else {
            openFragment(SearchResultFragment.newInstance(word))
        }
    }

    fun openFragment(fragment: BaseFragment<*>) {

        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.search_container, fragment, fragment.javaClass.simpleName)
        ft.addToBackStack(fragment.javaClass.simpleName)
        ft.commit()
    }

    override fun onResume() {

        super.onResume()
    }

    companion object {

        fun newInstance(): SearchFragment {

            val fragment = SearchFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
