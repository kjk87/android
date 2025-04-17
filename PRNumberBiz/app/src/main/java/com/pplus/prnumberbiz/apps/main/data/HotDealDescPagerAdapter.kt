package com.pplus.prnumberbiz.apps.main.data

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberbiz.R
import kotlinx.android.synthetic.main.item_hot_deal_desc_pager.view.*

class HotDealDescPagerAdapter : androidx.viewpager.widget.PagerAdapter {

    private val mInflater: LayoutInflater
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {

        return 2
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = mInflater.inflate(R.layout.item_hot_deal_desc_pager, container, false)

        val text_title = view.text_hot_deal_desc_title
        val image = view.image_hot_deal_desc_image
        val text_desc = view.text_hot_deal_desc


        if (position == 0) {
            text_title.setText(R.string.msg_hot_deal_desc_title1)
            image.setImageResource(R.drawable.img_hotdeal_guide_1)
            text_desc.setText(R.string.msg_hot_deal_desc1)
        } else if (position == 1) {
            text_title.setText(R.string.msg_hot_deal_desc_title2)
            image.setImageResource(R.drawable.img_hotdeal_guide_2)
            text_desc.setText(R.string.msg_hot_deal_desc2)
        }


        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as androidx.viewpager.widget.ViewPager).removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }
}
