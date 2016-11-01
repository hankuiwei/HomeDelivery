package com.lenovo.service.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.service.R;
import com.lenovo.service.Utils;
import com.lenovo.service.cache.SharedPrefManager;
import com.lenovo.service.entity.base.MachineInfoData;
import com.lenovo.service.http.HttpClientManager;
import com.lenovo.service.http.NetInterface;
import com.lenovo.service.http.ResultUtils;
import com.lenovo.service.http.callback.NoDoubleClickLinstenner;
import com.lenovo.service.http.callback.adapter.JsonHttpCallBack;

import java.util.HashMap;


public class MachineMoreFragment extends BaseFragment {
    private EditText mEditSN;
    private Button mBtnSearch;
    private TableLayout mTableParent;
    private TextView mTxtProductSN;
    private TextView mTxtProductName;
    private TextView mTxtProductCreateTime;
    private TextView mTxtProductBoutTime;
    private TextView mTxtEnsurenceEndTime;
    private TextView mTxtGetdoorEndTime;
    //变量
    private String productSN;//主机SN号码
    private SharedPreferences mSharedPreference;
    private String token;
    //常量
    public static final String PARAM_SN = "product_sn";

    public MachineMoreFragment() {
        // Required empty public constructor
    }

    public static MachineMoreFragment newInstance(String param1) {
        MachineMoreFragment fragment = new MachineMoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_machine_more, container, false);
        mEditSN = (EditText) view.findViewById(R.id.edit_inputSN);
        mBtnSearch = (Button) view.findViewById(R.id.btn_searchSN);
        mTableParent = (TableLayout) view.findViewById(R.id.tableParents);
        mTxtProductSN = (TextView) view.findViewById(R.id.text_productSN);
        mTxtProductName = (TextView) view.findViewById(R.id.text_productName);
        mTxtProductCreateTime = (TextView) view.findViewById(R.id.text_productCreateTime);
        mTxtProductBoutTime = (TextView) view.findViewById(R.id.text_productBoutTime);
        mTxtEnsurenceEndTime = (TextView) view.findViewById(R.id.text_ensurenceEndline);
        mTxtGetdoorEndTime = (TextView) view.findViewById(R.id.text_getDoorEndLine);

        mBtnSearch.setOnClickListener(noDoubleClickLinstenner);
        mEditSN.setText(productSN);
        mEditSN.setSelection(productSN.length());
        getMachineInfo(productSN);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void getMachineInfo(String SN) {
        if (Utils.checkInternetStatus(getContext()) == 0) {
            Toast.makeText(getContext(), R.string.text_internet_unavalible, Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("product_no", SN);
        showLoadingDialog();
        HttpClientManager.post(NetInterface.GET_MACHINE_INIFO + "token=" + token, params, new JsonHttpCallBack<MachineInfoData>() {
            @Override
            public void onSuccess(MachineInfoData result) {
                dismissLoadingDialog();
                if (ResultUtils.isSuccess(getContext(), result)) {
                    mTableParent.setVisibility(View.VISIBLE);
                    MachineInfoData.MachineDataArray array = result.getData();
                    MachineInfoData.MachineData machineData = array.getMachine_info();
                    if (machineData != null) {
                        mTxtProductSN.setText(machineData.getProduct_no());
                        mTxtProductName.setText(machineData.getMachine_name());
                        mTxtGetdoorEndTime.setText(machineData.getOn_site_end_date());
                        mTxtEnsurenceEndTime.setText(machineData.getPart_end_date());
                        mTxtProductBoutTime.setText(TextUtils.isEmpty(machineData.getBuy_date()) ? "无" : machineData.getBuy_date());
                        mTxtProductCreateTime.setText(machineData.getProduct_date());
                    }
                } else {
                    mTableParent.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Exception e) {
                dismissLoadingDialog();
                mTableParent.setVisibility(View.GONE);
                try {
                    Utils.showServerError(getContext());

                } catch (Exception el) {
                }

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private NoDoubleClickLinstenner noDoubleClickLinstenner = new NoDoubleClickLinstenner() {
        @Override
        public void doClick(View view) {
            switch (view.getId()) {
                case R.id.btn_searchSN:
                    String sn = mEditSN.getText().toString().trim();
                    if (sn != null && !TextUtils.isEmpty(sn)) {
                        getMachineInfo(sn);
                    } else {
                        Toast.makeText(getContext(), R.string.text_input_sn, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
