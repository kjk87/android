package com.pplus.prnumberbiz.apps.cash.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Payment;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by 김종경 on 2015-06-17.
 */
public class PayPendingAdapter extends RecyclerView.Adapter<PayPendingAdapter.ViewHolder>{

    private Context context;
    private List<Payment> mDataList;
    private OnClickListener mOnClickListener;

    public interface OnClickListener{

        void onClick(int position);
    }

    public PayPendingAdapter(Context context){

        this.context = context;
        mDataList = new ArrayList<>();
    }

    public void setOnClickListener(OnClickListener OnClickListener){

        this.mOnClickListener = OnClickListener;
    }

    public void setDataList(List<Payment> dataList){

        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void add(Payment data){

        mDataList.add(0, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_pay_pending_date, text_pay_pending_amount;

        public ViewHolder(View itemView){

            super(itemView);
            text_pay_pending_date = (TextView) itemView.findViewById(R.id.text_pending_date);
            text_pay_pending_amount = (TextView) itemView.findViewById(R.id.text_pending_amount);
        }
    }

    public Payment getItem(int position){

        return mDataList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Payment item = mDataList.get(position);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            Date d = sdf.parse(item.getProperties().getP_AUTH_DT());
            SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
            holder.text_pay_pending_date.setText(output.format(d));
        } catch (Exception e) {

        }

        holder.text_pay_pending_amount.setText(PplusCommonUtil.Companion.fromHtml(context.getString(R.string.html_msg_pay_pending_amount, FormatUtil.getMoneyType(item.getProperties().getP_AMT()))));

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(mOnClickListener != null) {
                    mOnClickListener.onClick(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
