//package com.pplus.prnumberbiz.apps.billing.ui;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.billing.data.BillingData;
//import com.pplus.prnumberbiz.apps.billing.data.BolBillingAdapter;
//import com.pplus.prnumberbiz.apps.billing.util.IabBroadcastReceiver;
//import com.pplus.prnumberbiz.apps.billing.util.IabHelper;
//import com.pplus.prnumberbiz.apps.billing.util.IabResult;
//import com.pplus.prnumberbiz.apps.billing.util.Inventory;
//import com.pplus.prnumberbiz.apps.billing.util.Purchase;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.mgmt.SystemBoardManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Cash;
//import com.pplus.prnumberbiz.core.network.model.dto.CashBillingProperties;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class BolBillingActivity extends BaseActivity implements ImplToolbar, IabBroadcastReceiver.IabBroadcastListener, DialogInterface.OnClickListener{
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
//        return R.layout.activity_cash_billing;
//    }
//
//    private LinearLayout layout_terms;
//    private NestedScrollView scroll_cash_charge;
//    private BolBillingAdapter mAdapter;
//    private ArrayList<BillingData> billingDataList;
//    private BillingData mBillingData;
//    private TextView text_retention_cash;
//    // The helper object
//    IabHelper mHelper;
//
//    // Provides purchase notification while this app is running
//    IabBroadcastReceiver mBroadcastReceiver;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        String base64EncodedPublicKey = getString(R.string.public_key);
//
//        // Create the helper, passing it our context and the public key to verify signatures with
//        mHelper = new IabHelper(this, base64EncodedPublicKey);
//
//        // enable debug logging (for a production application, you should set this to false).
//        mHelper.enableDebugLogging(true);
//
//        // Start setup. This is asynchronous and the specified listener
//        // will be called once setup completes.
//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
//
//            public void onIabSetupFinished(IabResult result){
//
//                LogUtil.e(LOG_TAG, "Setup finished.");
//
//                if(!result.isSuccess()) {
//                    // Oh noes, there was a problem.
//                    complain("Problem setting up in-app billing: " + result);
//                    return;
//                }
//
//                // Have we been disposed of in the meantime? If so, quit.
//                if(mHelper == null) return;
//
//                // Important: Dynamically register for broadcast messages about updated purchases.
//                // We register the receiver here instead of as a <receiver> in the Manifest
//                // because we always call getPurchases() at startup, so therefore we can ignore
//                // any broadcasts sent while the app isn't running.
//                // Note: registering this listener in an Activity is a bad idea, but is done here
//                // because this is a SAMPLE. Regardless, the receiver must be registered after
//                // IabHelper is setup, but before first call to getPurchases().
//                mBroadcastReceiver = new IabBroadcastReceiver(BolBillingActivity.this);
//                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
//                registerReceiver(mBroadcastReceiver, broadcastFilter);
//
//                // IAB is fully set up. Now, let's get an inventory of stuff we own.
//                LogUtil.e(LOG_TAG, "Setup successful. Querying inventory.");
//                try {
//                    mHelper.queryInventoryAsync(mGotInventoryListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error querying inventory. Another async operation in progress.");
//                }
//            }
//        });
//
//        TextView text_billing_description = (TextView)findViewById(R.id.text_billing_description);
//        text_billing_description.setText(R.string.msg_select_charge_bol);
//        TextView text_retention_title = (TextView)findViewById(R.id.text_billing_retention_title);
//        text_retention_title.setText(R.string.word_retention_bol);
//        text_retention_cash = (TextView) findViewById(R.id.text_cash_billing_retention_cash);
//
//        PplusCommonUtil.Companion.reloadSession(new PplusCommonUtil.Companion.ReloadListener(){
//
//            @Override
//            public void reload(){
//
//                text_retention_cash.setText(FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalBol()));
//            }
//        });
//
//        RecyclerView recycler_bank_list = (RecyclerView) findViewById(R.id.recycler_cash_billing);
//        recycler_bank_list.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new BolBillingAdapter(this);
//        recycler_bank_list.setAdapter(mAdapter);
//
//        billingDataList = new ArrayList<>();
//        billingDataList.add(new BillingData("5000","2500", "p_2.500"));
//        billingDataList.add(new BillingData("10000","5000", "p_5.000"));
//        billingDataList.add(new BillingData("20000", "10000", "p_10.000"));
//        billingDataList.add(new BillingData("50000", "25000", "p_25.000"));
//        billingDataList.add(new BillingData("100000", "50000", "p_50.000"));
//        mAdapter.setDataList(billingDataList);
//        mAdapter.setSelectData(billingDataList.get(2));
//
//        mAdapter.setOnClickListener(new BolBillingAdapter.OnClickListener(){
//
//            @Override
//            public void onClick(int position){
//
//                mAdapter.setSelectData(billingDataList.get(position));
//            }
//        });
//
//        scroll_cash_charge = (NestedScrollView) findViewById(R.id.scroll_cash_billing);
//
//        layout_terms = (LinearLayout) findViewById(R.id.layout_cash_billing_terms);
//        if(SystemBoardManager.getInstance().getBoard() != null) {
//            List<Post> list = SystemBoardManager.getInstance().getBoard().getCashList();
//
//            if(list != null) {
//                for(int i = 0; i < list.size(); i++) {
//                    View viewTerms = getLayoutInflater().inflate(R.layout.layout_cash_terms, new LinearLayout(this));
//                    final TextView title = (TextView) viewTerms.findViewById(R.id.text_cash_terms_title);
//                    final TextView contents = (TextView) viewTerms.findViewById(R.id.text_cash_terms_contents);
//                    title.setText(list.get(i).getSubject());
//                    contents.setText(list.get(i).getContents());
//                    title.setOnClickListener(new View.OnClickListener(){
//
//                        @Override
//                        public void onClick(View view){
//
//                            title.setSelected(!title.isSelected());
//                            Animation animation;
//                            if(contents.getVisibility() == View.VISIBLE) {
//                                animation = AnimationUtils.loadAnimation(BolBillingActivity.this, R.anim.scale_hide);
//                                animation.setAnimationListener(new Animation.AnimationListener(){
//
//                                    @Override
//                                    public void onAnimationStart(Animation animation){
//
//                                    }
//
//                                    @Override
//                                    public void onAnimationEnd(Animation animation){
//
//                                        contents.setVisibility(View.GONE);
//                                    }
//
//                                    @Override
//                                    public void onAnimationRepeat(Animation animation){
//
//                                    }
//                                });
//                            } else {
//                                contents.setVisibility(View.VISIBLE);
//
//                                animation = AnimationUtils.loadAnimation(BolBillingActivity.this, R.anim.scale_show);
//                                animation.setAnimationListener(new Animation.AnimationListener(){
//
//                                    @Override
//                                    public void onAnimationStart(Animation animation){
//
//                                    }
//
//                                    @Override
//                                    public void onAnimationEnd(Animation animation){
//
//                                        scroll_cash_charge.smoothScrollTo(0, scroll_cash_charge.getBottom());
//                                    }
//
//                                    @Override
//                                    public void onAnimationRepeat(Animation animation){
//
//                                    }
//                                });
//
//                            }
//
//                            contents.startAnimation(animation);
//                        }
//                    });
//                    layout_terms.addView(viewTerms);
//                }
//            }
//        }
//
//
//        findViewById(R.id.text_cash_billing_charge).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v){
//
//                showProgress("");
//
//                        /* TODO: for security, generate your payload here for verification. See the comments on
//         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//         *        an empty string, but on a production app you should carefully generate this. */
//                String payload = "";
//
//                LogUtil.e(LOG_TAG, "Launching purchase flow for gas subscription.");
//                mBillingData = mAdapter.getSelectData();
//                try {
//                    mHelper.launchPurchaseFlow(BolBillingActivity.this, mBillingData.getId(), Const.REQ_CASH_CHANGE, mPurchaseFinishedListener, payload);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error launching purchase flow. Another async operation in progress.");
//                    hideProgress();
//                }
//            }
//        });
//    }
//
//    // Listener that's called when we finish querying the items and subscriptions we own
//    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener(){
//
//        public void onQueryInventoryFinished(IabResult result, Inventory inventory){
//
//            LogUtil.e(LOG_TAG, "Query inventory finished.");
//
//            // Have we been disposed of in the meantime? If so, quit.
//            if(mHelper == null) return;
//
//            // Is it a failure?
//            if(result.isFailure()) {
//                complain("Failed to query inventory: " + result);
//                return;
//            }
//
//            LogUtil.e(LOG_TAG, "Query inventory was successful.");
//
//            /*
//             * Check for items we own. Notice that for each purchase, we check
//             * the developer payload to see if it's correct! See
//             * verifyDeveloperPayload().
//             */
//
//            for(int i = 0; i < billingDataList.size(); i++) {
//                Purchase purchase = inventory.getPurchase(billingDataList.get(i).getId());
//                if(purchase != null && verifyDeveloperPayload(purchase)) {
//                    try {
//
//                        LogUtil.e(LOG_TAG, "purchase id : {}", purchase.getOrderId());
//                        mHelper.consumeAsync(inventory.getPurchase(billingDataList.get(i).getId()), mConsumeFinishedListener);
//                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        complain("Error consuming gas. Another async operation in progress.");
//                    }
//                }
//            }
//
//
//            hideProgress();
//        }
//    };
//
//    @Override
//    protected void onDestroy(){
//
//        super.onDestroy();
//        unregisterReceiver(mBroadcastReceiver);
//    }
//
//    @Override
//    public void receivedBroadcast(){
//
//        LogUtil.e(LOG_TAG, "Received broadcast notification. Querying inventory.");
//        try {
//            mHelper.queryInventoryAsync(mGotInventoryListener);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error querying inventory. Another async operation in progress.");
//        }
//    }
//
//    @Override
//    public void onClick(DialogInterface dialog, int which){
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        LogUtil.e(LOG_TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
//        if(mHelper == null) return;
//
//        // Pass on the activity result to the helper for handling
//        if(!mHelper.handleActivityResult(requestCode, resultCode, data)) {
//            // not handled, so handle it ourselves (here's where you'd
//            // perform any handling of activity results not related to in-app
//            // billing...
//            super.onActivityResult(requestCode, resultCode, data);
//        } else {
//            LogUtil.e(LOG_TAG, "onActivityResult handled by IABUtil.");
//        }
//    }
//
//    /**
//     * Verifies the developer payload of a purchase.
//     */
//    boolean verifyDeveloperPayload(Purchase p){
//
//        String payload = p.getDeveloperPayload();
//
//        /*
//         * TODO: verify that the developer payload of the purchase is correct. It will be
//         * the same one that you sent when initiating the purchase.
//         *
//         * WARNING: Locally generating a random string when starting a purchase and
//         * verifying it here might seem like a good approach, but this will fail in the
//         * case where the user purchases an item on one device and then uses your app on
//         * a different device, because on the other device you will not have access to the
//         * random string you originally generated.
//         *
//         * So a good developer payload has these characteristics:
//         *
//         * 1. If two different users purchase an item, the payload is different between them,
//         *    so that one user's purchase can't be replayed to another user.
//         *
//         * 2. The payload must be such that you can verify it even when the app wasn't the
//         *    one who initiated the purchase flow (so that items purchased by the user on
//         *    one device work on other devices owned by the user).
//         *
//         * Using your own server to store and verify developer payloads across app
//         * installations is recommended.
//         */
//
//        return true;
//    }
//
//    // Called when consumption is complete
//    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener(){
//
//        public void onConsumeFinished(Purchase purchase, IabResult result){
//
//            LogUtil.e(LOG_TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
//
//            // if we were disposed of in the meantime, quit.
//            if(mHelper == null) return;
//
//            // We know this is the "gas" sku because it's the only one we consume,
//            // so we don't check which sku was consumed. If you have more than one
//            // sku, you probably should check...
//            if(result.isSuccess()) {
//                // successfully consumed, so we apply the effects of the item in our
//                // game world's logic, which in our case means filling the gas tank a bit
//
//            } else {
//                complain("Error while consuming: " + result);
//            }
//
//            LogUtil.e(LOG_TAG, "End consumption flow.");
//        }
//    };
//
//    // Callback for when a purchase is finished
//    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener(){
//
//        public void onIabPurchaseFinished(IabResult result, Purchase purchase){
//
//            LogUtil.e(LOG_TAG, "Purchase finished: " + result + ", purchase: " + purchase);
//            hideProgress();
//            // if we were disposed of in the meantime, quit.
//            if(mHelper == null) return;
//
//            if(result.isFailure()) {
//                complain("Error purchasing: " + result);
//                return;
//            }
//            if(!verifyDeveloperPayload(purchase)) {
//                complain("Error purchasing. Authenticity verification failed.");
//                return;
//            }
//            try {
//                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
//            } catch (IabHelper.IabAsyncInProgressException e) {
//                complain("Error consuming cash. Another async operation in progress.");
//                return;
//            }
//            LogUtil.e(LOG_TAG, "Purchase successful.");
//            chargeCash(purchase);
//
//        }
//    };
//
//    private void chargeCash(Purchase purchase){
//
//        String packageName = purchase.getPackageName();
//        String productId = purchase.getSku();
//        String token = purchase.getToken();
//
//        CashBillingProperties properties = new CashBillingProperties();
//        properties.setPackageName(packageName);
//        properties.setProductId(productId);
//        properties.setPurchaseToken(token);
//
//        Cash params = new Cash();
//        params.setAmount(mBillingData.getPoint());
//        params.setPaymentProperties(properties);
//        showProgress("");
//        ApiBuilder.create().chargeBol(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//                hideProgress();
//                showAlert(R.string.msg_charged_bol);
//                PplusCommonUtil.Companion.reloadSession(new PplusCommonUtil.Companion.ReloadListener(){
//
//                    @Override
//                    public void reload(){
//
//                        text_retention_cash.setText(FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalBol()));
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//
//    void complain(String message){
//
//        alert("Error: " + message);
//    }
//
//    void alert(String message){
//
//        AlertDialog.Builder bld = new AlertDialog.Builder(this);
//        bld.setMessage(message);
//        bld.setNeutralButton("OK", null);
//        LogUtil.e(LOG_TAG, "Showing alert dialog: " + message);
//        bld.create().show();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_charge), ToolbarOption.ToolbarMenu.RIGHT);
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
//                    case RIGHT:
//                        if(tag.equals(1)) {
//                            onBackPressed();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}
