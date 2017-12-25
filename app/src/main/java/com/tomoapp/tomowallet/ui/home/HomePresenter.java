package com.tomoapp.tomowallet.ui.home;

import com.tomoapp.tomowallet.base.MainApplication;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.ToastUtil;
import com.tomoapp.tomowallet.helper.socket.TOMOSocketListener;
import com.tomoapp.tomowallet.model.userInfo.UserInfoDataSource;
import com.tomoapp.tomowallet.model.userInfo.UserInfoRepository;
import com.tomoapp.tomowallet.model.wallet.Wallet;
import com.tomoapp.tomowallet.model.wallet.WalletDataSource;
import com.tomoapp.tomowallet.model.wallet.WalletRepository;
import com.tomoapp.tomowallet.model.walletAction.WalletActionDataSource;
import com.tomoapp.tomowallet.model.walletAction.WalletActionRepository;

import java.math.BigInteger;

/**
 * Created by macbook on 12/25/17.
 */

public class HomePresenter implements HomeContract.Presenter, TOMOSocketListener {
    private HomeContract.View mView;
    private WalletDataSource mWallet;
    private UserInfoDataSource mUserInfo;
    private WalletActionDataSource mWalletAction;

    public HomePresenter(HomeContract.View mView) {
        this.mView = mView;
        this.mWallet = new WalletRepository(mView.getContext());
        this.mWalletAction = new WalletActionRepository(mView.getContext());
        this.mUserInfo = new UserInfoRepository();
    }

    @Override
    public void init() {
        MainApplication.get().connectSocket(this);
    }

    @Override
    public void destroy() {
        MainApplication.get().disconnectSocket(this);
    }

    @Override
    public void onMine() {
        try {
            mView.onRewarding();
            mWalletAction.performReward(new WalletActionDataSource.ActionExecuteListener() {
                @Override
                public void onFail(int code, String why) {
                    ToastUtil.show(why);
                    mView.onRewarded(null);
                }
            });
        }catch (Exception e){
            LogUtil.e(e);
        }
    }


    @Override
    public void onSocketConnected() {
        MainApplication.get().emitUser(mWallet.getAddress());
    }

    @Override
    public void onSocketDisconnected(Object... args) {
        LogUtil.e("onSocketDisconnect: " + args.toString());
    }

    @Override
    public void onRetrieveUserInfo(String userInfoString) {
        try {
            if (userInfoString == null || userInfoString.isEmpty()) return;
            mView.setUserInfo(mUserInfo.createUserInfo(userInfoString));
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    @Override
    public void onRetrieveReward(String value) {
        try {
            mView.onRewarded(value);
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    @Override
    public void onCashedIn(String transactionDetail) {

    }

    @Override
    public void onCashedOut(String transactionDetail) {

    }


    @Override
    public void deleteWallet() {
        mWallet.deleteWallet();
        mView.onWalletDeleted();
    }

    @Override
    public void refreshUserInfo() {
        MainApplication.get().emitUser(mWallet.getAddress());
    }

    @Override
    public String getWalletAddress() {
        return mWallet == null ? "" : mWallet.getAddress();
    }

    @Override
    public String getMnemonic() {
        return mWallet == null ? "" : mWallet.getMnemonic();
    }
}
