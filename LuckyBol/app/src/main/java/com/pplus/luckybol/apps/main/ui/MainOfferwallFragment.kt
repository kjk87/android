package com.pplus.luckybol.apps.main.ui


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.igaworks.adpopcorn.IgawAdpopcorn
import com.igaworks.adpopcorn.IgawAdpopcornExtension
import com.igaworks.adpopcorn.activity.layout.ApOfferWallLayout
import com.igaworks.adpopcorn.style.ApStyleManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.databinding.FragmentMainOfferwallBinding

class MainOfferwallFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            mTab = it.getString(Const.TAB)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainOfferwallBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainOfferwallBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var offerwallLayout:ApOfferWallLayout? = null

    override fun init() {

        offerwallLayout = ApOfferWallLayout(activity)
        binding.layoutMainOfferwall.addView(offerwallLayout)

        offerwallLayout!!.offerwallType = 2


        val optionMap = HashMap<String, Any>()
        optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_THEME_COLOR, Color.parseColor("#fc5c57"))
        optionMap.put(ApStyleManager.CustomStyle.TOP_BAR_BG_COLOR, Color.parseColor("#fc5c57"))
        optionMap.put(ApStyleManager.CustomStyle.BOTTOM_BAR_BG_COLOR, Color.parseColor("#fc5c57"))
        ApStyleManager.setCustomOfferwallStyle(optionMap)
        IgawAdpopcornExtension.setOfferwallImpressions(activity)
        IgawAdpopcorn.setUserId(activity, LoginInfoManager.getInstance().user.no.toString())
        IgawAdpopcornExtension.setCustomOfferwallLayout(activity, offerwallLayout, true)
    }

    override fun onResume() {
        super.onResume()
        if(offerwallLayout != null){
            offerwallLayout!!.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(offerwallLayout != null){
            offerwallLayout!!.destroy()
        }
    }

    override fun getPID(): String {
        return "Main_invite"
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainOfferwallFragment().apply {
            arguments = Bundle().apply {
                //                        putString(Const.TAB, type)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
