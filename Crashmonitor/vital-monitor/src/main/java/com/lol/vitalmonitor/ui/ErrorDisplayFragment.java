package com.lol.vitalmonitor.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lol.vitalmonitor.R;
import com.lol.vitalmonitor.crashhandler.CrashActivityBackButtonPress;


public class ErrorDisplayFragment extends Fragment {

    @Nullable private TextView errorTextView;
    @Nullable private Button btnBack;
    @Nullable private ImageView bgImageView;
    CrashActivityBackButtonPress crashActivityBackButtonPress;

    public ErrorDisplayFragment() {

    }

    public ErrorDisplayFragment(CrashActivityBackButtonPress crashActivityBackButtonPress) {
        this.crashActivityBackButtonPress = crashActivityBackButtonPress;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.exception_display_fragment, container, false);

        errorTextView = root_view.findViewById(R.id.exception_text);
        btnBack = root_view.findViewById(R.id.btn_back);
        bgImageView = root_view.findViewById(R.id.bg_imageView);

        btnBack.setOnClickListener(view -> crashActivityBackButtonPress.backButtonPress());
        return root_view;
    }

    @Nullable
    public TextView getErrorTextView() {
        return errorTextView;
    }

    @Nullable
    public Button getBackButton() {
        return btnBack;
    }

    @Nullable
    public ImageView getBgImageView() {
        return bgImageView;
    }

    public void setErrorText(String errorText){
        if(errorTextView != null && errorText != null) {
            errorTextView.setText(errorText);
        }
    }

    public void setBackBtnText(String backBtnText){
        if(btnBack != null && backBtnText != null) {
            btnBack.setText(backBtnText);
        }
    }

    public void setErrorTextView(@Nullable TextView errorTextView) {
        this.errorTextView = errorTextView;
    }

    public void setBtnBack(@Nullable Button btnBack) {
        this.btnBack = btnBack;
    }

    public void setBgImageView(@Nullable ImageView bgImageView) {
        this.bgImageView = bgImageView;
    }
}
