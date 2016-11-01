package com.lenovo.service.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lenovo.service.R;
import com.lenovo.service.Utils;
import com.lenovo.service.adapter.BoxOrderAdapter;
import com.lenovo.service.cache.SharedPrefManager;
import com.lenovo.service.entity.base.BoxOrderData;
import com.lenovo.service.http.HttpClientManager;
import com.lenovo.service.http.NetInterface;
import com.lenovo.service.http.ResultUtils;
import com.lenovo.service.http.callback.NoDoubleClickLinstenner;
import com.lenovo.service.http.callback.adapter.JsonHttpCallBack;
import com.lenovo.service.http.callback.adapter.StringHttpCallBack;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBoxFragment extends BaseFragment {
    private EditText mEditSN;
    private Button mBtnSearch;
    private ListView mListView;

    //变量
    private BoxOrderAdapter mAdapter;//适配器
    private ArrayList<BoxOrderData.BoxBeanItem> datas;
    private String productSN;//主机SN号码
    private SharedPreferences mSharedPreference;
    private String token;
    private boolean canClick;//listview是否能点击
    //常量
    public static final String PARAM_SN = "product_sn";
    public static final String BOX_INFO = "box_info";

    public OrderBoxFragment() {
        // Required empty public constructor
    }

    public static OrderBoxFragment newInstance(String param1) {
        OrderBoxFragment fragment = new OrderBoxFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_SN, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productSN = getArguments().getString(PARAM_SN) == null ? "" : getArguments().getString(PARAM_SN);
        }
        mSharedPreference = SharedPrefManager.getSystemSharedPref(getContext());
        token = mSharedPreference.getString(SharedPrefManager.LOGIN_TOKEN, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_box, container, false);
        mEditSN = (EditText) view.findViewById(R.id.edit_inputSN);
        mBtnSearch = (Button) view.findViewById(R.id.btn_searchSN);
        mListView = (ListView) view.findViewById(R.id.list_boxOrders);

        mBtnSearch.setOnClickListener(noDoubleClickLinstenner);

        initData();
        return view;
    }

    private void initData() {
        mEditSN.setText(productSN);
        datas = new ArrayList<>();
        mAdapter = new BoxOrderAdapter(getContext(), datas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!canClick)
                    return;
                BoxOrderData.BoxBeanItem item = datas.get(position);
                Intent intentData = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BOX_INFO, item);
                intentData.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK, intentData);
                getActivity().finish();
            }
        });
        getOrderBoxInfo(productSN);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getOrderBoxInfo(String SN) {
        if (Utils.checkInternetStatus(getContext()) == 0) {
            Toast.makeText(getContext(), R.string.text_internet_unavalible, Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("product_no", SN);
        showLoadingDialog();
        HttpClientManager.post(NetInterface.GET_ORDERBOX_INFO + "token=" + token, params, new JsonHttpCallBack<BoxOrderData>() {
            @Override
            public void onSuccess(BoxOrderData result) {
                dismissLoadingDialog();
                if (ResultUtils.isSuccess(getActivity(), result)) {
                    mListView.setVisibility(View.VISIBLE);
                    BoxOrderData.BoxOrderBean bean = result.getData();
                    if (bean != null) {
                        ArrayList<BoxOrderData.BoxBeanItem> datas = bean.getMaterial();
//                        if (datas != null && datas.size() > 0) {
                        mAdapter.refreshDatas(datas);
//                        }
                    }
                } else {
                    mListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Exception e) {
                mListView.setVisibility(View.GONE);
                dismissLoadingDialog();
                if (isResumed())
                    Utils.showServerError(getActivity());
            }
        });

    }

    private NoDoubleClickLinstenner noDoubleClickLinstenner = new NoDoubleClickLinstenner() {
        @Override
        public void doClick(View view) {
            switch (view.getId()) {
                case R.id.btn_searchSN:
                    String sn = mEditSN.getText().toString().trim();
                    if (sn != null && !TextUtils.isEmpty(sn)) {
                        getOrderBoxInfo(sn);
                    } else {
                        Toast.makeText(getContext(), R.string.text_input_sn, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    public void setListCanClick(boolean clickable) {
        canClick = clickable;
    }
}
