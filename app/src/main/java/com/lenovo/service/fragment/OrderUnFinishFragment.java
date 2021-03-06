package com.lenovo.service.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.lenovo.service.R;
import com.lenovo.service.ServiceApplication;
import com.lenovo.service.Utils;
import com.lenovo.service.adapter.OrderListAdapter;
import com.lenovo.service.cache.SharedPrefManager;
import com.lenovo.service.entity.base.OrderData;
import com.lenovo.service.entity.base.OrderList;
import com.lenovo.service.entity.base.Orders;
import com.lenovo.service.http.ErrorCode;
import com.lenovo.service.http.HttpClientManager;
import com.lenovo.service.http.NetInterface;
import com.lenovo.service.http.ResultUtils;
import com.lenovo.service.http.callback.adapter.JsonHttpCallBack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hankw on 16-8-1.
 */
public class OrderUnFinishFragment extends BaseFragment implements OnRefreshListener,OnLoadMoreListener{

    private final String TAG = getClass().getSimpleName();

    private View view;
    private OrderListAdapter mAdapter;
    private LinearLayout mOrdersBox;
    private TextView mOrdersLebel;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mSwipeTarget;
    private List<Orders> mOrders;
    private String mUserCode;
    private String mToken;
    private String mOrder_state = "0";//"０"未完成"１"已完成
    private String mPage_count = "6";//每页显示的条数
    private int mPage_index = 1;//页码
    private String mTotalNumber;//总条数
    private OrderList mOrderList;
    private Map<String, String> params;

    //private UnFinishResult mUnFinishResult;

    private boolean parentIsSelected =true;
    private boolean selfIsSelected = true;
    private boolean isFirstLoad = true;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView is starting !!!");

        mOrders = new ArrayList<Orders>();
        params = new HashMap<String, String>();
        view = inflater.inflate(R.layout.fragment_work_order_page, null);
        mOrdersLebel = (TextView) view.findViewById(R.id.ordersLebel);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        mSwipeTarget = (RecyclerView) view.findViewById(R.id.swipe_target);
        mOrdersBox = (LinearLayout) view.findViewById(R.id.ordersBox);
        mAdapter = new OrderListAdapter(getActivity(),mOrders,getContext(),mOrder_state);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setAdapter(mAdapter);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        initData();
        //getData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG," onStart() is Starting ");
      // mOrders.clear();
       // getData(mPage_index);
       /* if (mOrders != null &&mOrders.size()< 0){
            mOrders.clear();
            getData();
        }*/

    }




    /**
     * 初始化数据
     */
    private void initData() {
        Log.e(TAG," initData() is Starting ");
        SharedPreferences sharedPreferences = SharedPrefManager.getSystemSharedPref(getContext());
        mUserCode = SharedPrefManager.getStringInSharePref(sharedPreferences,SharedPrefManager.LOGIN_USER_CODE,"userCode");
        mToken = SharedPrefManager.getStringInSharePref(sharedPreferences,SharedPrefManager.LOGIN_TOKEN,"mToken");
        mOrderList = new OrderList();
        params.put("token",mToken);
        params.put("user_code",mUserCode);
        params.put("order_state", mOrder_state);
        params.put("page_count",mPage_count);
        ServiceApplication mApp = ServiceApplication.getInstance();
        UnHandler unHandler = new UnHandler();
        mApp.setHandler(unHandler);
        getData(mPage_index);
    }

    public  void getData(final int pageIndex) {
        Log.e(TAG," getData() is Starting ");
        int netState =  Utils.checkInternetStatus(getContext());
        if (netState == 0) {
           showNull();
            return;
        }

        Log.e(TAG," getData()  pageIndex is ("+pageIndex+")");
        params.put("page_index",""+pageIndex);

        showLoadingDialog();
        HttpClientManager.post(NetInterface.GET_ORDERSLIST+"?token="+mToken, params, new JsonHttpCallBack<OrderList>() {
            @Override
            public void onSuccess(OrderList result) {
                dismissLoadingDialog();
                isFirstLoad = false;
                    mSwipeToLoadLayout.setRefreshing(false);
                    mSwipeToLoadLayout.setLoadingMore(false);
                if (ResultUtils.isSuccess(getContext(),result)) {
                    OrderData mdata = result.getData();
                    mTotalNumber = mdata.getTotal_number();
                    if ("0".equals(mTotalNumber)){
                        showNull();
                        return;
                    }
                    List<Orders> ordersList = mdata.getOrders();
                   // mOrders.clear();
                   // mOrders.addAll();
                    if (ordersList != null && ordersList.size() > 0) {//数据正确返回
                        if (pageIndex == 1) {//下拉刷新的数据
                            mAdapter.refreshData(ordersList);
                        } else {//上拉加载的数据
                            mAdapter.addData(ordersList);
                        }
                    } else {//state为200，但数据有问题
                        if (mPage_index != 1) {//如果页面不是首页，页码减去1
                            mPage_index--;
                        }
                    }
                } else {
                    //Utils.dismissLoadingDialog();
                    if (result != null)
                    Log.d(TAG,"onSuccess() == > fail  is ("+result.getMessage()+")");
                }

                showNull();
            }

            @Override
            public void onError(Exception e) {
                dismissLoadingDialog();
                //Utils.dismissLoadingDialog();
                mSwipeToLoadLayout.setRefreshing(false);
                mSwipeToLoadLayout.setLoadingMore(false);
                if (mPage_index != 1) {//如果页面不是首页，页码减去1
                    mPage_index--;
                }
                //isLoading = false;
                showNull();
                isFirstLoad = false;
                Utils.showServerError(getContext());
                Log.d(TAG,"onError() == > Exception is ("+e.getMessage()+")");
            }
        });

    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
           // Utils.showLoadingDialog(getContext());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // getData(mPage_index);
        if (mOrdersBox.getVisibility()== View.VISIBLE){
            mOrdersBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPage_index = 1;
                    getData(mPage_index);
                }
            });
        }
    }


    public class UnHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.d(TAG,"UnHandler  handleMessage() is starting !");
            mPage_index = 1;
            getData(mPage_index);
        }
    }



    private void showNull() {
        if (mOrders == null || mOrders.size() == 0) {
            if (mOrdersBox!=null)
            mOrdersBox.setVisibility(View.VISIBLE);
            if (mSwipeToLoadLayout != null)
            mSwipeToLoadLayout.setVisibility(View.GONE);
        } else {
            if (mOrdersBox!=null)
            mOrdersBox.setVisibility(View.GONE);
            if (mSwipeToLoadLayout != null)
            mSwipeToLoadLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadMore() {
        /*if (isLoading)
            return;*/
        mPage_index++;
        Log.d(TAG,"onLoadMore() == > mPage_index is ("+mPage_index+")");
        getData(mPage_index);

    }

    @Override
    public void onRefresh() {
        /*if (isLoading)
            return;*/
        mPage_index = 1;
        Log.d(TAG,"onRefresh() == > mPage_index is ("+mPage_index+")");
        getData(mPage_index);
    }


    /**
     * 刷新工单列表页
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult() is starting !");
           /* mPage_index = 1;
           getData(mPage_index);*/

    }
    public void setParentSelect(boolean parentSelect){
        parentIsSelected = parentSelect;
        if(selfIsSelected && parentIsSelected && isFirstLoad){
            mPage_index = 1;
            getData(mPage_index);
        }
    }
    public void setSelfSelect(boolean selfSelect){
        selfIsSelected = selfSelect;
        if(selfSelect && parentIsSelected && isFirstLoad){
            mPage_index = 1;
            getData(mPage_index);
        }
    }
}
