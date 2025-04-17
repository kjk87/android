package com.pplus.prnumberuser.apps.my.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.apps.my.data.CategoryMinorAdapter
import com.pplus.prnumberuser.apps.my.data.MyFavoriteAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.CategoryFavorite
import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityMyFavoriteBinding
import retrofit2.Call

class MyFavoriteActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_mypage_favorite"
    }

    private lateinit var binding: ActivityMyFavoriteBinding

    override fun getLayoutView(): View {
        binding = ActivityMyFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mAdapter: MyFavoriteAdapter? = null
    private var mMyAdapter: CategoryMinorAdapter? = null
    var mCategoryList = arrayListOf<CategoryMinor>()

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.recyclerMyFavorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerFavorite.layoutManager = LinearLayoutManager(this)
        mMyAdapter = CategoryMinorAdapter( true)
        binding.recyclerMyFavorite.adapter = mMyAdapter
        mAdapter = MyFavoriteAdapter()
        binding.recyclerFavorite.adapter = mAdapter
        mAdapter!!.setOnItemListener(object : MyFavoriteAdapter.OnItemListener {

            override fun onItemSelect(categoryMinor: CategoryMinor) {


                if (mCategoryList.contains(categoryMinor)) {
                    deleteCategoryFavorite(categoryMinor)
                } else {

                    if (mCategoryList.size < 10) {
                        val categoryFavorite = CategoryFavorite()
                        categoryFavorite.categoryMinorSeqNo = categoryMinor.seqNo
                        categoryFavorite.categoryMajorSeqNo = categoryMinor.major
                        categoryFavorite.memberSeqNo = LoginInfoManager.getInstance().user.no
                        insertCategoryFavorite(categoryFavorite)
                    } else {
                        ToastUtil.show(this@MyFavoriteActivity, R.string.msg_select_favorite_tag_until_10)
                        return
                    }
                }
            }
        })

        binding.recyclerFavorite.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_120))

        mMyAdapter!!.setOnItemClickListener(object : CategoryMinorAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                deleteCategoryFavorite(mCategoryList[position])
            }
        })

        getMyFavoriteCategoryList()
    }

    private fun deleteCategoryFavorite(categoryMinor: CategoryMinor){
        val params = HashMap<String, String>()
        params["categoryMinorSeqNo"] = categoryMinor.seqNo.toString()
        showProgress("")
        ApiBuilder.create().deleteCategoryFavorite(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                mCategoryList.remove(categoryMinor)

                if(mCategoryList.isEmpty()){
                    binding.layoutMyFavoriteNotExist.visibility = View.VISIBLE
                    binding.layoutMyFavorite.visibility = View.GONE
                }else{
                    binding.textMyFavoriteTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_my_favorite_tag_title, mCategoryList.size.toString()))
                }

                mMyAdapter!!.notifyDataSetChanged()
                mAdapter!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun insertCategoryFavorite(categoryFavorite: CategoryFavorite){
        showProgress("")
        ApiBuilder.create().insertCategoryFavorite(categoryFavorite).setCallback(object : PplusCallback<NewResultResponse<CategoryFavorite>>{
            override fun onResponse(call: Call<NewResultResponse<CategoryFavorite>>?, response: NewResultResponse<CategoryFavorite>?) {
                hideProgress()
                if(response?.data != null){
                    binding.layoutMyFavoriteNotExist.visibility = View.GONE
                    binding.layoutMyFavorite.visibility = View.VISIBLE
                    mCategoryList.add(response.data.categoryMinor!!)
                    binding.textMyFavoriteTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_my_favorite_tag_title, mCategoryList.size.toString()))
                    mMyAdapter!!.notifyDataSetChanged()
                    mAdapter!!.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<CategoryFavorite>>?, t: Throwable?, response: NewResultResponse<CategoryFavorite>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getMyFavoriteCategoryList() {

        showProgress("")
        ApiBuilder.create().myFavoriteCategoryList.setCallback(object : PplusCallback<NewResultResponse<CategoryFavorite>> {
            override fun onResponse(call: Call<NewResultResponse<CategoryFavorite>>?, response: NewResultResponse<CategoryFavorite>?) {
                hideProgress()
                if (response?.datas != null) {

                    if (response.datas.isNotEmpty()) {
                        binding.layoutMyFavoriteNotExist.visibility = View.GONE
                        binding.layoutMyFavorite.visibility = View.VISIBLE
                        mCategoryList = arrayListOf()
                        for(categoryFavorite in response.datas){
                            mCategoryList.add(categoryFavorite.categoryMinor!!)
                        }

                        binding.textMyFavoriteTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_my_favorite_tag_title, mCategoryList.size.toString()))
                    } else {
                        binding.layoutMyFavoriteNotExist.visibility = View.VISIBLE
                        binding.layoutMyFavorite.visibility = View.GONE
                        mCategoryList = arrayListOf()
                    }

                    mMyAdapter!!.setDataList(mCategoryList)
                    mAdapter!!.mMyCategoryList = mCategoryList

                    getCategoryList()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<CategoryFavorite>>?, t: Throwable?, response: NewResultResponse<CategoryFavorite>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getCategoryList() {

        showProgress("")

        ApiBuilder.create().categoryMajor.setCallback(object : PplusCallback<NewResultResponse<CategoryMajor>>{
            override fun onResponse(call: Call<NewResultResponse<CategoryMajor>>?, response: NewResultResponse<CategoryMajor>?) {
                if(response?.datas != null){
                    hideProgress()
                    mAdapter!!.setDataList(response.datas)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<CategoryMajor>>?, t: Throwable?, response: NewResultResponse<CategoryMajor>?) {
                hideProgress()
            }
        }).build().call()

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_favorite), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
