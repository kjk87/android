package com.pplus.prnumberuser.apps.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.category.data.CategoryAdapter
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.FragmentMainDeliveryBinding
import retrofit2.Call
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainDeliveryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeliveryPageFragment : BaseFragment<BaseActivity>() {

    override fun getPID(): String? {
        return ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainDeliveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainDeliveryBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mAdapter:CategoryAdapter? = null

    override fun init() {

        binding.recyclerMainDeliveryCategory.layoutManager = GridLayoutManager(requireActivity(), 4)
        mAdapter = CategoryAdapter()
        binding.recyclerMainDeliveryCategory.adapter = mAdapter
        getCategory()
    }

    private fun getCategory() {

        val params = HashMap<String, String>()
        params["major"] = "8"
        showProgress("")
        ApiBuilder.create().getCategoryMinorList(params).setCallback(object : PplusCallback<NewResultResponse<CategoryMinor>> {
            override fun onResponse(call: Call<NewResultResponse<CategoryMinor>>?, response: NewResultResponse<CategoryMinor>?) {
                hideProgress()
                val categoryList = response!!.datas
                val list = arrayListOf<CategoryMinor>()

                list.add(CategoryMinor(-1L, 8, getString(R.string.word_total)))

                if (categoryList != null) {
                    list.addAll(categoryList)
                }

                mAdapter!!.setDataList(list)

            }

            override fun onFailure(call: Call<NewResultResponse<CategoryMinor>>?, t: Throwable?, response: NewResultResponse<CategoryMinor>?) {
                hideProgress()
            }
        }).build().call()
    }

    companion object {
        @JvmStatic
        fun newInstance() = DeliveryPageFragment().apply {
            arguments = Bundle().apply {
//                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
            }
        }
    }
}