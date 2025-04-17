package com.pplus.luckybol.apps.main.ui


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.recommend.ui.RecommendHistoryActivity
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.FragmentMainInviteBinding
import com.pplus.utils.part.format.FormatUtil

class MainInviteFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            mTab = it.getString(Const.TAB)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainInviteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainInviteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {
        val recommendPoint = FormatUtil.getMoneyTypeFloat(CountryConfigManager.getInstance().config.properties.recommendBol.toString())
        val recommendKey = LoginInfoManager.getInstance().user.recommendKey

        binding.textMainInviteKey.text = recommendKey
//        binding.textMainInviteFirstBenefitDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_invite_first_benefit_desc))
        binding.textMainInviteDesc1.text = PplusCommonUtil.fromHtml(getString(R.string.html_invite_desc1, recommendPoint))
        binding.textMainInviteDesc2.text = PplusCommonUtil.fromHtml(getString(R.string.html_invite_desc2))
        binding.textMainInviteHistory.setOnClickListener {
            val intent = Intent(activity, RecommendHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMainInvite.setOnClickListener {

            share()
        }

        binding.imageMainInviteCopy.setOnClickListener {
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(getString(R.string.word_recommend_code), recommendKey)
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(activity, R.string.msg_copied_clipboard)
        }
    }

    private fun share() {
        val recommendKey = LoginInfoManager.getInstance().user.recommendKey
        val text = "${getString(R.string.format_invite_description, recommendKey)}\n${getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().user.recommendKey)}"
        //        val text = "${getString(R.string.msg_invite_desc)}\n${getString(R.string.msg_invite_url)}"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
        startActivity(chooserIntent)
    }


    override fun getPID(): String {
        return "Main_invite"
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainInviteFragment().apply {
            arguments = Bundle().apply {
                //                        putString(Const.TAB, type)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
