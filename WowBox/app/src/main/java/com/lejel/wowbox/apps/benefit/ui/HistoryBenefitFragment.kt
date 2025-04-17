package com.lejel.wowbox.apps.benefit.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.benefit.data.HistoryBenefitAdapter
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.HistoryBenefit
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.FragmentHistoryBenefitBinding
import retrofit2.Call
import java.util.Calendar

class HistoryBenefitFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private var _binding: FragmentHistoryBenefitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHistoryBenefitBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: HistoryBenefitAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    private var mStart = ""
    private var mEnd = ""
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    var mInputEnd = false
    var mInputStart = false

    override fun init() {
        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerHistoryBenefit.layoutManager = mLayoutManager
        mAdapter = HistoryBenefitAdapter()
        binding.recyclerHistoryBenefit.adapter = mAdapter

        binding.recyclerHistoryBenefit.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.listener = object : HistoryBenefitAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        }

        binding.textHistoryBenefitStartDate.setOnClickListener {
            DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                startCalendar.set(i, i1, i2)
                //                        if(startCalendar.compareTo(mTodayCalendar) > 0){
                //                            ToastUtil.show(getActivity(), R.string.msg_input_startday_before_endday);
                //                            return;
                //                        }

                if (mInputEnd) {
                    if (startCalendar > endCalendar) {
                        ToastUtil.show(activity, R.string.msg_input_startday_before_endday)
                        return@OnDateSetListener
                    }
                }
                mInputStart = true

                binding.textHistoryBenefitStartDate.text = "${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)}-${i.toString()}"
//                mStart = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 00:00:00"
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.textHistoryBenefitEndDate.setOnClickListener {
            DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                endCalendar.set(i, i1, i2)
                if (mInputStart) {
                    if (startCalendar > endCalendar) {
                        ToastUtil.show(activity, R.string.msg_input_endday_after_startday)
                        return@OnDateSetListener
                    }
                }

                mInputEnd = true

                binding.textHistoryBenefitEndDate.text = "${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)}-${i.toString()}"
//                mEnd = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 23:59:59"
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.textHistoryBenefitSearch.setOnClickListener {
            mStart = startCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
            mEnd = endCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
            getTotalBenefit()
        }


        startCalendar.set(Calendar.DAY_OF_MONTH, 1)
        mStart = startCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
        binding.textHistoryBenefitStartDate.text = "${DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1)}-${DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH))}-${startCalendar.get(Calendar.YEAR).toString()}"
        mEnd = endCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
        binding.textHistoryBenefitEndDate.text = "${DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1)}-${DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH))}-${endCalendar.get(Calendar.YEAR).toString()}"

        getTotalBenefit()
    }

    private fun getTotalBenefit(){
        val params = HashMap<String, String>()
        params["startDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mStart)))
        params["endDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEnd)))
        showProgress("")
        ApiBuilder.create().getTotalBenefit(params).setCallback(object : PplusCallback<NewResultResponse<Double>>{
            override fun onResponse(call: Call<NewResultResponse<Double>>?, response: NewResultResponse<Double>?) {
                hideProgress()

                if(response?.result != null){
                    binding.textHistoryBenefitAmount.text = getString(R.string.format_dollar_unit2, FormatUtil.getMoneyTypeFloat(response.result.toString()))
                }

                mPaging = 1
                getList(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Double>>?, t: Throwable?, response: NewResultResponse<Double>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["startDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mStart)))
        params["endDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEnd)))
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getHistoryBenefitList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<HistoryBenefit>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<HistoryBenefit>>>?, response: NewResultResponse<ListResultResponse<HistoryBenefit>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        binding.textHistoryBenefitCount.text = FormatUtil.getMoneyType(mTotalCount.toString())
                        if(mTotalCount == 0){
                            binding.textHistoryBenefitNotExist.visibility = View.VISIBLE
                            binding.layoutHistoryBenefitExist.visibility = View.GONE
                        }else{
                            binding.textHistoryBenefitNotExist.visibility = View.GONE
                            binding.layoutHistoryBenefitExist.visibility = View.VISIBLE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<HistoryBenefit>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<HistoryBenefit>>?) {
                hideProgress()
            }
        }).build().call()
    }

    companion object {

        @JvmStatic
        fun newInstance() = HistoryBenefitFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}