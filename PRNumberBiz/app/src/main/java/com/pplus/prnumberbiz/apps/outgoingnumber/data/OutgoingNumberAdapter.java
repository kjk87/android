package com.pplus.prnumberbiz.apps.outgoingnumber.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.outgoingnumber.OutGoingNumberConfigActivity;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.OutgoingNumber;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class OutgoingNumberAdapter extends RecyclerView.Adapter<OutgoingNumberAdapter.ViewHolder>{

    private Context mContext;
    private List<OutgoingNumber> mDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public OutgoingNumberAdapter(Context context){

        setHasStableIds(true);
        mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public OutgoingNumber getItem(int position){

        return mDataList.get(position);
    }

    public List<OutgoingNumber> getDataList(){

        return mDataList;
    }

    public void add(OutgoingNumber data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<OutgoingNumber> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, OutgoingNumber data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<OutgoingNumber> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text_no, text_number, text_date;
        View layout_delete;

        public ViewHolder(View itemView){

            super(itemView);
            text_no = (TextView)itemView.findViewById(R.id.text_outgoing_number_no);
            text_number = (TextView)itemView.findViewById(R.id.text_outgoing_number);
            text_date = (TextView)itemView.findViewById(R.id.text_outgoing_number_date);
            layout_delete = itemView.findViewById(R.id.layout_outgoing_number_delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outgoing_number, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final OutgoingNumber item = getItem(position);

        holder.text_no.setText(String.valueOf(position+1)+".");
        holder.text_number.setText(PhoneNumberUtils.formatNumber(item.getMobile(), Locale.getDefault().getCountry()));

        try {
            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.getRegDate());
            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
            holder.text_date.setText(outputDate.format(d));
        } catch (Exception e) {

        }

        holder.layout_delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setTitle(mContext.getString(R.string.word_notice_alert));
                builder.addContents(new AlertData.MessageData(mContext.getString(R.string.msg_question_delete_number), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                builder.setLeftText(mContext.getString(R.string.word_cancel)).setRightText(mContext.getString(R.string.word_confirm));
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
                        switch (event_alert){
                            case RIGHT:
                                Map<String, String> params = new HashMap<>();
                                params.put("mobile", item.getMobile());
                                ApiBuilder.create().deleteAuthedNumber(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                    @Override
                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){



                                        if(mContext instanceof OutGoingNumberConfigActivity){

                                            ((OutGoingNumberConfigActivity)mContext).showAlert(R.string.msg_delete_complete);

                                            ((OutGoingNumberConfigActivity)mContext).init();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                    }
                                }).build().call();
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
}
