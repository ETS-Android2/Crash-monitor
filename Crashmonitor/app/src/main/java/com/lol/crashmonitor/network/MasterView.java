package com.lol.crashmonitor.network;

public interface MasterView {
    void showLoader();

    void hideLoader();

    void showMessage(String message);
}