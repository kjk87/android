package com.pplus.prnumberbiz.apps.menu.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.core.content.ContextCompat
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.MenuCategoryConfigActivity
import com.pplus.prnumberbiz.apps.goods.ui.MenuRegActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.dto.PageGoodsCategory
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_menu_config.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class MenuConfigActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_menu_config
    }

    var mPage: Page? = null
    var categoryList: MutableList<PageGoodsCategory>? = null
    var mAdapter: PagerAdapter? = null
    var mKey = ""

    override fun initializeView(savedInstanceState: Bundle?) {

        mKey = intent.getStringExtra(Const.KEY)


        mPage = LoginInfoManager.getInstance().user.page

        tabLayout_category_menu.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_579ffb))
        tabLayout_category_menu.setCustomTabView(R.layout.item_goods_category_tab, R.id.text_goods_category_tab)
        tabLayout_category_menu.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_10))
        tabLayout_category_menu.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_80), 0)


        text_menu_config_reg_menu.setOnClickListener {
            val intent = Intent(this, MenuRegActivity::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Const.REQ_REG)
        }

        if (mPage!!.type == EnumData.PageTypeCode.store.name) {
            text_menu_config_reg_menu2.setText(R.string.msg_reg_menu)
            text_menu_config_reg_menu.setText(R.string.msg_reg_menu)
            text_menu_config_not_exist1.setText(R.string.msg_not_exist_menu)
            text_menu_config_not_exist2.setText(R.string.msg_not_exist_menu_desc)
        } else {
            text_menu_config_reg_menu2.setText(R.string.msg_reg_goods)
            text_menu_config_reg_menu.setText(R.string.msg_reg_goods)
            text_menu_config_not_exist1.setText(R.string.msg_not_exist_goods)
            text_menu_config_not_exist2.setText(R.string.msg_not_exist_goods_desc)
        }

        text_menu_config_reg_menu2.setOnClickListener {
            if(categoryList!!.size == 1){
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_first_reg_category), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val intent = Intent(this@MenuConfigActivity, MenuCategoryConfigActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivityForResult(intent, Const.REQ_CONFIG)
                            }
                        }
                    }
                }).builder().show(this)
            }else{
                val intent = Intent(this, MenuRegActivity::class.java)
                intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, Const.REQ_REG)
            }
        }

        layout_category_menu_select_desc.visibility = View.GONE
        if (StringUtils.isNotEmpty(mKey)) {
            when(mKey){
                Const.SELECT->{
                    text_menu_config_reg_menu.visibility = View.GONE
                    layout_category_menu_select_desc.visibility = View.VISIBLE
                    val type = intent.getStringExtra(Const.TYPE)
                    if (type == EnumData.GoodsType.hotdeal.name) {
                        if (mPage!!.type == EnumData.PageTypeCode.store.name) {
                            text_category_menu_select_desc.setText(R.string.msg_hotdeal_select_menu_desc)
                        }else{
                            text_category_menu_select_desc.setText(R.string.msg_hotdeal_select_goods_desc)
                        }

                    }else{
                        if (mPage!!.type == EnumData.PageTypeCode.store.name) {
                            text_category_menu_select_desc.setText(R.string.msg_plus_select_menu_desc)
                        }else{
                            text_category_menu_select_desc.setText(R.string.msg_plus_select_goods_desc)
                        }
                    }
                }
            }

        }

        getCategory()
    }

    private fun getCategory() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage!!.no.toString()
        params["depth"] = "1"
        showProgress("")
        ApiBuilder.create().getPageGoodsCategory(params).setCallback(object : PplusCallback<NewResultResponse<PageGoodsCategory>> {
            override fun onResponse(call: Call<NewResultResponse<PageGoodsCategory>>?, response: NewResultResponse<PageGoodsCategory>?) {
                hideProgress()
                if (response != null) {

                    supportFragmentManager.fragments.clear()

                    categoryList = ArrayList()
                    categoryList!!.add(PageGoodsCategory())
                    categoryList!!.addAll(response.datas)

                    mAdapter = PagerAdapter(supportFragmentManager)
                    pager_category_menu.adapter = mAdapter
                    mAdapter!!.setTitle(categoryList!!)
                    tabLayout_category_menu.setViewPager(pager_category_menu)
                    pager_category_menu.currentItem = 0
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PageGoodsCategory>>?, t: Throwable?, response: NewResultResponse<PageGoodsCategory>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun setNotExist(isEmpty: Boolean) {
        if (isEmpty) {
            coordinator_menu_config.visibility = View.GONE
            layout_menu_config_not_exist.visibility = View.VISIBLE
            text_menu_config_reg_menu.visibility = View.GONE
        } else {
            coordinator_menu_config.visibility = View.VISIBLE
            layout_menu_config_not_exist.visibility = View.GONE
            text_menu_config_reg_menu.visibility = View.VISIBLE
        }

    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        internal var mCategoryList: MutableList<PageGoodsCategory>
        var fragMap: SparseArray<CategoryMenuFragment>
            internal set

        init {
            fragMap = SparseArray()
            mCategoryList = ArrayList<PageGoodsCategory>()
        }

        fun setTitle(categoryList: MutableList<PageGoodsCategory>) {

            this.mCategoryList = categoryList
            notifyDataSetChanged()
        }

        override fun getPageTitle(position: Int): String? {

            if (position == 0) {
                return getString(R.string.word_total)
            }

            return mCategoryList[position].goodsCategory!!.name
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

            val fragment = CategoryMenuFragment.newInstance(mCategoryList[position])
//            val fragment = CategoryGoodsFragment.newInstance(mCategoryList[position])
            fragMap.put(position, fragment)
            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_REG->{
                if(resultCode == Activity.RESULT_OK){
                    for(i in 0 until mAdapter!!.fragMap.size()){
                        mAdapter!!.fragMap[i].onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
            Const.REQ_CONFIG -> {
                getCategory()
            }
        }
    }

    fun refreshChildFragment(){
        for(i in 0 until mAdapter!!.fragMap.size()){
            mAdapter!!.fragMap[i].setData()
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        val key = intent.getStringExtra(Const.KEY)
        if (StringUtils.isNotEmpty(key) && key == Const.SELECT) {
            if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
                toolbarOption.initializeDefaultToolbar(getString(R.string.msg_load_menu), ToolbarOption.ToolbarMenu.RIGHT)
            } else {
                toolbarOption.initializeDefaultToolbar(getString(R.string.msg_load_goods), ToolbarOption.ToolbarMenu.RIGHT)
            }

        } else {
            toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
            toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_plus_category))

        }

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    val key = intent.getStringExtra(Const.KEY)
                    if (StringUtils.isNotEmpty(key) && key == Const.SELECT) {
                        onBackPressed()
                    } else {
                        val intent = Intent(this, MenuCategoryConfigActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivityForResult(intent, Const.REQ_CONFIG)
                    }


                }
            }
        }
    }
}
