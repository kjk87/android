package com.pplus.prnumberbiz.apps.post.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.SelectGoodsFragment
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.Post
import kotlinx.android.synthetic.main.activity_select_post.*

class SelectPostActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_select_post
    }

    var mSelectPost: Post? = null
    var mSelectGoods: Goods? = null
    var mType = ""

    override fun initializeView(savedInstanceState: Bundle?) {
        mType = intent.getStringExtra(Const.TYPE)

        layout_select_post_tab1.setOnClickListener {
            if(mType != Const.POST){
                layout_select_post_tab1.isSelected = true
                layout_select_post_tab2.isSelected = false
                mType = Const.POST
                setPostFragment()
            }

        }

        layout_select_post_tab2.setOnClickListener {
            if(mType != Const.GOODS){
                layout_select_post_tab1.isSelected = false
                layout_select_post_tab2.isSelected = true
                mType = Const.GOODS
                setGoodsFragment()
            }
        }

        when(mType){
            Const.POST->{
                layout_select_post_tab1.isSelected = true
                layout_select_post_tab2.isSelected = false
                setPostFragment()
            }
            Const.GOODS->{
                layout_select_post_tab1.isSelected = false
                layout_select_post_tab2.isSelected = true
                setGoodsFragment()
            }
        }


    }

    var postFrag : SelectPostFragment? =null

    private fun setPostFragment() {
        postFrag = SelectPostFragment.newInstance(mSelectPost)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.select_post_container, postFrag!!, SelectPostFragment::class.java.simpleName)
        ft.commit()
    }

    var goodsFrag : SelectGoodsFragment? =null
    private fun setGoodsFragment() {
        goodsFrag = SelectGoodsFragment.newInstance(mSelectGoods)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.select_post_container, goodsFrag!!, SelectGoodsFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_send_type), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    when(mType){
                        Const.POST->{
                            val post = postFrag!!.getSelectData()
                            if(post != null){
                                val data = Intent()
                                data.putExtra(Const.TYPE, mType)
                                data.putExtra(Const.POST, post)
                                setResult(Activity.RESULT_OK, data)
                                finish()
                            }else{
                                showAlert(R.string.msg_select_send_post)
                            }

                        }
                        Const.GOODS->{
                            val goods = goodsFrag!!.getSelectData()
                            if(goods != null){
                                val data = Intent()
                                data.putExtra(Const.TYPE, mType)
                                data.putExtra(Const.GOODS, goods)
                                setResult(Activity.RESULT_OK, data)
                                finish()
                            }else{
                                showAlert(R.string.msg_select_send_goods)
                            }
                        }
                    }
                }
            }
        }

    }
}
