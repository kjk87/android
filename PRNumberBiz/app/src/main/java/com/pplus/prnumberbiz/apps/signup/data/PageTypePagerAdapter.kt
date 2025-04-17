package com.pplus.prnumberbiz.apps.signup.data

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.pplus.prnumberbiz.R
import kotlinx.android.synthetic.main.item_page_type_pager.view.*

class PageTypePagerAdapter : androidx.viewpager.widget.PagerAdapter {

    private val mInflater: LayoutInflater
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {

        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = mInflater.inflate(R.layout.item_page_type_pager, container, false)

        val text_title1 = view.text_page_type_title1
        val text_title2 = view.text_page_type_title2
        val text_type1 = view.text_page_type1
        val text_type2 = view.text_page_type2
        val text_type3 = view.text_page_type3
        val text_type4 = view.text_page_type4
        val text_type5 = view.text_page_type5
        val text_type6 = view.text_page_type6
        val text_description = view.text_page_type_description

        if(position == 0){
            text_title1.setText(R.string.msg_store_type_title1)
            text_title2.setText(R.string.msg_store_type_title2)
            text_type1.setText(R.string.word_korean_food)
            text_type2.setText(R.string.word_pizza_store)
            text_type3.setText(R.string.word_china_store)
            text_type4.setText(R.string.word_fast_food)
            text_type5.setText(R.string.word_chicken_store)
            text_type6.setText(R.string.word_cafe)
            text_type1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_delivery_01, 0, 0)
            text_type2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_delivery_02, 0, 0)
            text_type3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_delivery_03, 0, 0)
            text_type4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_delivery_04, 0, 0)
            text_type5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_delivery_05, 0, 0)
            text_type6.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_delivery_06, 0, 0)
            text_description.setText(R.string.msg_store_type_description)
        }else if(position == 1){
            text_title1.setText(R.string.msg_shop_type_title1)
            text_title2.setText(R.string.msg_store_type_title2)
            text_type1.setText(R.string.word_beauty)
            text_type2.setText(R.string.word_furniture)
            text_type3.setText(R.string.word_acc)
            text_type4.setText(R.string.word_shoes)
            text_type5.setText(R.string.word_clothes_store)
            text_type6.setText(R.string.word_interior)
            text_type1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_offline_01, 0, 0)
            text_type2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_offline_02, 0, 0)
            text_type3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_offline_03, 0, 0)
            text_type4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_offline_04, 0, 0)
            text_type5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_offline_05, 0, 0)
            text_type6.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_offline_06, 0, 0)
            text_description.setText(R.string.msg_shop_type_description)
        }
        else{
            text_title1.setText(R.string.msg_person_type_title)
            text_title2.setText(R.string.msg_person_type_title2)
            text_type1.setText(R.string.word_private_lesson)
            text_type2.setText(R.string.word_pt_teacher)
            text_type3.setText(R.string.word_helper)
            text_type4.setText(R.string.word_tutoring)
            text_type5.setText(R.string.word_pro)
            text_type6.setText(R.string.word_hair_designer)
            text_type1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_individual_01, 0, 0)
            text_type2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_individual_02, 0, 0)
            text_type3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_individual_03, 0, 0)
            text_type4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_individual_04, 0, 0)
            text_type5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_individual_05, 0, 0)
            text_type6.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_login_individual_06, 0, 0)
            text_description.setText(R.string.msg_person_type_description)
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
