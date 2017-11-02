package com.sjain.couponduniatask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sjain.couponduniatask.adapter.TaskReponseAdapter;
import com.sjain.couponduniatask.interfaces.OnLoadMoreListener;
import com.sjain.couponduniatask.model.ItemTask;
import com.sjain.couponduniatask.network.apimodel.APITaskResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public Context mContext;
    public int mTotalPages; // Total Number of pages
    public int mCurrentPage; // Current page
    public ArrayList<ItemTask> mItemTaskList; // List of item from the api
    public TaskReponseAdapter mTaskResponseAdapter; //Adapter to load the list in Recycler View
    @BindView(R.id.rv_task_reponse_list)
    RecyclerView rvTaskResponseList;
    //Endless scroll Listener for loading the data with pagenation functionality
    public OnLoadMoreListener mOnLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            try {
                if (mCurrentPage < mTotalPages) {
                    mCurrentPage += 1;
                    mItemTaskList.add(null);
                    rvTaskResponseList.post(new Runnable() {
                        @Override
                        public void run() {
                            mTaskResponseAdapter.notifyItemChanged(mItemTaskList.size() - 1);
                        }
                    });

                    // Load data
                    // final int index = mItemTaskList.size() - 1;
                    getData(mCurrentPage);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @BindView(R.id.ll_no_internet_connection)
    LinearLayout llNoInternetConnection;
    @BindView(R.id.btn_tap_to_retry)
    Button btnTapToRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); //Butter knife to bind views
        mContext = this; //setting context for this activity
        rvTaskResponseList.setLayoutManager(new LinearLayoutManager(mContext)); // addding layout manager for recycler view
        mCurrentPage = 1;
        initFetchData();
        btnTapToRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFetchData();
            }
        });
    }

    public void initFetchData() {
        if (isNetworkAvailable()) {
            getData(mCurrentPage);
            rvTaskResponseList.setVisibility(View.VISIBLE);
            llNoInternetConnection.setVisibility(View.GONE);
        } else {
            rvTaskResponseList.setVisibility(View.GONE);
            llNoInternetConnection.setVisibility(View.VISIBLE);
        }
    }

    //Get the data from Api Where page is the number of page we are going to hit for eg: page :1,2,3 etc..
    public void getData(int pages) {
        Call<APITaskResponse> apiTaskResponseCall = MyApp.getRestClient(mContext).getApiService().getData(pages);
        apiTaskResponseCall.enqueue(new Callback<APITaskResponse>() {
            @Override
            public void onResponse(Call<APITaskResponse> call, Response<APITaskResponse> response) {
                if (response != null && response.body().getSuccess()) {
                    mTotalPages = response.body().getResponse().getTotalPages();
                    if (response.body().getResponse().getList() != null && response.body().getResponse().getList().size() > 0) {
                        if (mItemTaskList == null || mItemTaskList.size() == 0) {
                            mItemTaskList = response.body().getResponse().getList();
                            initLoadData();
                        } else {
                            mItemTaskList.remove(mItemTaskList.size() - 1);
                            mItemTaskList.addAll(response.body().getResponse().getList());
                            addNewData();
                        }
                    } else {
                        Toast.makeText(mContext, R.string.no_data_found, Toast.LENGTH_LONG);
                    }
                } else {
                    Toast.makeText(mContext, R.string.error_msg, Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<APITaskResponse> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }


    //For first/init loading the data in adapter
    private void initLoadData() {
        mTaskResponseAdapter = new TaskReponseAdapter(mContext, mItemTaskList, rvTaskResponseList);
        rvTaskResponseList.setAdapter(mTaskResponseAdapter);
        mTaskResponseAdapter.setOnLoadMoreListener(mOnLoadMoreListener);
    }

    //For Updating the data and notifying the data
    private void addNewData() {
        mTaskResponseAdapter.notifyDataSetChanged();
        mTaskResponseAdapter.setLoaded();
    }


    //Check for network connection
    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

}
