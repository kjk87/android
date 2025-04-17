package com.lejel.wowbox.apps.benefit.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.benefit.data.BenefitCalculateAdapter
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.common.ui.common.MonthPickerActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.BenefitCalculate
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentBenefitCalculateBinding
import retrofit2.Call
import java.util.Calendar

class BenefitCalculateFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private var _binding: FragmentBenefitCalculateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentBenefitCalculateBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: BenefitCalculateAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    private var mStart = ""
    private var mStartYear = ""
    private var mStartMonth = ""
    private var mEnd = ""
    private var mEndYear = ""
    private var mEndMonth = ""
    var mInputEnd = false
    var mInputStart = false

    override fun init() {
        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerBenefitCalculate.layoutManager = mLayoutManager
        mAdapter = BenefitCalculateAdapter()
        binding.recyclerBenefitCalculate.adapter = mAdapter

        binding.recyclerBenefitCalculate.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        getList(mPaging)
                    }
                }
            }
        })

        mAdapter!!.listener = object : BenefitCalculateAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        }

        binding.textBenefitCalculateStartDate.setOnClickListener {
            val intent = Intent(requireActivity(), MonthPickerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startDateLauncher.launch(intent)
        }

        binding.textBenefitCalculateEndDate.setOnClickListener {
            val intent = Intent(requireActivity(), MonthPickerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            endDateLauncher.launch(intent)
        }

        binding.textBenefitCalculateSearch.setOnClickListener {
            mStart = "$mStartYear-$mStartMonth-01 00:00:00"
            mEnd = "$mStartMonth-$mEndMonth-31 23:59:59"
            mPaging = 1
            getList(mPaging)
        }

        val calendar = Calendar.getInstance()
        mStartYear = (calendar.get(Calendar.YEAR) - 1).toString()
        mStartMonth = DateFormatUtils.formatTime(calendar.get(Calendar.MONTH) + 1)
        mEndYear = calendar.get(Calendar.YEAR).toString()
        mEndMonth = DateFormatUtils.formatTime(calendar.get(Calendar.MONTH) + 1)

        mStart = "$mStartYear-$mStartMonth-01 00:00:00"
        binding.textBenefitCalculateStartDate.text = "$mStartMonth-$mStartYear"
        mEnd = "$mEndYear-$mEndMonth-31 23:59:59"
        binding.textBenefitCalculateEndDate.text = "$mEndMonth-$mEndYear"

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["startDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mStart)))
        params["endDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEnd)))
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getBenefitCalculateList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<BenefitCalculate>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<BenefitCalculate>>>?, response: NewResultResponse<ListResultResponse<BenefitCalculate>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        binding.textBenefitCalculateCount.text = FormatUtil.getMoneyType(mTotalCount.toString())
                        if(mTotalCount == 0){
                            binding.textBenefitCalculateNotExist.visibility = View.VISIBLE
                            binding.layoutBenefitCalculateExist.visibility = View.GONE
                        }else{
                            binding.textBenefitCalculateNotExist.visibility = View.GONE
                            binding.layoutBenefitCalculateExist.visibility = View.VISIBLE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<BenefitCalculate>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<BenefitCalculate>>?) {
                hideProgress()
            }
        }).build().call()
    }

    val startDateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                mStartYear = data.getStringExtra(Const.YEAR)!!
                mStartMonth = data.getStringExtra(Const.MONTH)!!
                binding.textBenefitCalculateStartDate.text = "$mStartMonth-$mStartYear"
            }
        }
    }
    val endDateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                mEndYear = data.getStringExtra(Const.YEAR)!!
                mEndMonth = data.getStringExtra(Const.MONTH)!!
                binding.textBenefitCalculateEndDate.text = "$mEndMonth-$mEndYear"
            }
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() = BenefitCalculateFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}