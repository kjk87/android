package com.pplus.prnumberuser.apps.page.ui


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.page.data.PageImagePagerAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.BusinessLicense
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.PageImage
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.FragmentStoreInfoBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class StoreInfoFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPage = it.getParcelable(Const.PAGE)!!
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentStoreInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentStoreInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    var mPageImagePagerAdapter: PageImagePagerAdapter? = null
    private lateinit var mPage: Page
    var mPageImagePagerAdapter: PageImagePagerAdapter? = null
    private var mPageImageList: MutableList<PageImage>? = null

    private fun revealShow(view: View, x: Int, y: Int) {

        val w = view.width
        val h = view.height

        val endRadius = (Math.max(w, h) * 1.1);

        val revealAnimator = ViewAnimationUtils.createCircularReveal(view, x, y, 0f, endRadius.toFloat())

        view.visibility = View.VISIBLE

        revealAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        revealAnimator.duration = 700
        revealAnimator.start()

    }

    override fun init() {
        LogUtil.e(LOG_TAG, "init")
//        if (OsUtil.isLollipop()) {
//            val viewTreeObserver = layout_store_parent.viewTreeObserver
//            if (viewTreeObserver.isAlive) {
//                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                    override fun onGlobalLayout() {
//                        revealShow(layout_store_parent, getParentActivity().x, getParentActivity().y)
//                        layout_store_parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    }
//                })
//            }
//        }

        binding.textStoreInfoBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.appbarStore.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (!isAdded) {
                    return
                }

//                if (verticalOffset <= -collapsing_page.height + toolbar_store.height) {
//                    //toolbar is collapsed here
//                    //write your code here
//                    text_store_name2.visibility = View.VISIBLE
////                    layout_page_plus.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_140)
//                } else {
//                    text_store_name2.visibility = View.GONE
////                    layout_page_plus.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_230)
//                }
            }
        })

        mPageImagePagerAdapter = PageImagePagerAdapter(requireActivity())
        binding.pagerStoreIntroduceImage.adapter = mPageImagePagerAdapter

        binding.pagerStoreIntroduceImage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                binding.indicatorStoreIntroduceImage.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        mPageImagePagerAdapter!!.setListener(object : PageImagePagerAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, IntroduceImageDetailActivity::class.java)
                intent.putExtra(Const.DATA, mPageImagePagerAdapter!!.getDataList() as ArrayList<PageImage>)
                intent.putExtra(Const.POSITION, position)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        })

        getPage()
        getPageImageList()
    }

    private fun getPage() {

        val params = HashMap<String, String>()
        params["no"] = mPage.no.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()

                if (!isAdded) {
                    return
                }

                if (response.data != null) {
                    mPage = response.data!!
                    setData()
                }

                getBusinessLicense()
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable?, response: NewResultResponse<Page>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPageImageList() {

        val params = HashMap<String, String>()
        params["no"] = mPage.no.toString()

        ApiBuilder.create().getPageImageAll(params).setCallback(object : PplusCallback<NewResultResponse<PageImage>> {
            override fun onResponse(call: Call<NewResultResponse<PageImage>>?, response: NewResultResponse<PageImage>?) {
                if (!isAdded) {
                    return
                }
                if(response?.datas != null && response.datas.isNotEmpty()){
                    binding.collapsingPage.visibility = View.VISIBLE
                    mPageImageList = response.datas

                    if(mPageImageList!!.size > 1){
                        binding.indicatorStoreIntroduceImage.removeAllViews()
                        binding.indicatorStoreIntroduceImage.visibility = View.VISIBLE
                        binding.indicatorStoreIntroduceImage.build(LinearLayout.HORIZONTAL, mPageImageList!!.size)
                    }else{
                        binding.indicatorStoreIntroduceImage.visibility = View.GONE
                    }

                    mPageImagePagerAdapter!!.setDataList(mPageImageList)
                }else{
                    binding.collapsingPage.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<PageImage>>?, t: Throwable?, response: NewResultResponse<PageImage>?) {

            }
        }).build().call()
    }

    private fun getBusinessLicense() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        showProgress("")
        ApiBuilder.create().getBusinessLicense(params).setCallback(object : PplusCallback<NewResultResponse<BusinessLicense>> {
            override fun onResponse(call: Call<NewResultResponse<BusinessLicense>>?, response: NewResultResponse<BusinessLicense>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response?.data != null) {
                    val businessLicense = response.data!!

                    binding.layoutStoreInfo.textStoreCompanyName.text = businessLicense.companyName
                    binding.layoutStoreInfo.textStoreCeo.text = businessLicense.ceo
                    binding.layoutStoreInfo.textStoreCompanyNumber.text = businessLicense.corporateNumber
                    binding.layoutStoreInfo.textStoreCompanyAddress.text = businessLicense.companyAddress
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BusinessLicense>>?, t: Throwable?, response: NewResultResponse<BusinessLicense>?) {
                hideProgress()
            }
        }).build().call()
    }


    private fun setData() {

//        text_store_name2?.text = mPage.name
//        text_store_name?.text = mPage.name


//        if (mPage.numberList != null && mPage.numberList!!.isNotEmpty()) {
//            text_home_number.text = PplusNumberUtil.getPrNumberFormat(mPage.numberList!![0].number)
//        }

//        Glide.with(activity!!).load(mPage.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(image_store_profile)


//        text_store_catchphrase.text = mPage.catchphrase

        if (StringUtils.isNotEmpty(mPage.introduction)) {
            binding.layoutStoreInfo.layoutStoreIntroduction.visibility = View.VISIBLE

            binding.layoutStoreInfo.textStoreIntroduction.text = mPage.introduction
        } else {
            binding.layoutStoreInfo.layoutStoreIntroduction.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(mPage.openHours)) {
            binding.layoutStoreInfo.layoutStoreOpenHours.visibility = View.VISIBLE
            binding.layoutStoreInfo.textStoreOpeningHours.text = mPage.openHours
        } else {
            binding.layoutStoreInfo.layoutStoreOpenHours.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(mPage.holiday)) {
            binding.layoutStoreInfo.layoutStoreHoliday.visibility = View.VISIBLE
            binding.layoutStoreInfo.textStoreHoliday.text = mPage.holiday
        } else {
            binding.layoutStoreInfo.layoutStoreHoliday.visibility = View.GONE
        }

        if (mPage.address == null || StringUtils.isEmpty(mPage.address!!.roadBase)) {
            binding.layoutStoreInfo.layoutStoreAddress.visibility = View.GONE
            binding.layoutStoreInfo.layoutStoreMapOption.visibility = View.GONE
            binding.layoutStoreInfo.layoutStoreMap.visibility = View.GONE
        } else {
            binding.layoutStoreInfo.layoutStoreAddress.visibility = View.VISIBLE
            var detailAddress = ""
            if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
                detailAddress = mPage.address!!.roadDetail!!
            }
            binding.layoutStoreInfo.textStoreAddress.text = "${mPage.address!!.roadBase} $detailAddress"

            if (mPage.latitude != null && mPage.longitude != null) {
                binding.layoutStoreInfo.layoutStoreMap.visibility = View.VISIBLE

                binding.layoutStoreInfo.mapStore.onCreate(null)
                binding.layoutStoreInfo.mapStore.getMapAsync(object : OnMapReadyCallback {
                    override fun onMapReady(googMap: GoogleMap) {
                        LogUtil.e("GOOGLEMAP", "onMapReady")
                        googMap.uiSettings.isScrollGesturesEnabled = false
                        googMap.uiSettings.isZoomGesturesEnabled = false
                        val latLng = LatLng(mPage.latitude!!, mPage.longitude!!)
                        googMap.addMarker(MarkerOptions().position(latLng).title(mPage.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)))
                        googMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                        googMap.setOnMapClickListener {
                            val intent = Intent(activity, LocationPageActivity::class.java)
                            intent.putExtra(Const.PAGE, mPage)
                            startActivity(intent)
                        }
                    }
                })

                binding.layoutStoreInfo.imageStoreMapFull.setOnClickListener {
                    val intent = Intent(activity, LocationPageActivity::class.java)
                    intent.putExtra(Const.PAGE, mPage)
                    startActivity(intent)
                }
            } else {
                binding.layoutStoreInfo.layoutStoreMap.visibility = View.GONE
            }
        }

        binding.layoutStoreInfo.layoutStoreFindRoad.setOnClickListener {
            if (existDaummapApp()) {
                val uri = Uri.parse("daummaps://route?ep=${mPage.latitude},${mPage.longitude}&by=CAR");
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        }

        binding.layoutStoreInfo.layoutStoreCallTaxi.setOnClickListener {
            val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage.latitude}&dest_lng=${mPage.longitude}&ref=pplus")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        binding.layoutStoreInfo.layoutStoreNavigation.setOnClickListener {
            if (NaviClient.instance.isKakaoNaviInstalled(requireActivity())) {
                LogUtil.e("kakonavi", "카카오내비 앱으로 길안내 가능")
                startActivity(
                        NaviClient.instance.navigateIntent(
                                Location(mPage.name!!, mPage.longitude.toString(), mPage.latitude.toString()),
                                NaviOption(coordType = CoordType.WGS84)
                        )
                )
            } else {
                LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")

                // 웹 브라우저에서 길안내
                // 카카오내비가 설치되지 않은 곳에서 활용할 수 있습니다.
                val uri =
                        NaviClient.instance.navigateWebUrl(
                                Location(mPage.name!!, mPage.longitude.toString(), mPage.latitude.toString()),
                                NaviOption(coordType = CoordType.WGS84)
                        )

                // CustomTabs로 길안내
                KakaoCustomTabsClient.openWithDefault(requireActivity(), uri)
            }
        }

        binding.layoutStoreInfo.layoutStoreCopyAddress.setOnClickListener {
            if (mPage.address != null && StringUtils.isNotEmpty(mPage.address!!.roadBase)) {
                var detailAddress = ""
                if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
                    detailAddress = mPage.address!!.roadDetail!!
                }

                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                val clip = ClipData.newPlainText("address", "${mPage.address!!.roadBase} $detailAddress")
                clipboard.setPrimaryClip(clip)
                ToastUtil.show(activity, R.string.msg_copied_clipboard)
            } else {
                ToastUtil.showAlert(activity, getString(R.string.msg_not_exist_address))
            }
        }

        if (StringUtils.isNotEmpty(mPage.phone)) {
            binding.layoutStoreInfo.textSoreCall.visibility = View.VISIBLE
            binding.layoutStoreInfo.textSoreCall.setOnClickListener {
                if (StringUtils.isNotEmpty(mPage.phone)) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
                    startActivity(intent)
                }
            }
        } else {
            binding.layoutStoreInfo.textSoreCall.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(mPage.email)) {
            binding.layoutStoreInfo.layoutStoreEmail.visibility = View.VISIBLE
            binding.layoutStoreInfo.textStoreEmail.text = mPage.email
        } else {
            binding.layoutStoreInfo.layoutStoreEmail.visibility = View.GONE
        }

    }

    private fun existDaummapApp(): Boolean {
        val pm = requireActivity().packageManager

        try {
            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNING_CERTIFICATES) != null)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance(page: Page) =
                StoreInfoFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.PAGE, page)
                    }
                }
    }
}
