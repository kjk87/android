package com.root37.buflexz.apps.gallery.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.gallery.data.ImageEditPagerAdapter
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityImageEditBinding
import java.util.*

class ImageEditActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityImageEditBinding

    override fun getLayoutView(): View {
        binding = ActivityImageEditBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mCroppedImageList: ArrayList<Uri>? = null
    private var mAdapter: ImageEditPagerAdapter? = null
    private var mSelectPosition = 0

    override fun initializeView(savedInstanceState: Bundle?) {
        mCroppedImageList = PplusCommonUtil.getParcelableArrayListExtra(intent, Const.CROPPED_IMAGE, Uri::class.java)

        binding.pagerImageEdit.clipToPadding = false
        mAdapter = ImageEditPagerAdapter(this, mCroppedImageList!!)
        binding.pagerImageEdit.adapter = mAdapter
        binding.textImageEditComplete.setOnClickListener {
            if (mCroppedImageList!!.size > 0) {
                val data = Intent()
                data.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mCroppedImageList)
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }

    val imageFilterLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            mAdapter!!.notifyDataSetChanged()
        }
    }

    val imageDeleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            mAdapter!!.notifyDataSetChanged()

            val data = result.data
            if(data != null){
                mCroppedImageList = PplusCommonUtil.getParcelableArrayListExtra(data, Const.CROPPED_IMAGE, Uri::class.java)
                if (mCroppedImageList == null || mCroppedImageList!!.size == 0) {
                    finish()
                    return@registerForActivityResult
                }
                mAdapter = ImageEditPagerAdapter(this, mCroppedImageList!!)
                binding.pagerImageEdit.adapter = mAdapter
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mCroppedImageList = PplusCommonUtil.getParcelableArrayListExtra(intent, Const.CROPPED_IMAGE, Uri::class.java)
        mAdapter!!.dataList = mCroppedImageList!!
    }

    fun goFilter(position: Int, uri: Uri?) {
        mSelectPosition = position
        val intent = Intent(this, ImageFilterActivity::class.java)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        imageFilterLauncher.launch(intent)
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_photo), ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarMenu.RIGHT, getString(R.string.word_delete))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }
                    ToolbarMenu.RIGHT -> if (tag == 1) {
                        val intent = Intent(this@ImageEditActivity, ImageDeleteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mAdapter!!.dataList)
                        imageDeleteLauncher.launch(intent)
                    }
                    else -> {}
                }
            }
        }
    }
}