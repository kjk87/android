package com.root37.buflexz.apps.gallery.ui

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.gallery.data.ImageDeleteAdapter
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.core.util.PplusCommonUtil.Companion.deleteFromMediaScanner
import com.root37.buflexz.databinding.ActivityImageDeleteBinding
import java.util.*

class ImageDeleteActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityImageDeleteBinding

    override fun getLayoutView(): View {
        binding = ActivityImageDeleteBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mCroppedImageList: ArrayList<Uri>? = null
    private var mAdapter: ImageDeleteAdapter? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mCroppedImageList = PplusCommonUtil.getParcelableArrayListExtra(intent, Const.CROPPED_IMAGE, Uri::class.java)
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerImageDelete.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_40)))
        binding.recyclerImageDelete.layoutManager = layoutManager
        mAdapter = ImageDeleteAdapter(this)
        binding.recyclerImageDelete.adapter = mAdapter
        mAdapter!!.setDataList(mCroppedImageList)
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_delete_image), ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarMenu.RIGHT, getString(R.string.word_complete))
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
                        if (mAdapter!!.selectGalleryList == null || mAdapter!!.selectGalleryList.size == 0) {
                            showAlert(R.string.msg_select_delete_image)
                            return
                        }
                        for (uri in mAdapter!!.selectGalleryList) {
                            if (mCroppedImageList!!.contains(uri)) {
                                mCroppedImageList!!.remove(uri) //                                    PplusCommonUtil.Companion.getFile(uri.getPath()).delete();
                                deleteFromMediaScanner(uri.path!!)
                            }
                        }
                        val data = Intent()
                        data.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mCroppedImageList)
                        setResult(RESULT_OK, data)
                        finish()
                    }
                    else -> {}
                }
            }
        }
    }

    internal inner class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
            if (itemPosition % 2 == 1) {
                outRect.right = 0
            } else {
                outRect.right = space
            }
            outRect.bottom = space // Add top margin only for the first item to avoid double space between items
        }
    }
}