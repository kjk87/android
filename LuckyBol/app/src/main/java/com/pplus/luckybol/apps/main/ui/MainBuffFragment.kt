package com.pplus.luckybol.apps.main.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.ui.*
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.main.data.BuffMemberThumbAdapter
import com.pplus.luckybol.apps.main.data.ContactSectionAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.BuffMember
import com.pplus.luckybol.core.network.model.dto.Contact
import com.pplus.luckybol.core.network.model.dto.NotificationBox
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.databinding.FragmentMainBuffBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainBuffFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainBuffFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getPID(): String? {
        return ""
    }

    private var _binding: FragmentMainBuffBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainBuffBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private lateinit var mAdapter : ContactSectionAdapter
    private lateinit var mBuffMemberAdapter : BuffMemberThumbAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mIsLast = false
    private var mLockListView = false
    private var mPaging = 0

    override fun init() {

        mLayoutManager = LinearLayoutManager(requireActivity())
        mAdapter = ContactSectionAdapter()
        binding.recyclerMainBuff.layoutManager = mLayoutManager
        binding.recyclerMainBuff.adapter = mAdapter

        binding.recyclerMainBuff.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })
        mBuffMemberAdapter = BuffMemberThumbAdapter()
        binding.recyclerMainBuffMember.adapter = mBuffMemberAdapter
        binding.recyclerMainBuffMember.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerMainBuffMember.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val position = parent.getChildAdapterPosition(view)
                if (position != 0) outRect.left = resources.getDimensionPixelSize(R.dimen.width_21) * -1
            }
        })

        binding.layoutMainBuffGuide.setOnClickListener {
            val intent = Intent(requireActivity(), BuffGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.layoutMainBuffGuide2.setOnClickListener {
            val intent = Intent(requireActivity(), BuffGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.layoutMainBuffMake.setOnClickListener {
            val intent = Intent(requireActivity(), BuffMakeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            buffMakeLauncher.launch(intent)
        }

        binding.layoutMainBuffReq.setOnClickListener {
            val intent = Intent(requireActivity(), BuffReceiveActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        getReqCont()
        binding.layoutMainBuffLoading.visibility = View.VISIBLE
        getMyBuff()
        getBuffWithdrawNotification()

    }

    private fun getBuffWithdrawNotification(){
        ApiBuilder.create().getBuffWithdrawNotification().setCallback(object : PplusCallback<NewResultResponse<NotificationBox>>{
            override fun onResponse(call: Call<NewResultResponse<NotificationBox>>?,
                                    response: NewResultResponse<NotificationBox>?) {
                if (!isAdded) {
                    return
                }
                if(response?.data != null){
                    val intent = Intent(requireActivity(), AlertForcedExitActivity::class.java)
                    intent.putExtra(Const.DATA, response.data)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    launcher.launch(intent)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<NotificationBox>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<NotificationBox>?) {

            }
        }).build().call()
    }

    private fun getReqCont(){
        ApiBuilder.create().getRequestCount().setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {
                if (!isAdded) {
                    return
                }
                if(response?.data != null && response.data!! > 0){
                    binding.textMainBuffReqCount.visibility = View.VISIBLE
                    if(response.data!! > 99){
                        binding.textMainBuffReqCount.text = "99+"
                    }else{
                        binding.textMainBuffReqCount.text = response.data!!.toString()
                    }
                }else{
                    binding.textMainBuffReqCount.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    private fun  getMyBuff(){
        ApiBuilder.create().getBuffMember().setCallback(object : PplusCallback<NewResultResponse<BuffMember>>{
            override fun onResponse(call: Call<NewResultResponse<BuffMember>>?,
                                    response: NewResultResponse<BuffMember>?) {

                if (!isAdded) {
                    return
                }
                if(response?.data != null && response.data!!.buff != null){
                    val buff = response.data!!.buff!!

                    mBuffMemberAdapter.listener = object : BuffMemberThumbAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int, view: View) {
                            val intent = Intent(requireActivity(), BuffActivity::class.java)
                            intent.putExtra(Const.DATA, buff)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            launcher.launch(intent)
                        }
                    }

                    binding.textMainBuffMemberCount.setOnClickListener {
                        val intent = Intent(requireActivity(), BuffActivity::class.java)
                        intent.putExtra(Const.DATA, buff)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        launcher.launch(intent)
                    }

                    binding.layoutMainBuffCash.setOnClickListener {
                        val intent = Intent(requireActivity(), BuffActivity::class.java)
                        intent.putExtra(Const.DATA, buff)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        launcher.launch(intent)
                    }

                    binding.layoutMainBuffBol.setOnClickListener {
                        val intent = Intent(requireActivity(), BuffActivity::class.java)
                        intent.putExtra(Const.DATA, buff)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        launcher.launch(intent)
                    }

                    binding.layoutMainBuffNone.visibility = View.GONE
                    binding.layoutMainBuffExist.visibility = View.VISIBLE

                    binding.textMainBuffTile.text = buff.title
                    binding.textMainBuffTotalBol.text = FormatUtil.getMoneyTypeFloat(buff.totalDividedBol.toString())
                    binding.textMainBuffTotalCash.text = FormatUtil.getMoneyTypeFloat(buff.totalDividedPoint.toString())

                    getBuffMemberList(buff.seqNo!!)
                }else{
                    binding.layoutMainBuffNone.visibility = View.VISIBLE
                    binding.layoutMainBuffExist.visibility = View.GONE
                }
                getMemberCount()
            }

            override fun onFailure(call: Call<NewResultResponse<BuffMember>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<BuffMember>?) {
            }
        }).build().call()
    }

    private fun  getBuffMemberList(buffSeqNo:Long) {
        val params = HashMap<String, String>()
        params["buffSeqNo"] = buffSeqNo.toString()
        params["size"] = "5"
        params["includeMe"] = "true"
        ApiBuilder.create().getBuffMemberList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuffMember>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuffMember>>>?,
                                    response: NewResultResponse<SubResultResponse<BuffMember>>?) {

                if (!isAdded) {
                    return
                }

                if(response?.data != null){
                    binding.textMainBuffMemberCount.text = getString(R.string.format_count_unit4, response.data!!.totalElements.toString())
                    mBuffMemberAdapter.setDataList(response.data!!.content as MutableList<BuffMember>)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuffMember>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<BuffMember>>?) {

            }
        }).build().call()

    }
    private fun getMemberCount(){
        ApiBuilder.create().getContactMemberCount().setCallback(object : PplusCallback<NewResultResponse<Int>>{
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {

                if (!isAdded) {
                    return
                }

                if(response?.data != null){
                    mAdapter.mExistMember = (response.data!! > 0)
                    mAdapter.mMemberCount = response.data!!
                }else{
                    mAdapter.mExistMember = false
                }

                mPaging = 0
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {
            }
        }).build().call()
    }

    private fun listCall(page: Int){
        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        ApiBuilder.create().getContactListWithMember(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Contact>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Contact>>>?,
                                    response: NewResultResponse<SubResultResponse<Contact>>?) {

                if (!isAdded) {
                    return
                }
                binding.layoutMainBuffLoading.visibility = View.GONE
                if (response?.data != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mAdapter.clear()
                    }
                    mLockListView = false

                    val dataList = response.data!!.content!!
                    mAdapter.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Contact>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<Contact>>?) {
            }
        }).build().call()
    }

    val buffMakeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            val intent = Intent(requireActivity(), AlertBuffMakeCompleteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }
        getMyBuff()
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getMyBuff()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainBuffFragment().apply {
            arguments = Bundle().apply {
//                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
            }
        }
    }
}