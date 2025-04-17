//package com.pplus.prnumberuser.apps.friend.ui;
//
//import android.content.Context;
//import android.graphics.Rect;
//import android.os.Bundle;
//import androidx.annotation.DimenRes;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.text.Html;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pplus.utils.part.format.FormatUtil;
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.page.data.PageAdapter;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.Friend;
//import com.pplus.prnumberuser.core.network.model.dto.Page;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberuser.core.util.PplusCommonUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class FriendPageActivity extends BaseActivity implements ImplToolbar, View.OnClickListener {
//
//    @Override
//    public String getPID() {
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes() {
//
//        return R.layout.activity_friend_page;
//    }
//
//    private TextView text_total_count;
//    private EditText edit_search;
//
//    private int mPaging = 1, mTotalCount = 0;
//    private boolean mLockListView = true;
//
//    private String mSearch;
//    private Friend mFriend;
//
//    private PageAdapter mAdapter;
//    private LinearLayoutManager mLayoutManager;
////    private String mName;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState) {
//
//        mFriend = getIntent().getParcelableExtra(Const.DATA);
//
//        text_total_count = (TextView) findViewById(R.id.text_friend_page_total);
//
////        ContactDao contactDao = DBManager.getInstance(this).getSession().getContactDao();
////
////        List<Contact> contacts = contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(mFriend.getMobile())).list();
////        if(contacts != null && contacts.size() > 0) {
////            mName = contacts.get(0).getMemberName();
////        } else {
////            if(StringUtils.isNotEmpty(mFriend.getFriend().getNickname())) {
////                mName = mFriend.getFriend().getNickname();
////            } else if(mFriend.getFriend().getPage() != null && StringUtils.isNotEmpty(mFriend.getFriend().getPage().getName())) {
////                mName = mFriend.getFriend().getPage().getName();
////            } else if(StringUtils.isNotEmpty(mFriend.getFriend().getName())) {
////                mName = mFriend.getFriend().getName();
////            } else {
////                mName = getString(R.string.word_unknown);
////            }
////        }
//
//        text_total_count.setText(Html.fromHtml(getString(R.string.html_friend_page_total_count, "0")));
//
//        edit_search = (EditText) findViewById(R.id.edit_friend_page_search);
//        edit_search.setSingleLine();
//        findViewById(R.id.image_friend_page_search).setOnClickListener(this);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_friend_page);
//        mAdapter = new PageAdapter();
//        mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(mAdapter);
////        recyclerView.addItemDecoration(new CustomItemOffsetDecoration(this, R.dimen.height_60, R.dimen.height_60));
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            int pastVisibleItems, visibleItemCount, totalItemCount;
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                super.onScrolled(recyclerView, dx, dy);
//                visibleItemCount = mLayoutManager.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
//                if (!mLockListView) {
//                    if ((totalItemCount < mTotalCount) && (visibleItemCount + pastVisibleItems) >= (totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN)) {
//                        mPaging++;
//                        listCall(mPaging);
//                    }
//                }
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new PageAdapter.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(int position, View view) {
//
//                int[] location = new int[2];
//                view.getLocationOnScreen(location);
//                final int x = location[0] + view.getWidth() / 2;
//                final int y = location[1] + view.getHeight() / 2;
//
//                HashMap<String, String> params = new HashMap<String, String>();
//                params.put("no", mAdapter.getItem(position).getNo().toString());
//                showProgress("");
//                ApiBuilder.create().getPage(params).setCallback(new PplusCallback<NewResultResponse<Page>>() {
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Page>> call, NewResultResponse<Page> response) {
//                        hideProgress();
//                        PplusCommonUtil.Companion.goPage(FriendPageActivity.this, response.getData(), x, y);
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Page>> call, Throwable t, NewResultResponse<Page> response) {
//                        hideProgress();
//                    }
//                }).build().call();
//
//            }
//        });
//
//        mSearch = "";
//        edit_search.setText("");
//        getCount();
//    }
//
//    private class CustomItemOffsetDecoration extends RecyclerView.ItemDecoration {
//
//        private int mItemOffset, mTopOffset;
//
//        public CustomItemOffsetDecoration(int itemOffset, int topOffset) {
//
//            mItemOffset = itemOffset;
//            mTopOffset = topOffset;
//        }
//
//        public CustomItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId, @DimenRes int topOffsetId) {
//
//            this(context.getResources().getDimensionPixelSize(itemOffsetId), context.getResources().getDimensionPixelSize(topOffsetId));
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//
//            super.getItemOffsets(outRect, view, parent, state);
//
//            int position = parent.getChildAdapterPosition(view);
//            if (position == 0) {
//                outRect.set(0, mTopOffset, 0, mItemOffset);
//            } else {
//                outRect.set(0, 0, 0, mItemOffset);
//            }
//
//        }
//    }
//
//    private void getCount() {
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mFriend.getFriend().getNo());
//        if (StringUtils.isNotEmpty(mSearch)) {
//            params.put("search", mSearch);
//        }
//        ApiBuilder.create().getFriendPageCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>() {
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response) {
//
//                mTotalCount = response.getData();
//                text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(String.valueOf(mTotalCount)))));
//                mPaging = 1;
//                mAdapter.clear();
//                listCall(mPaging);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response) {
//
//            }
//        }).build().call();
//    }
//
//    private void listCall(int page) {
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mFriend.getFriend().getNo());
//        params.put("pg", "" + page);
//        if (StringUtils.isNotEmpty(mSearch)) {
//            params.put("search", mSearch);
//        }
//        mLockListView = true;
//        showProgress("");
//        ApiBuilder.create().getFriendPageList(params).setCallback(new PplusCallback<NewResultResponse<Page>>() {
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Page>> call, NewResultResponse<Page> response) {
//
//                mLockListView = false;
//                hideProgress();
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Page>> call, Throwable t, NewResultResponse<Page> response) {
//
//                mLockListView = false;
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.image_friend_page_search:
//                mSearch = edit_search.getText().toString().trim();
//                if (StringUtils.isNotEmpty(mSearch)) {
//                    getCount();
//                }
//                break;
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption() {
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_my_friend_of_friend_shop), ToolbarOption.ToolbarMenu.LEFT);
//        return toolbarOption;
//    }
//
//    @Override
//    public OnToolbarListener getOnToolbarClickListener() {
//
//        return new OnToolbarListener() {
//
//            @Override
//            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag) {
//
//                switch (toolbarMenu) {
//                    case LEFT:
//                        if (tag.equals(1)) {
//                            onBackPressed();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}
