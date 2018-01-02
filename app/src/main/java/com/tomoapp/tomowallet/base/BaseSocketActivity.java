package com.tomoapp.tomowallet.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.alert.AlertHelper;
import com.tomoapp.tomowallet.helper.socket.TOMOSocketListener;
import com.tomoapp.tomowallet.model.userInfo.pojo.UserInfo;
import com.tomoapp.tomowallet.model.wallet.WalletRepository;
import com.tomoapp.tomowallet.model.walletActionResponse.CashActionResponse;
import com.tomoapp.tomowallet.model.walletActionResponse.RewardResponse;

import io.socket.client.Socket;

/**
 * Created by macbook on 12/28/17.
 */

public class BaseSocketActivity extends BaseActivity implements TOMOSocketListener{


    AlertHelper alertHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertHelper = new AlertHelper();
        MainApplication.get().connectSocket(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.get().connectSocket(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainApplication.get().disconnectSocket(this);
    }

    @Override
    public void onSocketConnected() {
        LogUtil.d("BASE SOCKET: onSocketConnected");

    }

    @Override
    public void onSocketDisconnected(Object... args) {
        LogUtil.d("BASE SOCKET: onSocketDisconnected");
    }

    @Override
    public void onRetrieveUserInfo(UserInfo userInfo) {
        LogUtil.d("BASE SOCKET: onRetrieveUserInfo");
    }

    @Override
    public void onRetrieveReward(RewardResponse reward) {
        LogUtil.d("BASE SOCKET: onRetrieveReward");
        alertHelper.showRewarded(reward);
    }

    @Override
    public void onCashedIn(CashActionResponse cashInDetail) {
        LogUtil.d("BASE SOCKET: onCashedIn");
        alertHelper.showCashedIn(cashInDetail);
    }

    @Override
    public void onCashedOut(CashActionResponse cashOutDetail) {
        LogUtil.d("BASE SOCKET: onCashedOut");
        alertHelper.showCashedOut(cashOutDetail);
    }

    @Override
    public void onTMCSent(CashActionResponse transactionDetail) {
        LogUtil.d("BASE SOCKET: onTMCSent");
        alertHelper.showSentTMC(transactionDetail);
    }

    @Override
    public void onTMCReceived(CashActionResponse transactionDetail) {
        LogUtil.d("BASE SOCKET: onTMCReceived");
        alertHelper.showReceivedTMC(transactionDetail);
    }


    protected void emitUser(){
        LogUtil.d("BASE SOCKET: emitUser");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                MainApplication.get().emitUser(new WalletRepository(MainApplication.getCurrentActivity()).getAddress());
            }
        });
    }
}
