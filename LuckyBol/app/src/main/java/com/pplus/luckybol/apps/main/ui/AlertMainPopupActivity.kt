package com.pplus.luckybol.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.ui.EventDetailActivity
import com.pplus.luckybol.apps.event.ui.LuckyLottoDetailActivity
import com.pplus.luckybol.apps.product.ui.ProductShipDetailActivity
import com.pplus.luckybol.apps.setting.ui.NoticeDetailActivity
import com.pplus.luckybol.core.code.common.MoveType1Code
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.Notice
import com.pplus.luckybol.core.network.model.dto.PopupMange
import com.pplus.luckybol.core.network.model.dto.ProductPrice
import com.pplus.luckybol.databinding.ActivityAlertMainPopupBinding
import com.pplus.utils.part.pref.PreferenceUtil

class AlertMainPopupActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertMainPopupBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertMainPopupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val popupMange = intent.getParcelableExtra<PopupMange>(Const.DATA)!!

        Glide.with(this).load(popupMange.image).apply(RequestOptions().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageAlertMainPopup)

        binding.imageAlertMainPopup.setOnClickListener {

            when(popupMange.moveType){
                MoveType1Code.inner.name->{
                    when(popupMange.innerType){
                        "online"->{
                            val productPrice = ProductPrice()
                            productPrice.seqNo = popupMange.moveTarget!!.toLong()

                            val intent = Intent(this, ProductShipDetailActivity::class.java)
                            intent.putExtra(Const.DATA, productPrice)
                            launcher.launch(intent)
                        }
                        "event"->{
                            val event = Event()
                            event.no = popupMange.moveTarget!!.toLong()
                            val intent = Intent(this, EventDetailActivity::class.java)
                            intent.putExtra(Const.DATA, event)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            launcher.launch(intent)
                        }
                        "lotto"->{
                            val event = Event()
                            event.no = popupMange.moveTarget!!.toLong()
                            val intent = Intent(this, LuckyLottoDetailActivity::class.java)
                            intent.putExtra(Const.DATA, event)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            launcher.launch(intent)
                        }
                        "notice"->{
                            val notice = Notice()
                            notice.no = popupMange.moveTarget!!.toLong()
                            val intent = Intent(this, NoticeDetailActivity::class.java)
                            intent.putExtra(Const.NOTICE, notice)
                            launcher.launch(intent)
                        }
                    }

                }
                MoveType1Code.outer.name->{
                    val intent = Intent(this, MainBannerDetailActivity::class.java)
                    intent.putExtra(Const.DATA, popupMange)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                MoveType1Code.none.name->{
                    finish()
                }
            }
        }

        binding.textAlertMainPopupClose.setOnClickListener {
            finish()
        }

        binding.textAlertMainPopupDoNotAgain.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(Const.POPUP+popupMange.seqNo, false)
            finish()
        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        finish()
    }

}
