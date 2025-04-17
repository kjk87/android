package com.pplus.prnumberuser.apps.post.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.custom.StartPointSeekBar
import com.pplus.prnumberuser.databinding.ActivityImageFilterBinding
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import jp.co.cyberagent.android.gpuimage.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImageFilterActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityImageFilterBinding

    override fun getLayoutView(): View {
        binding = ActivityImageFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    internal enum class Filter {
        brightness, contrast, saturation
    }

    private var mGPUImageBrightnessFilter: GPUImageBrightnessFilter? = null
    private var mGPUImageContrastFilter: GPUImageContrastFilter? = null
    private var mGPUImageSaturationFilter: GPUImageSaturationFilter? = null
    private var mFilter: Filter? = null
    private var mUri: Uri? = null
    private var mBrightness = 0
    private var mContrast = 0
    private var mSaturation = 0
    override fun initializeView(savedInstanceState: Bundle?) {
        mUri = intent.data
        mGPUImageBrightnessFilter = GPUImageBrightnessFilter(range(50, -0.3f, 0.3f))
        mGPUImageContrastFilter = GPUImageContrastFilter(range(50, 0.5f, 1.5f))
        mGPUImageSaturationFilter = GPUImageSaturationFilter(range(50, 0.0f, 2.0f))
        mBrightness = 0
        mContrast = 0
        mSaturation = 0
        mFilter = Filter.brightness
        setSelect(binding.textFilterBrightness, binding.textFilterContrast, binding.textFilterSaturation)
        binding.gpuimageImageFilter.layoutParams.height = (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * (3f / 4f)).toInt()
        binding.gpuimageImageFilter.setImage(mUri)
        binding.seekBarFilter.setProgress(0.0)
        binding.seekBarFilter.setOnSeekBarChangeListener(mOnSeekBarChangeListener)

        binding.textFilterBrightness.setOnClickListener {
            if (mFilter != Filter.brightness) {
                mFilter = Filter.brightness
                binding.seekBarFilter.setOnSeekBarChangeListener(null)
                binding.seekBarFilter.setProgress(mBrightness.toDouble())
                binding.seekBarFilter.setOnSeekBarChangeListener(mOnSeekBarChangeListener)
                setSelect(binding.textFilterBrightness, binding.textFilterContrast, binding.textFilterSaturation)
            }
        }

        binding.textFilterContrast.setOnClickListener {
            if (mFilter != Filter.contrast) {
                mFilter = Filter.contrast
                binding.seekBarFilter.setOnSeekBarChangeListener(null)
                binding.seekBarFilter.setProgress(mContrast.toDouble())
                binding.seekBarFilter.setOnSeekBarChangeListener(mOnSeekBarChangeListener)
                setSelect(binding.textFilterContrast, binding.textFilterBrightness, binding.textFilterSaturation)
            }
        }

        binding.textFilterSaturation.setOnClickListener {
            if (mFilter != Filter.saturation) {
                mFilter = Filter.saturation
                binding.seekBarFilter.setOnSeekBarChangeListener(null)
                binding.seekBarFilter.setProgress(mSaturation.toDouble())
                binding.seekBarFilter.setOnSeekBarChangeListener(mOnSeekBarChangeListener)
                setSelect(binding.textFilterSaturation, binding.textFilterContrast, binding.textFilterBrightness)
            }
        }

        setFilter()
    }

    private val mOnSeekBarChangeListener = StartPointSeekBar.OnSeekBarChangeListener { bar, value ->
        LogUtil.e(LOG_TAG, "prograss : {}", value)
        val per = ((value + 100) / 200 * 100).toInt()
        LogUtil.e(LOG_TAG, "percent {}", per)
        when (mFilter) {
            Filter.brightness -> {
                mGPUImageBrightnessFilter!!.setBrightness(range(per, -0.3f, 0.3f))
                mBrightness = value.toInt()
            }
            Filter.contrast -> {
                mGPUImageContrastFilter!!.setContrast(range(per, 0.5f, 1.5f))
                mContrast = value.toInt()
            }
            Filter.saturation -> {
                mGPUImageSaturationFilter!!.setSaturation(range(per, 0.0f, 2.0f))
                mSaturation = value.toInt()
            }
        }
        binding.gpuimageImageFilter.requestRender()
    }

    private fun range(percentage: Int, start: Float, end: Float): Float {
        return (end - start) * percentage / 100.0f + start
    }

    private fun setFilter() {
        val filters: MutableList<GPUImageFilter?> = LinkedList()
        filters.add(mGPUImageBrightnessFilter)
        filters.add(mGPUImageContrastFilter)
        filters.add(mGPUImageSaturationFilter)
        val filterGroup = GPUImageFilterGroup(filters)
        binding.gpuimageImageFilter.filter = filterGroup
        binding.gpuimageImageFilter.requestRender()
    }

    private fun setSelect(view1: View, view2: View, view3: View) {
        view1.isSelected = true
        view2.isSelected = false
        view3.isSelected = false
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_image_edit), ToolbarMenu.LEFT)
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
                        saveImage()
                    }
                }
            }
        }
    }

    private fun saveImage() {
        val result = binding.gpuimageImageFilter.capture()

        showProgress(getString(R.string.msg_editing_image))
        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(mUri!!.path)
                result.compress(Bitmap.CompressFormat.JPEG, 100, out) // bmp is your Bitmap instance
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    out?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        CoroutineScope(Dispatchers.Main + job).launch {
            hideProgress()
            setResult(RESULT_OK)
            finish()
        }
    }
}