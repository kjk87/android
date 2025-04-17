package com.pplus.prnumberbiz.apps.group.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.listener.OnSortChangedListener;
import com.pplus.prnumberbiz.apps.group.ui.GroupConfigActivity;
import com.pplus.prnumberbiz.apps.group.ui.GroupNameEditActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Group;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsGroupPriority;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

import static android.app.Activity.RESULT_OK;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class GroupConfigAdapter extends RecyclerView.Adapter<GroupConfigAdapter.ViewHolder> implements DraggableItemAdapter<GroupConfigAdapter.ViewHolder>{

    private Context mContext;
    private List<Group> mDataList;
    private OnItemClickListener listener;
    private OnSortChangedListener mOnSortChangedListener;
    private EnumData.CustomerType mType;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public GroupConfigAdapter(Context context, EnumData.CustomerType type){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
        this.mType = type;
    }

    @Override
    public long getItemId(int position){

        return mDataList.get(position).getNo(); // need to return stable (= not change even after reordered) value
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public void setOnSortChangedListener(OnSortChangedListener onSortChangedListener){

        this.mOnSortChangedListener = onSortChangedListener;
    }

    public Group getItem(int position){

        return mDataList.get(position);
    }

    public List<Group> getDataList(){

        return mDataList;
    }

    public void add(Group data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Group> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Group data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Group> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends AbstractDraggableItemViewHolder{

        public TextView textName;
        public View imageEdit;

        public ViewHolder(View itemView){

            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_group_list_name);
            imageEdit = itemView.findViewById(R.id.image_group_list_edit);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Group item = mDataList.get(position);
        holder.textName.setText(item.getName());
        holder.imageEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM);
                builder.setContents(new String[]{mContext.getString(R.string.word_change_groupName), mContext.getString(R.string.word_delete_group)});
                builder.setLeftText(mContext.getString(R.string.word_cancel));
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                        switch (event_alert) {
                            case LIST:
                                Intent intent = null;
                                switch (event_alert.getValue()) {
                                    case 1://그룹명변경
                                        intent = new Intent(mContext, GroupNameEditActivity.class);
                                        intent.putExtra(Const.DATA, item);
                                        intent.putParcelableArrayListExtra(Const.GROUP, (ArrayList<Group>)mDataList);
                                        intent.putExtra(Const.KEY, mType);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        ((Activity) mContext).startActivityForResult(intent, Const.REQ_GROUP_CONFIG);
                                        break;
//                                    case 2://그룹이동
//                                        intent = new Intent(mContext, GroupMoveActivity.class);
//                                        intent.putExtra(Const.KEY, Const.GROUP);
//                                        intent.putExtra(Const.GROUP, item);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        ((Activity) mContext).startActivityForResult(intent, Const.REQ_GROUP_CONFIG);
//                                        break;
                                    case 2://그룹삭제
                                        AlertBuilder.Builder builder = new AlertBuilder.Builder();
                                        builder.setTitle(mContext.getString(R.string.word_notice_alert));
                                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
                                        builder.addContents(new AlertData.MessageData(mContext.getString(R.string.format_msg_delete_group1, mContext.getString(R.string.word_plus_customer)), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
                                        builder.addContents(new AlertData.MessageData(mContext.getString(R.string.format_msg_delete_group2, item.getName()), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
                                        builder.setLeftText(mContext.getString(R.string.word_cancel)).setRightText(mContext.getString(R.string.word_confirm));
                                        builder.setOnAlertResultListener(new OnAlertResultListener(){

                                            @Override
                                            public void onCancel(){

                                            }

                                            @Override
                                            public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                                                switch (event_alert) {
                                                    case RIGHT://그룹삭제
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put("no", "" + item.getNo());
                                                        switch (mType){

                                                            case customer:
                                                                ApiBuilder.create().deleteGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                                                    @Override
                                                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                                                                        ((GroupConfigActivity) mContext).setResult(RESULT_OK);
                                                                        ((GroupConfigActivity) mContext).getGroupAll();

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                                                    }
                                                                }).build().call();
                                                                break;
                                                            case plus:
                                                                ApiBuilder.create().deleteFanGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                                                    @Override
                                                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                                                                        ((GroupConfigActivity) mContext).setResult(RESULT_OK);
                                                                        ((GroupConfigActivity) mContext).getPlusGroupAll();

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                                                    }
                                                                }).build().call();
                                                                break;
                                                        }

                                                        break;
                                                }
                                            }
                                        }).builder().show(mContext);
                                        break;
                                }
                                break;
                        }
                    }
                }).builder().show(mContext);
            }
        });
    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition){


        Group movedItem = mDataList.remove(fromPosition);
        mDataList.add(toPosition, movedItem);
        notifyItemMoved(fromPosition, toPosition);

        for(int i = 0; i < mDataList.size(); i++) {
            mDataList.get(i).setPriority(mDataList.size() - i);
        }

        ParamsGroupPriority params = new ParamsGroupPriority();
        params.setGroupList(mDataList);

        switch (mType){

            case customer:
                ApiBuilder.create().updateGroupPriorityAll(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                    @Override
                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                        if(mOnSortChangedListener != null) {
                            mOnSortChangedListener.onChange();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                    }
                }).build().call();
                break;
            case plus:
                ApiBuilder.create().updateFanGroupPriorityAll(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                    @Override
                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                        if(mOnSortChangedListener != null) {
                            mOnSortChangedListener.onChange();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                    }
                }).build().call();
                break;
        }


    }

    @Override
    public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y){

        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(ViewHolder holder, int position){

        return null;
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition){

        return true;
    }
}
