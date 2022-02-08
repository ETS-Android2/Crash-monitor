package com.lol.vitalmonitor.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lol.vitalmonitor.R;

public class ErrorDisplayView extends FrameLayout {

    @Nullable private TextView errorTextView;
    @Nullable private Button btnBack;
    @Nullable private ImageView bgImageView;

    public ErrorDisplayView(@NonNull Context context) {
        this(context,null);
    }

    public ErrorDisplayView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public ErrorDisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int playerLayoutId = R.layout.exception_display_fragment;

        LayoutInflater.from(context).inflate(playerLayoutId, this);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        errorTextView = findViewById(R.id.exception_text);
        btnBack = findViewById(R.id.btn_back);
        bgImageView = findViewById(R.id.bg_imageView);

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
