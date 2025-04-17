package com.pplus.prnumberbiz.apps.main.ui


import android.content.Intent
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.igaworks.adbrix.IgawAdbrix
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.LauncherScreenActivity
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.goods.ui.PlusGoodsDetailActivity
import com.pplus.prnumberbiz.apps.main.data.PersonMainHeaderAdapter
import com.pplus.prnumberbiz.apps.number.ui.MakePrnumberPreActivity
import com.pplus.prnumberbiz.apps.pages.ui.AlertPageSetGuideActivity
import com.pplus.prnumberbiz.apps.pages.ui.PageEditActivity
import com.pplus.prnumberbiz.apps.post.ui.PostDetailActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.dto.Post
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_person_main.*
import kotlinx.android.synthetic.main.fragment_person_main.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class PersonMainFragment : BaseFragment<PersonMainActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_person_main
    }

    override fun initializeView(container: View?) {

    }

    private lateinit var mPage: Page
//    private var attachmentList: MutableList<Attachment>? = null
//    private var mImageList: MutableList<Attachment>? = null
//    var mIntroPagerAdapter: IntroduceImagePagerAdapter? = null

    private var mPaging: Int = 1
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mIsLast = false
    private var mAdapter: PersonMainHeaderAdapter? = null

    override fun init() {

        appbar_person_main.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (!isAdded) {
                    return
                }
                if (verticalOffset <= -collapsing_person_main.height + toolbar_biz_main.height) {
                    //toolbar is collapsed here
                    //write your code here
//                    text_biz_main_page_name2.visibility = View.VISIBLE
                } else {
//                    text_biz_main_page_name2.visibility = View.GONE
                }
            }
        })


        image_person_main_menu.setOnClickListener {
            if (parentActivity is PersonMainActivity) {
                IgawAdbrix.retention("Main_menu")

                parentActivity.drawer_layout_person.openDrawer(GravityCompat.START)
            }

        }


        text_person_main_page_config.setOnClickListener {
            val intent = Intent(activity, PageEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

        if (checkPageSet()) {
            getPage()
        }

    }


    private fun checkPageSet(): Boolean {
        val page = LoginInfoManager.getInstance().user.page!!
        if (StringUtils.isEmpty(page.catchphrase)) {
            val intent = Intent(activity, AlertPageSetGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
            return false
        }
        return true
    }

    private fun getPage() {
        val params = HashMap<String, String>()
        params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page?>> {

            override fun onResponse(call: Call<NewResultResponse<Page?>>?, response: NewResultResponse<Page?>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response!!.data != null) {

                    LoginInfoManager.getInstance().user.page = response.data!!
                    LoginInfoManager.getInstance().save()

                    if (!LoginInfoManager.getInstance().user.page!!.usePrnumber!!) {
                        val intent = Intent(activity, MakePrnumberPreActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
                    }

                    setData()

                    parentActivity.setPageData()

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page?>>?, t: Throwable?, response: NewResultResponse<Page?>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun setData() {
        mPage = LoginInfoManager.getInstance().user.page!!
        text_person_main_page_name.text = mPage.name

        Glide.with(this).load(mPage.backgroundImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_person_main_background)
        image_person_main_background.setOnClickListener {
            val intent = Intent(activity, BackgroundImageDetailActivity::class.java)
            intent.putExtra(Const.URL, mPage.backgroundImage?.url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        mLayoutManager = LinearLayoutManager(activity)
        recycler_person_main.layoutManager = mLayoutManager
        mAdapter = PersonMainHeaderAdapter(activity!!)
        recycler_person_main.adapter = mAdapter

        recycler_person_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : PersonMainHeaderAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(activity, PostDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivityForResult(intent, Const.REQ_DETAIL)
            }

            override fun refresh() {
                getCount()
            }
        })

        getPlusCount()
        getCount()
    }

    private fun getPlusCount(){
        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
                for (group in response.datas) {
                    if (group.isDefaultGroup) {
                        text_person_main_plus_count?.text = getString(R.string.format_plus_count2, FormatUtil.getMoneyType(group.count.toString()))
                        break
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()
    }
    private fun getCount() {
        val params = HashMap<String, String>()
        params["boardNo"] = mPage.prBoard!!.no.toString()
        ApiBuilder.create().getBoardPostCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data
                mAdapter!!.mTotalCount = mTotalCount
                mAdapter!!.clear()
                mPaging = 1
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(paging: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["boardNo"] = mPage.prBoard!!.no.toString()
        params["pg"] = "" + paging
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getBoardPostList(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {

            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
                if (!isAdded) {
                    return
                }
                mLockListView = false
                hideProgress()
                mAdapter!!.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {

                mLockListView = false
                hideProgress()
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_SET_PAGE -> {
                if (checkPageSet()) {
                    getPage()
                }
            }
            Const.REQ_ALARM -> {
                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                    override fun reload() {
//                        checkAlarmCount()
                    }
                })
            }
            Const.REQ_REG, Const.REQ_MODIFY, Const.REQ_DETAIL -> {
                getPage()
            }
        }
    }

//    private var defaultUpload: DefaultUpload? = null
//    private var imgList: ArrayList<ImgUrl>? = null

//    private fun upload(size: Int, priority: Int, filepath: String, type: AttachmentTargetTypeCode) {
//        val attachment = ParamsAttachment()
//        attachment.targetType = type
//        attachment.setFile(filepath)
//        attachment.targetNo = LoginInfoManager.getInstance().user.page!!.no
//
//        defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {
//
//            override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {
//
//                if (!isAdded) {
//                    return
//                }
//                hideProgress()
//                val url = resultResponse.data.url
//                val no = resultResponse.data.no
//
//
//                imgList!!.add(ImgUrl(no, priority))
//                if (imgList!!.size == size + mImageList!!.size) {
//                    val params = ParamsIntroImage()
//                    params.no = LoginInfoManager.getInstance().user.page!!.no
//                    params.introImageList = imgList
//
//                    ApiBuilder.create().updateIntroImageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//
//                        override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
//                            showAlert(R.string.msg_add_complete)
//                            hideProgress()
//                            callIntroduceImage()
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
//
//                            hideProgress()
//                        }
//                    }).build().call()
//                }
//            }
//
//            override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {
//
//                LogUtil.e(LOG_TAG, "onFailure")
//                hideProgress()
//            }
//        })
//
//        showProgress("")
//        defaultUpload!!.request(filepath, attachment)
//    }

    override fun getPID(): String {
        return "Main"
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                PersonMainFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
