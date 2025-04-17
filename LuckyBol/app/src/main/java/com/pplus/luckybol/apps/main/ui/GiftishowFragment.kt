package com.pplus.luckybol.apps.main.ui


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
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.mobilegift.ui.GiftishowHistoryActivity
import com.pplus.luckybol.apps.mobilegift.ui.GiftishowListFragment
import com.pplus.luckybol.apps.point.ui.PointHistoryActivity
import com.pplus.luckybol.apps.signin.ui.SnsLoginActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.GiftishowCategory
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentGiftishowBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [GiftishowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GiftishowFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
    }

    private var _binding: FragmentGiftishowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentGiftishowBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private var mFragmentAdapter: PagerAdapter? = null

    override fun init() {


        binding.tabLayoutGiftishowCategory.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_232323))
        binding.tabLayoutGiftishowCategory.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
        binding.tabLayoutGiftishowCategory.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
        binding.tabLayoutGiftishowCategory.setDistributeEvenly(false)
        binding.tabLayoutGiftishowCategory.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)

//        text_mobile_gift_retention_bol.setOnClickListener {
//            val intent = Intent(activity, PointConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        binding.textGiftishowBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.textGiftishowLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        if (LoginInfoManager.getInstance().isMember) {
            binding.textGiftishowRetentionPoint.visibility = View.VISIBLE
            binding.textGiftishowLogin.visibility = View.GONE
            binding.textGiftishowBuyHistory.visibility = View.VISIBLE
            setRetentionBol()
        } else {
            binding.textGiftishowRetentionPoint.visibility = View.GONE
            binding.textGiftishowLogin.visibility = View.VISIBLE
            binding.textGiftishowBuyHistory.visibility = View.GONE
        }

        binding.textGiftishowBuyHistory.setOnClickListener {
            val intent = Intent(activity, GiftishowHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.textGiftishowRetentionPoint.setOnClickListener {
            val intent = Intent(activity, PointHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        getCategory()
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (LoginInfoManager.getInstance().isMember) {
            binding.textGiftishowRetentionPoint.visibility = View.VISIBLE
            binding.textGiftishowLogin.visibility = View.GONE
            setRetentionBol()
        } else {
            binding.textGiftishowRetentionPoint.visibility = View.GONE
            binding.textGiftishowLogin.visibility = View.VISIBLE
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (LoginInfoManager.getInstance().isMember) {
                binding.textGiftishowRetentionPoint.visibility = View.VISIBLE
                binding.textGiftishowLogin.visibility = View.GONE
                setRetentionBol()
            } else {
                binding.textGiftishowRetentionPoint.visibility = View.GONE
                binding.textGiftishowLogin.visibility = View.VISIBLE
            }
        }
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                if (!isAdded) {
                    return
                }
                binding.textGiftishowRetentionPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString()))
            }
        })
    }

    private fun getCategory() {

        ApiBuilder.create().giftishowCategoryList.setCallback(object : PplusCallback<NewResultResponse<GiftishowCategory>> {

            override fun onResponse(call: Call<NewResultResponse<GiftishowCategory>>, response: NewResultResponse<GiftishowCategory>) {
                if (!isAdded) {
                    return
                }

                mFragmentAdapter = PagerAdapter(childFragmentManager)

                val categoryList = ArrayList<GiftishowCategory>()
                val category = GiftishowCategory()
                category.name = getString(R.string.word_total)
                categoryList.add(category)
                categoryList.addAll(response.datas!!)
                mFragmentAdapter!!.setTitle(categoryList)
                binding.pagerGiftishow.adapter = mFragmentAdapter
                binding.tabLayoutGiftishowCategory.setViewPager(binding.pagerGiftishow)
                binding.pagerGiftishow.currentItem = 0
            }

            override fun onFailure(call: Call<NewResultResponse<GiftishowCategory>>, t: Throwable, response: NewResultResponse<GiftishowCategory>) {

            }
        }).build().call()
    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        internal var mCategoryList: MutableList<GiftishowCategory>
        var fragMap: SparseArray<Fragment>
            internal set

        init {
            fragMap = SparseArray()
            mCategoryList = ArrayList()
        }

        fun setTitle(categoryList: MutableList<GiftishowCategory>) {

            this.mCategoryList = categoryList
            notifyDataSetChanged()
        }

        override fun getPageTitle(position: Int): String? {

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

            val fragment = GiftishowListFragment.newInstance(mCategoryList[position], position)
            fragMap.put(position, fragment)
            return fragment

        }
    }

    override fun getPID(): String {
        return "Main_mypage_ Mobile Coupon"
    }

    companion object {

        fun newInstance(): GiftishowFragment {

            val fragment = GiftishowFragment()
            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
