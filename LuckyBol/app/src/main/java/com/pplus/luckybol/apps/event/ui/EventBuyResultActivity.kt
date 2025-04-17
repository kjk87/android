package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.component.MyBounceInterpolator
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventWinJpa
import com.pplus.luckybol.databinding.ActivityEventResultBinding
import com.pplus.utils.part.format.FormatUtil


class EventBuyResultActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventResultBinding

    override fun getLayoutView(): View {
        binding = ActivityEventResultBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mEventWin: EventWinJpa? = null
    private var mEvent: Event? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mEventWin = intent.getParcelableExtra(Const.EVENT_WIN)
        mEvent = intent.getParcelableExtra(Const.EVENT)

        if(mEvent!!.primaryType == EventType.PrimaryType.number.name){
            binding.imageEventResult2Rto.setImageResource(R.drawable.ic_number_event_popup_character)
        }else if(mEvent!!.primaryType == EventType.PrimaryType.lotto.name || mEvent!!.primaryType == EventType.PrimaryType.lottoPlaybol.name){
            binding.imageEventResult2Rto.setImageResource(R.drawable.img_lotto_event_popup_character)
        }else{
            binding.imageEventResult2Rto.setImageResource(R.drawable.ic_event_popup_character)
        }

        binding.textEventResultCancel.visibility = View.GONE
        binding.textEventResultBar.visibility = View.GONE
        if (mEventWin != null) {
            binding.layoutEventResult1.visibility = View.VISIBLE
            binding.layoutEventResult2.visibility = View.GONE
            binding.imageEventResultLoose.visibility = View.GONE
            binding.textEventResultTitle.setPadding(0, 0, 0, 0)
            binding.textEventResultDescription1.setPadding(0, 0, 0, 0)
            binding.textEventResultTitle.setText(R.string.msg_celebration)
            binding.textEventResultTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_84pt).toFloat())
            binding.imageEventResult.visibility = View.VISIBLE
            binding.textEventResultDescription2.visibility = View.GONE
            binding.textEventResultDescription2.setText(R.string.msg_write_winner_description)
            binding.textEventResultImpression.setText(R.string.msg_write_winner_impression)
            binding.textEventResultDescription1.text = mEventWin!!.eventGift!!.alert

            if(mEvent!!.primaryType.equals(EventType.PrimaryType.number.name)){
                binding.imageEventResult.setImageResource(R.drawable.img_lotto_event_gift)
            }else{
                if(mEventWin!!.eventGift != null){
                    Glide.with(this).load(mEventWin!!.eventGift!!.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_lotto_event_gift).error(R.drawable.img_lotto_event_gift)).into(binding.imageEventResult)
                }else{
                    binding.imageEventResult.setImageResource(R.drawable.img_lotto_event_gift)
                }
            }

        } else {
            if (mEvent!!.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                binding.layoutEventResult1.visibility = View.VISIBLE
                binding.layoutEventResult2.visibility = View.GONE

                binding.imageEventResultLoose.visibility = View.VISIBLE
                Glide.with(this).load(R.drawable.crying).into(binding.imageEventResultLoose)

                binding.textEventResultTitle.setPadding(0, resources.getDimensionPixelSize(R.dimen.height_60), 0, 0)
                binding.textEventResultDescription1.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.height_80))
                binding.textEventResultTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_64pt).toFloat())
                binding.imageEventResult.visibility = View.GONE
                binding.textEventResultDescription2.visibility = View.GONE

                if(mEvent!!.reward != null && mEvent!!.reward!! > 0){
                    binding.textEventResultDescription2.visibility = View.VISIBLE
                    binding.textEventResultDescription2.text = getString(R.string.format_bol_reward, FormatUtil.getMoneyTypeFloat(mEvent!!.reward!!.toString()))
                }

                binding.textEventResultTitle.setText(R.string.msg_next_time_event)

                if (mEvent!!.primaryType.equals(EventType.PrimaryType.goodluck.name)) {
                    binding.textEventResultCancel.visibility = View.GONE
                    binding.textEventResultBar.visibility = View.VISIBLE
                    binding.textEventResultDescription1.setText(R.string.msg_retry)

//                    if (mEvent!!.reward!! < 0) {
//                        text_event_result_description1.text = getString(R.string.format_use_bol2, FormatUtil.getMoneyType(Math.abs(mEvent!!.reward!!).toString()))
//                    } else {
//                        text_event_result_description1.text = "(${getString(R.string.word_free)})"
//                    }

                    binding.textEventResultImpression.setText(R.string.word_confirm)


                } else {
                    binding.textEventResultDescription1.setText(R.string.msg_join_other_event)
                    binding.textEventResultImpression.setText(R.string.word_confirm)
                }

            } else {

                binding.layoutEventResult1.visibility = View.GONE
                binding.layoutEventResult2.visibility = View.VISIBLE
                binding.textEventResult2Alert.text = mEvent!!.winnerAlert

                binding.textEventResult2Alert.visibility = View.INVISIBLE
                binding.textEventResult2Confirm.visibility = View.INVISIBLE
                val fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
                val anim = AnimationUtils.loadAnimation(this, R.anim.bounce)
                anim.duration = 1000
                val interpolator = MyBounceInterpolator(0.2, 20.0)
                anim.interpolator = interpolator
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        binding.textEventResult2Alert.visibility = View.VISIBLE
                        binding.textEventResult2Confirm.visibility = View.VISIBLE
                        binding.textEventResult2Alert.startAnimation(fadeInAnim)
                        binding.textEventResult2Confirm.startAnimation(fadeInAnim)
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })
                binding.imageEventResult2Rto.post(Runnable {
                    binding.imageEventResult2Rto.startAnimation(anim)
                })

            }
        }

        binding.textEventResultImpression.setOnClickListener {
            if (mEventWin != null) {
                winImpression()
            } else {

                val data = Intent()
                data.putExtra(Const.DATA, mEvent)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }

    private fun winImpression(){
        val intent = Intent(this, EventWinImpressionActivity::class.java)
        intent.putExtra(Const.EVENT_WIN_JPA, mEventWin)
        resultLauncher.launch(intent)
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            winImpression()
        }
    }

    override fun onBackPressed() {
    }
}
