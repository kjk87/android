package com.pplus.prnumberbiz.apps.main.ui


import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const

import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.customer.ui.PlusActivity
import com.pplus.prnumberbiz.apps.post.ui.PostWriteActivity
import com.pplus.prnumberbiz.apps.push.ui.PushSendActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_plus_config_fragment.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

class PlusConfigFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_plus_config_fragment
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {


        layout_customer_config_plus.setOnClickListener {
            val intent = Intent(activity, PlusActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_DETAIL)
        }

        layout_plus_config_post.setOnClickListener {
            val intent = Intent(activity, PostWriteActivity::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            activity!!.startActivityForResult(intent, Const.REQ_POST)
        }

        layout_customer_config_push.setOnClickListener {
            val intent = Intent(activity, PushSendActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
        }

        getCount()
    }

    private fun getCount() {

        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
                if (!isAdded) {
                    return
                }

                for (group in response.datas) {
                    if (group.isDefaultGroup) {
                        text_plus_config_count.text = FormatUtil.getMoneyType(group.count.toString())
                        break
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_DETAIL -> {
                getCount()
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                PlusConfigFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
