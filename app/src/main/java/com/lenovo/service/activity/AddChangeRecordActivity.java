package com.lenovo.service.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.lenovo.service.R;
import com.lenovo.service.Utils;
import com.lenovo.service.adapter.ChangeRecordAdapter;
import com.lenovo.service.cache.SharedPrefManager;
import com.lenovo.service.entity.base.BreakdownsInfo;
import com.lenovo.service.entity.base.ChangeBoxsInfo;
import com.lenovo.service.entity.base.ChangeCategorys;
import com.lenovo.service.entity.base.ChangeTypes;
import com.lenovo.service.entity.base.InitChange;
import com.lenovo.service.entity.base.LoginData;
import com.lenovo.service.entity.base.ReasonList;
import com.lenovo.service.entity.base.ReplacedPartsDescs;
import com.lenovo.service.entity.base.ReplacedPartsFlag;
import com.lenovo.service.entity.base.UpChangeInfo;
import com.lenovo.service.http.ErrorCode;
import com.lenovo.service.http.HttpClientManager;
import com.lenovo.service.http.NetInterface;
import com.lenovo.service.http.ResultUtils;
import com.lenovo.service.http.callback.adapter.JsonHttpCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by hankw on 16-8-13.
 */
public class AddChangeRecordActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private final String TAG = getClass().getSimpleName();

    private RelativeLayout mChangeBack;
    private Spinner mSpinChangeKind;
    private Spinner mSpinChangeType;

    private LinearLayout mChangeGroup;
    private LinearLayout mChangeUpGroup;
    private RecyclerView mRecycleChangeUp;
    //private RadioButton mRadio;
    private CheckBox mRadio;

    private LinearLayout mChangeDownGroup;
    private Spinner mSpinChangeidentif;

    private LinearLayout mChangeidentyGroup;
    private LinearLayout mChangeidentyLine;

    private Spinner mSpinNonDamageCause; //非损
    private LinearLayout mLinNoDamage;
    private EditText mEditSpareDownId;
    private EditText mEditSpareDownBar;

    private LinearLayout mCauseOfFormation;
    private Spinner  mSpinSparePartReason;
    //换下件
    private LinearLayout mDownBarLine;
    private LinearLayout mDownDescribeGroup;
    private TextView mTxtSparePartDescribe;//备件描述
    private TextView mTxtSparePartDownClass;//备件大类
    private TextView mTxtSparePartDownPrice;//押款价

    private Button mBtnSparePartPacking;//装箱单
    private Button mBtnGenerateBar;
    private Spinner mSpinFailurePhenomena;
    private ArrayAdapter<String> reasonAdapter;
    private TextView mTxtFailureCode;
    private TextView mEditSpecialRemarks;
    private Button mBtnAddChangeCommit;

    private ChangeRecordAdapter mRecordAdapter;
    private List<UpChangeInfo> mupChangeList;

    private String mBarCode;//系统生成条码
    private boolean isBoxVisible;
    private boolean isBarCode;

    private List<BreakdownsInfo> mBreakdowns;
    private List<ChangeCategorys> mChangeCategorys;
    private List<String> kindList;
    private Map<String,String> kindMap;
    private ArrayAdapter<String> kindAdapter;
    private Map<String,String> typeValueMap;
    private Map<String,String> replacedPartValueMap;
    private Map<String,String> replacedescValueMap;
    private Map<String,String> reasonValueMap;
    private Map<String,List<ChangeTypes>> typeMap;
    private Map<String,List<ReplacedPartsFlag>> replacedflagMap;
    private Map<String,List<ReplacedPartsDescs>> replacedDescsMap;
    private Map<String,List<ReasonList>> reasonMap;
    private List<String> partsValueList;
    private List<ReasonList> reasonList;
    private List<String> reasonValueList;
    private List<String> typevalueList;
    private static final String ORDER_CODE = "order_code";
    public static final String PACKINGLIST = "packingList";
    private static final String PRODUCTNO = "proNO";
    public static final String CHANGETYPE = "changeType";
    public static final String CHANGEDOWN = "changeDown";
    public static final String CHANGEUP = "changeUp";
    private String code;
    private String mProductCode;
    private Map<String, String> params;

    //需要提交数据
    private String categoryCode;
    private String categoryValue;
    private String typeCode;
    private String typeValue;
    private String upSn;
    private String upCode;
    private String downSn;
    private String downCode;
    private String breakDownsCode;//reason_list_code
    private String breakDownsValue;//reason_list_value
    private String replacedPartsFlag;
    private String replacedPartsDescs = "";
    private String remark ="";//生成条码原因
    private String spe_note;//备注

    //回传到工单操作页数据
    private UpChangeInfo mUpChangeInfo;
    private ChangeBoxsInfo.BoxList box;


    private boolean isClick;
    @Override
    protected void provideLayout() {
        setContentView(R.layout.activity_addchange);
    }

    @Override
    protected void initView() {
        mChangeBack = (RelativeLayout) findViewById(R.id.Rel_changeBack);
        mSpinChangeKind = (Spinner) findViewById(R.id.Spin_changeKind);
        mSpinChangeType = (Spinner) findViewById(R.id.Spin_changeType);
        mChangeGroup = (LinearLayout) findViewById(R.id.Lin_ChangeInfoGroup);
        mChangeUpGroup = (LinearLayout) findViewById(R.id.Lin_ChangeUpGroup);
        mRecycleChangeUp = (RecyclerView) findViewById(R.id.Recycle_ChangeUp);
        mChangeDownGroup = (LinearLayout) findViewById(R.id.Lin_ChangeDownGroup);
        mSpinChangeidentif = (Spinner) findViewById(R.id.Spin_changeidentification);
        mSpinNonDamageCause = (Spinner) findViewById(R.id.Spin_NonDamageCause);
        mEditSpareDownId = (EditText) findViewById(R.id.Edit_SparePartDownId);
        mEditSpareDownBar = (EditText) findViewById(R.id.Edit_SparePartDownBar);
        mSpinSparePartReason = (Spinner) findViewById(R.id.Spin_sparePartDownReason);
        mTxtSparePartDescribe = (TextView) findViewById(R.id.Txt_sparePartDownDescribe);
        mTxtSparePartDownClass = (TextView) findViewById(R.id.Txt_sparePartDownClass);
        mTxtSparePartDownPrice = (TextView) findViewById(R.id.Txt_sparePartDownPrice);
        mBtnSparePartPacking = (Button) findViewById(R.id.Btn_sparePartPacking);
        mBtnGenerateBar = (Button) findViewById(R.id.Btn_Generate_bar);
        mSpinFailurePhenomena = (Spinner) findViewById(R.id.Spin_FailurePhenomena);
        mTxtFailureCode = (TextView) findViewById(R.id.Txt_Failure_Code);
        mEditSpecialRemarks = (TextView) findViewById(R.id.Edit_Special_Remarks);
        mBtnAddChangeCommit = (Button) findViewById(R.id.Btn_AddChangeCommit);
        mLinNoDamage = (LinearLayout) findViewById(R.id.Lin_NoDamage);
        mCauseOfFormation = (LinearLayout) findViewById(R.id.Lin_CauseOfFormation);
        mChangeidentyGroup = (LinearLayout) findViewById(R.id.changeidentyGroup);
        mChangeidentyLine = (LinearLayout) findViewById(R.id.changeidentyLine);
        mDownBarLine = (LinearLayout) findViewById(R.id.Lin_DownBarLine);
        mDownDescribeGroup = (LinearLayout) findViewById(R.id.Lin_downDescribeGroup);
        params = new HashMap<String, String>();
        mBreakdowns = new ArrayList<BreakdownsInfo>();
        mupChangeList = new ArrayList<UpChangeInfo>();
        kindList = new ArrayList<String>();
        kindList.add("请选择");
        reasonList = new ArrayList<ReasonList>();
        kindMap = new HashMap<String, String>();
        typeValueMap = new HashMap<String, String>();
        replacedPartValueMap = new HashMap<String, String>();
        replacedescValueMap = new HashMap<String, String>();
        reasonValueMap = new HashMap<String, String>();
        typeMap  = new HashMap<String, List<ChangeTypes>>();
        replacedflagMap = new HashMap<String, List<ReplacedPartsFlag>>();
        replacedDescsMap = new HashMap<String, List<ReplacedPartsDescs>>();
        reasonMap = new HashMap<String,List<ReasonList>>();
        mRecordAdapter = new ChangeRecordAdapter(AddChangeRecordActivity.this,mupChangeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddChangeRecordActivity.this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleChangeUp.setLayoutManager(layoutManager);
        mRecycleChangeUp.setAdapter(mRecordAdapter);
        kindAdapter = new ArrayAdapter<String>(AddChangeRecordActivity.this,R.layout.item_change_spinner_select,R.id.text1,kindList);
        mSpinChangeKind.setAdapter(kindAdapter);

    }

    @Override
    protected void initData() {
        // TODO: 16-8-25 网络状态放在工单处理页点击添加换件时进行校验，无网络将不跳转到此页面
        int netState =  Utils.checkInternetStatus(AddChangeRecordActivity.this);
        if (netState == 0) {dismissLoadingDialog();
            Toast.makeText(AddChangeRecordActivity.this,"网络不可用,请连接网络后重试",Toast.LENGTH_SHORT).show();
            return;
        }
        showLoadingDialog();

        Intent intent = getIntent();
        code =  intent.getStringExtra(ORDER_CODE);
        mProductCode = intent.getStringExtra(PRODUCTNO);
       // mProductCode = "4290AP5R9FYV5Z";
        SharedPreferences preferences = SharedPrefManager.getSystemSharedPref(AddChangeRecordActivity.this);
        String userCode = SharedPrefManager.getStringInSharePref(preferences, SharedPrefManager.LOGIN_USER_CODE, "userCode");
        String token = SharedPrefManager.getStringInSharePref(preferences, SharedPrefManager.LOGIN_TOKEN, "mToken");

        //token = "g38SmC5uY7sUeg31RJtsGLrCywrXkDsC8asCKRv08vNLgOWdmvYIKLDYGcluDANw";
        //userCode = "C47018";
        //code = "201605052100319002";//"201601212100310001";//"201605262100310001";
        //code = "201606232100310001";//"201607132100319002";
        // code = "201609082100319001";
        //mProductCode = "WB06148332";//"EA00239013";//"000100927800P3BP0027";
        if (!"userCode".equals(userCode) && !"mToken".equals(token)){
            params.put("token", token);
            params.put("user_code", userCode);
        } else {
            dismissLoadingDialog();
            Toast.makeText(AddChangeRecordActivity.this,"请登录！",Toast.LENGTH_LONG).show();
            return;
        }
        if (code != null) {
            params.put("order_code", code);
            HttpClientManager.post(NetInterface.INIT_CHANGE, params, new JsonHttpCallBack<InitChange>() {
                @Override
                public void onSuccess(InitChange result) {
                   //Log.d(TAG,"initData() ==　> result.getStatus_code() is("+result.getStatus_code()+")");
                    dismissLoadingDialog();
                    if (ResultUtils.isSuccess(AddChangeRecordActivity.this,result)) {
                        InitChange.ChangeInfo changeInfo = result.getData();
                        mBarCode = changeInfo.getBarCode();
                        isBoxVisible = changeInfo.isBoxVisible();
                        if (isBoxVisible){
                            mBtnSparePartPacking.setVisibility(View.VISIBLE);
                        } else {
                            mBtnSparePartPacking.setVisibility(View.GONE);
                        }
                        List<UpChangeInfo> UpChangeList = changeInfo.getUpChanges();
                        mupChangeList.addAll(UpChangeList);
                        mRecordAdapter.notifyDataSetChanged();
                        List<BreakdownsInfo> breakdowns = changeInfo.getBreakdowns();
                        for (BreakdownsInfo info: breakdowns) {
                            String changeCode = info.getChangeCode();
                            reasonMap.put(changeCode,info.getReasonList());
                        }
                        List<ChangeCategorys> list = changeInfo.getChangeCategorys();

                        for (ChangeCategorys i:list) {
                            String value = i.getValue();
                            String code = i.getCode();
                            kindList.add(value);
                            kindMap.put(value,code);
                            typeMap.put(value,i.getChangeTypes());
                            replacedflagMap.put(value,i.getReplacedPartsFlag());

                        }
                        kindAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onError(Exception e) {
                    dismissLoadingDialog();
                    Utils.showServerError(AddChangeRecordActivity.this);
                    Log.d(TAG, "onError() == > Exception is (" + e.getMessage() + ")");
                }
            });

        } else {
            dismissLoadingDialog();
        }
        String[] causeOfFormation = {"请选择","装箱单不存在换下件条码","其他"};
        ArrayAdapter<String> partDownReason = new ArrayAdapter<String>(AddChangeRecordActivity.this,R.layout.item_change_spinner_select,R.id.text1,causeOfFormation);
        mSpinSparePartReason.setAdapter(partDownReason);
        reasonValueList = new ArrayList<String>();
        reasonValueList.add("其他");
        reasonAdapter = new ArrayAdapter<String>(AddChangeRecordActivity.this,R.layout.item_change_spinner_select,R.id.text1,reasonValueList);
        mSpinFailurePhenomena.setAdapter(reasonAdapter);

    }
   private boolean isSelect;
    @Override
    protected void setClickLintenner() {
        mChangeBack.setOnClickListener(this);
        mBtnAddChangeCommit.setOnClickListener(this);
        mSpinChangeKind.setOnItemSelectedListener(this);
        mSpinChangeType.setOnItemSelectedListener(this);
        mSpinChangeidentif.setOnItemSelectedListener(this);
        mSpinSparePartReason.setOnItemSelectedListener(this);
        mBtnSparePartPacking.setOnClickListener(this);
        mBtnGenerateBar.setOnClickListener(this);
        mSpinFailurePhenomena.setOnItemSelectedListener(this);
        //选择换上件获取换上件信息
        mRecordAdapter.setOnItemClickListener(new ChangeRecordAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, UpChangeInfo data) {
                mUpChangeInfo = data;
                Log.d(TAG,"mRecordAdapter onItemClick is starting");
              //mRadio = (RadioButton) view.findViewById(R.id.RadioBtn_select);
                mRadio = (CheckBox) view.findViewById(R.id.RadioBtn_select);
               // mRadio.setChecked(true);
                mRecordAdapter.notifyDataSetChanged();
               /* mTxtSparePartDescribe.setText(data.getMachineUpDesc());
                mTxtSparePartDownClass.setText(data.getChangeType());
                mTxtSparePartDownPrice.setText(data.getMachinePrice());*/
                upSn = data.getMachineUpSn();
                upCode = data.getChangeCode();
              /*  String upCode = mEditSpareDownId.getText().toString();
                if (upCode!= null && "".equals(upCode)){
                    mEditSpareDownId.setText(data.getChangeCode());
                }*/
                String value = data.getChangeCode();
                    Log.d(TAG,"onItemClick value is ("+value+")");
                 reasonList.addAll(reasonMap.get(value));
                reasonValueList.clear();
                reasonValueList.add("其他");
                for (ReasonList list : reasonList) {
                        String changeCode = list.getCode();
                        //Log.d(TAG,"mRecordAdapter.setOnItemClickListener changeCode ("+changeCode+")");
                        String changevalue = list.getValue();
                    reasonValueList.add(list.getValue());
                    reasonValueMap.put(changevalue,changeCode);
                }
                reasonAdapter.notifyDataSetChanged();
                reasonList.clear();

            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.Spin_changeKind:
                typevalueList = new ArrayList<String>();
                partsValueList = new ArrayList<String>();
                List<ChangeTypes> typesList =  typeMap.get(kindList.get(position));
                List<ReplacedPartsFlag> replacedPartsList = replacedflagMap.get(kindList.get(position));
                typevalueList.clear();
                typevalueList.clear();
                partsValueList.clear();
                if (position != 0){
                    for (ChangeTypes type:typesList) {
                        String code = type.getCode();
                        String value = type.getValue();
                        typeValueMap.put(value,code);
                        typevalueList.add(value);

                    }
                } else {
                    typevalueList.add("请选择");
                }
                //换件类型
                ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(AddChangeRecordActivity.this,R.layout.item_change_spinner_select,R.id.text1,typevalueList);
                mSpinChangeType.setAdapter(typeAdapter);

                //换下件标识
                if (position != 0){
                    for (ReplacedPartsFlag parts : replacedPartsList) {
                        String code = parts.getCode();
                        String value = parts.getValue();
                        partsValueList.add(value);
                        replacedPartValueMap.put(value,code);
                        replacedDescsMap.put(value,parts.getReplacedPartsDescs());

                    }
                }else {
                    partsValueList.add("请选择");
                }

                ArrayAdapter<String> replacedPartsAdater = new ArrayAdapter<String>(AddChangeRecordActivity.this,R.layout.item_change_spinner_select,R.id.text1,partsValueList);
                mSpinChangeidentif.setAdapter(replacedPartsAdater);
                String selectKind = kindList.get(position);
                Log.d(TAG,"onItemClick selectKind is ("+selectKind+")");
                if (!"请选择".equals(selectKind)){
                    mChangeGroup.setVisibility(View.VISIBLE);
                }else {
                    mChangeGroup.setVisibility(View.GONE);
                }
                if ("非联想换件".equals(selectKind)){
                mChangeidentyGroup.setVisibility(View.GONE);
                mChangeidentyLine.setVisibility(View.GONE);
               // Log.d(TAG,"onItemClick 非联想换件 is come in !!!");
            }else {
                    mChangeidentyGroup.setVisibility(View.VISIBLE);
                    mChangeidentyLine.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.Spin_changeType:
                String selectCType = typevalueList.get(position);
                Log.d(TAG,"");
                switch (selectCType) {
                    case "拆件":
                        mChangeDownGroup.setVisibility(View.VISIBLE);
                        mChangeUpGroup.setVisibility(View.GONE);
                        if (mRadio != null){
                            mRadio.setChecked(false);
                            reasonValueList.clear();
                            reasonValueList.add("其他");
                            reasonAdapter.notifyDataSetChanged();
                        }
                        break;
                    case "加装":
                        mChangeUpGroup.setVisibility(View.VISIBLE);
                        mChangeDownGroup.setVisibility(View.GONE);
                        break;
                    case "一换一":
                        if (mRadio != null){
                            mRadio.setChecked(false);
                            reasonValueList.clear();
                            reasonValueList.add("其他");
                            reasonAdapter.notifyDataSetChanged();
                        }
                        mChangeUpGroup.setVisibility(View.VISIBLE);
                        mChangeDownGroup.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case R.id.Spin_changeidentification:
                String indexvalue = partsValueList.get(position);
                if ("非损".equals(indexvalue)) {
                    mLinNoDamage.setVisibility(View.VISIBLE);
                   // Toast.makeText(AddChangeRecordActivity.this,indexvalue,Toast.LENGTH_SHORT).show();
                } else {
                    mLinNoDamage.setVisibility(View.GONE);
                }
                List<String>  descValueList = new ArrayList<String>();
                descValueList.clear();
                List<ReplacedPartsDescs> descsList =replacedDescsMap.get(indexvalue);
                if (position != 0) {
                    for (ReplacedPartsDescs desc:descsList) {
                        String code = desc.getCode();
                        String descvalue = desc.getValue();
                       // Log.e(TAG,"Spin_changeidentification == > descvalue ("+descvalue+")");
                        descValueList.add(descvalue);
                        replacedescValueMap.put(descvalue,code);
                    }
                }else {
                    descValueList.add("请选择");
                }


                if ("DOA".equals(indexvalue)) {
                    mEditSpareDownId.setText(upCode);
                    mEditSpareDownBar.setText(upSn);
                }else {
                    mEditSpareDownId.setText("");
                    mEditSpareDownBar.setText("");
                }
                ArrayAdapter<String> nonCauseAdapter = new ArrayAdapter<String>(AddChangeRecordActivity.this,R.layout.item_change_spinner_select,R.id.text1,descValueList);
                mSpinNonDamageCause.setAdapter(nonCauseAdapter);

                break;
            case R.id.Spin_FailurePhenomena:
               /* if (position != 0){
                    String changeCode = reasonList.get(position).getCode();
                    mTxtFailureCode.setText(changeCode);
                }*/

                //String code = reasonValueMap.get(descValueList.get(position));
                String code = "-";
                if (position != 0) {
                    code = reasonValueMap.get(reasonValueList.get(position));
                }
                mTxtFailureCode.setText(code);
               /* if (position != 0){
                    code = reasonValueList.get(position);
                } else {
                    code = "";
                }*/




                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int netState =  Utils.checkInternetStatus(AddChangeRecordActivity.this);
        switch (v.getId()) {
            case R.id.Rel_changeBack:
                finish();
                break;
            case R.id.Btn_sparePartPacking:
                mCauseOfFormation.setVisibility(View.GONE);
                mDownBarLine.setVisibility(View.GONE);
                if (netState == 0) {
                    Toast.makeText(AddChangeRecordActivity.this,"网络不可用,请连接网络后重试",Toast.LENGTH_SHORT).show();
                    return;
                }
                ChangePackingListActivity.toChangePack(AddChangeRecordActivity.this,code);
                isBarCode = true;
                break;
            case R.id.Btn_Generate_bar:
                //if (isBarCode && ("".equals(mEditSpareDownBar.getText().toString()))){
                    mEditSpareDownBar.setText(mBarCode);
                    mCauseOfFormation.setVisibility(View.VISIBLE);
                    mDownBarLine.setVisibility(View.VISIBLE);
                //}
                break;
            case R.id.Btn_AddChangeCommit:
                    if (netState == 0) {
                    Toast.makeText(AddChangeRecordActivity.this,"网络不可用,请连接网络后重试",Toast.LENGTH_SHORT).show();
                      return;
                    }

                    if (null == mProductCode || "".equals(mProductCode)){
                        return;
                    }
                    showLoadingDialog();
                    params.put("product_no",mProductCode);
                    String selectKind = mSpinChangeKind.getSelectedItem().toString();
                    Log.d(TAG," submit  == > selectKind ("+selectKind+")");
                    if ("请选择".equals(selectKind)){
                        dismissLoadingDialog();
                        Toast.makeText(AddChangeRecordActivity.this,"请选择换件类别！",Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        categoryCode = kindMap.get(selectKind);
                    }
                    params.put("change_category_code",categoryCode);
                    Log.d(TAG," submit  == > categoryCode ("+categoryCode+")");
                    categoryValue = selectKind;
                    Log.d(TAG," submit  == > categoryValue ("+categoryValue+")");
                    params.put("change_category_value",categoryValue);
                    String selectType = mSpinChangeType.getSelectedItem().toString();
                    Log.d(TAG," submit  == > selectType ("+selectType+")");
                    if ("请选择".equals(selectType)) {
                        dismissLoadingDialog();
                        Toast.makeText(AddChangeRecordActivity.this,"请选择换件类型！",Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        typeCode =  typeValueMap.get(selectType);
                    }
                    Log.d(TAG," submit  == > typeCode ("+typeCode+")");
                    params.put("change_type_code",typeCode);
                    typeValue = selectType;
                    Log.d(TAG," submit  == > typeValue ("+typeValue+")");
                    params.put("change_type_value",typeValue);
                    downSn = mEditSpareDownBar.getText().toString();
                    downCode = mEditSpareDownId.getText().toString();
                    String selectFailure = mSpinFailurePhenomena.getSelectedItem().toString();
                    String failureCode = mTxtFailureCode.getText().toString();
                    String  selectChangeidentif = "";
                    if (!"非联想换件".equals(selectKind)){
                        selectChangeidentif = mSpinChangeidentif.getSelectedItem().toString();
                    }

                    switch (selectType){
                        case "拆件":
                            if (downCode == null || downCode.equals("")){
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请输入换下件编码",Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                /*if(!Pattern.matches("[0-9]*", downCode)){
                                    dismissLoadingDialog();
                                    Toast.makeText(AddChangeRecordActivity.this,"换下件编码必须是数字",Toast.LENGTH_SHORT).show();
                                    return;
                                }*/
                            }
                            if (downSn == null || downSn.equals("")){
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请输入换下件条码",Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                //if(!Pattern.matches("[0-9a-zA-Z]*", downSn)){
                                if(!Pattern.matches("^[a-zA-Z\\d-\\s/|]*$", downSn)){
                                    dismissLoadingDialog();
                                    Toast.makeText(AddChangeRecordActivity.this,"换下件条码不能是汉字",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                           breakDownsCode = "-";
                           breakDownsValue = selectFailure;
                            Log.d(TAG," submit  == > 拆件  breakDownsCode("+breakDownsCode+")");
                            Log.d(TAG," submit  == > 拆件  breakDownsValue("+breakDownsValue+")");
                            if ("请选择".equals(selectChangeidentif)){
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请选择换件标识",Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                               // replacedPartsFlag = selectChangeidentif;
                                 replacedPartsDescs = selectChangeidentif;
                                String desc = replacedPartValueMap.get(selectChangeidentif);

                                if (desc != null){
                                    replacedPartsFlag = desc;
                                }

                                Log.d(TAG," submit  == > 拆件  replacedPartsFlag ("+replacedPartsFlag+")");
                                Log.d(TAG," submit  == > 拆件  replacedPartsDescs ("+replacedPartsDescs+")");
                            }

                            break;
                        case "加装":
                            if (upSn == null || upSn.equals("")){
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请选择换上件信息",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            /*if ("其他".equals(selectFailure)) {
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请选择故障现象",Toast.LENGTH_SHORT).show();
                                return;
                            } else {*/
                                breakDownsValue = selectFailure;
                                breakDownsCode = failureCode;
                                Log.d(TAG," submit  == > 加装  breakDownsCode("+breakDownsCode+")");
                                Log.d(TAG," submit  == > 加装  breakDownsValue("+breakDownsValue+")");
                           // }
                            replacedPartsFlag = "";
                            replacedPartsDescs = "";
                            Log.d(TAG," submit  == > 加装  replacedPartsFlag("+replacedPartsFlag+")");
                            Log.d(TAG," submit  == > 加装  replacedPartsDescs("+replacedPartsDescs+")");
                            break;
                        case "一换一":
                            if (upSn == null || upSn.equals("")){
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请选择换上件信息",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (downCode == null || downCode.equals("")){
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请输入换下件编码",Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                              /*  if(!Pattern.matches("[0-9]*", downCode)){
                                    dismissLoadingDialog();
                                    Toast.makeText(AddChangeRecordActivity.this,"换下件编码必须是数字",Toast.LENGTH_SHORT).show();
                                    return;
                                }*/
                            }
                            if (downSn == null || downSn.equals("")){
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请输入换下件条码",Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                //if(!Pattern.matches("[0-9a-zA-Z]*", downSn)){
                                if(!Pattern.matches("^[a-zA-Z\\d-\\s/|]*$", downSn)){
                                    dismissLoadingDialog();
                                    Toast.makeText(AddChangeRecordActivity.this,"换下件条码不能是汉字",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                          /*  if ("其他".equals(selectFailure)) {
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请选择故障现象",Toast.LENGTH_SHORT).show();
                                return;
                            } else {*/
                                breakDownsValue = selectFailure;
                                breakDownsCode = failureCode;
                                Log.d(TAG," submit  == > 一换一  breakDownsCode("+breakDownsCode+")");
                                Log.d(TAG," submit  == > 一换一  breakDownsValue("+breakDownsValue+")");
                           // }
                            if ("请选择".equals(selectChangeidentif)){
                                dismissLoadingDialog();
                                Toast.makeText(AddChangeRecordActivity.this,"请选择换件标识",Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                               // replacedPartsFlag = selectChangeidentif;
                                replacedPartsDescs = selectChangeidentif;
                                String desc = replacedPartValueMap.get(selectChangeidentif);
                                replacedPartsFlag = desc;
                                Log.d(TAG," submit  == > 一换一  replacedPartsFlag("+replacedPartsFlag+")");
                                Log.d(TAG," submit  == > 一换一  replacedPartsDescs("+replacedPartsDescs+")");
                            }
                            break;
                    }


                    spe_note = mEditSpecialRemarks.getText().toString().trim();
                     if (spe_note.length()>200) {
                         dismissLoadingDialog();
                         Toast.makeText(AddChangeRecordActivity.this,"特殊备注输入过长",Toast.LENGTH_SHORT).show();
                         return;
                     }
                    int isShowCause = mCauseOfFormation.getVisibility();
                    String causeInfo = mSpinSparePartReason.getSelectedItem().toString();
                    if (isShowCause == 0 && "请选择".equals(causeInfo)){
                        dismissLoadingDialog();
                        Toast.makeText(AddChangeRecordActivity.this,"系统生成条码原因",Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        remark = causeInfo;
                    }
                    if (upSn != null && !"".equals(upSn)){
                        params.put("machine_up_sn",upSn);
                    }
                if (upCode != null && !"".equals(upCode)){
                    params.put("machine_up_code",upCode);
                }
                if (downSn != null && !"".equals(downSn)){
                    params.put("machine_down_sn",downSn);
                }
                if (downCode != null && !"".equals(downCode)){
                    params.put("machine_down_code",downCode);
                }
                if (breakDownsCode != null && !"".equals(breakDownsCode)){
                    params.put("breakdowns_code",breakDownsCode);
                    Log.e(TAG,"breakdowns_code   is   ("+breakDownsCode+")");
                }
                if (breakDownsValue != null && !"".equals(breakDownsValue)){
                    params.put("breakdowns_value",breakDownsValue);
                }
                if (replacedPartsFlag != null && !"".equals(replacedPartsFlag)){
                    params.put("replaced_parts_flag",replacedPartsFlag);
                    Log.e(TAG,"replaced_parts_flag   is   ("+replacedPartsFlag+")");
                }
                if (replacedPartsDescs != null && !"".equals(replacedPartsDescs)){
                    params.put("replaced_parts_descs",replacedPartsDescs);
                    Log.e(TAG,"replaced_parts_descs   is   ("+replacedPartsDescs+")");
                }

                if (remark != null && !"".equals(remark)&&!"请选择".equals(remark)){
                    params.put("remark",remark);
                    Log.e(TAG,"remark   is   ("+remark+")");
                }

                if (spe_note != null && !"".equals(spe_note)){
                    params.put("spe_note",spe_note);
                }



                /*for(Map.Entry<String, String> entry:params.entrySet()){
                    //System.out.println(entry.getKey()+"--->"+entry.getValue());
                    Log.d(TAG,"submit ==　>params  is("+entry.getKey()+"=="+entry.getValue()+")");
                }*/
                HttpClientManager.post(NetInterface.SUBMIT_CHANGE, params, new JsonHttpCallBack<LoginData>() {
                        @Override
                        public void onSuccess(LoginData result) {
                           // Log.d(TAG,"onSuccess() ==　> result.getStatus_code() is("+result.getStatus_code()+")");
                            if (ResultUtils.isSuccess(AddChangeRecordActivity.this,result)) {
                                dismissLoadingDialog();
                                setResult(RESULT_OK,new Intent());
                                finish();
                            }else {
                                dismissLoadingDialog();
                                String err = result.getMessage();
                                Log.d(TAG,"onSuccess() ==　> err is("+err+")");
                                Toast.makeText(AddChangeRecordActivity.this,err,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            dismissLoadingDialog();
                            Utils.showServerError(AddChangeRecordActivity.this);
                            Log.d(TAG, "onError() == > Exception is (" + e.getMessage() + ")");
                        }
                    });
                break;
        }
    }


    public static void toAddChangeRecord(Activity activity,String orderCode,String productNo,int reqCode){
        Intent intent = new Intent(activity,AddChangeRecordActivity.class);
        intent.putExtra(ORDER_CODE,orderCode);
        intent.putExtra(PRODUCTNO,productNo);
        activity.startActivityForResult(intent,reqCode);

    }
    /*public static void toAddChangeRecord(Activity activity, ChangeBoxsInfo.BoxList boxs){
        Intent intent = new Intent(activity,AddChangeRecordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PACKINGLIST,boxs);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            box = (ChangeBoxsInfo.BoxList) bundle.get(PACKINGLIST);
            mEditSpareDownId.setText(box.getCode());
            mEditSpareDownBar.setText(box.getSn());
            mTxtSparePartDescribe.setText(box.getDesc());
            mTxtSparePartDownClass.setText(box.getType());
            mTxtSparePartDownPrice.setText(box.getPrice());
            mDownBarLine.setVisibility(View.VISIBLE);
            mDownDescribeGroup.setVisibility(View.VISIBLE);

        }
    }
}
