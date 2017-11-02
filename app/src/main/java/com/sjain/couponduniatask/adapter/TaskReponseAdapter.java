package com.sjain.couponduniatask.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sjain.couponduniatask.R;
import com.sjain.couponduniatask.interfaces.OnLoadMoreListener;
import com.sjain.couponduniatask.model.ItemTask;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskReponseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<ItemTask> mItemTaskList = Collections.emptyList();
    private LayoutInflater inflater;
    private Context mContext;
    private OnLoadMoreListener mOnLoadMoreListener;
    private RecyclerView mRecyclerView;
    private int mBackground;

    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    public TaskReponseAdapter(final Context mContext, List<ItemTask> mItemTaskList, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.mItemTaskList = mItemTaskList;
        mBackground = mTypedValue.resourceId;
        this.mRecyclerView = mRecyclerView;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_task_data, parent, false);
            return new TaskReponseAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new TaskReponseAdapter.LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TaskReponseAdapter.ViewHolder) {
            ViewHolder holderGen = (ViewHolder) holder;
            ItemTask currentData = mItemTaskList.get(position);
            //    holderGen.ivTaskImage.setImageURI(Uri.parse(currentData.getIcon()));
            holderGen.tvTaskText.setText(currentData.getName());
            Picasso.with(mContext)
                    .load(currentData.getIcon())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holderGen.ivTaskImage);

        } else if (holder instanceof TaskReponseAdapter.LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.pbLoadingTaskResponse.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mItemTaskList == null ? 0 : mItemTaskList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemTaskList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_task_text)
        TextView tvTaskText;
        @BindView(R.id.iv_task_image)
        ImageView ivTaskImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pb_loading_task_reponse)
        ProgressBar pbLoadingTaskResponse;

        private LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
