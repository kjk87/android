package com.lejel.wowbox.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.event.ui.PlayFragment
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.luckyball.ui.BallConfigActivity
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentMainWowBallBinding
import com.pplus.utils.part.format.FormatUtil

/**
 * A simple [Fragment] subclass.
 * Use the [MainWowBallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainWowBallFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainWowBallBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainWowBallBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    override fun init() {

        binding.textMainWowBallRetentionBall.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), BallConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainWowBallPlay.setOnClickListener {
            setPlayFragment()
        }

        binding.textMainWowBallLuckyDraw.setOnClickListener {
            setLuckyDrawFragment()
        }

        loginCheck()
        setPlayFragment()
    }

    private fun setPlayFragment() {
        binding.layoutMainWowBallPlayTab.visibility = View.VISIBLE
        binding.layoutMainWowBallLuckyDrawTab.visibility = View.GONE
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.main_wow_ball_container, PlayFragment.newInstance(), PlayFragment::class.java.simpleName)
        ft.commitNow()
    }

    private fun setLuckyDrawFragment() {
        binding.layoutMainWowBallPlayTab.visibility = View.GONE
        binding.layoutMainWowBallLuckyDrawTab.visibility = View.VISIBLE
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.main_wow_ball_container, MainLuckyDrawFragment.newInstance(), MainLuckyDrawFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.textMainLuckyDrawLogin.visibility = View.GONE
            binding.layoutMainWowBallRetentionBall.visibility = View.GONE
            reloadSession()
        } else {
            binding.textMainLuckyDrawLogin.visibility = View.VISIBLE
            binding.layoutMainWowBallRetentionBall.visibility = View.GONE
            binding.textMainLuckyDrawLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
        }
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if(!isAdded){
                    return
                }
                binding.textMainWowBallRetentionBall.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString())
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainWowBallFragment().apply {
            arguments = Bundle().apply {
//                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
            }
        }
    }
}