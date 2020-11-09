package utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;




public abstract class CommonAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private Activity context;
    private List<T> itemData;
    private static final String TAG = "CommonAdapter";
    public CommonAdapter(Activity context) {
        itemData = new ArrayList<>();
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, itemData.get(position));
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutResId(viewType), parent, false);
        return holderInstance(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        T item = itemData.get(position);
        fillView(holder, item, position);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return itemData == null ? 0 : itemData.size();
    }

    public int getItemViewType(int position, T data){
        return 0;
    }

    public abstract int layoutResId(int viewType);
    /**
     * ViewHolder实例化
     */
    public abstract VH holderInstance(View itemView,int viewType);

    /**
     * 进行视图填充,推荐在ViewHolder里面写逻辑代码
     */
    public abstract void fillView(VH holder, T data, int position);

    /**
     * 关于数据源的增加
     */
    public void append(T item) {
        itemData.add(item);
        notifyItemInserted(itemData.size()-1);
    }

    public void appendAll(@NonNull List<T> item) {
        itemData.addAll(item);
        notifyItemRangeInserted(itemData.size() - 1, item.size());
    }

    public void appendHeader(T item) {
        itemData.add(0, item);
        notifyItemInserted(0);
    }

    public void appendAt(T item, int poistion) {
        if (poistion >= itemData.size()) {
            return;
        }
        itemData.add(poistion, item);
        notifyItemInserted(poistion);

    }

    /**
     * 关于数据源的修改
     */
    public void repalceHeader(T item) {
        try {
            itemData.set(0, item);
            notifyItemChanged(0);
        }catch (Exception e){
        }

    }

    public void repalceFooter(T item) {
        try {
            itemData.set(itemData.size() - 1, item);
            notifyItemChanged(itemData.size() - 1);
        }catch (Exception e){
        }

    }

    public void replace(T item, int poistion) {
        if (poistion >= itemData.size()) {
            return;
        }
        itemData.set(poistion, item);
        notifyItemChanged(poistion);
    }

    /**
     * 关于数据源的删除
     */
    public void delete(int poistion) {
        if (poistion >= itemData.size()) {
            return;
        }
        itemData.remove(poistion);
        notifyItemRemoved(poistion);
    }

    public void delete(T item) {
        try {
            int index = getItemIndex(item);
            itemData.remove(item);
            notifyItemRemoved(index);
        }catch (Exception e){
        }

    }

    public void deleteHeader() {
        try {
            itemData.remove(0);
            notifyItemRemoved(0);
        }catch (Exception e){
        }


    }

    public void deleteFooter() {
        try {
            itemData.remove(itemData.size() - 1);
            notifyItemRemoved(itemData.size());
        }catch (Exception e){
        }

    }

    public void clearAll() {
        itemData.clear();
        notifyDataSetChanged();
    }

    /**
     * 关于数据源的查找
     */
    public T getTheItem(int index) {
        if (index >= itemData.size()) {
            return null;
        }
        return itemData.get(index);
    }

    public T getItemHeader() {
        if (itemData.size() == 0){
            return null;
        }
        return itemData.get(0);
    }

    public T getItemFooter() {
        if (itemData.size() == 0){
            return null;
        }
        return itemData.get(itemData.size() - 1);
    }

    public int getItemIndex(T item) {
        return itemData.indexOf(item);
    }

    public List<T> getItemData() {
        return itemData;
    }
}
