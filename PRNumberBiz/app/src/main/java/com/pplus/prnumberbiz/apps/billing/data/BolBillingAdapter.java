package com.pplus.prnumberbiz.apps.billing.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pplus.prnumberbiz.R;

import java.util.ArrayList;

/**
 * Created by 김종경 on 2015-06-17.
 */
public class BolBillingAdapter extends RecyclerView.Adapter<BolBillingAdapter.ViewHolder>{

    private Context context;
    private ArrayList<BillingData> mDataList;
    private OnClickListener mOnClickListener;
    private BillingData mSelectData;

    public interface OnClickListener{

        void onClick(int position);
    }

    public BolBillingAdapter(Context context){

        this.context = context;
        mDataList = new ArrayList<>();
    }

    public void setOnClickListener(OnClickListener OnClickListener){

        this.mOnClickListener = OnClickListener;
    }

    public void setDataList(ArrayList<BillingData> dataList){

        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void add(BillingData data){

        mDataList.add(0, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList.clear();
        notifyDataSetChanged();
    }

    public BillingData getSelectData(){

        return mSelectData;
    }

    public void setSelectData(BillingData data){

        this.mSelectData = data;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text_cash, text_krw;

        public ViewHolder(View itemView){

            super(itemView);
            text_cash = (TextView) itemView.findViewById(R.id.text_cash_billing_cash);
            text_krw = (TextView)itemView.findViewById(R.id.text_cash_billing_krw);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cash_billing, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final BillingData item = mDataList.get(position);
        holder.text_cash.setText(FormatUtil.getMoneyType(item.getPoint())+ " " + context.getString(R.string.word_point));
        holder.text_krw.setText(context.getString(R.string.word_money_unit_en) + " " + FormatUtil.getMoneyType(item.getAmount()));

        holder.text_cash.setSelected(item.equals(mSelectData));

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
