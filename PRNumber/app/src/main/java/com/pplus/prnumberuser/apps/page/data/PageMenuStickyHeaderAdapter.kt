package com.pplus.prnumberuser.apps.page.data

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.menu.data.MainMenuAdapter
import com.pplus.prnumberuser.apps.menu.ui.OrderMenuDetailActivity
import com.pplus.prnumberuser.apps.menu.ui.PageMenuReviewActivity
import com.pplus.prnumberuser.apps.menu.ui.TicketDetailActivity
import com.pplus.prnumberuser.apps.page.ui.LocationPageActivity
import com.pplus.prnumberuser.apps.page.ui.PageDeliveryMenuActivity
import com.pplus.prnumberuser.apps.page.ui.PageInfoActivity
import com.pplus.prnumberuser.apps.page.ui.PageVisitMenuActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.OrderMenu
import com.pplus.prnumberuser.core.network.model.dto.OrderMenuGroup
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.dto.PageImage
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.*
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class PageMenuStickyHeaderAdapter(var mPage: Page2, var recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var recyclerItemList: ArrayList<PageMenuAdapterItem> = ArrayList()
    var mDataList: MutableList<OrderMenuGroup>? = null
    var mDelegateList:MutableList<OrderMenu>? = null
    var listener: OnItemClickListener? = null
    var mTab = 0
    var mIsVisit = false

    init {
        mTab = 0
        mDataList = arrayListOf()
        setListData()
    }

    companion object {
        const val TYPE_TOP = 0
        const val TYPE_HOLDER = 1
        const val TYPE_MAIN_MENU = 2
        const val TYPE_GROUP = 3
        const val TYPE_LIST = 4
    }

    override fun getItemViewType(position: Int): Int {
        return recyclerItemList[position].type
    }

    fun getGroupPosition(titlePos:Int) : Int{
        val group = mDataList!![titlePos]
        var i = 0
        for(item in recyclerItemList){
            if(item.type == TYPE_GROUP && item.group!!.seqNo == group.seqNo){
                return i
            }
            i++
        }
        return 0
    }


    interface OnItemClickListener {

        fun onTabChanged(tab: Int)
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TOP -> {
                val binding = HeaderPageMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TopViewHolder(binding)
            }
            TYPE_HOLDER -> {
                val binding = HolderPageMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HolderViewHolder(binding)
            }
            TYPE_MAIN_MENU -> {
                val binding = PageMainMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MainMenuViewHolder(binding)
            }
            TYPE_GROUP -> {
                val binding = ItemPageMenuGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemPageMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }
        }

    }

    fun isHolder(position: Int): Boolean {
        return recyclerItemList[position].type == TYPE_HOLDER
    }

    fun clear() {

        mDataList = arrayListOf()
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<OrderMenuGroup>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun setListData() {
        recyclerItemList.clear()
        recyclerItemList.add(PageMenuAdapterItem(TYPE_TOP))
        recyclerItemList.add(PageMenuAdapterItem(TYPE_HOLDER))
        if(mDelegateList != null && mDelegateList!!.isNotEmpty()){
            recyclerItemList.add(PageMenuAdapterItem(TYPE_MAIN_MENU))
        }

        var i = 0
        for (data in mDataList!!) {
            val groupPos = recyclerItemList.size
            recyclerItemList.add(PageMenuAdapterItem(TYPE_GROUP, data, null, groupPos, i))
            for (menu in data.orderMenuList!!) {
                recyclerItemList.add(PageMenuAdapterItem(TYPE_LIST, data, menu, groupPos, i))
            }
            i++
        }

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = recyclerItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TopViewHolder -> {
                holder.bindView()
            }
            is HolderViewHolder -> {
                holder.bindView()
            }
            is MainMenuViewHolder -> {
                holder.bindView()
            }
            is HeaderViewHolder -> {
                holder.bindView(recyclerItemList[position].group!!)
            }
            is ItemViewHolder -> {
                val item = recyclerItemList[position].menu!!
                holder.bindView(item)
            }
        }
    }

    var topHeight = 0

    inner class TopViewHolder(val binding: HeaderPageMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {

            binding.root.post{
                topHeight = binding.root.measuredHeight
            }

            val page = mPage
            val pageImageAdapter = PageImageAdapter()
            binding.pagerPageMenu.adapter = pageImageAdapter
            binding.pagerPageMenu.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.indicatorPageMenuImage.setCurrentItem(position)
                }
            })
            val params = HashMap<String, String>()
            params["no"] = mPage.seqNo.toString()

            ApiBuilder.create().getPageImageAll(params).setCallback(object : PplusCallback<NewResultResponse<PageImage>> {
                override fun onResponse(call: Call<NewResultResponse<PageImage>>?, response: NewResultResponse<PageImage>?) {

                    if (response?.datas != null && response.datas.isNotEmpty()) {
                        val list = response.datas

                        if (list!!.size > 1) {
                            binding.indicatorPageMenuImage.removeAllViews()
                            binding.indicatorPageMenuImage.visibility = View.VISIBLE
                            binding.indicatorPageMenuImage.build(LinearLayout.HORIZONTAL, list.size)
                        } else {
                            binding.indicatorPageMenuImage.visibility = View.GONE
                        }

                        pageImageAdapter.setDataList(list)
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<PageImage>>?, t: Throwable?, response: NewResultResponse<PageImage>?) {

                }
            }).build().call()

            binding.textPageMenuName.text = page.name


            if (page.avgEval != null) {
                val eval = String.format("%.1f", page.avgEval)
                binding.textPageMenuAvgEval.text = eval
            } else {
                binding.textPageMenuAvgEval.text = "0.0"
            }

            binding.textPageMenuAvgEval.setOnClickListener {
                val intent = Intent(itemView.context, PageMenuReviewActivity::class.java)
                intent.putExtra(Const.PAGE, mPage)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                itemView.context.startActivity(intent)
            }

            binding.textPageMenuReviewCount.setOnClickListener {
                val intent = Intent(itemView.context, PageMenuReviewActivity::class.java)
                intent.putExtra(Const.PAGE, mPage)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                itemView.context.startActivity(intent)
            }

            binding.textPageMenuReviewCount.text = FormatUtil.getMoneyType(mPage.reviewCount.toString())

            val distance = page.distance

            var strDistance = ""
            if (distance != null) {

                if (distance > 1) {
                    strDistance = String.format("%.2f", distance) + "km"
                } else {
                    strDistance = (distance * 1000).toInt().toString() + "m"
                }
            }

            if(page.businessCategory == "service"){
                binding.layoutPageMenuSalesType.visibility = View.GONE
                binding.textPageMenuCategory.visibility = View.VISIBLE
                binding.layoutPageMenuRestaurant.visibility = View.GONE
                binding.layoutPageMenuService.visibility = View.VISIBLE

                binding.textPageMenuCategory.text = "${page.categoryMajor!!.name} > ${page.categoryMinor!!.name}"

                val dayOfWeeks = itemView.context.resources.getStringArray(R.array.day_of_week)
                when(page.businessHoursType){
                    1->{
                        binding.textPageMenuServiceOpeningHours.text = itemView.context.getString(R.string.format_daily_time, page.pageBusinessHoursList!![0].openTime!!.substring(0, 5), page.pageBusinessHoursList!![0].closeTime!!.substring(0, 5))
                    }
                    2->{
                        val holidayOpenTime = page.pageBusinessHoursList!![0].openTime!!.substring(0, 5)
                        val holidayCloseTime = page.pageBusinessHoursList!![0].closeTime!!.substring(0, 5)
                        val openTime = page.pageBusinessHoursList!![1].openTime!!.substring(0, 5)
                        val closeTime = page.pageBusinessHoursList!![1].closeTime!!.substring(0, 5)
                        binding.textPageMenuServiceOpeningHours.text = itemView.context.getString(R.string.format_week_day_end_time, holidayOpenTime, holidayCloseTime, openTime, closeTime)
                    }
                    3->{
                        var businessTime = ""
                        for((i, businessHours) in page.pageBusinessHoursList!!.withIndex()){
                            if(i != 0){
                                businessTime += "\n"
                            }
                            businessTime += itemView.context.getString(R.string.format_day_of_week_time, dayOfWeeks[businessHours.day!!-1], businessHours.openTime!!.substring(0, 5), businessHours.closeTime!!.substring(0, 5))
                        }
                        binding.textPageMenuServiceOpeningHours.text = businessTime
                    }
                    4->{
                        binding.textPageMenuServiceOpeningHours.text = itemView.context.getString(R.string.word_daily_24hour)
                    }
                }

                if(page.pageDayOffList != null && page.pageDayOffList!!.isNotEmpty()){
                    var dayOff = ""
                    for((i, pageDayOff) in page.pageDayOffList!!.withIndex()){
                        if(i != 0){
                            dayOff += "\n"
                        }
                        when(pageDayOff.week){
                            0->{
                                dayOff += itemView.context.getString(R.string.format_weekly_day_off, dayOfWeeks[pageDayOff.day!!])
                            }
                            else->{
                                dayOff += itemView.context.getString(R.string.format_day_off, pageDayOff.week.toString(), dayOfWeeks[pageDayOff.day!!])
                            }
                        }
                    }

                    binding.textPageMenuServiceHoliday.text = dayOff
                }else{
                    binding.textPageMenuServiceHoliday.text = itemView.context.getString(R.string.word_none_day_off)
                }

                if(page.pageTimeOffList != null && page.pageTimeOffList!!.isNotEmpty()){
                    var timeOff = ""
                    for((i, pageTimeOff) in page.pageTimeOffList!!.withIndex()){
                        if(i != 0){
                            timeOff += "\n"
                        }

                        timeOff += itemView.context.getString(R.string.format_time, pageTimeOff.start!!.substring(0, 5), pageTimeOff.end!!.substring(0, 5))

                    }

                    binding.textPageMenuServiceBreakTime.text = timeOff
                }else{
                    binding.textPageMenuServiceBreakTime.text = itemView.context.getString(R.string.word_none)
                }

                binding.textPageMenuServicePhone.text = FormatUtil.getPhoneNumber(page.phone)
                binding.textPageMenuServicePhone.setOnClickListener {
                    if (StringUtils.isNotEmpty(page.phone)) {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
                        itemView.context.startActivity(intent)
                    }
                }

                binding.textPageMenuServiceCallPhone.setOnClickListener {
                    if (StringUtils.isNotEmpty(page.phone)) {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
                        itemView.context.startActivity(intent)
                    }
                }

                binding.textPageMenuServiceAddress.text = "${page.roadAddress} ${page.roadDetailAddress}($strDistance)"
                binding.textPageMenuServiceViewMap.setOnClickListener {
                    val intent = Intent(itemView.context, LocationPageActivity::class.java)
                    intent.putExtra(Const.PAGE2, page)
                    itemView.context.startActivity(intent)
                }

            }else if(page.businessCategory == "restaurant"){

                binding.layoutPageMenuSalesType.visibility = View.VISIBLE
                binding.textPageMenuCategory.visibility = View.GONE
                binding.layoutPageMenuRestaurant.visibility = View.VISIBLE
                binding.layoutPageMenuService.visibility = View.GONE

                binding.textPageMenuPageInfo.setOnClickListener {
                    val intent = Intent(itemView.context, PageInfoActivity::class.java)
                    intent.putExtra(Const.DATA, page)
                    itemView.context.startActivity(intent)
                }

                binding.textPageMenuDeliveryInfoTab.setOnClickListener {
                    binding.textPageMenuDeliveryInfoTab.isSelected = true
                    binding.textPageMenuPackageInfoTab.isSelected = false
                    binding.layoutPageMenuDeliveryInfo.visibility = View.VISIBLE
                    binding.layoutPageMenuPackageInfoInfo.visibility = View.GONE
                }

                binding.textPageMenuPackageInfoTab.setOnClickListener {
                    binding.textPageMenuDeliveryInfoTab.isSelected = false
                    binding.textPageMenuPackageInfoTab.isSelected = true
                    binding.layoutPageMenuDeliveryInfo.visibility = View.GONE
                    binding.layoutPageMenuPackageInfoInfo.visibility = View.VISIBLE
                }

                binding.textPageMenuPackageInfoCookingTime.text = itemView.context.getString(R.string.format_minute_after_visit, mPage.cookingTime.toString())

                if(mIsVisit){
                    binding.textPageMenuDelivery.visibility = View.GONE
                    binding.textPageMenuPackage.visibility = View.GONE
                    binding.layoutPageMenuInfoTab.visibility = View.GONE

                    binding.layoutPageMenuDeliveryInfo.visibility = View.GONE
                    binding.layoutPageMenuPackageInfoInfo.visibility = View.VISIBLE
                }else{
                    if (page.orderType!!.contains("3")) {
                        binding.textPageMenuDelivery.visibility = View.VISIBLE
                    } else {
                        binding.textPageMenuDelivery.visibility = View.GONE
                    }

                    if (page.orderType!!.contains("2")) {
                        binding.textPageMenuPackage.visibility = View.VISIBLE
                    } else {
                        binding.textPageMenuPackage.visibility = View.GONE
                    }

                    if (page.orderType!!.contains("2") && page.orderType!!.contains("3")) {
                        binding.layoutPageMenuInfoTab.visibility = View.VISIBLE

                        binding.textPageMenuDeliveryInfoTab.isSelected = true
                        binding.textPageMenuPackageInfoTab.isSelected = false
                        binding.layoutPageMenuDeliveryInfo.visibility = View.VISIBLE
                        binding.layoutPageMenuPackageInfoInfo.visibility = View.GONE

                    } else {
                        binding.layoutPageMenuInfoTab.visibility = View.GONE
                        if (page.orderType!!.contains("3")) {
                            binding.layoutPageMenuDeliveryInfo.visibility = View.VISIBLE
                            binding.layoutPageMenuPackageInfoInfo.visibility = View.GONE
                        } else if (page.orderType!!.contains("2")) {
                            binding.layoutPageMenuDeliveryInfo.visibility = View.GONE
                            binding.layoutPageMenuPackageInfoInfo.visibility = View.VISIBLE
                        }
                    }
                }

                if (page.minOrderPrice != null && page.minOrderPrice!! > 0) {
                    binding.textPageMenuDeliveryInfoMinPayPrice.text = itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(page.minOrderPrice!!.toInt().toString()))
                    binding.textPageMenuPackageInfoMinPayPrice.text = itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(page.minOrderPrice!!.toInt().toString()))
                } else {
                    binding.textPageMenuDeliveryInfoMinPayPrice.text = itemView.context.getString(R.string.word_none)
                    binding.textPageMenuPackageInfoMinPayPrice.text = itemView.context.getString(R.string.word_none)
                }

                if (page.riderFee != null && page.riderFee!! > 0) {
                    binding.textPageMenuDeliveryInfoDeliveryPrice.text = itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(page.riderFee!!.toInt().toString()))
                } else {
                    binding.textPageMenuDeliveryInfoDeliveryPrice.text = itemView.context.getString(R.string.word_none)
                }

                binding.textPageMenuPackageInfoAddress.text = "${page.roadAddress} ${page.roadDetailAddress}($strDistance)"
                binding.textPageMenuDeliveryInfoAddress.text = "${page.roadAddress} ${page.roadDetailAddress}($strDistance)"

                binding.textPageMenuDeliveryInfoViewMap.setOnClickListener {
                    val intent = Intent(itemView.context, LocationPageActivity::class.java)
                    intent.putExtra(Const.PAGE2, page)
                    itemView.context.startActivity(intent)
                }

                binding.textPageMenuPackageInfoViewMap.setOnClickListener {
                    val intent = Intent(itemView.context, LocationPageActivity::class.java)
                    intent.putExtra(Const.PAGE2, page)
                    itemView.context.startActivity(intent)
                }
            }

        }
    }

    inner class HolderViewHolder(val binding: HolderPageMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tabLayoutPageMenu.setSelectedIndicatorColors(ContextCompat.getColor(itemView.context, android.R.color.transparent))
            binding.tabLayoutPageMenu.setCustomTabView(R.layout.item_menu_group, R.id.text_menu_group)
            binding.tabLayoutPageMenu.setBottomBorder(itemView.context.resources.getDimensionPixelSize(R.dimen.height_0))
            binding.tabLayoutPageMenu.setDividerWidthHeight(itemView.context.resources.getDimensionPixelSize(R.dimen.width_12), 0)
        }

        fun bindView() {

            val titleList = arrayListOf<String>()
            if(mDelegateList != null && mDelegateList!!.isNotEmpty()){
                titleList.add(itemView.context.getString(R.string.word_main_menu))
            }

            for(group in mDataList!!){
                titleList.add(group.name!!)
            }

            binding.tabLayoutPageMenu.setRecyclerView(recyclerView, titleList)
        }
    }

    inner class MainMenuViewHolder(val binding: PageMainMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        var isOpen = false

        init {
        }

        fun bindView() {
            
            val adapter = MainMenuAdapter()
            binding.recyclerMainMenu.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerMainMenu.adapter = adapter
            if(mDelegateList != null && mDelegateList!!.isNotEmpty()){
                adapter.setDataList(mDelegateList!!)
            }

            adapter.listener = object : MainMenuAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {

                    if(mPage.businessCategory == "service"){
                        val intent = Intent(itemView.context, TicketDetailActivity::class.java)
                        intent.putExtra(Const.DATA, adapter.getItem(position))
                        intent.putExtra(Const.PAGE, mPage)
                        if(itemView.context is PageVisitMenuActivity){
                            (itemView.context as PageVisitMenuActivity).menuDetailLauncher.launch(intent)
                        }else{
                            itemView.context.startActivity(intent)
                        }
                    }else{
                        val intent = Intent(itemView.context, OrderMenuDetailActivity::class.java)
                        intent.putExtra(Const.DATA, adapter.getItem(position))
                        intent.putExtra(Const.PAGE, mPage)
                        if(mIsVisit){
                            intent.putExtra(Const.TYPE, 1)
                        }else{
                            intent.putExtra(Const.TYPE, 2)
                        }

                        if(itemView.context is PageDeliveryMenuActivity){
                            (itemView.context as PageDeliveryMenuActivity).menuDetailLauncher.launch(intent)
                        }else if(itemView.context is PageVisitMenuActivity){
                            (itemView.context as PageVisitMenuActivity).menuDetailLauncher.launch(intent)
                        }
                    }

                }
            }
        }
    }


    inner class HeaderViewHolder(var binding: ItemPageMenuGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: OrderMenuGroup) {
            binding.textPageMenuGroup.text = item.name
        }
    }

    inner class ItemViewHolder(val binding: ItemPageMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }

        fun bindView(item: OrderMenu) {
            binding.textPageMenuName.text = item.title

            if(item.delegate != null && item.delegate!!){
                binding.textPageMenuDelegate.visibility = View.VISIBLE
            }else{
                binding.textPageMenuDelegate.visibility = View.GONE
            }

            if(StringUtils.isNotEmpty(item.menuInfo)){
                binding.textPageMenuDesc.visibility = View.VISIBLE
                binding.textPageMenuDesc.text = item.menuInfo
            }else{
                binding.textPageMenuDesc.visibility = View.GONE
            }
            binding.textPageMenuPrice.text = itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price!!.toInt().toString()))

            if (item.originPrice != null && item.originPrice!! > 0) {

                if (item.originPrice!! <= item.price!!) {
                    binding.layoutPageMenuOriginPrice.visibility = View.GONE
                } else {
                    binding.textPageMenuOriginPrice.paintFlags = binding.textPageMenuOriginPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.textPageMenuOriginPrice.text = itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice!!.toInt().toString()))
                    binding.layoutPageMenuOriginPrice.visibility = View.VISIBLE

                    if(item.discount != null && item.discount!! > 0){
                        binding.textPageMenuDiscount.visibility = View.VISIBLE
                        val discountRatio = item.discount!!/item.originPrice!!*100
                        binding.textPageMenuDiscount.text = itemView.context.getString(R.string.format_discount, discountRatio.toInt().toString())
                    }else{
                        binding.textPageMenuDiscount.visibility = View.GONE
                    }
                }

            } else {
                binding.layoutPageMenuOriginPrice.visibility = View.GONE
            }



            binding.root.setOnClickListener {

                if(mPage.businessCategory == "service"){
                    val intent = Intent(itemView.context, TicketDetailActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.putExtra(Const.PAGE, mPage)
                    if(itemView.context is PageVisitMenuActivity){
                        (itemView.context as PageVisitMenuActivity).menuDetailLauncher.launch(intent)
                    }else{
                        itemView.context.startActivity(intent)
                    }
                }else{
                    val intent = Intent(itemView.context, OrderMenuDetailActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.putExtra(Const.PAGE, mPage)
                    if(mIsVisit){
                        intent.putExtra(Const.TYPE, 1)
                    }else{
                        intent.putExtra(Const.TYPE, 2)
                    }
                    if(itemView.context is PageDeliveryMenuActivity){
                        (itemView.context as PageDeliveryMenuActivity).menuDetailLauncher.launch(intent)
                    }else if(itemView.context is PageVisitMenuActivity){
                        (itemView.context as PageVisitMenuActivity).menuDetailLauncher.launch(intent)
                    }
                }

            }

            if (item.imageList!!.isNotEmpty() && StringUtils.isNotEmpty(item.imageList!![0].image)) {
                binding.layoutPageMenuImage.visibility = View.VISIBLE
                Glide.with(itemView.context).load(item.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.bg_f1f2f4_radius_6).error(R.drawable.bg_f1f2f4_radius_6)).into(binding.imagePageMenu)
            } else {
                binding.layoutPageMenuImage.visibility = View.GONE
            }
        }
    }
}