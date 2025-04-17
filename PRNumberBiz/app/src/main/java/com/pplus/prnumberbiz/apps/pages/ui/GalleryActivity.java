package com.pplus.prnumberbiz.apps.pages.ui;

import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.permission.Permission;
import com.pple.pplus.utils.part.apps.permission.PermissionListener;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.PPlusPermission;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.pages.data.GalleryIntroduceAdapter;
import com.pplus.prnumberbiz.core.network.model.dto.GalleryData;
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl;
import com.pplus.prnumberbiz.core.network.model.dto.Page;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroImage;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload;
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener;

import java.util.ArrayList;
import java.util.List;

import network.common.PplusCallback;
import retrofit2.Call;

public class GalleryActivity extends BaseActivity implements View.OnClickListener{

    private int mMaxCount = -1;
    private ArrayList<Attachment> mAttchedList;
    private ArrayList<GalleryData> mTotalGalleryList;
    private GalleryIntroduceAdapter mAdapter;
    private String mSelectFolder;
    private ArrayList<String> mFolderList;
    private DropBoxAdapter mDropBoxAdapter;

    private TextView mTextFolderName;
    private ImageView mImageArrow;
    private View mLayoutCategory;


    @Override
    public String getPID(){

        return "";
    }


    @Override
    public int getLayoutRes(){

        return R.layout.activity_gallery;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        mFolderList = new ArrayList<>();
        mFolderList.add(getString(R.string.word_total));
        mSelectFolder = getString(R.string.word_total);

        mTextFolderName = (TextView) findViewById(R.id.text_gallery_folderName);
        mTextFolderName.setText(mSelectFolder);
        mTextFolderName.setOnClickListener(this);
        mImageArrow = (ImageView) findViewById(R.id.image_gallery_arrow);
        mImageArrow.setOnClickListener(this);

        findViewById(R.id.view_gallery_dropBoxBottom).setOnClickListener(this);

        findViewById(R.id.image_gallery_back).setOnClickListener(this);

        RecyclerView recyclerDropBox = (RecyclerView) findViewById(R.id.recycler_gallery_dropBox);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerDropBox.setLayoutManager(layoutManager);
        mDropBoxAdapter = new DropBoxAdapter();
        recyclerDropBox.setAdapter(mDropBoxAdapter);

        mDropBoxAdapter.setOnItemClickListener(new DropBoxAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                mSelectFolder = mFolderList.get(position);
                mTextFolderName.setText(mSelectFolder);
                setGalleryList();
                dropBoxAnim();
            }
        });

        mLayoutCategory = findViewById(R.id.layout_gallery_dropBox);
        mLayoutCategory.setVisibility(View.GONE);

        RecyclerView recyclerGallery = (RecyclerView) findViewById(R.id.recycler_gallery);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerGallery.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.width_3)));
        recyclerGallery.setLayoutManager(gridLayoutManager);
        mAdapter = new GalleryIntroduceAdapter(this);
        recyclerGallery.setAdapter(mAdapter);

        mAttchedList = getIntent().getParcelableArrayListExtra(Const.DATA);
        if(mAttchedList == null || mAttchedList.size() == 0){
            mAttchedList = new ArrayList<>();
            mMaxCount = Const.IMAGE_UPLOAD_MAX_COUNT;
        }else {
            mMaxCount = Const.IMAGE_UPLOAD_MAX_COUNT - mAttchedList.size();
        }

        mAdapter.setMaxCount(mMaxCount);

        findViewById(R.id.text_gallery_upload).setOnClickListener(this);

        PPlusPermission pPlusPermission = new PPlusPermission(this);
        pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE);
        pPlusPermission.setPermissionListener(new PermissionListener(){

            @Override
            public void onPermissionGranted(){

                GalleryListSync galleryListSync = new GalleryListSync();
                galleryListSync.execute();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions){

            }
        });
        pPlusPermission.checkPermission();

    }

    private void setGalleryList(){

        mAdapter.clear();
        if(mSelectFolder.equals(getString(R.string.word_total))) {
            mAdapter.addAll(mTotalGalleryList);
        } else {
            LogUtil.e(LOG_TAG, "folder name {}, sizse {}", mSelectFolder, mTotalGalleryList.size());

            for(GalleryData data : mTotalGalleryList) {

                if(data.getFolder().equals(mSelectFolder)) {
                    mAdapter.add(data);

                }
            }
        }

    }

    @Override
    public void onClick(View v){

        switch (v.getId()) {
            case R.id.text_gallery_folderName:
            case R.id.image_gallery_arrow:
            case R.id.view_gallery_dropBoxBottom:
                dropBoxAnim();
                break;
            case R.id.image_gallery_back:
                onBackPressed();
                break;
            case R.id.text_gallery_upload:
                if(mAdapter.getSelectGalleryList().isEmpty()) {
                    showAlert(R.string.msg_select_image);
                } else {
                    if(imgList == null) {
                        imgList = new ArrayList<>();
                    }
                    if(mAttchedList != null && mAttchedList.size() > 0){
                        for(int i = 0; i < mAttchedList.size(); i++){
                            int priority = Const.IMAGE_UPLOAD_MAX_COUNT - i;
                            imgList.add(new ImgUrl(mAttchedList.get(i).getNo(), priority));
                        }
                    }
                    for(int i = 0; i < mAdapter.getSelectGalleryList().size(); i++) {
                        int priority = mMaxCount - i;
                        upload(priority, mAdapter.getSelectGalleryList().get(i).getImageUrl(), AttachmentTargetTypeCode.pageIntro);
                    }
                }
                break;
        }
    }

    private DefaultUpload defaultUpload;
    private List<ImgUrl> imgList;

    public void upload(final int priority, final String filepath, AttachmentTargetTypeCode type){
        ParamsAttachment attachment = new ParamsAttachment();
        attachment.setTargetType(type);
        attachment.setFile(filepath);
        attachment.setTargetNo(LoginInfoManager.getInstance().getUser().getPage().getNo());

        defaultUpload = new DefaultUpload(new PplusUploadListener<Attachment>(){

            @Override
            public void onResult(String tag, NewResultResponse<Attachment> resultResponse){

                hideProgress();
                String url = resultResponse.getData().getUrl();
                Long no = resultResponse.getData().getNo();


                imgList.add(new ImgUrl(no, priority));
                if(imgList.size() == (mAdapter.getSelectGalleryList().size()+mAttchedList.size())) {
                    ParamsIntroImage params = new ParamsIntroImage();
                    params.setNo(LoginInfoManager.getInstance().getUser().getPage().getNo());
                    params.setIntroImageList(imgList);

                    ApiBuilder.create().updateIntroImageList(params).setCallback(new PplusCallback<NewResultResponse<Page>>(){

                        @Override
                        public void onResponse(Call<NewResultResponse<Page>> call, NewResultResponse<Page> response){
                            showAlert(R.string.msg_add_complete);
                            hideProgress();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<NewResultResponse<Page>> call, Throwable t, NewResultResponse<Page> response){

                            hideProgress();
                        }
                    }).build().call();
                }
            }

            @Override
            public void onFailure(String tag, NewResultResponse resultResponse){

                LogUtil.e(LOG_TAG, "onFailure");
                hideProgress();
            }
        });

        showProgress("");
        defaultUpload.request(filepath, attachment);
    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpacesItemDecoration(int space){

            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            if(itemPosition % 4 == 3) {
                outRect.right = 0;
            } else {
                outRect.right = space;
            }

            outRect.bottom = space;
            // Add top margin only for the first item to avoid double space between items
        }
    }

    class GalleryListSync extends AsyncTask<Void, Void, ArrayList<GalleryData>>{

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            //            showProgress("gallery prg");
        }

        @Override
        protected ArrayList<GalleryData> doInBackground(Void... params){

            final String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED;

            Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
            ArrayList<GalleryData> infos = new ArrayList<>();
            GalleryData galleryData = null;

            if(imageCursor != null && imageCursor.moveToFirst()) {
                do {
                    galleryData = new GalleryData();
                    int idColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                    int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    int orientationColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
                    int mimeTypeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
                    int sizeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                    galleryData.setId(imageCursor.getLong(idColumnIndex));
                    galleryData.setImageUrl(imageCursor.getString(dataColumnIndex));
                    String type = imageCursor.getString(dataColumnIndex).substring(imageCursor.getString(dataColumnIndex).lastIndexOf(".") + 1);
                    galleryData.setOrientation(imageCursor.getInt(orientationColumnIndex));
                    galleryData.setImageType(type);
                    galleryData.setMimeType(imageCursor.getString(mimeTypeIndex));
                    galleryData.setSize(imageCursor.getInt(sizeIndex));
                    String[] folderList = galleryData.getImageUrl().split("/");
                    String folder = folderList[folderList.length - 2];
                    galleryData.setFolder(folder);
                    if(!mFolderList.contains(folder)) {
                        mFolderList.add(folder);
                    }

                    infos.add(galleryData);

                } while (imageCursor.moveToNext());

                imageCursor.close();
            }

            return infos;
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryData> result){

            super.onPostExecute(result);
            hideProgress();
            mDropBoxAdapter.setDataList(mFolderList);
            mTotalGalleryList = result;
            setGalleryList();
        }

    }

    static class DropBoxAdapter extends RecyclerView.Adapter<DropBoxAdapter.ViewHolder>{

        private DropBoxAdapter.OnItemClickListener listener;

        public interface OnItemClickListener{

            void onItemClick(int position);
        }

        private ArrayList<String> mDataList;

        public DropBoxAdapter(){

            this.mDataList = new ArrayList<>();
        }

        public void setOnItemClickListener(DropBoxAdapter.OnItemClickListener listener){

            this.listener = listener;
        }

        public void add(String data){

            if(mDataList == null) {
                mDataList = new ArrayList<>();
            }
            mDataList.add(data);
            notifyDataSetChanged();
        }

        public void addAll(ArrayList<String> dataList){

            if(this.mDataList == null) {
                this.mDataList = new ArrayList<>();
            }

            this.mDataList.addAll(dataList);
            notifyDataSetChanged();
        }

        public void clear(){

            mDataList = new ArrayList<>();
            notifyDataSetChanged();
        }

        public void setDataList(ArrayList<String> dataList){

            this.mDataList = dataList;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder{

            TextView textName;

            public ViewHolder(View itemView){

                super(itemView);
                textName = (TextView) itemView.findViewById(R.id.text_dropbox_category_name);
            }
        }

        @Override
        public DropBoxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dropbox_category, parent, false);
            return new DropBoxAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DropBoxAdapter.ViewHolder holder, int position){

            String item = mDataList.get(position);
            holder.textName.setText(item);

            holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){

                    if(listener != null) {
                        listener.onItemClick(holder.getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public int getItemCount(){

            return mDataList.size();
        }
    }

    private void dropBoxAnim(){

        Animation animation;
        if(mLayoutCategory.getVisibility() == View.VISIBLE) {
            mImageArrow.setSelected(false);
            animation = AnimationUtils.loadAnimation(this, R.anim.scale_hide);
            animation.setAnimationListener(new Animation.AnimationListener(){

                @Override
                public void onAnimationStart(Animation animation){

                }

                @Override
                public void onAnimationEnd(Animation animation){

                    mLayoutCategory.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation){

                }
            });

        } else {
            mImageArrow.setSelected(true);
            mLayoutCategory.setVisibility(View.VISIBLE);
            animation = AnimationUtils.loadAnimation(this, R.anim.scale_show);
        }
        mLayoutCategory.startAnimation(animation);
    }
}
