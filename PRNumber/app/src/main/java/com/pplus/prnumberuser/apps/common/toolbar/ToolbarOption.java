package com.pplus.prnumberuser.apps.common.toolbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pplus.utils.part.apps.resource.ResourceUtil;
import com.pplus.prnumberuser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by j2n on 2016. 8. 16..
 * <p>
 * <pre>
 *     1. toolbar menu tag 기능 추가
 * </pre>
 * <pre>
 *     @TODO int 타입으로 기능을 추가 하였으나 개발자의 실수로인한 int타입이 중복으로 등록될 가능성이 있음.. 그럼으로 태그를 별도의 타입으로 변경기능을
 * 생각해봐야함.
 * </pre>
 */
public class ToolbarOption{

    private Map<ToolbarMenu, ArrayList<View>> toolbarMenuArrayListMap;

    private Context context;

    private float defaultMargin;

    private int actionBarHeight = 0;
    /**
     * 타이틀 색상
     */
    @ColorInt
    private int toolbarBackgroundColor = Color.WHITE;

    private ToolbarGravity titleGravity = ToolbarGravity.CENTER;

    private String title = "";

    private boolean scrollFlags = false;

    private ImageView imageButton;

    public enum ToolbarGravity{
        LEFT, CENTER, RIGHT
    }

    public enum ToolbarMenu{
        LEFT, RIGHT
    }

    public ToolbarOption(Context context){

        toolbarMenuArrayListMap = new HashMap<>();
        this.context = context;
        defaultMargin = ResourceUtil.getDpToPixel(context, 2);
    }

    public int getToolbarBackgroundColor(){

        return toolbarBackgroundColor;
    }

    public ToolbarGravity getTitleGravity(){

        return titleGravity;
    }

    /**
     * 타이틀 위치 변경 (left , center , right)
     */
    public void setTitleGravity(ToolbarGravity titleGravity){

        this.titleGravity = titleGravity;
    }

    /**
     * 타이틀 색상
     */
    public void setToolbarBackgroundColor(@ColorInt int toolbarBackgroundColor){

        this.toolbarBackgroundColor = toolbarBackgroundColor;
    }

    public void setToolbarMenu(ToolbarMenu toolbarMenu, View view, int tag){

        if(!toolbarMenuArrayListMap.containsKey(toolbarMenu)) {
            toolbarMenuArrayListMap.put(toolbarMenu, new ArrayList<View>());
        }

        ArrayList<View> viewArrayList = toolbarMenuArrayListMap.get(toolbarMenu);
        //        view.setBackgroundResource(R.drawable.drawable_toolbar);
        viewArrayList.add(view);
        if(tag == 0) {
            tag = viewArrayList.size();
        }
        view.setTag(tag);
    }

    public void setToolbarMenu(ToolbarMenu toolbarMenu, @DrawableRes int res, int tag){

        imageButton = new ImageView(new ContextThemeWrapper(context, R.style.buttonStyle));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageButton.setForegroundGravity(Gravity.CENTER);
        }
        imageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageButton.setImageResource(res);
        if(toolbarMenu.equals(ToolbarMenu.RIGHT) && res != R.drawable.ic_top_close) {
            imageButton.setPadding(0, 0, context.getResources().getDimensionPixelSize(R.dimen.width_54), 0);
        } else if(toolbarMenu.equals(ToolbarMenu.LEFT)) {
            if(res == R.drawable.ic_top_bol) {
                imageButton.setPadding(0, 0, context.getResources().getDimensionPixelSize(R.dimen.width_10), 0);
            } else {
                imageButton.setPadding(context.getResources().getDimensionPixelSize(R.dimen.width_54), 0, 0, 0);
            }

        }

        imageButton.setMinimumWidth(actionBarHeight);

        setToolbarMenu(toolbarMenu, imageButton, tag);
    }

    public void setToolbarMenu(ToolbarMenu toolbarMenu, @DrawableRes int res){

        setToolbarMenu(toolbarMenu, res, 0);
    }

    public void setToolbarMenu(ToolbarMenu toolbarMenu, String value, int tag){

        TextView textView = new TextView(new ContextThemeWrapper(context, R.style.buttonStyle));
        textView.setText(value);
        textView.setClickable(true);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 0, context.getResources().getDimensionPixelSize(R.dimen.width_66), 0);
        textView.setTextColor(ResourceUtil.getColorStateList(context, R.color.color_579ffb));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.textSize_48pt));
        textView.setSingleLine();

        textView.setMinimumWidth(actionBarHeight);
        setToolbarMenu(toolbarMenu, textView, tag);
    }

    public void setToolbarMenu(ToolbarMenu toolbarMenu, String value){

        setToolbarMenu(toolbarMenu, value, 0);
    }

    public ArrayList<View> getToolbarMenuArrayList(ToolbarMenu toolbarMenu){

        return toolbarMenuArrayListMap.get(toolbarMenu);
    }

    public RelativeLayout.LayoutParams getViewParams(){

        TypedValue tv = new TypedValue();
        if(actionBarHeight == 0 && context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        /**
         * 높이 = 액션바의 높이만큼 (48dp)
         * 넓이 = 액션바의 높이 - 액션바의 높이 3/1
         * */
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, actionBarHeight);
        //        int btn_size = context.getResources().getDimensionPixelSize(R.dimen.height_144);
        //        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(btn_size, btn_size);
        viewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        //        viewParams.setMargins((int) defaultMargin, 0, (int) defaultMargin, 0);
        return viewParams;
    }

    public String getTitle(){

        return title;
    }

    public void setTitle(String title){

        this.title = title;
    }

    public boolean isScrollFlags(){

        return scrollFlags;
    }

    public void setScrollFlags(boolean scrollFlags){

        this.scrollFlags = scrollFlags;
    }

    public void initializeDefaultToolbar(String title, ToolbarMenu toolbarMenu){

        setTitle(title);

        switch (toolbarMenu) {
            case LEFT:
                setTitleGravity(ToolbarGravity.CENTER);
                setToolbarMenu(toolbarMenu, R.drawable.ic_top_prev);
                break;

            case RIGHT:
                setTitleGravity(ToolbarGravity.CENTER);
                setToolbarMenu(toolbarMenu, R.drawable.ic_top_close);
                break;
        }
    }

    public void setRIghtImageRes(@DrawableRes int res){

        if(res != -1 && imageButton != null) {
            imageButton.setImageResource(res);
        } else {
            imageButton.setImageDrawable(null);
        }
    }
}
