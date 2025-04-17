package com.pplus.prnumberuser.apps.post.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.post.data.PostImageEditPagerAdapter
import com.pplus.prnumberuser.databinding.ActivityPostImageEditBinding
import java.util.*

class PostImageEditActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityPostImageEditBinding

    override fun getLayoutView(): View {
        binding = ActivityPostImageEditBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mCroppedImageList: ArrayList<Uri>? = null
    private var mAdapter: PostImageEditPagerAdapter? = null
    private var mSelectPosition = 0

    override fun initializeView(savedInstanceState: Bundle?) {
        mCroppedImageList = intent.getParcelableArrayListExtra(Const.CROPPED_IMAGE)

        binding.pagerImageEdit.clipToPadding = false
        mAdapter = PostImageEditPagerAdapter(this, mCroppedImageList)
        binding.pagerImageEdit.adapter = mAdapter
        binding.textPostImageEditComplete.setOnClickListener {
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
                mCroppedImageList = data.getParcelableArrayListExtra(Const.CROPPED_IMAGE)
                if (mCroppedImageList == null || mCroppedImageList!!.size == 0) {
                    finish()
                    return@registerForActivityResult
                }
                mAdapter = PostImageEditPagerAdapter(this, mCroppedImageList)
                binding.pagerImageEdit.adapter = mAdapter
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mCroppedImageList = intent.getParcelableArrayListExtra(Const.CROPPED_IMAGE)
        mAdapter!!.dataList = mCroppedImageList
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
                        onBackPressed()
                    }
                    ToolbarMenu.RIGHT -> if (tag == 1) {
                        val intent = Intent(this@PostImageEditActivity, ImageDeleteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mAdapter!!.dataList)
                        imageDeleteLauncher.launch(intent)
                    }
                }
            }
        }
    }
}