package com.pplus.prnumberuser.apps.setting.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.logOutAndRestart
import com.pplus.prnumberuser.databinding.FragmentSecessionResultBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * 회원 탈퇴 마지막 호출
 */
class SecessionResultFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getPID(): String? {
        return ""
    }

    private var _binding: FragmentSecessionResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSecessionResultBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {
        val todayDate = Date(System.currentTimeMillis())
        val format = SimpleDateFormat("yyyy.MM.dd")
        val date = format.format(todayDate)
        binding.textSecessionDate.text = getString(R.string.word_secessionDate) + date
        val ss = SpannableString(getString(R.string.msg_secession_description2))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
                ds.color = ContextCompat.getColor(activity!!, R.color.color_579ffb)
            }

            override fun onClick(view: View) {
                logOutAndRestart()
            }
        }
        ss.setSpan(clickableSpan, 14, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textSecessionDescription2.text = ss
        binding.textSecessionDescription2.movementMethod = LinkMovementMethod.getInstance()
        binding.textAuthConfirm.setOnClickListener { logOutAndRestart() }

    }

    companion object {
        @JvmStatic
        fun newInstance(): SecessionResultFragment {
            val fragment = SecessionResultFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}