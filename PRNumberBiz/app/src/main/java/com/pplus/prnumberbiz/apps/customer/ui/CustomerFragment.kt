package com.pplus.prnumberbiz.apps.customer.ui


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.group.ui.GroupAddActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_customer.*

import network.common.PplusCallback
import retrofit2.Call
import java.util.ArrayList
import java.util.HashMap


/**
 * A simple [Fragment] subclass.
 * Use the [CustomerFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 */
class CustomerFragment : BaseFragment<BaseActivity>() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_customer
    }

    override fun initializeView(container: View?) {

    }

    //    private var mFragmentAdapter: PagerAdapter? = null
    var mCurrentPosition = 0
    var mGroupList: MutableList<Group>? = null

    override fun init() {

        text_customer_title.setText(R.string.word_contact_customer)

        text_customer_title.setOnClickListener {
            activity?.finish()
        }

        tabLayout_customer_category.setSelectedIndicatorColors(ContextCompat.getColor(activity!!, R.color.color_579ffb))
        tabLayout_customer_category.setCustomTabView(R.layout.item_group_tab, R.id.text_group_title)
        tabLayout_customer_category.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_0))
        tabLayout_customer_category.setDistributeEvenly(false)
        tabLayout_customer_category.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_72), 0)

        text_customer_group_add.setOnClickListener {
            val intent = Intent(activity, GroupAddActivity::class.java)
            intent.putExtra(Const.KEY, EnumData.CustomerType.customer)
            if (mGroupList != null && mGroupList!!.isNotEmpty()) {
                intent.putExtra(Const.GROUP, mGroupList as ArrayList<Group>)
            }
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity!!.startActivityForResult(intent, Const.REQ_GROUP_CONFIG)
        }

        text_customer_add.setOnClickListener {
            val intent = Intent(activity, CustomerAddActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_CUSTOMER)

//            val intent = Intent(activity, CustomerDirectRegActivity::class.java)
//            intent.putExtra(Const.KEY, EnumData.CustomerType.customer)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            activity!!.startActivityForResult(intent, Const.REQ_CUSTOMER)
        }

        pager_customer.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mCurrentPosition = position
            }
        })
        text_customer_add.isEnabled = false
        getGroupAll()
    }

    fun getGroupAll() {

        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
                if (!isAdded) {
                    return
                }

                text_customer_add.isEnabled = true

                if (childFragmentManager.fragments != null) {
                    childFragmentManager.fragments.clear()
                }

                val adapter = PagerAdapter(childFragmentManager)
                pager_customer.adapter = adapter

                mGroupList = response.datas
                if (mGroupList != null && mGroupList!!.isNotEmpty()) {

                    for (i in mGroupList!!.indices) {
                        if (mGroupList!![i].isDefaultGroup) {

                            mGroupList!![i].name = getString(R.string.word_all_en)
                            break
                        }
                    }

                    if (mCurrentPosition > mGroupList!!.size) {
                        mCurrentPosition = 0
                    }

                    adapter.setTitle(mGroupList!!)
                    tabLayout_customer_category.setViewPager(pager_customer)
                    pager_customer.currentItem = mCurrentPosition

                }

            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()
    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        internal var mGroupList: MutableList<Group>
        var fragMap: SparseArray<Fragment>
            internal set

        init {
            fragMap = SparseArray()
            mGroupList = ArrayList()
        }

        fun setTitle(groupList: MutableList<Group>) {

            this.mGroupList = groupList
            notifyDataSetChanged()
        }

        override fun getPageTitle(position: Int): String? {

            return mGroupList[position].name
        }

        override fun getCount(): Int {

            return mGroupList.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
            fragMap.remove(position)
        }

        fun clear() {

            mGroupList.clear()
            fragMap = SparseArray()
            notifyDataSetChanged()
        }

        fun getFragment(key: Int): Fragment {

            return fragMap.get(key)
        }

        override fun getItem(position: Int): Fragment {
            val fragment = CustomerListFragment.newInstance(mGroupList[position])
            fragMap.put(position, fragment)
            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            Const.REQ_GROUP_CONFIG, Const.REQ_CUSTOMER -> getGroupAll()
        }
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        fun newInstance(): CustomerFragment {

            val fragment = CustomerFragment()
            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
