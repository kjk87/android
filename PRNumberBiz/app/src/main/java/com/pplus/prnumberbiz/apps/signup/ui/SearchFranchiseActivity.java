//package com.pplus.prnumberbiz.apps.signup.ui;
//
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.signup.data.ImplFranchiseFragment;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//public class SearchFranchiseActivity extends BaseActivity implements ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_search_franchise;
//    }
//
//    private EditText edit_search;
//    private ImplFranchiseFragment mImpl;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//        openFragment(FranchiseListFragment.newInstance());
//
//        edit_search = (EditText)findViewById(R.id.edit_franchise_search);
//        edit_search.setSingleLine();
//        edit_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener(){
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
//
//                switch (actionId) {
//                    case EditorInfo.IME_ACTION_SEARCH:
//                        search();
//                        break;
//                }
//
//                return true;
//            }
//        });
//        findViewById(R.id.image_franchise_search).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                search();
//            }
//        });
//
//        findViewById(R.id.text_franchise_search_next).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                if(mImpl != null && mImpl.getSelectData() != null){
//                    Intent data = new Intent();
//                    data.putExtra(Const.DATA, mImpl.getSelectData());
//                    setResult(RESULT_OK, data);
//                    finish();
//                }else {
//                    showAlert(R.string.msg_select_franchise);
//                }
//            }
//        });
//
//        edit_search.addTextChangedListener(new TextWatcher(){
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable){
//                if(editable.length() == 0){
//                    openFragment(FranchiseListFragment.newInstance());
//                }
//            }
//        });
//    }
//
//    private void search(){
//
//        String word = edit_search.getText().toString().trim();
//
//        if(word.trim().length() == 0) {
//            showAlert(R.string.msg_input_searchWord);
//            return;
//        }
//        PplusCommonUtil.Companion.hideKeyboard(this);
//
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.search_franchise_container);
//
//        if(fragment instanceof FranchiseResultFragment) {
//            ((FranchiseResultFragment) fragment).reSearch(word);
//        } else {
//            openFragment(FranchiseResultFragment.newInstance(word));
//        }
//    }
//
//    public void openFragment(BaseFragment fragment){
//        mImpl = (ImplFranchiseFragment)fragment;
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.search_franchise_container, fragment, fragment.getClass().getSimpleName());
////        ft.addToBackStack(mapFragment.getClass().getSimpleName());
//        ft.commit();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_search_franchise), ToolbarOption.ToolbarMenu.LEFT);
//
//        return toolbarOption;
//    }
//
//    @Override
//    public OnToolbarListener getOnToolbarClickListener(){
//
//        return new OnToolbarListener(){
//
//            @Override
//            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){
//
//                switch (toolbarMenu) {
//                    case LEFT:
//                        onBackPressed();
//                        break;
//                }
//            }
//        };
//
//    }
//}
