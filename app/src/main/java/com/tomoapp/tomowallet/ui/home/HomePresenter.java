package com.tomoapp.tomowallet.ui.home;

import com.tomoapp.tomowallet.base.MainApplication;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.helper.ToastUtil;
import com.tomoapp.tomowallet.helper.socket.TOMOSocketListener;
import com.tomoapp.tomowallet.model.userInfo.UserInfoDataSource;
import com.tomoapp.tomowallet.model.userInfo.UserInfoRepository;
import com.tomoapp.tomowallet.model.userInfo.pojo.UserInfo;
import com.tomoapp.tomowallet.model.wallet.Wallet;
import com.tomoapp.tomowallet.model.wallet.WalletDataSource;
import com.tomoapp.tomowallet.model.wallet.WalletRepository;
import com.tomoapp.tomowallet.model.walletAction.WalletActionDataSource;
import com.tomoapp.tomowallet.model.walletAction.WalletActionRepository;
import com.tomoapp.tomowallet.model.walletActionResponse.CashActionResponse;
import com.tomoapp.tomowallet.model.walletActionResponse.RewardResponse;

import java.math.BigInteger;

/**
 * Created by macbook on 12/25/17.
 */

public class HomePresenter implements HomeContract.Presenter, TOMOSocketListener {
    private HomeContract.View mView;
    private WalletDataSource mWallet;
    private UserInfo userInfo;
    private WalletActionDataSource mWalletAction;

    public HomePresenter(HomeContract.View mView) {
        this.mView = mView;
        this.mWallet = new WalletRepository(mView.getContext());
        this.mWalletAction = new WalletActionRepository(mView.getContext());

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
    public void onRetrieveUserInfo(UserInfo userInfo) {
        try {
            //if (userInfoString == null || userInfoString.isEmpty()) return;
            if (userInfo == null) return;
            this.userInfo = userInfo;
            mView.setUserInfo(userInfo);
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    @Override
    public void onRetrieveReward(RewardResponse rewardResponse) {
        try {
            mView.onRewarded(rewardResponse);
        }catch (Exception e){
            LogUtil.e(e);
        }
    }

    @Override
    public void onCashedIn(CashActionResponse cashInResponse) {
        mView.onCashed(cashInResponse);
    }

    @Override
    public void onCashedOut(CashActionResponse cashOutResponse) {
        mView.onCashed(cashOutResponse);
    }


    @Override
    public void onCashIn(double value) {
        mWalletAction.cashIn(value, new WalletActionDataSource.ActionExecuteListener() {
            @Override
            public void onFail(int code, String why) {
                mView.onCashFail(why);
            }
        });
    }

    @Override
    public void onCashOut(double value) {
        mWalletAction.cashOut(value, new WalletActionDataSource.ActionExecuteListener() {
            @Override
            public void onFail(int code, String why) {
                mView.onCashFail(why);
            }
        });
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


    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }
}
