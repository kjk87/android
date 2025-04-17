package com.pplus.prnumberbiz.apps.post.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;

import com.pple.pplus.utils.part.info.DeviceUtil;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.custom.StartPointSeekBar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class ImageFilterActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_image_filter;
    }

    enum Filter{
        brightness, contrast, saturation
    }

    private GPUImageView mGPUImageView;
    private GPUImageBrightnessFilter mGPUImageBrightnessFilter;
    private GPUImageContrastFilter mGPUImageContrastFilter;
    private GPUImageSaturationFilter mGPUImageSaturationFilter;

    private Filter mFilter;
    private StartPointSeekBar mSeekBar;
    private Uri mUri;

    private int mBrightness, mContrast, mSaturation;
    private View text_brightness, text_contrast, text_saturation;


    @Override
    public void initializeView(Bundle savedInstanceState){

        mUri = getIntent().getData();

        text_brightness = findViewById(R.id.text_filter_brightness);
        text_brightness.setOnClickListener(this);
        text_contrast = findViewById(R.id.text_filter_contrast);
        text_contrast.setOnClickListener(this);
        text_saturation = findViewById(R.id.text_filter_saturation);
        text_saturation.setOnClickListener(this);

        mGPUImageBrightnessFilter = new GPUImageBrightnessFilter(range(50, -0.3f, 0.3f));
        mGPUImageContrastFilter = new GPUImageContrastFilter(range(50, 0.5f, 1.5f));
        mGPUImageSaturationFilter = new GPUImageSaturationFilter(range(50, 0.0f, 2.0f));
        mBrightness = 0;
        mContrast = 0;
        mSaturation = 0;
        mFilter = Filter.brightness;
        setSelect(text_brightness, text_contrast, text_saturation);

        mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage_image_filter);
        mGPUImageView.getLayoutParams().height = (int) (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * (3f / 4f));
        mGPUImageView.setImage(mUri);

        mSeekBar = (StartPointSeekBar) findViewById(R.id.seekBar_filter);
        mSeekBar.setProgress(0);
        mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        setFilter();
    }

    private StartPointSeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new StartPointSeekBar.OnSeekBarChangeListener(){

        @Override
        public void onOnSeekBarValueChange(StartPointSeekBar bar, double value){
            LogUtil.e(LOG_TAG, "prograss : {}", value);
            int per = (int)((value + 100)/200*100);
            LogUtil.e(LOG_TAG, "percent {}", per);
            switch (mFilter) {

                case brightness:
                    mGPUImageBrightnessFilter.setBrightness(range(per, -0.3f, 0.3f));
                    mBrightness = (int)value;
                    break;
                case contrast:
                    mGPUImageContrastFilter.setContrast(range(per, 0.5f, 1.5f));
                    mContrast = (int)value;
                    break;
                case saturation:
                    mGPUImageSaturationFilter.setSaturation(range(per, 0.0f, 2.0f));
                    mSaturation = (int)value;
                    break;
            }
            mGPUImageView.requestRender();
        }
    };

    private float range(final int percentage, final float start, final float end){

        return (end - start) * percentage / 100.0f + start;
    }

    private void setFilter(){

        List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
        filters.add(mGPUImageBrightnessFilter);
        filters.add(mGPUImageContrastFilter);
        filters.add(mGPUImageSaturationFilter);
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup(filters);
        mGPUImageView.setFilter(filterGroup);
        mGPUImageView.requestRender();
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_filter_brightness:
                if(mFilter != Filter.brightness) {
                    mFilter = Filter.brightness;
                    mSeekBar.setOnSeekBarChangeListener(null);
                    mSeekBar.setProgress(mBrightness);
                    mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
                    setSelect(text_brightness, text_contrast, text_saturation);
                }

                break;
            case R.id.text_filter_contrast:
                if(mFilter != Filter.contrast) {
                    mFilter = Filter.contrast;
                    mSeekBar.setOnSeekBarChangeListener(null);
                    mSeekBar.setProgress(mContrast);
                    mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
                    setSelect(text_contrast, text_brightness, text_saturation);
                }

                break;
            case R.id.text_filter_saturation:
                if(mFilter != Filter.saturation) {
                    mFilter = Filter.saturation;
                    mSeekBar.setOnSeekBarChangeListener(null);
                    mSeekBar.setProgress(mSaturation);
                    mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
                    setSelect(text_saturation, text_contrast, text_brightness);
                }
                break;
        }
    }

    private void setSelect(View view1, View view2, View view3){
        view1.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_image_edit), ToolbarOption.ToolbarMenu.LEFT);
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete));
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case LEFT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                    case RIGHT:
                        if(tag.equals(1)) {
                            saveImage();
                        }
                        break;
                }
            }
        };
    }

    private void saveImage(){

        try {
            Bitmap result = mGPUImageView.capture();
            new AsyncTask<Bitmap, Void, Void>(){

                @Override
                protected void onPreExecute(){

                    super.onPreExecute();
                    showProgress(getString(R.string.msg_editing_image));
                }

                @Override
                protected Void doInBackground(Bitmap... bitmaps){
                    FileOutputStream out = null;
                    try {

                        out = new FileOutputStream(mUri.getPath());
                        bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid){

                    super.onPostExecute(aVoid);
                    hideProgress();
                    setResult(RESULT_OK);
                    finish();
                }
            }.execute(result);
        }catch (Exception e){

        }

    }
}
