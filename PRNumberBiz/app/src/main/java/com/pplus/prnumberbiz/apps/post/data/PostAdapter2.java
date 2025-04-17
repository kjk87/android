package com.pplus.prnumberbiz.apps.post.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class PostAdapter2 extends RecyclerView.Adapter<PostAdapter2.ViewHolder>{

    private Context mContext;
    private List<Post> mDataList;
    private OnItemClickListener listener;


    public HashMap<Integer, Integer> viewPageStates = new HashMap<>();

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public PostAdapter2(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return mDataList.get(position).getNo();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Post getItem(int position){

        return mDataList.get(position);
    }

    public List<Post> getDataList(){

        return mDataList;
    }

    public void add(Post data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Post> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Post data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Post> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView text_contents;

        public ViewHolder(View itemView){

            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image_select_post);
            text_contents = (TextView) itemView.findViewById(R.id.text_select_post_contents);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Post item = mDataList.get(position);
        holder.text_contents.setText(item.getContents());

        if(item.getAttachList() != null && item.getAttachList().size() > 0) {
            Glide.with(mContext).load(item.getAttachList().get(0).getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image);
        }else{
            holder.image.setImageResource(R.drawable.prnumber_default_img);
        }

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
