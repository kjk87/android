package com.pplus.luckybol.apps.main.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.data.LottoBannerAdapter
import com.pplus.luckybol.apps.event.ui.*
import com.pplus.luckybol.apps.my.ui.MyWinHistoryActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentMainPlayBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call
import java.util.*

class MainPlayFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { //            mTab = it.getString(Const.TAB)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainPlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentMainPlayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mLottoAdapter: LottoBannerAdapter? = null

    override fun init() {

        binding.textMainPlayLottoDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_lotto_desc))

        binding.textMainPlayLottoWinHistory.setOnClickListener {
            val intent = Intent(activity, LottoHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMainPlayLottoViewDetail.setOnClickListener {
            val intent = Intent(activity, LuckyLottoGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)

        }

        binding.layoutMainViewWinImpression.setOnClickListener {
            val intent = Intent(activity, EventReviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMainMyWin.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), null)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MyWinHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.imageMainPlayA.setOnClickListener {
            val intent = Intent(activity, PlayActivity::class.java)
            intent.putExtra(Const.KEY, 1)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.imageMainPlayB.setOnClickListener {
            val intent = Intent(activity, PlayActivity::class.java)
            intent.putExtra(Const.KEY, 2)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.imageMainPlayC.setOnClickListener {
            val intent = Intent(activity, PlayActivity::class.java)
            intent.putExtra(Const.KEY, 3)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.imageMainPlayD.setOnClickListener {
            val intent = Intent(activity, PlayActivity::class.java)
            intent.putExtra(Const.KEY, 4)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        mLottoAdapter = LottoBannerAdapter()
        binding.pagerMainPlayLotto.adapter = mLottoAdapter
        binding.indicatorMainPlayLotto.setImageResId(R.drawable.indi_lotto_banner)

        mLottoAdapter!!.listener = object : LottoBannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (!PplusCommonUtil.loginCheck(activity!!, null)) {
                    return
                }
                val event = mLottoAdapter!!.getItem(position)
                val intent = Intent(activity, LuckyLottoDetailActivity::class.java)
                intent.putExtra(Const.DATA, event)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        if(CountryConfigManager.getInstance().config.properties.lottoOpen != null && CountryConfigManager.getInstance().config.properties.lottoOpen!!){
            binding.layoutLuckyLotto.visibility = View.VISIBLE
            getLottoBannerList()
        }else{
            binding.layoutLuckyLotto.visibility = View.GONE
            binding.layoutMainPlayLotto.visibility = View.GONE
        }

    }

    private fun getLottoBannerList() {
        val params = HashMap<String, String>()
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        ApiBuilder.create().getMainBannerLottoList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>,
                                    response: NewResultResponse<Event>) {

                //                hideProgress()
                if (!isAdded) {
                    return
                }

                val lottoList = response.datas!!

                if (lottoList.isEmpty()) {
                    binding.layoutMainPlayLotto.visibility = View.GONE
                } else {
                    binding.layoutMainPlayLotto.visibility = View.VISIBLE
                    binding.indicatorMainPlayLotto.build(LinearLayout.HORIZONTAL, lottoList.size)
                    binding.pagerMainPlayLotto.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            binding.indicatorMainPlayLotto.setCurrentItem(position)
                            binding.textMainPlayLottoPage.text = "${position + 1}/${lottoList.size}"
                        }
                    })
                    mLottoAdapter?.setDataList(response.datas!! as MutableList<Event>)
                    binding.textMainPlayLottoPage.text = "${binding.pagerMainPlayLotto.currentItem + 1}/${lottoList.size}"
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>,
                                   t: Throwable,
                                   response: NewResultResponse<Event>) { //                hideProgress()
            }
        }).build().call()
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainPlayFragment().apply {
            arguments = Bundle().apply { //                        putString(Const.TAB, type)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
