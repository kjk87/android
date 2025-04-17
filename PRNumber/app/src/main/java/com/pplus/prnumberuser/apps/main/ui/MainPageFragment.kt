package com.pplus.prnumberuser.apps.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.card.ui.QRPayActivity
import com.pplus.prnumberuser.apps.category.ui.CategoryPageFragment
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.CategoryInfoManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.search.ui.LocationAroundPageActivity
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainPageBinding
import java.util.*

class MainPageFragment : BaseFragment<BaseActivity>() {


    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mAdapter: PagerAdapter? = null
    var mSort = "distance"
    var currentPos = 0

    override fun init() {

//        if (PreferenceUtil.getDefaultPreference(activity).get(Const.GUIDE_POINT_PAGE, true)) {
//            val intent = Intent(activity, AlertPointPageActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        binding.tabLayoutPage.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_232323))
        binding.tabLayoutPage.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
        binding.tabLayoutPage.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
//        tabLayout_category_page.setDistributeEvenly(false)
        binding.tabLayoutPage.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)


        binding.textPageAddress.setSingleLine()
        binding.textPageAddress.setOnClickListener {
            val intent = Intent(activity, LocationSelectActivity::class.java)
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val x = location[0] + it.width / 2
            val y = location[1] + it.height / 2
            intent.putExtra(Const.X, x)
            intent.putExtra(Const.Y, y)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            locationSelectLauncher.launch(intent)
        }

//        text_main_page_search.setOnClickListener {
//            val intent = Intent(activity, SearchActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//            startActivity(intent)
//        }

        binding.imageMainPageNumber.setOnClickListener {
            val intent = Intent(activity, PadActivity::class.java)
            intent.putExtra(Const.KEY, Const.PAD)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.textMainPageViewMap.setOnClickListener {
            val intent = Intent(activity, LocationAroundPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.textMainPageLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        binding.textMainPageQr.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            if(LoginInfoManager.getInstance().user.verification!!.media != "external"){
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {
                    override fun onCancel() {}
                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val intent = Intent(activity, VerificationMeActivity::class.java)
                                intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                verificationLauncher.launch(intent)
                            }
                        }
                    }
                }).builder().show(activity)
            }else{
                val intent = Intent(activity, QRPayActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }


        }

        loginCheck()
        setData()
    }

    private fun loginCheck(){
        if (LoginInfoManager.getInstance().isMember) {
            binding.textMainPageLogin.visibility = View.GONE
            binding.textMainPageTitle.visibility = View.VISIBLE
        }else{
            binding.textMainPageLogin.visibility = View.VISIBLE
            binding.textMainPageTitle.visibility = View.GONE
        }
    }

    val locationSelectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            currentPos = binding.pagerPage.currentItem
            setData()
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    private fun setData() {

        PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
            override fun onSuccess() {
                if (!isAdded) {
                    return
                }
                PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                    override fun onResult(address: String) {

                        if (!isAdded) {
                            return
                        }

                        binding.textPageAddress.text = address
                    }
                })
                getCategory()
            }
        })
    }

    private fun getCategory() {

        val categoryList = CategoryInfoManager.getInstance().categoryList.filter {
            it.type == "offline" || it.type == "common"
        }

        val list = arrayListOf<CategoryMajor>()

        val category = CategoryMajor()
        category.seqNo = -1L
        category.name = getString(R.string.word_total)
        list.add(category)

        if (categoryList != null) {
            list.addAll(categoryList)
        }

        mAdapter = PagerAdapter(childFragmentManager)
        binding.pagerPage.adapter = mAdapter
        mAdapter!!.setTitle(list)
        binding.tabLayoutPage.setViewPager(binding.pagerPage)
        binding.pagerPage.currentItem = currentPos
    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        internal var mCategoryList: MutableList<CategoryMajor>
        var fragMap: SparseArray<Fragment>
            internal set

        init {
            fragMap = SparseArray()
            mCategoryList = ArrayList()
        }

        fun setTitle(categoryList: MutableList<CategoryMajor>) {

            this.mCategoryList = categoryList
            notifyDataSetChanged()
        }

        override fun getPageTitle(position: Int): String? {

            if (position == 0) {
                return getString(R.string.word_total)
            }

            return mCategoryList[position].name
        }

        override fun getCount(): Int {

            return mCategoryList.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
            fragMap.remove(position)
        }

        fun clear() {

            mCategoryList.clear()
            fragMap = SparseArray()
            notifyDataSetChanged()
        }

        fun getFragment(key: Int): Fragment {

            return fragMap.get(key)
        }

        override fun getItem(position: Int): Fragment {

            val fragment = CategoryPageFragment.newInstance(mCategoryList[position])
//            val fragment = CategoryGoodsFragment.newInstance(mCategoryList[position])
            fragMap.put(position, fragment)
            return fragment
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                MainPageFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
