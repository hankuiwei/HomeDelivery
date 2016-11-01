package com.lenovo.service.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lenovo.service.R;
import com.lenovo.service.Utils;

import java.util.ArrayList;

public class QRCodeFragment extends DialogFragment {

    private String qrUrl = "";//二维码地址
    private ImageView mImageQR;
    private TextView mTxtClose;
    public static final String QR_URL = "qr_url";

    public QRCodeFragment() {

    }

    public static QRCodeFragment newInstance(String qrUrl) {
        QRCodeFragment fragment = new QRCodeFragment();
        Bundle args = new Bundle();
        args.putString(QR_URL, qrUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qrUrl = getArguments().getString(QR_URL);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        mImageQR = (ImageView) view.findViewById(R.id.img_QRcode);
        mTxtClose = (TextView) view.findViewById(R.id.textClose);
        mTxtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        mListener = (OnFragmentInteractionListener) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_qrcode, null);
//        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        mImageQR = (ImageView) layout.findViewById(R.id.img_QRcode);
        mTxtClose = (TextView) layout.findViewById(R.id.textClose);
        mTxtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Utils.loadImageWithGlide(getActivity(), qrUrl, mImageQR, R.drawable.default_load_img);
        builder.setView(layout);
        return builder.create();
//        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = getActivity().getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        getDialog().getWindow().setLayout(metrics.widthPixels - 2 * ((int) getResources().getDimension(R.dimen.layout_65dp)), getDialog().getWindow().getAttributes().height);
        Utils.loadImageWithGlide(getActivity(), qrUrl, mImageQR, R.drawable.default_load_img);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
