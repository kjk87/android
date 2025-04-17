package com.pplus.prnumberbiz.apps.setting.data;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.ui.custom.swipeLayout.SwipeRevealLayout;
import com.pplus.prnumberbiz.apps.common.ui.custom.swipeLayout.ViewBinderHelper;
import com.pplus.prnumberbiz.apps.setting.ui.AlarmContainerActivity;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>{

    private Context context;
    private List<Msg> mDataList;
    private OnItemClickListener listener;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private Activity mActivity;

    public void setActivity(Activity mActivity){

        this.mActivity = mActivity;
    }

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public AlarmAdapter(Context context){

        this.context = context;
        this.mDataList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Msg getItem(int position){

        return mDataList.get(position);
    }

    public void add(Msg data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Msg> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Msg data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setAlimList(ArrayList<Msg> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private SwipeRevealLayout swipeLayout;
        private LinearLayout ll_alim_layout;
        public TextView textTitle, textDate;
        public TextView tv_alim_leave;

        public ViewHolder(View itemView){

            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            ll_alim_layout = (LinearLayout)itemView.findViewById(R.id.layout_alarm);
            textTitle = (TextView) itemView.findViewById(R.id.text_alarm_title);
            textDate = (TextView) itemView.findViewById(R.id.text_alarm_date);
            tv_alim_leave = (TextView) itemView.findViewById(R.id.tv_alarm_leave);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        final Msg item = mDataList.get(position);

        binderHelper.bind(holder.swipeLayout, String.valueOf(position));

        holder.ll_alim_layout.setTag(position);
        holder.ll_alim_layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if(listener != null) {
                    int pos = (int) view.getTag();
                    listener.onItemClick(pos);
                }
            }
        });

        if(StringUtils.isNotEmpty(item.getContents())) {
            holder.textTitle.setText(item.getContents());
        }

        if(StringUtils.isNotEmpty(item.getCompleteDate())) {
            holder.textDate.setVisibility(View.VISIBLE);
            String date = DateFormatUtils.convertDateFormat(item.getCompleteDate(), "yyyy.MM.dd");
            holder.textDate.setText(date);
        }else{
            holder.textDate.setVisibility(View.GONE);
        }

        holder.tv_alim_leave.setTag(item);
        holder.tv_alim_leave.setTag(holder.tv_alim_leave.getId(), position);
        holder.tv_alim_leave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setTitle(context.getString(R.string.word_notice_alert));
                builder.addContents(new AlertData.MessageData(context.getString(R.string.msg_question_delete_alim), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
                builder.setLeftText(context.getString(R.string.word_cancel)).setRightText(context.getString(R.string.word_confirm));
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
                        switch (event_alert){
                            case RIGHT:
                                if(context instanceof AlarmContainerActivity){
                                    holder.swipeLayout.close(true);
                                    ((AlarmContainerActivity)context).delete(item.getNo());
                                }
                                break;
                        }
                    }
                }).builder().show(context);
            }
        });

    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
