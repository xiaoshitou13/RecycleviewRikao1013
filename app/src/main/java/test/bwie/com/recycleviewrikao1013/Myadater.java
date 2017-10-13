package test.bwie.com.recycleviewrikao1013;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 白玉春 on 2017/10/13.
 */

public class Myadater extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<Mydata.DataBean> lists;
    private final int EMPTY_VIEW = 1;
    private final int PROGRESS_VIEW = 2;

    public Myadater(Context context, List<Mydata.DataBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    // dian ji
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemViewType(int position) {
        if(lists.get(position).getNews_id().equals("13811")){
            return PROGRESS_VIEW;
        } else if(lists.size() ==0){
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == PROGRESS_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
            return new leftViewHolder(view);
        } else if(viewType == EMPTY_VIEW){
            return null;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof leftViewHolder) {
            leftViewHolder leftViewHolders = ((leftViewHolder) holder);
            leftViewHolders.textView.setText(lists.get(position).getNews_title());

            Glide.with(context).load(lists.get(position).getPic_url()).into(leftViewHolders.imageView);
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
            itemViewHolder.textViews.setText(lists.get(position).getNews_title());

            Glide.with(context).load(lists.get(position).getPic_url()).into(itemViewHolder.imageViews);
        }


        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }

    }






    /**
     *  返回 多少条数据
     * @return
     */
    @Override
    public int getItemCount() {
        return lists!=null?lists.size():0;
    }


    /**
     *
     */
    class leftViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public leftViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.tv);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViews;
        TextView textViews;
        public ItemViewHolder(View itemView) {
            super(itemView);
            imageViews = itemView.findViewById(R.id.image);
            textViews = itemView.findViewById(R.id.tv);
        }
    }

    public void Add(List<Mydata.DataBean> newlists,int page,int postaion){
        if(page != 1){
         //   this.lists.clear();
            this.lists.addAll(newlists);
            notifyItemInserted(postaion);
        }else{
           // this.lists.clear();
            Toast.makeText(context, "傻子 谁让你传1了", Toast.LENGTH_SHORT).show();
        }
    }

    public void Remove(int potaion){

            if(lists.size()>potaion) {
                this.lists.remove(potaion);
                notifyItemRemoved(potaion);
            }else{
                Toast.makeText(context, "xingle", Toast.LENGTH_SHORT).show();
            }
}



}
