package com.pplus.luckybol.apps.main.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ActivitySnsEventBinding

class SnsEventActivity : BaseActivity() {

    private lateinit var binding: ActivitySnsEventBinding

    override fun getLayoutView(): View {
        binding = ActivitySnsEventBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val key = intent.getStringExtra(Const.KEY)

        binding.imageSnsEventBack.setOnClickListener {
            onBackPressed()
        }

        when (key) {
            Const.TELEGRAM -> {
                binding.textSnsEventEvent01Desc.text = PplusCommonUtil.fromHtml(getString(R.string.html_telegram_event_01_desc))

                binding.textSnsEventEvent02Title.text = getString(R.string.msg_event_02_telegram_title)
                binding.imageSnsEventEvent02Desc1.setImageResource(R.drawable.ic_event_02_telegram1)
                binding.imageSnsEventEvent02Desc2.setImageResource(R.drawable.ic_event_02_telegram2)
                binding.imageSnsEventEvent02Desc3.setImageResource(R.drawable.ic_event_02_telegram3)
                binding.textSnsEventEvent02Desc1.text = getString(R.string.msg_event_02_telegram_desc1)
                binding.textSnsEventEvent02Desc2.text = getString(R.string.msg_event_02_telegram_desc2)
                binding.textSnsEventEvent02Desc3.text = getString(R.string.msg_event_02_telegram_desc3)
                binding.imageSnsEvent02Go.setImageResource(R.drawable.btn_event_02_telegram)
                binding.textSnsEventEvent02Desc4.text = getString(R.string.msg_event_02_telegram_desc4)

                binding.textSnsEventEvent03Title.text = getString(R.string.msg_event_03_telegram_title)
                binding.textSnsEventEvent03Desc1.text = getString(R.string.msg_event_03_telegram_desc1)
                binding.textSnsEventEvent03Desc2.text = getString(R.string.msg_event_03_telegram_desc2)
                binding.textSnsEventEvent03Desc3.text = getString(R.string.msg_event_03_telegram_desc3)
                binding.textEvent03LinkTitle.text = getString(R.string.word_telegram_link)
                binding.textEvent03Link.text = getString(R.string.msg_telegram_event_02_link)

                binding.imageEvent03LinkCopy.setOnClickListener {
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                    val clip = ClipData.newPlainText(getString(R.string.word_telegram_link), getString(R.string.msg_telegram_event_02_link))
                    clipboard.setPrimaryClip(clip)
                    ToastUtil.show(this, R.string.msg_copied_clipboard)
                }
            }
            Const.TWITTER -> {
                binding.textSnsEventEvent01Desc.text = PplusCommonUtil.fromHtml(getString(R.string.html_twitter_event_01_desc))

                binding.textSnsEventEvent02Title.text = getString(R.string.msg_event_02_twitter_title)
                binding.imageSnsEventEvent02Desc1.setImageResource(R.drawable.ic_event_02_twitter1)
                binding.imageSnsEventEvent02Desc2.setImageResource(R.drawable.ic_event_02_twitter2)
                binding.imageSnsEventEvent02Desc3.setImageResource(R.drawable.ic_event_02_twitter3)
                binding.textSnsEventEvent02Desc1.text = getString(R.string.msg_event_02_twitter_desc1)
                binding.textSnsEventEvent02Desc2.text = getString(R.string.msg_event_02_twitter_desc2)
                binding.textSnsEventEvent02Desc3.text = getString(R.string.msg_event_02_twitter_desc3)
                binding.imageSnsEvent02Go.setImageResource(R.drawable.btn_event_02_twitter)
                binding.textSnsEventEvent02Desc4.text = getString(R.string.msg_event_02_twitter_desc4)

                binding.textSnsEventEvent03Title.text = getString(R.string.msg_event_03_telegram_title)
                binding.textSnsEventEvent03Desc1.text = getString(R.string.msg_event_03_twitter_desc1)
                binding.textSnsEventEvent03Desc2.text = getString(R.string.msg_event_03_twitter_desc2)
                binding.textSnsEventEvent03Desc3.text = getString(R.string.msg_event_03_twitter_desc3)
                binding.textEvent03LinkTitle.text = getString(R.string.word_twitter_link)
                binding.textEvent03Link.text = getString(R.string.msg_twitter_event_02_link)

                binding.imageEvent03LinkCopy.setOnClickListener {
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                    val clip = ClipData.newPlainText(getString(R.string.word_twitter_link), getString(R.string.msg_twitter_event_02_link))
                    clipboard.setPrimaryClip(clip)
                    ToastUtil.show(this, R.string.msg_copied_clipboard)
                }
            }
        }

        binding.imageSnsEvent02Go.setOnClickListener {
            var link = ""
            when (key) {
                Const.TELEGRAM -> {
                    link = getString(R.string.msg_telegram_event_02_link)
                }
                Const.TWITTER -> {
                    link = getString(R.string.msg_twitter_event_02_link)
                }
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }

        binding.imageEvent03Share.setOnClickListener {

            var link = ""
            when (key) {
                Const.TELEGRAM -> {
                    link = getString(R.string.msg_telegram_event_02_link)
                }
                Const.TWITTER -> {
                    link = getString(R.string.msg_twitter_event_02_link)
                }
            }
            val text = link
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
            startActivity(chooserIntent)
        }
    }

}